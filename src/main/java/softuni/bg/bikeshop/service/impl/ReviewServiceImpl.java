package softuni.bg.bikeshop.service.impl;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestClient;
import softuni.bg.bikeshop.models.dto.AddReviewDto;
import softuni.bg.bikeshop.models.dto.ViewReviewDto;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.service.ReviewService;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final RestClient restClient;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(RestClient restClient, ProductRepository productRepository) {
        this.restClient = restClient;
        this.productRepository = productRepository;
    }

    @Override
    public void addReview(AddReviewDto addReviewDto) {

         restClient
                .post()
                .uri("http://localhost:8081/reviews")
                .body(addReviewDto)
                .retrieve();
    }

    @Override
    public List<ViewReviewDto> getAllReviewsOfProduct(Long productId) {
        return restClient
                .get()
                .uri("http://localhost:8081/reviews/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }
}
