package ru.alwertus.digimemb.info.page;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InfoPageTest {

    InfoPage page;

    @BeforeEach
    void prepare() {
        page = new InfoPage();
    }

    @Test
    public void id() {
        page.setId(1L);
        assertEquals(1L, page.getId());
        InfoPage page2 = new InfoPage();
        page2.setId(1L);
        assertEquals(page.getId(), page2.getId());
    }

    @Test
    public void html() {
        page.setHtml("123");
        assertEquals("123", page.getHtml());
    }

    @Test
    public void hashCodeTest() {
        page.setId(1L);
        page.setHtml("123321");
        assertEquals(new Long(1L).hashCode() + "123321".hashCode(), page.hashCode());
    }

}