package softuni.bg.bikeshop.service.impl;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.orders.DeliveryDetailsDto;
import softuni.bg.bikeshop.models.orders.OrderItemView;
import softuni.bg.bikeshop.models.orders.*;
import softuni.bg.bikeshop.repository.OrderRepository;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.repository.UserRepository;
import softuni.bg.bikeshop.service.EmailSenderService;
import softuni.bg.bikeshop.service.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final EmailSenderService emailSenderService;


    @Value("${stripe.api.publicKey}")
    private String publicKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = publicKey;
    }

    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository, ModelMapper modelMapper, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.emailSenderService = emailSenderService;
    }

    @Override
    @Transactional
    public void addProductToBag(Product product, Integer quantity, String username) {
        User user = getUser(username);
        Order myBag = getMyBag(user.getUsername());

        OrderItem orderItem = myBag.getOrderItems()
                .stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst()
                .orElse(null);
        if(orderItem == null){
            orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setOrder(myBag);
            orderItem.setPrice(product.getPrice() * quantity);
            myBag.getOrderItems().add(orderItem);
            product.setQuantity(product.getQuantity() - quantity);
        } else {
            int newQuantity = orderItem.getQuantity() + quantity;

            orderItem.setQuantity(newQuantity);
            orderItem.setPrice(newQuantity * product.getPrice());

            product.setQuantity(product.getQuantity() - quantity);
        }
        myBag.setTotalAmount(myBag.getTotalAmount() + orderItem.getPrice());

        productRepository.save(product);
        orderRepository.saveAndFlush(myBag);

    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + " is not found!"));
    }

    @Override
    public Order getMyBag(String username) {
        User user = getUser(username);
        return orderRepository.findByUserAndStatus(user, OrderStatus.ACTIVE)
                .orElseGet(() -> createNewCart(user.getUsername()));
    }
    @Override
    @Transactional
    public int getMyBagSize(String username){
        return getMyBag(username).getOrderItems().size();
    }

    @Override
    @Transactional
    public List<OrderItemView> getMyBagItems(Order myBag) {
        return myBag.getItems()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateQuantities(Map<String, String> quantities, Order myBag) {
        double totalPrice = 0.0;

        for (Map.Entry<String, String> entry : quantities.entrySet()) {
            if (entry.getKey().equals("_csrf")) {
                continue;
            }

            long id = Long.parseLong(entry.getKey());
            int newQuantity = Integer.parseInt(entry.getValue());

            Optional<OrderItem> optional = myBag.getOrderItems()
                    .stream()
                    .filter(item -> item.getId() == id)
                    .findFirst();

            if (optional.isPresent()) {
                OrderItem orderItem = optional.get();
                Product actualProduct = orderItem.getProduct();
                int currentQuantity = orderItem.getQuantity();

                int quantityDifference = newQuantity - currentQuantity;
                actualProduct.setQuantity(actualProduct.getQuantity() - quantityDifference);

                orderItem.setQuantity(newQuantity);
                orderItem.setPrice(newQuantity * actualProduct.getPrice());
                totalPrice += orderItem.getPrice();

                productRepository.save(actualProduct);
            }
        }
        myBag.setTotalAmount(totalPrice);
        orderRepository.saveAndFlush(myBag);
    }

    @Override
    @Transactional
    public void removeItemFromBag(Order myBag, Long itemId) {
        OrderItem itemToRemove = myBag.getOrderItems()
                .stream()
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .get();

        Product actualProduct = itemToRemove.getProduct();
        actualProduct.setQuantity(actualProduct.getQuantity() + itemToRemove.getQuantity());
        productRepository.save(actualProduct);

        myBag.getOrderItems().remove(itemToRemove);
        myBag.setTotalAmount(myBag.getTotalAmount() - itemToRemove.getPrice());

        orderRepository.saveAndFlush(myBag);
    }

    @Override
    public DeliveryDetailsDto getUserDeliveryDetails(String username) {
        User user = getUser(username);

        DeliveryDetailsDto deliveryDetailsDto = modelMapper.map(user.getAddress(), DeliveryDetailsDto.class);
        deliveryDetailsDto.setRecipientName(user.getFullName());
        return deliveryDetailsDto;
    }

    @Override
    public void saveDeliveryDetails(String username, DeliveryDetailsDto deliveryDetailsDto) {
        Order myBag = getMyBag(username);
        DeliveryDetails newDeliveryDetails = modelMapper.map(deliveryDetailsDto, DeliveryDetails.class);
        newDeliveryDetails.setOrder(myBag);

        DeliveryDetails existingDetails = myBag.getDeliveryDetails();
        if(existingDetails == null){
            myBag.setDeliveryDetails(newDeliveryDetails);
        } else {
            existingDetails.setCity(newDeliveryDetails.getCity());
            existingDetails.setStreet(newDeliveryDetails.getStreet());
            existingDetails.setRecipientName(newDeliveryDetails.getRecipientName());
            existingDetails.setPostalCode(newDeliveryDetails.getPostalCode());
            existingDetails.setPhoneNumber(newDeliveryDetails.getPhoneNumber());
        }

        orderRepository.saveAndFlush(myBag);
    }
    @Override
    public void checkAndUpdateDeliveryDetails(String username, DeliveryDetailsDto deliveryDetailsDto) {
        Order myBag = getMyBag(username);

        DeliveryDetails existingDetails = myBag.getDeliveryDetails();

        if (!isSameDeliveryDetails(existingDetails, deliveryDetailsDto)) {
            saveDeliveryDetails(username, deliveryDetailsDto);
        }
    }

    private boolean isSameDeliveryDetails(DeliveryDetails existingDetails, DeliveryDetailsDto newDetails) {
        return existingDetails != null &&
                existingDetails.getCity().equals(newDetails.getCity()) &&
                existingDetails.getStreet().equals(newDetails.getStreet()) &&
                existingDetails.getPostalCode().equals(newDetails.getPostalCode()) &&
                existingDetails.getPhoneNumber().equals(newDetails.getPhoneNumber()) &&
                existingDetails.getRecipientName().equals(newDetails.getRecipientName());
    }

    @Override
    public String getPublicKey() {
        return publicKey;
    }

    @Override
    @Transactional
    public void setOrderToCompleted(String username) {
        Order myBag = getMyBag(username);
        myBag.setStatus(OrderStatus.COMPLETED);
        orderRepository.saveAndFlush(myBag);

        User user = getUser(username);
        String email = user.getEmail();
        emailSenderService.sendOrderConfirmationEmail(email,String.valueOf(myBag.getId()),myBag.getOrderItems(),myBag.getTotalAmount());
    }

    @Override
    @Transactional
    public List<OrderViewDto> getCompletedOrders(String name) {
        User user = getUser(name);
        List<OrderViewDto> ordersList = new ArrayList<>();
        for (Order o : user.getOrders()) {
            if (!o.getStatus().equals(OrderStatus.ACTIVE)) {
                OrderViewDto orderDto = modelMapper.map(o, OrderViewDto.class);
                List<OrderItemView> itemsList = new ArrayList<>();
                for (OrderItem orderItem : o.getOrderItems()) {
                    OrderItemView itemDto = modelMapper.map(orderItem, OrderItemView.class);
                    itemDto.setPictureUrl(orderItem.getProduct().getPictures().get(0).getUrl());
                    itemsList.add(itemDto);
                }
                orderDto.setItems(itemsList);
                ordersList.add(orderDto);
            }
        }
        return ordersList;
    }

    private OrderItemView map(OrderItem orderItem){
        OrderItemView dto = new OrderItemView();
        dto.setId(orderItem.getId());
        dto.setName(orderItem.getProduct().getName());
        dto.setDescription(orderItem.getProduct().getDescription());
        dto.setQuantity(orderItem.getQuantity());
        dto.setProductPrice(orderItem.getProduct().getPrice());
        dto.setTotalAmount(orderItem.getPrice());
        dto.setPictureUrl(orderItem.getProduct().getPictures().get(0).getUrl());
        dto.setMaxQuantity(orderItem.getProduct().getQuantity() + orderItem.getQuantity());
        return dto;
    }

    private Order createNewCart(String username) {
        User user = getUser(username);
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.ACTIVE);
        order.setOrderDate(LocalDate.now());
        return orderRepository.saveAndFlush(order);
    }


}
