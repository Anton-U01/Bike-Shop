package softuni.bg.bikeshop.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.bg.bikeshop.models.Role;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.UserDetailEntity;
import softuni.bg.bikeshop.models.UserRole;
import softuni.bg.bikeshop.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(username)
                .map(this::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " is not found!"));
    }

    private UserDetails map(User user) {
        return new UserDetailEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFullName(),
                user.getAge(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).map(UserDetailsServiceImpl::map).toList()
        );
    }

    private static GrantedAuthority map(UserRole role){
        return new SimpleGrantedAuthority("ROLE_" + role);
    }
}
