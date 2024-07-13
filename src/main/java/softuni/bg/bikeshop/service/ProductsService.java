package softuni.bg.bikeshop.service;


import softuni.bg.bikeshop.models.Product;

import java.util.List;

public interface ProductsService{


    List<Product> getAll();

    Product getProductById(Long id);
}
