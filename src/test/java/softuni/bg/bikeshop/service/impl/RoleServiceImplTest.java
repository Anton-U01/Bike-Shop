package softuni.bg.bikeshop.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import softuni.bg.bikeshop.models.Role;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.UserRole;
import softuni.bg.bikeshop.repository.RoleRepository;
import softuni.bg.bikeshop.service.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleServiceImplTest {
     private RoleRepository mockedRoleRepository;
     private RoleService roleService;
     private User user;
     private Role adminRole;
     private Role userRole;
     private Role moderatorRole;

     @BeforeEach
     void init(){
          adminRole = new Role();
          adminRole.setName(UserRole.ADMIN);
          userRole = new Role();
          userRole.setName(UserRole.USER);
          moderatorRole = new Role();
          moderatorRole.setName(UserRole.MODERATOR);

          user = new User(){{
               setRoles(Set.of(moderatorRole,userRole));
          }};
          this.mockedRoleRepository = Mockito.mock(RoleRepository.class);
          roleService = new RoleServiceImpl(mockedRoleRepository);
     }

     @Test
     public void getUsersOtherRolesTest(){
          Set<Role> expected = Set.of(adminRole);
          Mockito.when(this.mockedRoleRepository
                  .findAll())
                  .thenReturn(List.of(adminRole,moderatorRole,userRole));

          List<Role> actual = roleService.getUsersOtherRoles(user);
          Assertions.assertEquals(expected.size(),actual.size());
          Assertions.assertTrue(actual.contains(adminRole));
          Assertions.assertFalse(actual.contains(moderatorRole));
          Assertions.assertFalse(actual.contains(userRole));
     }
     @Test
     public void getUsersCurrentRolesTest(){
          Set<Role> expected = Set.of(moderatorRole,userRole);
          List<Role> actual = roleService.getUsersCurrentRoles(user);

          Assertions.assertEquals(expected.size(),actual.size());
          Assertions.assertFalse(actual.contains(adminRole));
          Assertions.assertTrue(actual.contains(moderatorRole));
          Assertions.assertTrue(actual.contains(userRole));
     }
     @Test
     public void getUsersCurrentRolesWhenUserHasNoRoles() {
          User userWithoutRoles = new User();
          userWithoutRoles.setRoles(new HashSet<>());

          List<Role> actualRoles = roleService.getUsersCurrentRoles(userWithoutRoles);

          Assertions.assertTrue(actualRoles.isEmpty());
     }

     @Test
     public void initRolesWhenRepoIsEmpty(){
          List<Role> expected = List.of(adminRole,moderatorRole,userRole);

          Mockito.when(mockedRoleRepository
                  .count())
                  .thenReturn(Long.valueOf(0));
          roleService.initRoles();
          Mockito.when(mockedRoleRepository
                  .findAll())
                  .thenReturn(List.of(adminRole,moderatorRole,userRole));

          List<Role> actual = mockedRoleRepository.findAll();

          Assertions.assertEquals(expected.size(),actual.size());
          Assertions.assertFalse(actual.isEmpty());
          Assertions.assertTrue(actual.contains(adminRole));
          Assertions.assertTrue(actual.contains(moderatorRole));
          Assertions.assertTrue(actual.contains(userRole));
     }


}
