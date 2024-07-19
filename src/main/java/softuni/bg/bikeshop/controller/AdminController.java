package softuni.bg.bikeshop.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.UserWithRoleDto;
import softuni.bg.bikeshop.models.dto.ViewRoleDto;
import softuni.bg.bikeshop.models.dto.ViewUserDto;
import softuni.bg.bikeshop.service.RoleService;
import softuni.bg.bikeshop.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    public AdminController(UserService userService, RoleService roleService, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/admin")
    public String viewAdminPage(Model model){
        List<ViewUserDto> allUsers = userService.getAllUser();
        model.addAttribute("allUsers",allUsers);
        return "admin";
    }
    @ModelAttribute("userWithRole")
    public UserWithRoleDto createDto(){
        return new UserWithRoleDto();
    }
    @GetMapping("/admin/add-role/{username}")
    @Transactional
    public String getAddRole(@PathVariable("username") String username,Model model){
        User myUser = userService.getUserByUsername(username);
        UserWithRoleDto dto = modelMapper.map(myUser, UserWithRoleDto.class);
        List<ViewRoleDto> otherRoles = roleService.getUsersRoles(myUser)
                .stream().map(role -> modelMapper.map(role, ViewRoleDto.class))
                .collect(Collectors.toList());
        model.addAttribute("otherRoles",otherRoles);
        model.addAttribute("userWithRole",dto);
        return "add-role";
    }

    @PostMapping("/admin/add-role/{username}")
    public String addRole(@Valid UserWithRoleDto userWithRoleDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @PathVariable("username")String username
    ){
        if(bindingResult.hasErrors() || !userService.addRole(username,userWithRoleDto.getRole())){
            redirectAttributes.addFlashAttribute("userWithRole",userWithRoleDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userWithRole", bindingResult);
            redirectAttributes.addFlashAttribute("noRoleSelected",true);
            return "redirect:/admin/add-role/{username}";
        }
        return "redirect:/admin";
    }
}
