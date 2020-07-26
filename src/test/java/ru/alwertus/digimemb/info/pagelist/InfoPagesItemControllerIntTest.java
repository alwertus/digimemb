package ru.alwertus.digimemb.info.pagelist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InfoPagesItemControllerIntTest {
    private final String TEST_USERNAME = "user";
    private final String TEST_PASSWORD = "user123";
    private final String URL = "http://localhost:5188/infopages";
    private final RequestBody rq = new RequestBody();

    @Autowired
    private MockMvc mockMvc;    // подмена Spring MVC

    @Test
    public void getResponse_OK() throws Exception {
        RequestBody requestBody = new RequestBody();
        requestBody.setOperation("get");

        this.mockMvc.perform(post(URL)
                .headers(createHeaders(TEST_USERNAME, TEST_PASSWORD))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(getJsonBody(requestBody)))
//                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getResponse_Unauthorized() throws Exception {
        rq.setOperation("get");

        this.mockMvc.perform(post(URL)
                .headers(createHeaders(TEST_USERNAME, TEST_PASSWORD + "1"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(getJsonBody(rq)))
//                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void CRUD_Test() throws Exception {
        // CREATE
        log.info("!!! TEST CREATE !!!");
        rq.setOperation("create");
        Long id = createAndGetId("TEST_TITLE");
        rq.setParentId(id);
        Long childId1 = createAndGetId("CHILD 1");
        Long childId2 = createAndGetId("CHILD 2");
        Long childId3 = createAndGetId("CHILD 3");

        // READ
        log.info("!!! TEST READ !!!");
        checkCreate();

        // UPDATE
        log.info("!!! TEST UPDATE !!!");
        rq.reset();
        rq.setOperation("update");
        rq.setField("title");
        rq.setId(id);
        rq.setTitle("CHANGED_TITLE");
        sendMsgGetResponse();

        rq.setId(childId3);
        rq.setField("parent");
        rq.setParentId(childId2);
        sendMsgGetResponse();

        rq.setField("title");
        rq.setTitle("CHILD-CHILD 3");
        sendMsgGetResponse();

        // DELETE

        log.info("!!! TEST DELETE !!!");
        rq.reset();
        rq.setOperation("delete");
        rq.setId(childId3);
        sendMsgGetResponse();
        rq.setId(childId2);
        sendMsgGetResponse();
        rq.setId(childId1);
        sendMsgGetResponse();
        rq.setId(id);
        sendMsgGetResponse();
    }

    private JSONObject sendMsgGetResponse() throws Exception {
        MvcResult result = this.mockMvc.perform(post(URL)
                .headers(createHeaders(TEST_USERNAME, TEST_PASSWORD))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(getJsonBody(rq)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"Result\":\"OK\"")))
                .andReturn();
        return new JSONObject(result.getResponse().getContentAsString());
    }

    private Long createAndGetId(String title) throws Exception {
        rq.setTitle(title);
        return sendMsgGetResponse().getLong("ID");
    }

    private void checkCreate() throws Exception {
        rq.setOperation("get");
        this.mockMvc.perform(post("http://localhost:5188/infopages")
                .headers(createHeaders(TEST_USERNAME, TEST_PASSWORD))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(getJsonBody(rq)))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("TEST_TITLE")))
                .andExpect(content().string(containsString("CHILD 1")))
                .andExpect(content().string(containsString("CHILD 2")))
                .andExpect(content().string(containsString("CHILD 3")))
                ;
    }

    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set(HttpHeaders.AUTHORIZATION, authHeader);
        }};
    }

    private String getJsonBody(RequestBody requestBody) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(requestBody);
    }
}

// https://mkyong.com/spring-boot/spring-rest-integration-test-example/
// Authorization:"Basic dXNlcjp1c2VyMTIz", Content-Type:"application/json;charset=UTF-8", Content-Length:"30"
// curl -u user:user123 --header "Content-Type: application/json" --request POST --data {"operation":"get"} localhost:5188/infopages


/*@Test
    public void test() throws Exception {
//        assertThat(controller).isNotNull();
        this.mockMvc.perform(post("/infopages"))
                .andDo(print()) // console output
//                .andExpect(status().isOk()) // return status 200
//                .andExpect(content().string(containsString("Hello World")))
        ;
    }*/
