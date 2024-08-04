package softuni.bg.bikeshop.controller;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.ViewUserDto;
import softuni.bg.bikeshop.service.UserService;

import java.security.Principal;

@Controller
public class UserController {
    private UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }
    @ModelAttribute("viewUserDto")
    public ViewUserDto createDto(){
        return new ViewUserDto();
    }

    @GetMapping("/user/profile")
    public String viewProfile(Principal principal, Model model){
        User currentUser = userService.getUserByUsername(principal.getName());
        ViewUserDto viewUserDto = modelMapper.map(currentUser,ViewUserDto.class);
        model.addAttribute(viewUserDto);

        return "profile";
    }
    @PostMapping("/user/edit-username")
    public String editUsername(@RequestParam("newUsername") String newUsername,
                               RedirectAttributes redirectAttributes){

        boolean usernameExists = userService.checkIfUsernameExists(newUsername);
        if(usernameExists){
            redirectAttributes.addFlashAttribute("usernameExists","This username already exists!");

            return "redirect:/user/profile";
        }
        if(newUsername.length() < 4 || newUsername.length() > 20){
            redirectAttributes.addFlashAttribute("usernameLengthError","Username must be between 4 and 20 symbols!");

            return "redirect:/user/profile";
        }

        userService.editUsername(newUsername);
        redirectAttributes.addFlashAttribute("successfulMessage","Username is successfully updated!");
        return "redirect:/user/profile";
    }


}
