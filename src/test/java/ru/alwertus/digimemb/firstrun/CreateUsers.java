package ru.alwertus.digimemb.firstrun;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.alwertus.digimemb.auth.UserService;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateUsers {

    @Autowired
    private UserService userService;

    @Test
    public void createUsers() {
        Map<String, String> users = new HashMap<>();
        users.put("admin", "admin123");
        users.put("user", "user123");
        users.forEach((key, val) -> userService.createUser(key, val));
    }
}
