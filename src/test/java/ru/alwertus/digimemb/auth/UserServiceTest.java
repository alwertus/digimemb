package ru.alwertus.digimemb.auth;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class UserServiceTest {
    private Set<Role> roles;
    private final String defaultRoleName = "ROLE_ANONYMOUS";
    private final String userName = "user";
    private final String userPassword = "passw";
    private User user;

    @Autowired
    UserService userService;

    @MockBean
    BCryptPasswordEncoder passwordEncoder;

    @MockBean
    UserRepo userRepo;

    @MockBean
    RoleRepo roleRepo;

    @BeforeEach
    public void prepare() {
        roles = new HashSet<>();
        roles.add(new Role(defaultRoleName));
        user = new User(userName, null, roles);
    }

    @Test
    public void createUser() {

        when(userRepo.findByName(userName))
                .thenReturn(Optional.empty());

        userService.createUser(userName, userPassword);

        verify(userRepo, times(1))
                .save(user);
    }

    @Test
    public void deleteUser() {
        when(userRepo.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(user));

        userService.deleteUser(userName);

        verify(userRepo, times(1))
                .deleteByName(userName);
    }

    @Test
    public void addRole() {
        String newRoleName = "ROLE_NEW";
        roles.add(new Role(newRoleName));

        userService.addRole(user, newRoleName);

        user.setRoles(roles);

        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void removeRole() {
        userService.removeRole(user, defaultRoleName);

        user.setRoles(new HashSet<>());

        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void removeRole_RoleNotExist() {
        userService.removeRole(user, defaultRoleName + "err");

        user.setRoles(new HashSet<>());

        verify(userRepo, times(0)).save(user);
    }

    @Test
    public void loadUserByUsername() {
        when(userRepo.findByName(userName))
                .thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername(userName);

        Assert.assertEquals(userDetails, user);
    }

    @Test
    public void loadUserByUsername_Exception() {
        when(userRepo.findByName(userName))
                .thenReturn(Optional.empty());

        UsernameNotFoundException thrown = Assert.assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(userName)
        );

        Assert.assertTrue(thrown.getMessage().contains("User not found"));
    }

}