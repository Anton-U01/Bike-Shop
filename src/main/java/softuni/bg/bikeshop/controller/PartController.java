package softuni.bg.bikeshop.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import softuni.bg.bikeshop.models.dto.parts.AddPartDto;
import softuni.bg.bikeshop.models.parts.PartType;
import softuni.bg.bikeshop.service.PartService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@RestController
public class PartController {

    private final PartService partService;
    public PartController(PartService partService) {

        this.partService = partService;
    }


    @PostMapping("/products/add-part")
    public ResponseEntity<?> addPart(@RequestBody @Valid AddPartDto addPartDto, BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        boolean success = partService.add(addPartDto, principal);
        if (!success) {
            return ResponseEntity.badRequest().body("noUserLogged");
        }
        return ResponseEntity.ok(Map.of("redirectUrl", "/products"));
    }
}
