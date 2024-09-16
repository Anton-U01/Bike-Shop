package softuni.bg.bikeshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.bg.bikeshop.models.*;
import softuni.bg.bikeshop.models.parts.PartType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    @Query("SELECT u.favouriteProducts FROM User u WHERE u = :user")
    Set<Product> getFavouritesListByUser(User user);

    @Query("SELECT u.products FROM User u WHERE u = :user")
    List<Product> getAllCurrentUserProducts(User user);

    List<Product> findAllByCreatedOnBefore(LocalDateTime createdOn);

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN Bike b ON p.id = b.id " +
            "LEFT JOIN Part pa ON p.id = pa.id " +
            "WHERE p.name LIKE %:query% " +
            "OR (b IS NOT NULL AND (:query = '' OR CAST(b.type AS string) LIKE %:query%)) " +
            "OR (pa IS NOT NULL AND (:query = '' OR CAST(pa.type AS string) LIKE %:query%))")
    List<Product> findProductByQuery(String query);
}
