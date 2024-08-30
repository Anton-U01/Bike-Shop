package softuni.bg.bikeshop.service.impl;
;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import softuni.bg.bikeshop.exceptions.ProductNotFoundException;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.*;
import softuni.bg.bikeshop.models.dto.ProductSearchDto;
import softuni.bg.bikeshop.models.parts.PartType;
import softuni.bg.bikeshop.repository.*;
import softuni.bg.bikeshop.service.ProductsService;
import softuni.bg.bikeshop.util.ProductSpecification;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductsServiceImpl implements ProductsService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;
    private final RestClient restClient;
    private final ModelMapper modelMapper;
    private final BikeRepository bikeRepository;
    private final PartRepository partRepository;

    public ProductsServiceImpl(ProductRepository productRepository, UserRepository userRepository, PictureRepository pictureRepository, RestClient restClient, ModelMapper modelMapper, BikeRepository bikeRepository, PartRepository partRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
        this.restClient = restClient;
        this.modelMapper = modelMapper;
        this.bikeRepository = bikeRepository;
        this.partRepository = partRepository;
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
                .orElseThrow(()-> new UserNotFoundException("User with username " + principal.getName() + " is not found!"));

        if(user.getFavouriteProducts().contains(product)){
            return false;
        }
        user.getFavouriteProducts().add(product);
        product.getIsFavouriteOf().add(user);
        product.setFavourite(true);

        userRepository.save(user);
        return true;
    }

    @Override
    public Set<Product> getFavourites(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + " is not found!"));

        return productRepository.getFavouritesListByUser(user);

    }

    @Override
    @Transactional
    public void removeFromFavourites(Long productId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + " is not found!"));

        Product product =  productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product with id " + productId + " is not found!"));

        if(!user.getFavouriteProducts().contains(product)){
            return;
        }
        user.getFavouriteProducts().remove(product);
        product.getIsFavouriteOf().remove(user);
        if(product.getIsFavouriteOf().isEmpty()){
            product.setFavourite(false);
        }
    }


    @Override
    public List<Product> getAllCurrentUserProducts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + " is not found!"));

        return productRepository.getAllCurrentUserProducts(user);
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long productId, String username) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product with id " + productId + " is not found!"));


        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + " is not found!"));

        if(!user.getProducts().contains(product)){
            return false;
        }
        if(product.isFavourite()){
            return false;
        }
        user.getProducts().remove(product);
        pictureRepository.deleteAll(product.getPictures());

        product.getReviews().forEach(r -> {
            restClient
                    .delete()
                    .uri("http://localhost:8081/reviews/" + r.getId())
                    .retrieve();
        });


        productRepository.delete(product);

        return true;
    }

    @Override
    public long getProductsCount() {
        return this.productRepository.count();
    }

    @Override
    @Transactional
    public void deleteOldProducts() {
        List<Product> createdOnBeforeProducts = this.productRepository.findAllByCreatedOnBefore(LocalDateTime.now().minusDays(60));
        createdOnBeforeProducts.forEach(p -> {
            deleteProduct(p.getId(), p.getSeller().getUsername());
        });
    }

    @Override
    @Transactional
    public void buyProduct(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " is not found!"));


        product.setQuantity(product.getQuantity() - quantity);
        if (product.getQuantity() == 0) {
            deleteProduct(id, product.getSeller().getUsername());
            return;
        }
        productRepository.saveAndFlush(product);

    }
    @Override
    public Page<Product> getAll(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getAllFilteredProducts(List<String> bikes, List<String> parts, Pageable pageable) {
        Specification<Product> spec = Specification.where(null);

        if (!bikes.contains("ALL")) {
            spec = spec.or(ProductSpecification.hasBikeType(bikes));
        }

        if (!parts.contains("ALL")) {
            spec = spec.or(ProductSpecification.hasPartType(parts));
        }
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<ProductSearchDto> searchProducts(String query) {
        BikeType bikeType = null;
        PartType partType = null;

        try {
            bikeType = BikeType.valueOf(query.toUpperCase());
        } catch (IllegalArgumentException e) {
        }

        try {
            partType = PartType.valueOf(query.toUpperCase());
        } catch (IllegalArgumentException e) {
        }

        List<Product> products = productRepository.findProductByQuery(query);
        List<ProductSearchDto> productsList = products.stream().map(p -> {
                    ProductSearchDto map = modelMapper.map(p, ProductSearchDto.class);
                    return map;
                }
        ).toList();

        return productsList;

    }


}
