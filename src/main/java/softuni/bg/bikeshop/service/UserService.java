package softuni.bg.bikeshop.service;

import softuni.bg.bikeshop.models.dto.UserRegisterDto;

public interface UserService {
    void initFirstUserAsAdmin();

    boolean register(UserRegisterDto userRegisterDto);
}
