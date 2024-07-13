package softuni.bg.bikeshop.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.bg.bikeshop.models.BikeType;
import softuni.bg.bikeshop.models.dto.AddBikeDto;
import softuni.bg.bikeshop.service.BikeService;

import java.security.Principal;

@Controller
public class BikeController {
    private final BikeService bikeService;

    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }


    @ModelAttribute("addBike")
    public AddBikeDto createAddBikeDto(){
        return new AddBikeDto();
    }
    @GetMapping("/products/add-bike")
    public String viewAddBike(Model model){
        model.addAttribute("bikeTypes", BikeType.values());
        return "add-bike";
    }
    @PostMapping("/products/add-bike")
    public String addBike(@Valid AddBikeDto addBikeDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Principal principal){
        if(bindingResult.hasErrors() || !bikeService.add(addBikeDto,principal)){

            redirectAttributes.addFlashAttribute("addBike",addBikeDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addBike",bindingResult);

            return "redirect:/products/add-bike";
        }

        return "redirect:/products";
    }
}
