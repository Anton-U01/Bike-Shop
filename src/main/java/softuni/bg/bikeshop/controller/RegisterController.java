package softuni.bg.bikeshop.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.bg.bikeshop.models.dto.UserRegisterDto;
import softuni.bg.bikeshop.service.UserService;

@Controller
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userRegister")
    public UserRegisterDto createRegisterDto(){
        return new UserRegisterDto();
    }

    @GetMapping("/users/register")
    public String viewRegister(){
        return "register";
    }

    @PostMapping("/users/register")
    public String doRegister(@Valid UserRegisterDto userRegisterDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("userRegister",userRegisterDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegister",bindingResult);

            return "redirect:/users/register";
        }
        if(!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("userRegister", userRegisterDto);
            redirectAttributes.addFlashAttribute("passwordsMisMatch", "Passwords are not the same!");

            return "redirect:/users/register";
        }

        if(userService.checkIfUsernameExists(userRegisterDto.getUsername())){
            redirectAttributes.addFlashAttribute("userRegister",userRegisterDto);
            redirectAttributes.addFlashAttribute("usernameExists","This username already exists!");

            return "redirect:/users/register";
        }
        if(userService.checkIfEmailExists(userRegisterDto.getEmail())){
            redirectAttributes.addFlashAttribute("userRegister",userRegisterDto);
            redirectAttributes.addFlashAttribute("emailExists","This email already exists!");

            return "redirect:/users/register";
        }
        userService.register(userRegisterDto);

        return "redirect:/login";
    }
}
