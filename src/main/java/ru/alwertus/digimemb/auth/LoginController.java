package ru.alwertus.digimemb.auth;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*
    Authorization request processing
 */

@Log4j2
@RestController
@RequestMapping("signin")
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping
    public String tryToLogin(@RequestBody String requestBody) {
        log.info("get msg: " + requestBody);

        JSONObject jsonIn;
        try {
            jsonIn = new JSONObject(requestBody);
        } catch (JSONException e) {
            log.error(e.toString());
            return "Error parse request";
        }

        JSONObject jsonOut = new JSONObject();

        if (!jsonIn.has("operation"))
            return "Operation not specified";
        String operation = jsonIn.getString("operation");
        if (operation.equals("login")) {
            String sLogin;
            String sPassword;

            try {
                sLogin = jsonIn.getString("name");
            } catch (JSONException e) {
                log.error(e.toString());
                return e.toString();
            }

            try {
                sPassword = jsonIn.getString("password");
            } catch (JSONException e) {
                log.error(e.toString());
                return e.toString();
            }

            log.info("TRY LOGIN: " + sLogin + " - " + sPassword);

            try {
                User user = (User) userService.loadUserByUsername(sLogin);

                log.info("SUCCESS! " + user.getRolesAsString());
                jsonOut.put("userName", sLogin);
                jsonOut.put("status", 200);
                JSONArray roles = new JSONArray();
                roles.put(user.getRoles());
                jsonOut.put("roles", roles);
            } catch (Exception e) {
                log.error("Error: " + e.toString());
                jsonOut.put("html", "User/password not found");
                jsonOut.put("status", 401);
            }

        } else {
            jsonOut.put("html", "Wrong request");
            jsonOut.put("status", 401);
        }

        return jsonOut.toString();
    }
}
