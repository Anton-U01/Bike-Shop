package softuni.bg.bikeshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.bikeshop.models.parts.Part;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
}
