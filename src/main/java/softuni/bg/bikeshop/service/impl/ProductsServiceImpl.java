package softuni.bg.bikeshop.service.impl;
;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.bg.bikeshop.exceptions.ProductNotFoundException;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.*;
import softuni.bg.bikeshop.repository.PictureRepository;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.repository.UserRepository;
import softuni.bg.bikeshop.service.ProductsService;
import java.security.Principal;
import java.util.*;

@Service
public class ProductsServiceImpl implements ProductsService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;

    public ProductsServiceImpl(ProductRepository productRepository, UserRepository userRepository, PictureRepository pictureRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product with id " + id + " is not found!"));

    }

    @Override
    @Transactional
    public boolean addToFavourites(Long id, Principal principal) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product with id " + id + " is not found!"));

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(()-> new UserNotFoundException("User with username " + principal.getName() + "is not found!"));

        if(user.getFavouriteProducts().contains(product)){
            return false;
        }
        user.getFavouriteProducts().add(product);
        product.setFavourite(true);

        userRepository.save(user);
        return true;
    }

    @Override
    public Set<Product> getFavourites(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + "is not found!"));

        return productRepository.getFavouritesListByUser(user);

    }

    @Override
    @Transactional
    public void removeFromFavourites(Long productId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + "is not found!"));

        Product product =  productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product with id " + productId + " is not found!"));

        if(!user.getFavouriteProducts().contains(product)){
            return;
        }
        user.getFavouriteProducts().remove(product);
        product.setFavourite(false);
    }

    @Override
    public List<Product> getAllCurrentUserProducts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + "is not found!"));

        return productRepository.getAllCurrentUserProducts(user);
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long productId, String username) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product with id " + productId + " is not found!"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + "is not found!"));

        if(!user.getProducts().contains(product)){
            return false;
        }
        if(product.isFavourite()){
            return false;
        }
        user.getProducts().remove(product);
        pictureRepository.deleteAll(product.getPictures());

        productRepository.deleteById(productId);

        return true;
    }
}
