package softuni.bg.bikeshop.service;

import org.springframework.web.multipart.MultipartFile;
import softuni.bg.bikeshop.models.dto.AddBikeDto;
import softuni.bg.bikeshop.models.dto.parts.EditBikeDto;

import java.io.IOException;
import java.security.Principal;

public interface BikeService {
    boolean add(AddBikeDto addBikeDto, Principal principal, MultipartFile file) throws IOException;

    boolean edit(EditBikeDto editBike, Long id);
}
