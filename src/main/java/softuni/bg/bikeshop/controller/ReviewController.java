package softuni.bg.bikeshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softuni.bg.bikeshop.models.dto.AddReviewDto;
import softuni.bg.bikeshop.models.dto.ViewReviewDto;
import softuni.bg.bikeshop.service.ReviewService;

import java.security.Principal;
import java.util.List;

@Controller
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/products/details/{id}/reviews")
    @ResponseBody
    public List<ViewReviewDto> allReviews(@PathVariable("id") Long productId){
        return reviewService.getAllReviewsOfProduct(productId);
    }

    @PostMapping("/add-review/{id}")
    public String addReview(@PathVariable("id") Long productId,
                            @RequestParam("content") String content,
                            Principal principal){
        AddReviewDto addReviewDto = new AddReviewDto();
        addReviewDto.setContent(content);
        addReviewDto.setProduct(productId);
        addReviewDto.setAuthorUsername(principal.getName());

        reviewService.addReview(addReviewDto);

        return "redirect:/products/details/" + productId;
    }
}
