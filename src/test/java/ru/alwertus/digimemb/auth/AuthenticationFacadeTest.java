package ru.alwertus.digimemb.auth;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class AuthenticationFacadeTest {

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Test
    void getAuthentication() {
        assertEquals(SecurityContextHolder.getContext().getAuthentication(), authenticationFacade.getAuthentication());
    }
}