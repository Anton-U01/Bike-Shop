package softuni.bg.bikeshop.controller;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.dto.BagUpdateDto;
import softuni.bg.bikeshop.models.dto.OrderItemView;
import softuni.bg.bikeshop.models.dto.ProductBuyDto;
import softuni.bg.bikeshop.models.orders.Order;
import softuni.bg.bikeshop.models.orders.OrderItem;
import softuni.bg.bikeshop.service.OrderService;
import softuni.bg.bikeshop.service.ProductsService;

import java.security.Principal;
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
}
