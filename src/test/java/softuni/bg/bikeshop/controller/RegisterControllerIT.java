package softuni.bg.bikeshop.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import softuni.bg.bikeshop.models.dto.UserRegisterDto;
import softuni.bg.bikeshop.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class RegisterControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Mock
    private BindingResult bindingResult;

    @Test
    void registerPageTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userRegister"))
                .andExpect(view().name("register"));
    }
    @Test
    void registerSuccessfulTest() throws Exception {


        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.register(Mockito.any(UserRegisterDto.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .param("username", "test")
                        .param("fullName", "test testov")
                        .param("age", "20")
                        .param("email", "test@abv.bg")
                        .param("password", "test123")
                        .param("confirmPassword", "test123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
    @Test
    void registerFailureWithValidationTest() throws Exception {


        when(bindingResult.hasErrors()).thenReturn(true);
        when(userService.register(Mockito.any(UserRegisterDto.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .param("username", "t")
                        .param("fullName", "test testov")
                        .param("age", "20")
                        .param("email", "test@abv.bg")
                        .param("password", "test123")
                        .param("confirmPassword", "test123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));
    }
}

