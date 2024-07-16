package softuni.bg.bikeshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.bikeshop.models.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture,Long> {
}
