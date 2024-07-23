package softuni.bg.bikeshop.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import softuni.bg.bikeshop.exceptions.UserNotFoundException;
import softuni.bg.bikeshop.models.Role;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.UserRole;
import softuni.bg.bikeshop.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Mock
    private UserRepository mockUserRepository;
    private User user;

    @BeforeEach
    void init(){
        user = new User(){{
            setUsername("Ivan");
            setPassword("Ivan123");
            setAge(20);
            setEmail("ivan@abv.bg");
            setFullName("Ivan Ivanov");
            setRoles(Set.of(new Role(){{setName(UserRole.USER);}}));
        }};
    }

    @Test
    void loadUserByUsernameWhenUserExists(){
        Mockito.when(this.mockUserRepository
                .findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("Ivan");

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("Ivan",userDetails.getUsername());
        Assertions.assertEquals(user.getRoles().size(),userDetails.getAuthorities().size());
    }
    @Test
    void loadUserByUsernameWhenUserDoesNotExist() {
        Mockito.when(mockUserRepository.findByUsername("NotExists"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> userDetailsServiceImpl.loadUserByUsername("NotExists"));
    }
}

