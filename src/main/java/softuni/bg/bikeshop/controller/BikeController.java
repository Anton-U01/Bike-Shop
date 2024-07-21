package softuni.bg.bikeshop.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.bg.bikeshop.exceptions.ProductNotFoundException;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.BikeType;
import softuni.bg.bikeshop.models.dto.AddBikeDto;
import softuni.bg.bikeshop.models.dto.parts.EditBikeDto;
import softuni.bg.bikeshop.service.BikeService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

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
                          Principal principal,
                          @RequestParam("bikeImages") List<MultipartFile> files) throws IOException {

        if (files.get(0).isEmpty()) {
            redirectAttributes.addFlashAttribute("noImage", "Please select an image file to upload!");
        }

        if(bindingResult.hasErrors() || !bikeService.add(addBikeDto,principal,files) || files.get(0).isEmpty()){

            redirectAttributes.addFlashAttribute("addBike",addBikeDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addBike",bindingResult);

            return "redirect:/products/add-bike";
        }


        return "redirect:/products/my-offers";
    }
    @PutMapping("/edit-bike/{id}")
    public String editBike(@PathVariable("id") Long id,
                           @Valid EditBikeDto editBikeDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors() || !bikeService.edit(editBikeDto,id)){

            redirectAttributes.addFlashAttribute("product",editBikeDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.product",bindingResult);

            return "redirect:/products/edit-bike";
        }

        return "redirect:/products/my-offers";
    }
    @GetMapping("/products/edit-bike")
    public String viewEditBike(){
        return "edit-bike";
    }



}
