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
import softuni.bg.bikeshop.models.BikeType;
import softuni.bg.bikeshop.models.dto.parts.EditBikeDto;
import softuni.bg.bikeshop.service.BikeService;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BikeControllerIT {
    @MockBean
    private BikeService bikeService;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private BindingResult bindingResult;

    @Test
    @WithMockUser(username = "test")
    void addBikePageTest() throws Exception {
        mockMvc.perform(get("/products/add-bike"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("addBike"))
                .andExpect(model().attributeExists("bikeTypes"))
                .andExpect(view().name("add-bike"))
                .andExpect(model().attribute("bikeTypes", BikeType.values()));
    }

    @Test
    @WithMockUser(username = "test")
    void editBikeTestSuccessful() throws Exception {
        Long productId = 1L;

        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(bikeService.edit(Mockito.any(EditBikeDto.class), Mockito.eq(productId)))
                .thenReturn(true);

        mockMvc.perform(put("/edit-bike/{id}",productId)
                        .param("name","bike")
                        .param("description","aaaaaaaaaaaaaaaaaaaaaaaaaaa")
                        .param("price","255")
                        .param("type","ROAD")
                        .param("frame","long alum")
                        .param("brakes","strong")
                        .param("wheelsSize","18")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/my-offers"));
    }
    @Test
    @WithMockUser(username = "test")
    void editBikeTestFail() throws Exception {
        Long productId = 1L;

        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(bikeService.edit(Mockito.any(EditBikeDto.class), Mockito.eq(productId)))
                .thenReturn(false);

        mockMvc.perform(put("/edit-bike/{id}",productId)
                        .param("name","b")
                        .param("description","aaaaaaaaaaaaaaaaaaaaaaaaaaa")
                        .param("price","255")
                        .param("type","ROAD")
                        .param("frame","long alum")
                        .param("brakes","strong")
                        .param("wheelsSize","18")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/edit-bike"));
    }
}

