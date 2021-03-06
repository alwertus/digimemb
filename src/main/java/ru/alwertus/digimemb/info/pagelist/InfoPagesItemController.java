package ru.alwertus.digimemb.info.pagelist;


import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alwertus.digimemb.auth.IAuthenticationFacade;
import ru.alwertus.digimemb.auth.User;
import ru.alwertus.digimemb.rest.AddingError;

@Log4j2
@RestController
@RequestMapping("infopages")
public class InfoPagesItemController implements AddingError {
    @Autowired
    InfoPagesItemService pages;

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @PostMapping
    public String request(@RequestBody String requestBody) {
        log.info("get msg: " + requestBody);

        User user;

        JSONObject rq = new JSONObject(requestBody);
        JSONObject rs = new JSONObject();

        if (!rq.has("operation")) {
            log.error(addError(rs, "Operation is not defined"));
        } else
        try {
            user = authenticationFacade.getCurrentUser();
            if (user == null) {
                log.error(addError(rs, "Current User not defined"));
            }
            else
                switch (rq.getString("operation")) {
                    case "create":
                        String title = rq.getString("title");
                        long parentId = -1L;
                        try {
                            parentId = rq.getLong("parentId");
                        } catch(Exception ignored) {}
                        Long created = pages.create(title, parentId, AccessLevel.PRIVATE, user);
                        rs.put("ID", created);
                        rs.put("Result", "OK");
                        break;

                    case "get":
                        log.info("get records");
                        rs.put("List", getPagesListAsJsonString(user));
                        rs.put("Result", "OK");
                        break;

                    case "update":
                        Long id = rq.getLong("id");
                        log.info("Change record id=" + id);
                        boolean recordChanged = false;

                        if (rq.has("title")) {
                            String newTitle = rq.getString("title");
                            log.info("Update Title to '" + newTitle + "'");
                            if (!pages.updateTitle(id, newTitle)) {
                                log.error(addError(rs, "Update record id '" + id + "' title to '" + newTitle + "' error"));
                                break;
                            }
                            log.info("Update Title: Success");
                            recordChanged = true;
                        }

                        if (rq.has("parentId")) {
                            long newParent = -1L;
                            if (!rq.isNull("parentId"))
                                try {
                                    newParent = rq.getLong("parentId");
                                } catch(JSONException ignored) { }
                            log.info("Update ParentId to '" + newParent + "'");
                            if (!pages.updateParentId(id, newParent)) {
                                log.error(addError(rs, "Update record id '" + id + "' parent to '" + newParent + "' error"));
                                break;
                            }
                            log.info("Update ParentId: Success");
                            recordChanged = true;
                        }
                        rs.put("Result", recordChanged ? "OK" : "No Changes");
                        break;

                    case "delete":
                        try {
                            pages.delete(rq.getLong("id"));
                            rs.put("Result", "OK");
                        } catch(Exception e) {
                            log.error(addError(rs, "Error get id or delete. " + e.getMessage()));
                        }
                        break;

                    default:
                        log.error(addError(rs, "Unknown operation"));
                }
        } catch (Exception e) {
            log.error(addError(rs, "Exception! " + e.getMessage()));
        }

        return rs.toString();
    }

    private String getPagesListAsJsonString(User user) {
        JSONArray json = new JSONArray();
        pages.getAllAvailableForUser(user).forEach(
                infoPagesItem -> {
                    JSONObject element = infoPagesItem.getJSONObject();
                    if (element != null)
                        if (infoPagesItem.getParentItem() == null)
                            json.put(element);
                }
        );
        return json.toString();
    }

}
