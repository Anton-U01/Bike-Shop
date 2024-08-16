package softuni.bg.bikeshop.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import softuni.bg.bikeshop.models.Product;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface ProductsService{


    List<Product> getAll();

    Product getProductById(Long id);

    boolean addToFavourites(Long productById, Principal principal);

    Set<Product> getFavourites(String username);

    void removeFromFavourites(Long productId, String username);

    List<Product> getAllCurrentUserProducts(String username);

    boolean deleteProduct(Long productId, String name);

    long getProductsCount();

    void deleteOldProducts();

    void buyProduct(Long id, Integer quantity);

    Page<Product> getAll(Pageable pageable);

    Page<Product> getAllFilteredProducts(List<String> bikes, List<String> parts, Pageable pageable);
}
