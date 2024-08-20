package softuni.bg.bikeshop.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.OrderItemView;
import softuni.bg.bikeshop.models.orders.Order;
import softuni.bg.bikeshop.models.orders.OrderItem;
import softuni.bg.bikeshop.models.orders.OrderStatus;
import softuni.bg.bikeshop.repository.OrderRepository;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.repository.UserRepository;
import softuni.bg.bikeshop.service.OrderService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
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
            myBag.setTotalAmount(myBag.getTotalAmount() + orderItem.getPrice());
        } else {
            orderItem.setQuantity(orderItem.getQuantity() + quantity);
            orderItem.setPrice(product.getPrice() * orderItem.getQuantity());
        }

        product.setQuantity(product.getQuantity() - quantity);

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

        int iteration = 0;
        for (Map.Entry<String, String> entry : quantities.entrySet()) {
            iteration++;
            if (iteration == 1) {
                continue;
            }
            long id = Long.parseLong(entry.getKey());
            int newQuantity = Integer.parseInt(entry.getValue());

            Optional<OrderItem> optional = myBag.getOrderItems().stream().filter(item -> item.getId() == id).findFirst();
            OrderItem orderItem = optional.get();
            int currentQuantity = orderItem.getQuantity();
            Product actualProduct = orderItem.getProduct();
            if(currentQuantity < newQuantity){
                actualProduct.setQuantity(actualProduct.getQuantity() - (newQuantity - currentQuantity));
            } else {
                actualProduct.setQuantity(actualProduct.getQuantity() + (currentQuantity - newQuantity));
            }
            productRepository.saveAndFlush(actualProduct);
            orderItem.setQuantity(newQuantity);
            orderItem.setPrice(orderItem.getQuantity() * actualProduct.getPrice());
            totalPrice += orderItem.getPrice();
        }
        myBag.setTotalAmount(totalPrice);
        orderRepository.saveAndFlush(myBag);
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
        dto.setMaxQuantity(orderItem.getProduct().getQuantity());
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
