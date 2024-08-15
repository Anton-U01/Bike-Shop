package softuni.bg.bikeshop.service;

import org.springframework.web.multipart.MultipartFile;
import softuni.bg.bikeshop.models.dto.parts.AddPartDto;
import softuni.bg.bikeshop.models.dto.parts.EditPartDto;
import softuni.bg.bikeshop.models.parts.Part;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface PartService {
    boolean add(AddPartDto addPartDto, Principal principal, List<MultipartFile> files) throws IOException;

    boolean edit(EditPartDto editPart);

}
