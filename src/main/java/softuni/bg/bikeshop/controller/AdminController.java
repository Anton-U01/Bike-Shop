package softuni.bg.bikeshop.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.UserWithRoleDto;
import softuni.bg.bikeshop.models.dto.ViewRoleDto;
import softuni.bg.bikeshop.models.dto.ViewUserDto;
import softuni.bg.bikeshop.service.RoleService;
import softuni.bg.bikeshop.service.UserService;

import java.security.Principal;
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
        List<ViewRoleDto> otherRoles = roleService.getUsersOtherRoles(myUser)
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
    @GetMapping("/admin/remove-role/{username}")
    @Transactional
    public String getRemoveRole(@PathVariable("username") String username,Model model){
        User myUser = userService.getUserByUsername(username);
        UserWithRoleDto dto = modelMapper.map(myUser, UserWithRoleDto.class);
        List<ViewRoleDto> currentRoles = roleService.getUsersCurrentRoles(myUser)
                .stream().map(role -> modelMapper.map(role, ViewRoleDto.class))
                .toList();
        model.addAttribute("currentRoles",currentRoles);
        model.addAttribute("userWithRole",dto);
        return "remove-role";
    }
    @PostMapping("/admin/remove-role/{username}")
    public String removeRole(@Valid UserWithRoleDto userWithRoleDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          @PathVariable("username")String username
    ){
        if(bindingResult.hasErrors() || !userService.removeRole(username,userWithRoleDto.getRole())){
            redirectAttributes.addFlashAttribute("userWithRole",userWithRoleDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userWithRole", bindingResult);
            redirectAttributes.addFlashAttribute("noRoleSelected",true);
            return "redirect:/admin/add-role/{username}";
        }
        return "redirect:/admin";
    }
    @DeleteMapping("/admin/delete-user/{username}")
    public String deleteUser(@PathVariable("username")String username, Principal principal,RedirectAttributes redirectAttributes){

        if(username.equals(principal.getName())){
            redirectAttributes.addFlashAttribute("thisUserIsLogged","The user you want to delete is currently logged!");
            return "redirect:/admin";
        }
        boolean success = userService.deleteUser(username);
        if(!success){
            redirectAttributes.addFlashAttribute("userHasActiveProducts","The user you want to delete has active offers!");
            return "redirect:/admin";
        }
        redirectAttributes.addFlashAttribute("deletedUser",username);
        return "redirect:/admin";
    }
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFound(UserNotFoundException e){
        ModelAndView modelAndView = new ModelAndView("object-not-found");
        modelAndView.addObject("errorMessage",e.getMessage());

        return modelAndView;
    }
}
