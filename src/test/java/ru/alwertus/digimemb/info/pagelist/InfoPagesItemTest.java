package ru.alwertus.digimemb.info.pagelist;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;
import ru.alwertus.digimemb.auth.User;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class InfoPagesItemTest {

    @Test
    @Description("Test valid returned JSON object")
    void getJSONObject() {
        String TITLE = "title";

        InfoPagesItem parent = new InfoPagesItem("Parent");

        InfoPagesItem ipi = new InfoPagesItem("Element");
        ipi.setId(1L);
        ipi.setParentItem(parent);

        InfoPagesItem children1 = new InfoPagesItem("Child 1");
        InfoPagesItem children2 = new InfoPagesItem("Child 2");
        List<InfoPagesItem> childrens = new LinkedList<>();
        childrens.add(children1);
        childrens.add(children2);
        ipi.setChildren(childrens);


        JSONObject child1 = new JSONObject();
        child1.put(TITLE, "Child 1");
        JSONObject child2 = new JSONObject();
        child2.put(TITLE, "Child 2");

        JSONArray childarr = new JSONArray();
        childarr.put(child1);
        childarr.put(child2);

        JSONObject json = new JSONObject();
        json.put("id", 1L);
        json.put(TITLE, "Element");
        json.put("children", childarr);

        assertTrue(json.similar(ipi.getJSONObject()));
    }

    @Test
    void testHashCode() {
        InfoPagesItem parent = new InfoPagesItem("Parent");

        InfoPagesItem ipi = new InfoPagesItem("Element");
        ipi.setId(1L);
        ipi.setParentItem(parent);

        InfoPagesItem children1 = new InfoPagesItem("Child 1");
        InfoPagesItem children2 = new InfoPagesItem("Child 2");
        List<InfoPagesItem> childrens = new LinkedList<>();
        childrens.add(children1);
        childrens.add(children2);
        ipi.setChildren(childrens);

        assertEquals(211256046, ipi.hashCode());
    }

    @Test
    void getCreator() {
        User user = new User();
        InfoPagesItem ipi = new InfoPagesItem("TITLE", null, AccessLevel.ALL, user);
        assertEquals(ipi.getCreator(), user);
    }
}