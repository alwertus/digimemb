package ru.alwertus.digimemb.info.pagelist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.alwertus.digimemb.auth.IAuthenticationFacade;
import ru.alwertus.digimemb.auth.User;
import ru.alwertus.digimemb.info.page.InfoPageService;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InfoPagesItemServiceTest {
    private final Long TEST_ID = 1L;
    private final Long TEST_ID2 = 10L;
    private final String TEST_TITLE = "Title";
    private final InfoPagesItem TEST_PAGE_ITEM = new InfoPagesItem();

    @MockBean
    InfoPagesItemRepo db;

    @MockBean
    InfoPagesItem infoPagesItem;

    @MockBean
    IAuthenticationFacade authenticationFacade;

    @MockBean
    InfoPageService pageService;

    @Autowired
    InfoPagesItemService pagesService;

    @Test
    @Description("Test Create with 4 params")
    public void createNewPageItem() {
        InfoPagesItem parentObj = new InfoPagesItem("parent");
        parentObj.setId(TEST_ID);
        InfoPagesItem ipi = new InfoPagesItem(TEST_TITLE, parentObj, AccessLevel.ALL, new User());
        doReturn(Optional.of(parentObj))
                .when(db)
                .findById(TEST_ID);

        pagesService.create(TEST_TITLE, TEST_ID, AccessLevel.ALL, new User());

        verify(db, times(1))
                .save(ArgumentMatchers.eq(ipi));
    }

    @Test
    @Description("Test Create with 4 params and parent = null")
    public void createNewPageItem_NoParent() {
        InfoPagesItem ipi = new InfoPagesItem(TEST_TITLE, null, AccessLevel.ALL, new User());

        pagesService.create(TEST_TITLE, null, AccessLevel.ALL, new User());

        verify(db, times(1)).save(ArgumentMatchers.eq(ipi));
    }

    @Test
    @Description("Test Create with 3 params")
    public void createNewPageItem_CurrentUser() {
        InfoPagesItem parentObj = new InfoPagesItem("parent");
        parentObj.setId(TEST_ID);
        User user = new User();
        InfoPagesItem ipi = new InfoPagesItem(TEST_TITLE, parentObj, AccessLevel.ALL, user);
        doReturn(Optional.of(parentObj))
                .when(db)
                .findById(TEST_ID);

        doReturn(user)
                .when(authenticationFacade)
                .getCurrentUser();

        pagesService.create(TEST_TITLE, TEST_ID, AccessLevel.ALL);

        verify(db, times(1))
                .save(ArgumentMatchers.eq(ipi));
    }

    @Test
    public void createNewPageItem_1param() {
        pagesService.create(TEST_TITLE);

        InfoPagesItem ipi = new InfoPagesItem(TEST_TITLE);

        verify(db, times(1))
                .save(ArgumentMatchers.eq(ipi));
    }

    @Test
    @Description("Delete - success")
    public void delete() {
        InfoPagesItem pageItem = new InfoPagesItem(TEST_TITLE);
        pageItem.setId(TEST_ID);
        doReturn(Optional.of(pageItem))
                .when(db)
                .findById(TEST_ID);

        pagesService.delete(TEST_ID);

        verify(db, times(1))
                .delete(ArgumentMatchers.eq(pageItem));
        verify(pageService, times(0))
                .delete(TEST_ID);
    }

    @Test
    public void delete_WrongId() {
        InfoPagesItem pageItem = new InfoPagesItem(TEST_TITLE);
        pageItem.setId(TEST_ID);
        doReturn(Optional.of(pageItem))
                .when(db)
                .findById(TEST_ID);

        pagesService.delete(TEST_ID2);

        verify(db, times(0)).delete(ArgumentMatchers.any());
    }

    @Test
    @Description("getById - success")
    public void getById() {
        doReturn(Optional.of(TEST_PAGE_ITEM))
                .when(db)
                .findById(TEST_ID);

        InfoPagesItem returnPageItem = pagesService.getById(TEST_ID);

        assertEquals(TEST_PAGE_ITEM, returnPageItem);
    }

    @Test
    public void getById_NotFind() {
        doReturn(Optional.of(TEST_PAGE_ITEM))
                .when(db)
                .findById(TEST_ID);

        InfoPagesItem returnPageItem = pagesService.getById(TEST_ID2);

        assertNotEquals(TEST_PAGE_ITEM, returnPageItem);
        assertNull(returnPageItem);
    }

    @Test
    public void updateTitle() {
        String newTitle = "New Title";
        InfoPagesItem changedPageItem = new InfoPagesItem();
        changedPageItem.setTitle(newTitle);
        doReturn(Optional.of(TEST_PAGE_ITEM))
                .when(db)
                .findById(TEST_ID);

        boolean result = pagesService.updateTitle(TEST_ID, newTitle);

        assertTrue(result);
        verify(db, times(1)).save(ArgumentMatchers.eq(changedPageItem));
    }

    @Test
    public void updateTitle_WrongId() {
        String newTitle = "New Title";
        InfoPagesItem changedPageItem = new InfoPagesItem();
        changedPageItem.setTitle(newTitle);
        doReturn(Optional.of(TEST_PAGE_ITEM))
                .when(db)
                .findById(TEST_ID);

        boolean result = pagesService.updateTitle(TEST_ID2, newTitle);

        assertFalse(result);
        verify(db, times(0)).save(ArgumentMatchers.any());
    }

    @Test
    public void updateParentId() {
        InfoPagesItem item1 = new InfoPagesItem();
        doReturn(Optional.of(item1))
                .when(db)
                .findById(TEST_ID);

        InfoPagesItem item2 = new InfoPagesItem();
        item2.setTitle("parent");
        doReturn(Optional.of(item2))
                .when(db)
                .findById(TEST_ID2);

        boolean result = pagesService.updateParentId(TEST_ID, TEST_ID2);
        item1.setParentItem(item2);

        assertTrue(result);
        verify(db, times(1)).save(ArgumentMatchers.eq(item1));
    }

    @Test
    public void updateParentId_WrongId() {
        InfoPagesItem item1 = new InfoPagesItem();
        doReturn(Optional.of(item1))
                .when(db)
                .findById(TEST_ID);

        InfoPagesItem item2 = new InfoPagesItem();
        item2.setTitle("parent");
        doReturn(Optional.of(item2))
                .when(db)
                .findById(TEST_ID2);

        boolean result = pagesService.updateParentId(TEST_ID + 1, TEST_ID2 + 1);
        item1.setParentItem(item2);

        assertFalse(result);
        verify(db, times(0)).save(ArgumentMatchers.any());
    }

    @Test
    public void getAllAvailableForUser() {
        List<InfoPagesItem> pages = new LinkedList<>();
        pages.add(TEST_PAGE_ITEM);

        User user = new User();

        doReturn(pages)
                .when(db)
                .findAllByUserId(user);

        List<InfoPagesItem> returnPages = (List<InfoPagesItem>) pagesService.getAllAvailableForUser(user);

        assertEquals(pages, returnPages);
        verify(db, times(1)).findAllByUserId(user);
    }

    /*private void mockAuth() {
        String TEST_USERNAME = "uu";
        String TEST_PASSWORD = "pp";
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_TEST"));
        Mockito.doReturn(
                new User(TEST_USERNAME,
                        passwordEncoder.encode(TEST_PASSWORD),
                        roles))
                .when(userService)
                .loadUserByUsername(TEST_USERNAME);
    }*/

}