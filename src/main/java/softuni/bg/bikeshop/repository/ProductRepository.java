package softuni.bg.bikeshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT u.favouriteProducts FROM User u WHERE u = :user")
    Set<Product> getFavouritesListByUser(User user);

    @Query("SELECT u.products FROM User u WHERE u = :user")
    List<Product> getAllCurrentUserProducts(User user);

    List<Product> findAllByCreatedOnBefore(LocalDate localDate);
}
