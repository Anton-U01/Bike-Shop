package softuni.bg.bikeshop.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.validation.BindingResult;
import softuni.bg.bikeshop.models.dto.parts.EditBikeDto;
import softuni.bg.bikeshop.models.dto.parts.EditPartDto;
import softuni.bg.bikeshop.service.PartService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1," +
                                " \"description\": \"aaaaaaaaaaaaaaaaaaaaaaaaaaa\"," +
                                " \"name\": \"tires\"," +
                                " \"price\": \"255\"," +
                                " \"type\": \"TIRES\"," +
                                " \"manufacturer\": \"plovdiv tires\"," +
                                " \"size\": \"18\" }")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redirectUrl").value("/products/my-offers"));
    }
    @Test
    @WithMockUser(username = "test")
    void editPartIfNotSuccessTest() throws Exception {

        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(partService.edit(Mockito.any(EditPartDto.class)))
                .thenReturn(false);

        mockMvc.perform(put("/edit-part")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1," +
                                " \"description\": \"aaaaaaaaaaaaaaaaaaaaaaaaaaa\"," +
                                " \"name\": \"tires\"," +
                                " \"price\": \"255\"," +
                                " \"type\": \"TIRES\"," +
                                " \"manufacturer\": \"plovdiv tires\"," +
                                " \"size\": \"18\" }")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("noPartWithThisId"));
    }
    @Test
    @WithMockUser(username = "test")
    void editPartBindingResultHasErrorsTest() throws Exception {

        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(partService.edit(Mockito.any(EditPartDto.class)))
                .thenReturn(true);

        mockMvc.perform(put("/edit-part")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1," +
                                " \"description\": \"a\"," +
                                " \"name\": \"tires\"," +
                                " \"price\": \"255\"," +
                                " \"type\": \"TIRES\"," +
                                " \"manufacturer\": \"plovdiv tires\"," +
                                " \"size\": \"18\" }")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

}
