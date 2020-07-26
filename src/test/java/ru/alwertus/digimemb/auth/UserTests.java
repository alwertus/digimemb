package ru.alwertus.digimemb.auth;

import org.junit.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class UserTests {
    private static User testUser;
    private static final String userName = "TestName";
    private static final String userPassw = "TestPassword";
    private static final String roleName = "ROLE_TEST";

    private static Set<Role> roles;

    @BeforeClass
    public static void initTest() {
        roles = new HashSet<>();
        roles.add(new Role(roleName));
        testUser = new User(userName, userPassw, roles);
    }

    @AfterClass
    public static void afterTest() {
        testUser = null;
    }

    @Test
    public void getterSetter() {
        Assert.assertEquals(testUser.getName(), userName);
        Assert.assertEquals(testUser.getPassword(), userPassw);
        Assert.assertEquals(testUser.getRoles(), roles);
    }

    @Test
    public void getRolesAsString() {
        Assert.assertEquals(testUser.getRolesAsString(), "<" + roleName + ">");
    }
}