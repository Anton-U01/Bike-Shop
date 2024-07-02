package softuni.bg.bikeshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.bikeshop.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
