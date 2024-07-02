package softuni.bg.bikeshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.bikeshop.models.Role;
import softuni.bg.bikeshop.models.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(UserRole userRole);
}
