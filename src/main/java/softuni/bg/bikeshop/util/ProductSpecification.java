package softuni.bg.bikeshop.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import softuni.bg.bikeshop.models.Bike;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.parts.Part;

import java.util.List;

public class ProductSpecification {
    public static Specification<Product> hasBikeType(List<String> bikeTypes) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Root<Bike> bikeRoot = criteriaBuilder.treat(root, Bike.class);
            return bikeRoot.get("type").in(bikeTypes);
        };
    }

    public static Specification<Product> hasPartType(List<String> partTypes) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Root<Part> partRoot = criteriaBuilder.treat(root, Part.class);
            return partRoot.get("type").in(partTypes);
        };
    }
}
