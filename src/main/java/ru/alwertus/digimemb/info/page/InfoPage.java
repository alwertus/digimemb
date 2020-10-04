package ru.alwertus.digimemb.info.page;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "page")
public class InfoPage {
    @Id
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Getter @Setter
    private String html;

    public InfoPage() {
    }

    public InfoPage(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoPage infoPage = (InfoPage) o;
        return id.equals(infoPage.id) &&
                html.equals(infoPage.html);
    }

    @Override
    public int hashCode() {
        return html.hashCode() + id.hashCode();
    }
}