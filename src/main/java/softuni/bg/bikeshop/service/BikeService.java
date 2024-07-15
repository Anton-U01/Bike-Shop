package softuni.bg.bikeshop.service;

import softuni.bg.bikeshop.models.dto.AddBikeDto;
import softuni.bg.bikeshop.models.dto.parts.EditBikeDto;

import java.security.Principal;

public interface BikeService {
    boolean add(AddBikeDto addBikeDto, Principal principal);

    boolean edit(EditBikeDto editBike, Long id);
}
