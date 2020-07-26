package ru.alwertus.digimemb.info.page;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("infopage")
public class InfoPageController {
    @Autowired
    InfoPageService pageService;

    @PostMapping
    public String response(@RequestBody String requestBody) {
        log.info("get msg: " + requestBody);

        JSONObject rq = new JSONObject(requestBody);
        JSONObject rs = new JSONObject();
        try {
            Long id = rq.getLong("id");
            InfoPage page = null;

            switch (rq.getString("operation")) {
                case "get":
                    page = pageService.getPage(id);
                    rs.put("html", page == null ? "" : page.getHtml());
                    rs.put("Result", "OK");
                    break;

                case "set":
                    String html = rq.getString("html");
                    pageService.save(id, html);
                    rs.put("Result", "OK");
                    break;

                case "del":
                    pageService.delete(id);
                    rs.put("Result", "OK");
                    break;

                default:
                    rs.put("Result", "Error");
                    rs.put("Error", "Unknown operation");
            }
        } catch (Exception e) {
            rs.put("Result", "Error");
            rs.put("Error", e.getMessage());
        }

        return rs.toString();
    }
}