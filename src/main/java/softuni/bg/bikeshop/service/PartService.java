package softuni.bg.bikeshop.service;

import jakarta.validation.Valid;
import softuni.bg.bikeshop.models.dto.parts.AddPartDto;
import softuni.bg.bikeshop.models.dto.parts.EditPartDto;

import java.security.Principal;

public interface PartService {
    boolean add(AddPartDto addPartDto, Principal principal);

    boolean edit(EditPartDto editPart);
}
