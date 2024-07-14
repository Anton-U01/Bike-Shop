package softuni.bg.bikeshop.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.bg.bikeshop.models.Bike;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.parts.ChainPart;
import softuni.bg.bikeshop.models.parts.FramePart;
import softuni.bg.bikeshop.models.parts.Part;
import softuni.bg.bikeshop.models.parts.TiresPart;
import softuni.bg.bikeshop.service.ProductsService;

import java.security.Principal;
import java.util.List;
import java.util.Set;


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
    public String addToFavourites(@PathVariable("id") Long id,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes,
                                  HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        boolean success = productsService.addToFavourites(id, principal);
        if(!success){
            redirectAttributes.addFlashAttribute("errorMessage","This product is already added to favourites.");
            if(referer.contains("/products/details")){
                return "redirect:" + referer;
            }
            return "redirect:/products";
        }
        redirectAttributes.addFlashAttribute("successMessage","This product is added successfully to favourites!");
        if(referer.contains("/products/details")){
            return "redirect:" + referer;
        }
        return "redirect:/products";
    }
    @GetMapping("/products/favourites")
    public String getFavouritesPage(Model model,Principal principal){
        Set<Product> favourites = productsService.getFavourites(principal.getName());
        model.addAttribute("favourites",favourites);

        return "favourites";
    }
    @GetMapping("/products/remove-from-favourites/{id}")
    public String removeFromFavourites(@PathVariable("id") Long id,Principal principal){
        productsService.removeFromFavourites(id,principal.getName());
        return "redirect:/products/favourites";
    }

    @GetMapping("/products/my-offers")
    public String viewMyOffers(Model model,Principal principal){
        List<Product> productList = productsService.getAllCurrentUserProducts(principal.getName());
        model.addAttribute("productList",productList);
        return "my-offers";
    }
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Principal principal, RedirectAttributes redirectAttributes){
        boolean success = productsService.deleteProduct(id, principal.getName());
        if(!success){
            redirectAttributes.addFlashAttribute("errorMessage","Unable to delete product. Тhis product may have been added to someone else's favorites list. Please try again.");
            return "redirect:/products/my-offers";
        }
        redirectAttributes.addFlashAttribute("successMessage","This product is successfully deleted.");
        return "redirect:/products/my-offers";
    }
}
