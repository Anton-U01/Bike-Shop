package softuni.bg.bikeshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.bg.bikeshop.models.Bike;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.dto.ProductSearchDto;
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
    public String viewAddPart() {
        return "add-part";
    }

    @GetMapping("/products")
    public String viewShop(
                            @RequestParam(name = "page",required = false) Integer page,
                            @RequestParam(name = "sortBy", required = false) String sortBy,
                            @RequestParam(name = "bikes", required = false) List<String> bikes,
                            @RequestParam(name = "parts", required = false) List<String> parts,
                            Model model,
                           Principal principal) {
        String username = principal.getName();

        Sort sort = Sort.by(Sort.Direction.DESC, "createdOn");;

        int currentPage = (page != null) ? page : 1;
        if(sortBy != null){
                switch (sortBy){
                    case "priceAsc":
                        sort = Sort.by(Sort.Direction.ASC, "price");
                        model.addAttribute("sortType", "Price: Low to High");
                        break;
                    case "priceDsc":
                        sort = Sort.by(Sort.Direction.DESC, "price");
                        model.addAttribute("sortType", "Price: High to Low");
                        break;
                    case "oldest":
                        sort = Sort.by(Sort.Direction.ASC, "createdOn");
                        model.addAttribute("sortType", "Oldest");
                        break;
                    default:
                        sort = Sort.by(Sort.Direction.DESC, "createdOn");
                        model.addAttribute("sortType", "Latest");
                        break;
                }
        } else {
            sortBy = "latest";
            model.addAttribute("sortType", "Latest");
        }

        if (bikes == null || bikes.isEmpty()) {
            bikes = List.of("ALL");
        }
        if (parts == null || parts.isEmpty()) {
            parts = List.of("ALL");
        }

        Page<Product> productPage = productsService.getAllFilteredProducts(bikes,parts,PageRequest.of(currentPage - 1, 6,sort));
        List<Product> allProducts = productPage.getContent();


        model.addAttribute("ownerUsername",username);
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("totalPages",productPage.getTotalPages());
        model.addAttribute("currentPage",currentPage);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("bikes",bikes);
        model.addAttribute("parts",parts);

        return "all-products";
    }

    @GetMapping("/products/details/{id}")
    public String viewDetail(@PathVariable("id") Long id, Model model,Principal principal) {
        Product productById = productsService.getProductById(id);
        model.addAttribute("product", productById);
        if(productById.getQuantity() == 0){
            model.addAttribute("soldOut","Sold Out!");
        }
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
        String username = principal.getName();
        model.addAttribute("ownerUsername",username);

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
    @GetMapping("/products/product-management")
    public String viewMyOffers(Model model,Principal principal){
        List<Product> productList = productsService.getAllCurrentUserProducts(principal.getName());
        model.addAttribute("productList",productList);
        return "product-management";
    }
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Principal principal, RedirectAttributes redirectAttributes){
        boolean success = productsService.deleteProduct(id, principal.getName());
        if(!success){
            redirectAttributes.addFlashAttribute("errorMessage","Unable to delete product. Тhis product may have been added to someone else's favorites list. Please try again.");
            return "redirect:/products/product-management";
        }
        redirectAttributes.addFlashAttribute("successMessage","This product is successfully deleted.");
        return "redirect:/products/product-management";
    }
    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id")Long id,
                              Model model){

        Product product = productsService.getProductById(id);
        if (product instanceof Bike bike) {
            model.addAttribute("product", bike);
            model.addAttribute("productType", "BIKE");
            return "edit-bike";
        } else if (product instanceof Part part) {
            model.addAttribute("productType", "PART");
            if(product instanceof FramePart framePart){
                model.addAttribute("product", framePart);
                model.addAttribute("type", "FRAME");
            } else if(part instanceof ChainPart chainPart){
                model.addAttribute("product", chainPart);
                model.addAttribute("type", "CHAIN");
            } else if(part instanceof TiresPart tiresPart){
                model.addAttribute("product", tiresPart);
                model.addAttribute("type", "TIRES");
            }
        }
        return "edit-part";
    }

    @GetMapping("/products/edit-part")
    public String viewEditPart(){
        return "edit-part";
    }
    @GetMapping("/products/search")
    @ResponseBody
    public ResponseEntity<List<ProductSearchDto>> searchProducts(@RequestParam("query") String query) {
        return ResponseEntity.ok(productsService.searchProducts(query));
    }

}
