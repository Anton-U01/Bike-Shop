package softuni.bg.bikeshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    @GetMapping("/users/login")
    public String viewLogin(){
        return "login";
    }
    @PostMapping("/login-error")
    public String failedLogin(@ModelAttribute("username") String username, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("username", username);
        redirectAttributes.addFlashAttribute("invalidInput", true);

        return "redirect:/users/login";
    }
}
