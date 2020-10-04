package ru.alwertus.digimemb.info.page;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class InfoPageService {
    private InfoPageRepo db;

    @Autowired
    public InfoPageService(InfoPageRepo db) { this.db = db; }

    public void save(Long id, String html) {
        log.info("Create page id=" + id);
        if (id < 0) return;
        InfoPage page = getPage(id, new InfoPage(id));
        page.setHtml(html);
        db.save(page);
    }

    public InfoPage getPage(Long id) {
        return getPage(id, null);
    }

    public InfoPage getPage(Long id, InfoPage defaultReturn) {
        log.info("Get page id=" + id);
        Optional<InfoPage> page = db.findById(id);
        return page.orElse(defaultReturn);
    }

    public void delete(Long id) {
        log.info("Delete page id=" + id);
        InfoPage page = getPage(id);
        if (page != null)
            db.delete(page);
    }
}