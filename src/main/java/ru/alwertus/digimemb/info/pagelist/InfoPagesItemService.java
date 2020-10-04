package ru.alwertus.digimemb.info.pagelist;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.digimemb.auth.IAuthenticationFacade;
import ru.alwertus.digimemb.auth.User;
import ru.alwertus.digimemb.info.page.InfoPageService;

import java.util.Optional;

@Log4j2
@Service
public class InfoPagesItemService {
    @Autowired
    private InfoPageService page;

    private final InfoPagesItemRepo db;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    public InfoPagesItemService(@Autowired InfoPagesItemRepo db) {
        this.db = db;
    }

    public Long create(String title) {
        log.info("create menuitem '" + title + "'");

        InfoPagesItem item = new InfoPagesItem(title);
        db.save(item);

        return item.getId();
    }

    public Long create(String title, Long parentId, AccessLevel access) {
        User user = authenticationFacade.getCurrentUser();
        return create(title, parentId, access, user);
    }

    public Long create(String title, Long parentId, AccessLevel access, User user) {
        log.info("create menuitem '" + title + "'");

        Optional<InfoPagesItem> parentMenu = db.findById(parentId);
        InfoPagesItem item = new InfoPagesItem(title, (parentMenu.orElse(null)), access, user);
        db.save(item);

        return item.getId();
    }

    public void delete(Long id) {

        log.info("Delete page menu item id = " + id);
        Optional<InfoPagesItem> infoPagesItem = db.findById(id);
        if (!infoPagesItem.isPresent()) return;

        db.delete(infoPagesItem.get());
        if (!db.findById(id).isPresent()) page.delete(id);
    }

    public InfoPagesItem getById(Long id) {
        log.info("Get page id=" + id);
        Optional<InfoPagesItem> page = db.findById(id);
        return page.orElse(null);
    }

    public boolean updateTitle(Long id, String newTitle) {
        InfoPagesItem original = getById(id);
        log.info("Update Title InfoPagesItem id = " + (original == null ? "null" : original.getId()));
        if (original == null) return false;

        original.setTitle(newTitle);
        db.save(original);
        return true;
    }

    public boolean updateParentId(Long id, Long newParentId) {
        InfoPagesItem original = getById(id);
        log.info("Update ParentId InfoPagesItem id = " + (original == null ? "null" : original.getId()));
        if (original == null) return false;

        InfoPagesItem newParent = getById(newParentId);
        original.setParentItem(newParent);
        db.save(original);
        return true;
    }

    public Iterable<InfoPagesItem> getAllAvailableForUser(User user) {
        log.info("Get all available to user (id=" + user.getId() + ")");
        return db.findAllByUserId(user);
    }

}
