package softuni.bg.bikeshop.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import softuni.bg.bikeshop.models.dto.parts.AddPartDto;
import softuni.bg.bikeshop.models.dto.parts.EditPartDto;
import softuni.bg.bikeshop.service.PartService;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class PartController {

    private final PartService partService;
    public PartController(PartService partService) {

        this.partService = partService;
    }


    @PostMapping(value = "/products/add-part",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,})
    public ResponseEntity<?> addPart(
            @Valid @ModelAttribute AddPartDto addPartDto,
            BindingResult bindingResult,
            @RequestPart("partImages") List<MultipartFile> files,
            Principal principal) throws IOException {

        boolean noFilesUploaded = files.get(0).isEmpty();
        if (noFilesUploaded) {
            bindingResult.addError(new FieldError("partImages", "partImages", "Please select an image file to upload!"));
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            if (noFilesUploaded) {
                errors.put("partImages", "Please select an image file to upload!");
            }
            return ResponseEntity.badRequest().body(errors);
        }

        boolean success = partService.add(addPartDto, principal, files);
        if (!success) {
            return ResponseEntity.badRequest().body("noUserLogged");
        }
        return ResponseEntity.ok("");
    }


    @PutMapping("/edit-part")
    public ResponseEntity<?> editPart(@Valid @RequestBody EditPartDto editPart,
                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(errors);
        }
        boolean success = partService.edit(editPart);
        if(!success){
            return ResponseEntity.badRequest().body("noPartWithThisId");
        }

        return ResponseEntity.ok(Map.of("redirectUrl", "/products/product-management"));

    }



}
