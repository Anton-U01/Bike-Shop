package softuni.bg.bikeshop.service.impl;
;
import org.springframework.stereotype.Service;
import softuni.bg.bikeshop.models.*;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.service.ProductsService;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductsServiceImpl implements ProductsService {
    private final ProductRepository productRepository;

    public ProductsServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
}
