package ru.alwertus.digimemb.auth;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class IAuthenticationFacadeTest {

    @Test
    void getCurrentUser() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("Role1"));
        roles.add(new Role("Role2"));
        User user = new User("sLogin", "sPassword", roles);

        IAuthenticationFacade auth = () -> {
            Authentication request = new UsernamePasswordAuthenticationToken("sLogin", "sPassword");
            return new UsernamePasswordAuthenticationToken(user, request.getCredentials(), new ArrayList<>());
        };

        User currentUser = auth.getCurrentUser();

        assertEquals(user, currentUser);
    }
}