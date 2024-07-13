package softuni.bg.bikeshop.service;

import softuni.bg.bikeshop.models.dto.parts.AddPartDto;

import java.security.Principal;

public interface PartService {
    boolean add(AddPartDto addPartDto, Principal principal);

}
