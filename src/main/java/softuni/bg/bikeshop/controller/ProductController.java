package softuni.bg.bikeshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import softuni.bg.bikeshop.models.Bike;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.parts.ChainPart;
import softuni.bg.bikeshop.models.parts.FramePart;
import softuni.bg.bikeshop.models.parts.Part;
import softuni.bg.bikeshop.models.parts.TiresPart;
import softuni.bg.bikeshop.service.ProductsService;

import java.security.Principal;
import java.util.List;


@Controller
public class ProductController {
    private final ProductsService productsService;


    public ProductController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/products/add")
    public String viewAddProducts() {
        return "add-product";
    }

    @GetMapping("/products/add-part")
    public String viewAddPart(Model model) {
        return "add-part";
    }

    @GetMapping("/products")
    public String viewShop(Model model) {
        List<Product> allProducts = productsService.getAll();
        model.addAttribute("allProducts", allProducts);
        return "all-products";
    }

    @GetMapping("/products/details/{id}")
    public String viewDetail(@PathVariable("id") Long id, Model model) {
        Product productById = productsService.getProductById(id);
        model.addAttribute("product", productById);
        if (productById instanceof Bike bike) {
            model.addAttribute("bike", bike);
            model.addAttribute("isBike", true);
        } else {
            model.addAttribute("isBike", false);
        }
        if (productById instanceof Part part) {
            model.addAttribute("part", part);
            model.addAttribute("isPart", true);
        } else {
            model.addAttribute("isPart", false);
        }
        if (productById instanceof ChainPart chainPart) {
            model.addAttribute("chainPart", chainPart);
            model.addAttribute("isChainPart", true);
        } else {
            model.addAttribute("isChainPart", false);
        }
        if (productById instanceof FramePart framePart) {
            model.addAttribute("framePart", framePart);
            model.addAttribute("isFramePart", true);
        } else {
            model.addAttribute("isFramePart", false);
        }
        if (productById instanceof TiresPart tiresPart) {
            model.addAttribute("tiresPart", tiresPart);
            model.addAttribute("isTiresPart", true);
        } else {
            model.addAttribute("isTiresPart", false);
        }

        return "product-details";
    }

    @GetMapping("/products/add-to-favourites/{id}")
    public String addToFavourites(@PathVariable("id") Long id, Principal principal) {
        productsService.addToFavourites(id,principal);
        return "redirect:/products";
    }
}
