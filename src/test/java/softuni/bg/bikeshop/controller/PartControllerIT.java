package softuni.bg.bikeshop.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import softuni.bg.bikeshop.models.dto.parts.EditBikeDto;
import softuni.bg.bikeshop.models.dto.parts.EditPartDto;
import softuni.bg.bikeshop.service.PartService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class PartControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PartService partService;
    @Mock
    private BindingResult bindingResult;

    @Test
    @WithMockUser(username = "test")
    void editPartTestSuccess() throws Exception {

        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(partService.edit(Mockito.any(EditPartDto.class)))
                .thenReturn(true);

        mockMvc.perform(put("/edit-part")
                        .param("id","1")
                        .param("name","tires")
                        .param("description","aaaaaaaaaaaaaaaaaaaaaaaaaaa")
                        .param("price","255")
                        .param("type","TIRES")
                        .param("manufacturer","plovdiv tires")
                        .param("size","18")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/my-offers"));
    }
}
