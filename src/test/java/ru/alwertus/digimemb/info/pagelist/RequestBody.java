package ru.alwertus.digimemb.info.pagelist;

import lombok.Getter;
import lombok.Setter;

class RequestBody {
    @Getter @Setter private String operation;
    @Getter @Setter private Long id;
    @Getter @Setter private String field;
    @Getter @Setter private String title;
    @Getter @Setter private Long parentId = -1L;

    public void reset() {
        field = null;
        operation = null;
        title = null;
        parentId = null;
        id = null;
    }
}
