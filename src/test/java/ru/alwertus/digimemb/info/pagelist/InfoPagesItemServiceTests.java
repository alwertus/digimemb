package ru.alwertus.digimemb.info.pagelist;

import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.alwertus.digimemb.auth.User;
import ru.alwertus.digimemb.auth.UserService;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class InfoPagesItemServiceTests {
    public static final String username = "username";
    public static final String userpassw = "userpassword";
    public static User user;

    @Autowired
    private UserService userService;

    @Autowired
    private InfoPagesItemService pages;

    @Before
    public void beforeTests() {
        user = userService.createUser(username, userpassw);
    }

    @After
    public void afterTests() {
        userService.deleteUser(username);
    }

    @Test
    public void createPageItem() {
        Long createdIdTitle = pages.create("testTitle0");
        Long createdIdFull = pages.create("testTitle1", null, AccessLevel.PRIVATE, user);

        Assert.assertNotNull(pages.getById(createdIdTitle));
        Assert.assertNotNull(pages.getById(createdIdFull));

        pages.delete(createdIdTitle);
        pages.delete(createdIdFull);

        Assert.assertNull(pages.getById(createdIdTitle));
    }
}
