package softuni.bg.bikeshop.service;

import softuni.bg.bikeshop.models.dto.AddBikeDto;

import java.security.Principal;

public interface BikeService {
    boolean add(AddBikeDto addBikeDto, Principal principal);

}
