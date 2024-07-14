package softuni.bg.bikeshop.service.impl;
;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.bg.bikeshop.models.*;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.repository.UserRepository;
import softuni.bg.bikeshop.service.ProductsService;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductsServiceImpl implements ProductsService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductsServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional
    public void addToFavourites(Long id, Principal principal) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            return;
        }
        Product product = optionalProduct.get();
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if(optionalUser.isEmpty()){
            return;
        }
        User user = optionalUser.get();
        if(user.getFavouriteProducts().contains(product)){
            return;
        }
        user.getFavouriteProducts().add(product);
        product.setFavourite(true);

        userRepository.save(user);
    }
}
