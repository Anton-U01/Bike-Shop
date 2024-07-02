package softuni.bg.bikeshop.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import softuni.bg.bikeshop.service.RoleService;
import softuni.bg.bikeshop.service.UserService;

@Component
public class DataBaseInit implements CommandLineRunner {
    private final RoleService roleService;
    private final UserService userService;

    public DataBaseInit(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        roleService.initRoles();
        userService.initFirstUserAsAdmin();
    }
}
