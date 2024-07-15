package softuni.bg.bikeshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.bikeshop.models.Bike;

@Repository
public interface BikeRepository extends JpaRepository<Bike,Long> {
}
