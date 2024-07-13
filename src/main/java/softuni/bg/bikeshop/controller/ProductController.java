package softuni.bg.bikeshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import softuni.bg.bikeshop.models.parts.PartType;
import softuni.bg.bikeshop.service.ProductsService;


@Controller
public class ProductController {
    private final ProductsService productsService;

    public ProductController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/products/add")
    public String viewAddProducts(){
        return "add-product";
    }

    @GetMapping("/products/add-part")
    public String viewAddPart(Model model){
        model.addAttribute("partTypes", PartType.values());
        return "add-part";
    }

    @GetMapping("/products")
    public String viewShop(){return "all-products";}
    @GetMapping("/detail")
    public String viewDetail(){return "product-details";}
}
