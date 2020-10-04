package ru.alwertus.digimemb.info.page;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class InfoPageControllerTest {
    private final String FIELD_RESULT = "Result";
    private final String FIELD_ERROR = "Error";
    private final String FIELD_OPERATION = "operation";
    private final String RESULT_ERR = "Error";
    private final String RESULT_OK = "OK";
    private JSONObject rq,
            rs,
            rsExpected;

    @Autowired
    InfoPageController controller;

    @MockBean
    InfoPageService service;

    @BeforeEach
    public void prepare() {
        rq = new JSONObject();
        rs = new JSONObject();
        rsExpected = new JSONObject();
    }

    @Test
    public void response_get() {
        Long id = 1L;
        String html = "OneTwoThree";
        rq.put(FIELD_OPERATION, "get");
        rq.put("id", id);
        InfoPage page = new InfoPage(id);
        page.setHtml(html);

        doReturn(page)
                .when(service)
                .getPage(id);

        rsExpected.put(FIELD_RESULT, RESULT_OK);
        rsExpected.put("html", html);

        sendRqGetRs();

        similarJsons();
    }

    @Test
    public void response_set() {
        Long id = 1L;
        String html = "OneTwoThree";
        rq.put(FIELD_OPERATION, "set");
        rq.put("id", id);
        rq.put("html", html);
        rsExpected.put(FIELD_RESULT, RESULT_OK);

        sendRqGetRs();

        similarJsons();
        verify(service, times(1)).save(id, html);
    }

    @Test
    public void response_del() {
        Long id = 1L;
        rq.put(FIELD_OPERATION, "del");
        rq.put("id", id);
        rsExpected.put(FIELD_RESULT, RESULT_OK);

        sendRqGetRs();

        similarJsons();
        verify(service, times(1)).delete(id);
    }

    @Test
    public void response_unknownOperation() {
        Long id = 1L;
        rq.put(FIELD_OPERATION, "azaza");
        rq.put("id", id);
        rsExpected.put(FIELD_RESULT, RESULT_ERR);
        rsExpected.put(FIELD_ERROR, "Unknown operation");

        sendRqGetRs();

        similarJsons();
    }

    @Test
    public void response_exception() {
        rsExpected.put(FIELD_RESULT, RESULT_ERR);
        rsExpected.put(FIELD_ERROR, "JSONObject[\"id\"] not found.");

        sendRqGetRs();

        similarJsons();
    }

    private void sendRqGetRs() {
        rs = new JSONObject(controller.response(rq.toString()));
    }

    private void similarJsons() {
        System.out.println("RS:       " + rs);
        System.out.println("Excepted: " + rsExpected);
        Assert.assertTrue(rsExpected.similar(rs));
    }
}