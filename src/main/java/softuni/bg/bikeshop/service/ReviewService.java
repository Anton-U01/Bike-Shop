package softuni.bg.bikeshop.service;

import softuni.bg.bikeshop.models.dto.AddReviewDto;
import softuni.bg.bikeshop.models.dto.ViewReviewDto;

import java.util.List;

public interface ReviewService {
    void addReview(AddReviewDto addReviewDto);

    List<ViewReviewDto> getAllReviewsOfProduct(Long productId);
}
