package ru.alwertus.digimemb.info.page;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class InfoPageServiceTest {

    @MockBean
    InfoPageRepo db;

    @Autowired
    InfoPageService service;

    @Test
    public void save() {
        Long id = 1L;
        String newText = "Html Text";
        doReturn(Optional.of(new InfoPage(id)))
                .when(db)
                .findById(id);

        service.save(id, newText);

        InfoPage page = new InfoPage();
        page.setId(id);
        page.setHtml(newText);

        verify(db, times(1)).save(page);
    }

    @Test
    public void getPage() {
        Long id = 1L;

        InfoPage page = new InfoPage(id);
        doReturn(Optional.of(page))
                .when(db)
                .findById(id);

        InfoPage receivedInfoPage = service.getPage(id);

        verify(db, times(1)).findById(id);
        Assert.assertEquals(receivedInfoPage, page);
    }

    @Test
    public void delete() {
        Long id = 1L;
        InfoPage page = new InfoPage(id);
        doReturn(Optional.of(page))
                .when(db)
                .findById(id);
        service.delete(id);

        verify(db, times(1)).delete(page);
    }
}