package ru.alwertus.digimemb.auth;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {
    public static final String username = "username";
    public static final String userpassw = "userpassword";
    public static final String userrole = "ROLE_TEST";
    public static User user;

    @Autowired
    private UserService userService;

    @Before
    public void beforeTests() {
        user = userService.createUser(username, userpassw);
    }

    @After
    public void afterTests() {
        userService.deleteUser(username);
    }

    @Test
    public void userServiceNotNull() {
        Assert.assertNotNull(userService);
    }

    @Test
    public void addAndRemoveRole() {
        userService.addRole(user, userrole);
        Assert.assertTrue(user.hasRole(userrole));
        userService.removeRole(user, userrole);
        Assert.assertFalse(user.hasRole(userrole));
        userService.removeRole(user, userrole);
        Assert.assertFalse(user.hasRole(userrole));
    }
}