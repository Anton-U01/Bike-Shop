package softuni.bg.bikeshop.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import softuni.bg.bikeshop.models.Bike;

@Repository
public interface BikeRepository extends JpaRepository<Bike,Long>, JpaSpecificationExecutor<Bike> {
}
