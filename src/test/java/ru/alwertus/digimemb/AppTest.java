package ru.alwertus.digimemb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
    @MockBean
    SpringApplication springApplication;

    @Test
    public void mainTest() {
        String[] args = new String[0];
        App.main(args);
    }
}
