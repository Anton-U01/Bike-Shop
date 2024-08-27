package softuni.bg.bikeshop.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.bg.bikeshop.models.BikeType;
import softuni.bg.bikeshop.models.dto.AddBikeDto;
import softuni.bg.bikeshop.models.dto.EditBikeDto;
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

            return "redirect:/products/add-bike";
        }

        for (MultipartFile file : files) {
            String contentType = file.getContentType();

            if (!("image/png".equals(contentType) || "image/jpeg".equals(contentType))) {
                redirectAttributes.addFlashAttribute("invalidFileType", "Only PNG and JPG images are allowed!");
                return "redirect:/products/add-bike";
            }
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
