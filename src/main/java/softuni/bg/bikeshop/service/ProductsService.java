package softuni.bg.bikeshop.service;

import softuni.bg.bikeshop.models.dto.AddBikeDto;

import java.security.Principal;

public interface ProductsService{
    boolean add(AddBikeDto addBikeDto, Principal principal);
}
