package ru.alwertus.digimemb.info.pagelist;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.alwertus.digimemb.auth.User;

import javax.persistence.*;
import java.util.List;

/*
    Поля:
        id
        creator
        access
        title
        parent_menu_id

        (calc)parentItem
        (calc)children
 */
@Entity
@Table(name = "pages")
public class InfoPagesItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;

    @ManyToOne (optional = true)
    @JoinColumn
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private AccessLevel access = AccessLevel.ALL;

    @Getter @Setter
    private String title;

    @Getter @Setter
    @ManyToOne (optional = true)
    @JoinColumn
    private InfoPagesItem parentItem;

    @OneToMany(mappedBy = "parentItem")
    private List<InfoPagesItem> children;

    // constructor 0
    public InfoPagesItem() { }

    // constructor 1
    public InfoPagesItem(String title) {
        this.title = title;
    }

    // constructor 2
    public InfoPagesItem(String title, InfoPagesItem parent, AccessLevel access, User creator) {
        this.title = title;
        if (parent != null)
            parentItem = parent;
        this.access = access;
        this.creator = creator;
    }

    public JSONObject getJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("title", title);
        // fill childrens
        if (children != null)
            if (children.size() > 0) {
                JSONArray jsonArr = new JSONArray();
                children.forEach(infoPagesItem -> {
                    if (infoPagesItem.getJSONObject() != null)
                        jsonArr.put(infoPagesItem.getJSONObject());
                });
                json.put("children", jsonArr);
            }
        return json;
    }
}
