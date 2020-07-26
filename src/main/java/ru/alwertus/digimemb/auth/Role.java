package ru.alwertus.digimemb.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONPropertyIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter @Setter
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Role(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Role)
            return name.equals(((Role)obj).name);
        else
            return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @JSONPropertyIgnore
    @Override
    public String getAuthority() {
        return getName();
    }
}
