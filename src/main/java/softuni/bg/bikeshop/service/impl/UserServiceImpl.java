package softuni.bg.bikeshop.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.*;
import softuni.bg.bikeshop.models.dto.UserRegisterDto;
import softuni.bg.bikeshop.models.dto.ViewUserDto;
import softuni.bg.bikeshop.repository.AddressRepository;
import softuni.bg.bikeshop.repository.ProductRepository;
import softuni.bg.bikeshop.repository.RoleRepository;
import softuni.bg.bikeshop.repository.UserRepository;
import softuni.bg.bikeshop.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserDetailsService userDetailsService;
    private final RestClient restClient;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, UserDetailsService userDetailsService, RestClient restClient, ProductRepository productRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.userDetailsService = userDetailsService;
        this.restClient = restClient;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
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
    public void register(UserRegisterDto userRegisterDto) {
        User user = modelMapper.map(userRegisterDto,User.class);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        Role role = roleRepository.findByName(UserRole.USER);
        user.getRoles().add(role);

        Address address = addressRepository.findByStreetAndCityAndPostalCode(
                userRegisterDto.getStreet(),
                userRegisterDto.getCity(),
                userRegisterDto.getPostalCode()
        ).orElseGet(() -> {
            Address newAddress = new Address();
            newAddress.setCountry(userRegisterDto.getCountry());
            newAddress.setStreet(userRegisterDto.getStreet());
            newAddress.setCity(userRegisterDto.getCity());
            newAddress.setPostalCode(userRegisterDto.getPostalCode());
            return addressRepository.save(newAddress);
        });

        user.setAddress(address);

        userRepository.saveAndFlush(user);
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
                new UserNotFoundException("No such user with id " + id + " found!")
        );
    }

    @Override
    @Transactional
    public boolean addRole(String username, String role) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow( () -> new UserNotFoundException("User with username " + username + " was not found"));
        Role addRole = roleRepository.findByName(UserRole.valueOf(role));
        user.getRoles().add(addRole);
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public User getUserByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + " was not found!"));
    }

    @Override
    @Transactional
    public boolean removeRole(String username, String role) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + " was not found!"));

        Role roleForRemove = roleRepository.findByName(UserRole.valueOf(role));
        user.getRoles().remove(roleForRemove);
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + " was not found!"));

        if(!user.getProducts().isEmpty()){
            return false;
        }
        user.getReviews().forEach(r -> {
                restClient
                        .delete()
                        .uri("http://localhost:8081/reviews/" + r.getId())
                        .retrieve();
        });
        user.getFavouriteProducts().forEach(p -> {
            p.getIsFavouriteOf().remove(user);
            if(p.getIsFavouriteOf().isEmpty()){
                p.setFavourite(false);
            }
            productRepository.saveAndFlush(p);
        });
        user.getFavouriteProducts().clear();

        userRepository.delete(user);
        Address address = user.getAddress();
        if (address != null) {
            List<User> usersWithAddress = userRepository.findAllByAddress(address);

            if (usersWithAddress.isEmpty()) {
                addressRepository.delete(address);
            }
        }
        return true;
    }

    @Override
    public void editUsername(String newUsername) {
        UserDetailEntity userDetails = (UserDetailEntity)  userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User with username " + userDetails.getUsername() + " was not found!"));


        user.setUsername(newUsername);
        userDetails.setUsername(user.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        userRepository.saveAndFlush(user);

    }

    @Override
    public boolean checkIfUsernameExists(String newUsername) {
        Optional<User> optionalUser = userRepository.findByUsername(newUsername);
        return optionalUser.isPresent();
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.isPresent();
    }


}
