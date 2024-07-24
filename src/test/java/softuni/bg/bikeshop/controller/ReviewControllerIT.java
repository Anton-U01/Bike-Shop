package softuni.bg.bikeshop.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import softuni.bg.bikeshop.models.dto.ViewReviewDto;
import softuni.bg.bikeshop.service.ReviewService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
public class ReviewControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Test
    @WithMockUser(username = "test")
    void getAllReviewsOfProductTest() throws Exception {
        List<ViewReviewDto> reviewList = new ArrayList<>();
        ViewReviewDto viewReviewDto = new ViewReviewDto();
        reviewList.add(viewReviewDto);

        Mockito.when(reviewService.getAllReviewsOfProduct(1L))
                        .thenReturn(reviewList);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/details/{id}/reviews", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
    @Test
    @WithMockUser(username = "test")
    void addReviewTest() throws Exception {
        String content = "This is a test review content";

        mockMvc.perform(post("/add-review/{id}", 1L)
                        .param("content",content)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/details/" + 1L));


    }
}
