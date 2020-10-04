package ru.alwertus.digimemb.auth;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class LoginControllerTest {
    private JSONObject rq,
            rs,
            rsExpected;

    @Autowired
    LoginController controller;

    @MockBean
    UserService userservice;

    @MockBean
    BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void prepare() {
        rq = new JSONObject();
        rs = new JSONObject();
        rsExpected = new JSONObject();
    }

    @Test
    void tryToLogin_ParseError() {
        assertEquals("Error parse request", controller.tryToLogin("azaza"));
    }

    @Test
    void tryToLogin_noOperation() {
        rq.put("one", "two");
        assertEquals("Operation not specified", controller.tryToLogin(rq.toString()));
    }

    @Test
    void tryToLogin_wrongRequest() {
        rq.put("operation", "azaza");
        rsExpected.put("html", "Wrong request");
        rsExpected.put("status", 401);
        rs = new JSONObject(controller.tryToLogin(rq.toString()));
        assertTrue(rsExpected.similar(rs));
    }

    @Test
    void tryToLogin_nameNotFound() {
        rq.put("operation", "login");
        assertEquals("org.json.JSONException: JSONObject[\"name\"] not found.", controller.tryToLogin(rq.toString()));
    }

    @Test
    void tryToLogin_passwordNotFound() {
        rq.put("operation", "login");
        rq.put("name", "one");
        assertEquals("org.json.JSONException: JSONObject[\"password\"] not found.", controller.tryToLogin(rq.toString()));
    }

    @Test
    void tryToLogin_Success() {
        rq.put("operation", "login");
        rq.put("name", "name");
        rq.put("password", "pass");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_TEST"));

        Mockito.when(userservice.loadUserByUsername("name"))
                .thenReturn(new User("name", "pass", roles));

        assertEquals("{\"roles\":[[{\"name\":\"ROLE_TEST\"}]],\"userName\":\"name\",\"status\":200}",
                controller.tryToLogin(rq.toString()));
    }

    @Test
    void tryToLogin_ErrorAuth() {
        rq.put("operation", "login");
        rq.put("name", "wrongname");
        rq.put("password", "pasdass");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_TEST"));

        Mockito.when(userservice.loadUserByUsername("name"))
                .thenReturn(new User("name", "pass", roles));

        assertEquals("{\"html\":\"User/password not found\",\"status\":401}",
                controller.tryToLogin(rq.toString()));
    }
}