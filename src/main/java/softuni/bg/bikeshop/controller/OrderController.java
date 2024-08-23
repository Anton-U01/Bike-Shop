package softuni.bg.bikeshop.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.dto.DeliveryDetailsDto;
import softuni.bg.bikeshop.models.dto.OrderItemView;
import softuni.bg.bikeshop.models.dto.ProductBuyDto;
import softuni.bg.bikeshop.models.orders.Order;
import softuni.bg.bikeshop.service.OrderService;
import softuni.bg.bikeshop.service.ProductsService;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {
    private final ProductsService productsService;
    private final ModelMapper modelMapper;
    private final OrderService orderService;

    public OrderController(ProductsService productsService, ModelMapper modelMapper, OrderService orderService) {
        this.productsService = productsService;
        this.modelMapper = modelMapper;
        this.orderService = orderService;
    }

    @GetMapping("/products/buy/{id}")
    public String buyProductView(@PathVariable("id") Long id, Model model){
        Product product = productsService.getProductById(id);
        ProductBuyDto productBuyDto = modelMapper.map(product, ProductBuyDto.class);
        productBuyDto.setPicture(product.getPictures().get(0).getUrl());
        model.addAttribute("product",productBuyDto);
        if(productBuyDto.getQuantity() == 0){
            model.addAttribute("soldOut","Sold out!");
        }

        return "buy-product";
    }
    @GetMapping("/products/add-to-bag/{id}")
    public String addProductToBag(@PathVariable("id") Long id,
                                  @RequestParam(value = "quantity", required = false, defaultValue = "0") Integer quantity,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes){
        Product product = productsService.getProductById(id);
        if (quantity <= 0 || quantity > product.getQuantity()) {
            redirectAttributes.addFlashAttribute("quantityError", "Quantity must be between " + 1 + " and " + product.getQuantity() + "!");
            return "redirect:/products/add-to-bag/" + id;
        }

        orderService.addProductToBag(product,quantity,principal.getName());
        return "redirect:/products";
    }
    @GetMapping("/user/my-bag")
    @Transactional
    public String viewMyBag(Principal principal,
                            Model model){
        Order myBag = orderService.getMyBag(principal.getName());
        List<OrderItemView> orderItems = orderService.getMyBagItems(myBag);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalPrice", myBag.getTotalAmount());
        return "my-bag";
    }
    @PostMapping("/order/update-quantities")
    @Transactional
    public String updateQuantities(@RequestParam Map<String, String> quantities,
                                   Principal principal){
        Order myBag = orderService.getMyBag(principal.getName());

        orderService.updateQuantities(quantities,myBag);

        return "redirect:/user/my-bag";
    }
    @GetMapping("/order/remove/{id}")
    @Transactional
    public String removeItemFromBag(@PathVariable("id") Long itemId,Principal principal){
        Order myBag = orderService.getMyBag(principal.getName());
        orderService.removeItemFromBag(myBag,itemId);

        return "redirect:/user/my-bag";
    }

    @GetMapping("/order/delivery-details")
    public String viewDeliveryDetails(Model model){
        if (!model.containsAttribute("deliveryDetails")) {
            model.addAttribute("deliveryDetails", new DeliveryDetailsDto());
        }
        return "delivery-details";
    }
    @PostMapping("/order/submit-delivery-details")
    @Transactional
    public String submitDeliveryDetails(@Valid @ModelAttribute("deliveryDetails") DeliveryDetailsDto deliveryDetailsDto,
                                        BindingResult bindingResult,
                                        Principal principal,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("deliveryDetails", deliveryDetailsDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.deliveryDetails",bindingResult);

            return "redirect:/order/delivery-details";
        }

        Order myBag = orderService.getMyBag(principal.getName());
        List<OrderItemView> myBagItems = orderService.getMyBagItems(myBag);
        model.addAttribute("deliveryDetails",myBag.getDeliveryDetails());
        model.addAttribute("bagItems",myBagItems);
        model.addAttribute("totalAmount",myBag.getTotalAmount());
        model.addAttribute("publicKey",orderService.getPublicKey());

        if(orderService.myBagHasAlreadyDeliveryDetails(principal.getName())){
            return "checkout";
        }
        orderService.saveDeliveryDetails(principal.getName(),deliveryDetailsDto);
        return "checkout";
    }
    @GetMapping("/order/load-delivery-details")
    public String loadUserDeliveryDetails(Principal principal,RedirectAttributes redirectAttributes){
        DeliveryDetailsDto deliveryDetails = orderService.getUserDeliveryDetails(principal.getName());
        redirectAttributes.addFlashAttribute("deliveryDetails", deliveryDetails);


        return "redirect:/order/delivery-details";
    }
    @GetMapping("/result")
    public String handleResult(@RequestParam(required = false) String session_id,
                               @RequestParam(required = false) String error,
                               Principal principal,
                               Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        } else if (session_id != null) {
            try {
                Session session = Session.retrieve(session_id);

                PaymentIntent paymentIntent = PaymentIntent.retrieve(session.getPaymentIntent());

                Long amount = paymentIntent.getAmountReceived();
                String currency = paymentIntent.getCurrency();
                String paymentMethod = paymentIntent.getCharges().getData().get(0).getPaymentMethodDetails().getCard().getLast4();
                String email = session.getCustomerDetails().getEmail();
                String receiptUrl = paymentIntent.getCharges().getData().get(0).getReceiptUrl();

                Date date = new Date(paymentIntent.getCreated() * 1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(date);

                model.addAttribute("id", session_id);
                model.addAttribute("status", "succeeded");
                model.addAttribute("amount", amount / 100.00);
                model.addAttribute("currency", currency.toUpperCase());
                model.addAttribute("paymentMethod", "**** **** **** " + paymentMethod);
                model.addAttribute("email", email);
                model.addAttribute("receiptUrl", receiptUrl);
                model.addAttribute("transactionDate", formattedDate);

            } catch (StripeException e) {
                model.addAttribute("error", "Error retrieving payment details.");
                e.printStackTrace();
            }
            orderService.setOrderToCompleted(principal.getName());

        } else {
            model.addAttribute("error", "Unknown error occurred.");
        }
        return "paymentResult";
    }

}
