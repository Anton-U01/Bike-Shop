package softuni.bg.bikeshop.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import softuni.bg.bikeshop.exceptions.ProductNotFoundException;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.repository.UserRepository;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @InjectMocks
    private ProductsServiceImpl productsService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private  UserRepository userRepository;
    private User user;

    private Product product;
    private Principal principal;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("Product1");
        user = new User();
        user.setUsername("test");
        principal = () -> "test";
    }

    @Test
    void getAllTest(){
        List<Product> products = new ArrayList<>();
        when(productRepository.findAll())
                .thenReturn(products);

        List<Product> actualList = productsService.getAll();
        Assertions.assertEquals(products,actualList);
        Assertions.assertEquals(0,actualList.size());
    }
    @Test
    void getProductByIdTest(){
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(product));

        Optional<Product> optionalProduct = Optional.ofNullable(productsService.getProductById(1L));

        Assertions.assertTrue(optionalProduct.isPresent());
        Product actualProduct = optionalProduct.get();

        Assertions.assertEquals(product.getName(),actualProduct.getName());
    }
    @Test
    void getProductByIdNotFoundTest(){
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productsService.getProductById(1L);
        });
        assertEquals("Product with id " + 1L + " is not found!",exception.getMessage());

    }

    @Test
    void getFavouritesSuccessTest(){
        user.setUsername("test");
        Set<Product> usersFavourites = new HashSet<>();
        usersFavourites.add(product);
        user.setFavouriteProducts(usersFavourites);
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        when(productRepository.getFavouritesListByUser(user))
                .thenReturn(user.getFavouriteProducts());

        Set<Product> favourites = productsService.getFavourites("test");

        Assertions.assertFalse(favourites.isEmpty());
        Assertions.assertEquals(1,favourites.size());
    }
    @Test
    void getFavouritesUserNotFoundTest(){
        String testUsername = "test";

        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> {
                    productsService.getFavourites(testUsername);
                }
        );

        Assertions.assertEquals("User with username " + testUsername + " is not found!",exception.getMessage());
    }
    @Test
    void getProductsCountTest(){
        long count = 1L;
        when(productRepository.count())
                .thenReturn(count);

        long actual = productsService.getProductsCount();
        assertEquals(count,actual);
    }

    @Test
    void getAllCurrentUsersProductsSuccessTest(){
        user.setUsername("test");

        List<Product> products = new ArrayList<>();
        products.add(product);
        user.setProducts(products);

        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        when(productRepository.getAllCurrentUserProducts(user))
                .thenReturn(products);


        List<Product> actualList = productsService.getAllCurrentUserProducts("test");

        Assertions.assertFalse(actualList.isEmpty());
        Assertions.assertEquals(1,actualList.size());
    }
    @Test
    void getCurrentProductsUserNotFoundTest(){
        String testUsername = "test";

        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> {
                    productsService.getAllCurrentUserProducts(testUsername);
                }
        );

        Assertions.assertEquals("User with username " + testUsername + " is not found!",exception.getMessage());
    }
    @Test
    void addToFavouritesUserNotFound(){
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(product));
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> {
                    productsService.addToFavourites(product.getId(),principal);
                }
        );
        Assertions.assertEquals("User with username " + principal.getName() + " is not found!",exception.getMessage());

    }
    @Test
    void addToFavouritesProductNotFound(){
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> {
                    productsService.addToFavourites(product.getId(),principal);
                }
        );
        Assertions.assertEquals("Product with id " + product.getId() + " is not found!",exception.getMessage());

    }
    @Test
    void addToFavouritesProductIsAlreadyInList(){
        user.getFavouriteProducts().add(product);
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(product));
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));


        boolean success = productsService.addToFavourites(product.getId(), principal);
        assertFalse(success);
    }
    @Test
    void addToFavourites(){
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(product));
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));


        boolean success = productsService.addToFavourites(product.getId(), principal);

        assertTrue(success);
        assertTrue(product.isFavourite());
        Assertions.assertTrue(user.getFavouriteProducts().contains(product));
        verify(userRepository).save(user);
    }
    @Test
    void removeFromFavouritesTest(){
        user.getFavouriteProducts().add(product);
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(product));
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));


        productsService.removeFromFavourites(product.getId(), principal.getName());

        Assertions.assertFalse(user.getFavouriteProducts().contains(product));
        Assertions.assertFalse(product.isFavourite());
    }
    @Test
    void removeFromFavouritesUserNotFoundTest(){
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> {
                    productsService.removeFromFavourites(product.getId(),principal.getName());
                }
        );
        Assertions.assertEquals("User with username " + principal.getName() + " is not found!",exception.getMessage());

    }
    @Test
    void removeFromFavouritesProductNotFoundTest(){
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> {
                    productsService.removeFromFavourites(product.getId(),principal.getName());
                }
        );
        Assertions.assertEquals("Product with id " + product.getId() + " is not found!",exception.getMessage());

    }
    @Test
    void deleteProductProductNotFoundTest(){
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> {
                    productsService.deleteProduct(product.getId(),principal.getName());
                }
        );
        Assertions.assertEquals("Product with id " + product.getId() + " is not found!",exception.getMessage());

    }
    @Test
    void deleteProductUserNotFoundTest(){
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(product));
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> {
                    productsService.deleteProduct(product.getId(),principal.getName());
                }
        );
        Assertions.assertEquals("User with username " + principal.getName() + " is not found!",exception.getMessage());

    }
    @Test
    void deleteProductUserDoesntHaveThisProductTest(){
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(product));
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));


        boolean success = productsService.deleteProduct(product.getId(), principal.getName());

        Assertions.assertFalse(success);
        Assertions.assertFalse(user.getProducts().contains(product));
    }
    @Test
    void deleteProductProductIsSomeonesFavouriteTest(){
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(product));
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        productsService.addToFavourites(product.getId(),principal);

        boolean success = productsService.deleteProduct(product.getId(), principal.getName());

        Assertions.assertFalse(success);
        Assertions.assertTrue(product.isFavourite());
    }
    @Test
    void buyProductProductNotFoundTest(){
        product.setQuantity(5);
        when(productRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(product));

        productsService.buyProduct(Mockito.anyLong(),2);

        assertEquals(3, product.getQuantity());
    }


}
