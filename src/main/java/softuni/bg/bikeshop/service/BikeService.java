package softuni.bg.bikeshop.service;

import org.springframework.web.multipart.MultipartFile;
import softuni.bg.bikeshop.models.dto.AddBikeDto;
import softuni.bg.bikeshop.models.dto.parts.EditBikeDto;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface BikeService {
    boolean add(AddBikeDto addBikeDto, Principal principal, List<MultipartFile> files) throws IOException;

    boolean edit(EditBikeDto editBike, Long id);
}
