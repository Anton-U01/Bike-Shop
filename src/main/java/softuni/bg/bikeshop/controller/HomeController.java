package softuni.bg.bikeshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "index";
    }
    @GetMapping("/about")
    public String about(){
        return "about";
    }
    @GetMapping("/add")
    public String viewAdd(){return "add-product";}
    @GetMapping("/add-bike")
    public String viewAddBike(){return "add-bike";}
    @GetMapping("/add-part")
    public String viewAddPart(){return "add-part";}
    @GetMapping("/all-products")
    public String viewShop(){return "all-products";}
    @GetMapping("/detail")
    public String viewDetail(){return "product-details";}
}
