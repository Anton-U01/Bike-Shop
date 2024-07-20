package softuni.bg.bikeshop.service;

import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.UserRegisterDto;
import softuni.bg.bikeshop.models.dto.ViewUserDto;

import java.util.List;

public interface UserService {
    void initFirstUserAsAdmin();

    boolean register(UserRegisterDto userRegisterDto);

    List<ViewUserDto> getAllUser();

    User getUserById(Long id);

    boolean addRole(String username, String role);

    User getUserByUsername(String username);

    boolean removeRole(String username, String role);

    boolean deleteUser(String username);

    void editUsername(String newUsername);

    boolean checkIfUsernameExists(String newUsername);
}
