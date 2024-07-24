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
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.ViewUserDto;
import softuni.bg.bikeshop.service.UserService;

import java.security.Principal;

import static java.nio.file.Paths.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService mockedUserService;
    @MockBean
    private ModelMapper mockedModelMapper;
    private User user;

    @BeforeEach
    public void init(){
        user = new User(){{setUsername("test");}};
    }

    @Test
    @WithMockUser(username = "test")
    void viewUserProfilePage() throws Exception {
        Principal mockPrincipal = () -> "test";

        Mockito.when(mockedUserService.getUserByUsername(mockPrincipal.getName()))
                .thenReturn(user);

        ViewUserDto viewUserDto = new ViewUserDto();
        Mockito.when(mockedModelMapper.map(user, ViewUserDto.class))
                .thenReturn(viewUserDto);


        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("viewUserDto"))
                .andExpect(model().attribute("viewUserDto", viewUserDto))
                .andExpect(view().name("profile"));
    }
    @Test
    @WithMockUser(username = "testUser")
    void editUsernameWithSuccessTest() throws Exception {
        String newUsername = "newUsername";
        Mockito.when(mockedUserService.checkIfUsernameExists(newUsername))
                .thenReturn(false);

        mockMvc.perform(post("/user/edit-username")
                .param("newUsername",newUsername)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"))
                .andExpect(flash().attributeExists("successfulMessage"));
    }
    @Test
    @WithMockUser(username = "testUser")
    void editUsernameWithFailureTest() throws Exception {
        String newUsername = "newUsername";
        Mockito.when(mockedUserService.checkIfUsernameExists(newUsername))
                .thenReturn(true);

        mockMvc.perform(post("/user/edit-username")
                        .param("newUsername",newUsername)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"))
                .andExpect(flash().attributeExists("usernameExists"));
    }
}
