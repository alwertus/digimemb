package ru.alwertus.digimemb.info.page;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "page")
public class InfoPage {
    @Id
    private Long id;

    @Lob
    private String html;

    public InfoPage() {
    }

    public InfoPage(Long id) {
        this.id = id;
    }
}