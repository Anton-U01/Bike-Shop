package softuni.bg.bikeshop.service.impl;

import org.springframework.stereotype.Service;
import softuni.bg.bikeshop.models.Role;
import softuni.bg.bikeshop.models.UserRole;
import softuni.bg.bikeshop.service.RoleService;
import softuni.bg.bikeshop.repository.RoleRepository;

import java.util.Arrays;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public void initRoles(){
        if(roleRepository.count() > 0){
            return;
        }

        Arrays.stream(UserRole.values()).forEach(r -> {
            Role role = new Role();
            role.setName(r);
            roleRepository.saveAndFlush(role);
        });
    }
}
