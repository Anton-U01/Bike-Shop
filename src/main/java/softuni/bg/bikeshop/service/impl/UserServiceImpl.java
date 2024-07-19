package softuni.bg.bikeshop.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.bg.bikeshop.models.Role;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.UserRole;
import softuni.bg.bikeshop.models.dto.UserRegisterDto;
import softuni.bg.bikeshop.models.dto.ViewUserDto;
import softuni.bg.bikeshop.repository.RoleRepository;
import softuni.bg.bikeshop.repository.UserRepository;
import softuni.bg.bikeshop.service.UserService;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public void initFirstUserAsAdmin() {
        if(userRepository.count() > 0){
            return;
        }

        User firstUser = new User();
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository.findByName(UserRole.ADMIN);
        roleRepository.saveAndFlush(adminRole);
        roles.add(adminRole);

        firstUser.setUsername("admin");
        firstUser.setPassword(passwordEncoder.encode("admin"));
        firstUser.setAge(35);
        firstUser.setEmail("admin@example.com");
        firstUser.setFullName("admin adminov");
        firstUser.setRoles(roles);

        userRepository.saveAndFlush(firstUser);
    }

    @Override
    public boolean register(UserRegisterDto userRegisterDto) {
        if(!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())){
            return false;
        }
        Optional<User> byUsername = userRepository.findByUsername(userRegisterDto.getUsername());
        if(byUsername.isPresent()){
            return false;
        }
        Optional<User> byEmail = userRepository.findByEmail(userRegisterDto.getEmail());
        if (byEmail.isPresent()){
            return false;
        }
        User user = modelMapper.map(userRegisterDto,User.class);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        Role role = roleRepository.findByName(UserRole.USER);
        user.getRoles().add(role);

        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public List<ViewUserDto> getAllUser() {
        return userRepository
                .findAll()
                .stream()
                .map(user -> modelMapper.map(user,ViewUserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow( () ->
                new NullPointerException("No such user found!")
        );
    }

    @Override
    @Transactional
    public boolean addRole(String username, String role) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            return false;
        }
        User user = optionalUser.get();
        Role addRole = roleRepository.findByName(UserRole.valueOf(role));
        user.getRoles().add(addRole);
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(NullPointerException::new);
    }

    @Override
    @Transactional
    public boolean removeRole(String username, String role) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            return false;
        }
        User user = optionalUser.get();
        Role roleForRemove = roleRepository.findByName(UserRole.valueOf(role));
        user.getRoles().remove(roleForRemove);
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public void deleteUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            return;
        }
        User user = optionalUser.get();
        userRepository.delete(user);
    }


}
