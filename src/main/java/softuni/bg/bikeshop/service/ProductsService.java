package softuni.bg.bikeshop.service;


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

}
