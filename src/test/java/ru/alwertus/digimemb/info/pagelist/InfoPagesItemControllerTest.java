package ru.alwertus.digimemb.info.pagelist;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.alwertus.digimemb.auth.AuthenticationFacade;
import ru.alwertus.digimemb.auth.Role;
import ru.alwertus.digimemb.auth.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class InfoPagesItemControllerTest {
    private final String FIELD_RESULT = "Result";
    private final String FIELD_ERROR = "Error";
    private final String FIELD_OPERATION = "operation";
    private final String RESULT_ERR = "Error";
    private final String RESULT_OK = "OK";
    private User user;
    private JSONObject rq,
            rs,
            rsExpected;

    @Autowired
    InfoPagesItemController controller;

    @MockBean
    AuthenticationFacade authenticationFacade;

    @MockBean
    InfoPagesItemRepo db;

    @MockBean
    InfoPagesItemService pages;

    @BeforeEach
    public void prepare() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_ANONYMOUS"));

        user = new User("name", "pass", roles);
        user.setId(1L);

        doReturn(user)
                .when(authenticationFacade)
                .getCurrentUser();

        rq = new JSONObject();
        rs = new JSONObject();
        rsExpected = new JSONObject();
    }

    @Test
    public void request_EmptyRequestBody() {
        rs = new JSONObject(controller.request(rq.toString()));
        rsExpected.put(FIELD_RESULT, RESULT_ERR);
        rsExpected.put(FIELD_ERROR, "Operation is not defined");

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_UserNotDefined() {
        doReturn(null)
                .when(authenticationFacade)
                .getCurrentUser();
        rq.put(FIELD_OPERATION, "create");
        rs = new JSONObject(controller.request(rq.toString()));
        rsExpected.put(FIELD_RESULT, RESULT_ERR);
        rsExpected.put(FIELD_ERROR, "Current User not defined");

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_Create() {
        String title = "Title 1";
        doReturn(2L)
                .when(pages)
                .create(title, 1L, AccessLevel.PRIVATE, user);
        rq.put(FIELD_OPERATION, "create");
        rq.put("title", title);
        rq.put("parentId", 1L);

        rs = new JSONObject(controller.request(rq.toString()));
        rsExpected.put(FIELD_RESULT, RESULT_OK);
        rsExpected.put("ID", 2);

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_Create_WrongParent() {
        String title = "Title 1";
        doReturn(2L)
                .when(pages)
                .create(title, -1L, AccessLevel.PRIVATE, user);
        rq.put(FIELD_OPERATION, "create");
        rq.put("title", title);
        rq.put("parentId", "azaza");

        rs = new JSONObject(controller.request(rq.toString()));
        rsExpected.put(FIELD_RESULT, RESULT_OK);
        rsExpected.put("ID", 2);

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_Get() {
        InfoPagesItem item1 = new InfoPagesItem("Title 1");
        InfoPagesItem item2 = new InfoPagesItem("Title 2");

        List<InfoPagesItem> dbReturnAvailableList = new ArrayList<>();
        dbReturnAvailableList.add(item1);
        dbReturnAvailableList.add(item2);

        rq.put(FIELD_OPERATION, "get");
        doReturn(dbReturnAvailableList)
                .when(pages)
                .getAllAvailableForUser(user);
        JSONArray arr = new JSONArray();
        arr.put(item1.getJSONObject());
        arr.put(item2.getJSONObject());
        rsExpected.put(FIELD_RESULT, RESULT_OK);
        rsExpected.put("List", arr.toString());

        rs = new JSONObject(controller.request(rq.toString()));

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_UpdateTitle_Success() {
        String newTitle = "newTitle";
        rq.put(FIELD_OPERATION, "update");
        rq.put("id", 1);
        rq.put("title", newTitle);
        doReturn(true)
                .when(pages)
                .updateTitle(1L, newTitle);
        rsExpected.put(FIELD_RESULT, RESULT_OK);

        rs = new JSONObject(controller.request(rq.toString()));

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_UpdateTitle_Fail() {
        String newTitle = "newTitle";
        rq.put(FIELD_OPERATION, "update");
        rq.put("id", 1);
        rq.put("title", newTitle);
        doReturn(true)
                .when(pages)
                .updateTitle(2L, newTitle);
        rsExpected.put(FIELD_RESULT, RESULT_ERR);
        rsExpected.put(FIELD_ERROR, "Update record id '" + 1 + "' title to '" + newTitle + "' error");

        rs = new JSONObject(controller.request(rq.toString()));

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_UpdateParentId_Success() {
        rq.put(FIELD_OPERATION, "update");
        rq.put("id", 1);
        rq.put("parentId", 2);
        rsExpected.put(FIELD_RESULT, RESULT_OK);
        doReturn(true)
                .when(pages)
                .updateParentId(1L, 2L);

        rs = new JSONObject(controller.request(rq.toString()));

        System.out.println(rs);

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_UpdateParentId_Fail() {
        rq.put(FIELD_OPERATION, "update");
        rq.put("id", 1);
        rq.put("parentId", 2);
        rsExpected.put(FIELD_RESULT, RESULT_ERR);
        rsExpected.put(FIELD_ERROR, "Update record id '" + 1 + "' parent to '" + 2 + "' error");
        doReturn(false)
                .when(pages)
                .updateParentId(1L, 2L);

        rs = new JSONObject(controller.request(rq.toString()));

        System.out.println(rs);

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_Update_NoChanges() {
        rq.put(FIELD_OPERATION, "update");
        rq.put("id", 1);
        rsExpected.put(FIELD_RESULT, "No Changes");

        rs = new JSONObject(controller.request(rq.toString()));

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_Delete_Success() {
        rq.put(FIELD_OPERATION, "delete");
        rq.put("id", 1);
        rsExpected.put(FIELD_RESULT, RESULT_OK);

        rs = new JSONObject(controller.request(rq.toString()));

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_Delete_Fail() {
        rq.put(FIELD_OPERATION, "delete");
        rq.put("id", "asd");
        rsExpected.put(FIELD_RESULT, RESULT_ERR);
        rsExpected.put(FIELD_ERROR, "Error get id or delete. JSONObject[\"id\"] is not a long.");

        rs = new JSONObject(controller.request(rq.toString()));

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_UnknownOperation() {
        rq.put(FIELD_OPERATION, "azaza");
        rsExpected.put(FIELD_RESULT, RESULT_ERR);
        rsExpected.put(FIELD_ERROR, "Unknown operation");

        rs = new JSONObject(controller.request(rq.toString()));

        assertTrue(rsExpected.similar(rs));
    }

    @Test
    public void request_Exception() {
        rq.put(FIELD_OPERATION, "create");
        rsExpected.put(FIELD_RESULT, RESULT_ERR);
        rsExpected.put(FIELD_ERROR, "Exception! JSONObject[\"title\"] not found.");

        rs = new JSONObject(controller.request(rq.toString()));

        assertTrue(rsExpected.similar(rs));
    }
}