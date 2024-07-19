package softuni.bg.bikeshop.service;

import softuni.bg.bikeshop.models.Role;
import softuni.bg.bikeshop.models.User;

import java.util.List;

public interface RoleService {
    void initRoles();
    List<Role> getUsersOtherRoles(User user);

    List<Role> getUsersCurrentRoles(User myUser);
}
