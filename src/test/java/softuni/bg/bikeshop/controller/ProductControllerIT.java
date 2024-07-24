package softuni.bg.bikeshop.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import softuni.bg.bikeshop.models.Bike;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.parts.ChainPart;
import softuni.bg.bikeshop.models.parts.FramePart;
import softuni.bg.bikeshop.models.parts.TiresPart;
import softuni.bg.bikeshop.service.ProductsService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class ProductControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductsService productsService;

    @Test
    @WithMockUser(username = "test")
    void viewAddProductPageTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-product"));
    }
    @Test
    @WithMockUser(username = "test")
    void viewAddPartPageTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/add-part"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-part"));
    }
    @Test
    @WithMockUser(username = "test")
    void viewProductsPageTest() throws Exception {
        List<Product> products = new ArrayList<>();
        Mockito.when(productsService.getAll()).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allProducts",products))
                .andExpect(view().name("all-products"));
    }
    @Test
    @WithMockUser(username = "test")
    void viewFavouritesPageTest() throws Exception {
        Set<Product> favourites = new HashSet<>();
        Mockito.when(productsService.getFavourites(Mockito.anyString())).thenReturn(favourites);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/favourites"))
                .andExpect(model().attribute("favourites",favourites))
                .andExpect(status().isOk())
                .andExpect(view().name("favourites"));
    }
    @Test
    @WithMockUser(username = "test")
    void removeFromFavouritesTest() throws Exception {
        Long productId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/products/remove-from-favourites/{id}", productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/favourites"));
    }
    @Test
    @WithMockUser(username = "test")
    void viewMyOffersPageTest() throws Exception {
        List<Product> productList = new ArrayList<>();

        Mockito.when(productsService.getAllCurrentUserProducts(Mockito.anyString())).thenReturn(productList);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/my-offers"))
                .andExpect(model().attribute("productList",productList))
                .andExpect(status().isOk())
                .andExpect(view().name("my-offers"));
    }
    @Test
    @WithMockUser(username = "test")
    void deleteProductSuccessfulTest() throws Exception {
        Long productId = 1L;
        Mockito.when( productsService.deleteProduct(productId,"test"))
                        .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/delete/{id}",productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/my-offers"))
                .andExpect(flash().attributeExists("successMessage"));
    }
    @Test
    @WithMockUser(username = "test")
    void deleteProductFailureTest() throws Exception {
        Long productId = 1L;
        Mockito.when( productsService.deleteProduct(productId,"test"))
                .thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/delete/{id}",productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/my-offers"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
    @Test
    @WithMockUser(username = "test")
    void editProductPageForBikeTest() throws Exception {
        Long productId = 1L;

        Bike bike = new Bike();
        Mockito.when(productsService.getProductById(productId))
                        .thenReturn(bike);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/edit/{id}",productId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("product",bike))
                .andExpect(model().attribute("productType","BIKE"))
                .andExpect(view().name("edit-bike"));
    }

    @Test
    @WithMockUser(username = "test")
    void editProductPageForChainTest() throws Exception {
        Long productId = 1L;

        ChainPart chain = new ChainPart();
        Mockito.when(productsService.getProductById(productId))
                .thenReturn(chain);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/edit/{id}",productId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("product",chain))
                .andExpect(model().attribute("type","CHAIN"))
                .andExpect(view().name("edit-part"));
    }
    @Test
    @WithMockUser(username = "test")
    void editProductPageForFrameTest() throws Exception {
        Long productId = 1L;

        FramePart framePart = new FramePart();
        Mockito.when(productsService.getProductById(productId))
                .thenReturn(framePart);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/edit/{id}",productId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("product",framePart))
                .andExpect(model().attribute("type","FRAME"))
                .andExpect(view().name("edit-part"));
    }
    @Test
    @WithMockUser(username = "test")
    void editProductPageForTiresTest() throws Exception {
        Long productId = 1L;

        TiresPart tiresPart = new TiresPart();
        Mockito.when(productsService.getProductById(productId))
                .thenReturn(tiresPart);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/edit/{id}",productId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("product",tiresPart))
                .andExpect(model().attribute("type","TIRES"))
                .andExpect(view().name("edit-part"));
    }

    @Test
    @WithMockUser(username = "test")
    void addToFavouritesTestWhenFailingInProducts() throws Exception {
        Long productId = 1L;
        Principal principal = () -> "test";
        String referer =  "http://localhost:8080/products";


        Mockito.when(productsService.addToFavourites(productId,principal))
                .thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/add-to-favourites/{id}",productId)
                        .header("Referer",referer))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errorMessage","This product is already added to favourites."))
                .andExpect(redirectedUrl("/products"));
    }
    @Test
    @WithMockUser(username = "test")
    void addToFavouritesTestWhenFailingDetails() throws Exception {
        Long productId = 1L;
        Principal principal = () -> "test";
        String referer =  "http://localhost:8080/products/details";


        Mockito.when(productsService.addToFavourites(productId,principal))
                .thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/add-to-favourites/{id}",productId)
                        .header("Referer",referer))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errorMessage","This product is already added to favourites."))
                .andExpect(redirectedUrl(referer));
    }
    @Test
    @WithMockUser(username = "test")
    void productDetails() throws Exception {
        Long productId = 1L;

        Product product = new Product();
        Mockito.when(productsService.getProductById(productId))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/details/{id}",productId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("product",product))
                .andExpect(view().name("product-details"));
    }
    @Test
    @WithMockUser(username = "test")
    void bikeProductDetails() throws Exception {
        Long productId = 1L;

        Bike product = new Bike();
        Mockito.when(productsService.getProductById(productId))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/details/{id}",productId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("product",product))
                .andExpect(view().name("product-details"));
    }
    @Test
    @WithMockUser(username = "test")
    void chainProductDetails() throws Exception {
        Long productId = 1L;

        ChainPart product = new ChainPart();
        Mockito.when(productsService.getProductById(productId))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/details/{id}",productId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("product",product))
                .andExpect(view().name("product-details"));
    }
    @Test
    @WithMockUser(username = "test")
    void tiresProductDetails() throws Exception {
        Long productId = 1L;

        TiresPart product = new TiresPart();
        Mockito.when(productsService.getProductById(productId))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/details/{id}",productId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("product",product))
                .andExpect(view().name("product-details"));
    }
    @Test
    @WithMockUser(username = "test")
    void frameProductDetails() throws Exception {
        Long productId = 1L;

        FramePart product = new FramePart();
        Mockito.when(productsService.getProductById(productId))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/details/{id}",productId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("product",product))
                .andExpect(view().name("product-details"));
    }



}

