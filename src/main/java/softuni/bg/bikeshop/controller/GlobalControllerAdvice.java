package softuni.bg.bikeshop.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import softuni.bg.bikeshop.service.OrderService;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final OrderService orderService;

    public GlobalControllerAdvice(OrderService orderService) {
        this.orderService = orderService;
    }
    @ModelAttribute
    public void addItemCounter(Model model, Principal principal) {
        if (principal != null) {
            int itemCount = orderService.getMyBagSize(principal.getName());
            model.addAttribute("itemCounter", itemCount);
        } else {
            model.addAttribute("itemCounter", 0);
        }
    }
}
