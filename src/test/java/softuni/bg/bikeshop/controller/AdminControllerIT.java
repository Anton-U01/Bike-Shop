package softuni.bg.bikeshop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.ViewUserDto;
import softuni.bg.bikeshop.service.RoleService;
import softuni.bg.bikeshop.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class AdminControllerIT {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private UserService userService;
    private Principal principal;
    private User user;

    @BeforeEach
    void init(){
        principal = Mockito.mock();
        Mockito.when(principal.getName()).thenReturn("test");

        user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setFullName("test testov");
        user.setEmail("test@abv.bg");
        user.setAge(19);
    }

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    void viewAdminPageTest() throws Exception {
        List<ViewUserDto> allUsers = new ArrayList<>();
        ViewUserDto viewUserDto = new ViewUserDto();
        viewUserDto.setId(1L);
        viewUserDto.setUsername("test");
        viewUserDto.setFullName("test testov");
        viewUserDto.setEmail("test@abv.bg");
        viewUserDto.setAge(19);
        allUsers.add(viewUserDto);

        when(userService.getAllUser()).thenReturn(allUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allUsers",allUsers))
                .andExpect(view().name("admin"));
    }
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void deleteLoggedUserTest() throws Exception {
        String username = "test";

                mockMvc
                        .perform(MockMvcRequestBuilders.delete("/admin/delete-user/{username}",username)
                        .principal(principal)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attribute("thisUserIsLogged", "The user you want to delete is currently logged!"))
                .andReturn();

        verify(userService, never()).deleteUser(anyString());

    }
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void deleteUserHasActiveOffersTest() throws Exception {
        String username = "test1";
        when(userService.deleteUser(username))
                .thenReturn(false);

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/admin/delete-user/{username}",username)
                        .principal(principal)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attribute("userHasActiveProducts","The user you want to delete has active offers!"))
                .andReturn();

    }
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void deleteSuccessTest() throws Exception {
        String username = "test1";
        when(userService.deleteUser(username))
                .thenReturn(true);

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/admin/delete-user/{username}",username)
                        .principal(principal)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attribute("deletedUser",username))
                .andReturn();

    }
    @Test
    @WithMockUser(username = "test",roles = {"ADMIN"})
    void addRoleFailureTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/add-role/{username}", "testUser")
                        .param("role", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/add-role/testUser"))
                .andExpect(flash().attributeExists("userWithRole"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userWithRole"))
                .andExpect(flash().attribute("noRoleSelected", true));
    }
    @Test
    @WithMockUser(username = "test",roles = {"ADMIN"})
    void addRoleSuccessTest() throws Exception {
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(userService.addRole(anyString(), anyString())).thenReturn(true);
        when(bindingResult.hasErrors())
                .thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/add-role/{username}", "testUser")
                        .param("role", "ADMIN")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

    }

}
