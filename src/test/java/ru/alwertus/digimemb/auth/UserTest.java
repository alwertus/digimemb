package ru.alwertus.digimemb.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    User user;

    @BeforeEach
    void prepare() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("Role1"));
        roles.add(new Role("Role2"));
        user = new User("name", "password", roles);
    }

    @Test
    public void getRolesAsString() {
        String rolesAsString = user.getRolesAsString();

        assertEquals("<Role2><Role1>", rolesAsString);
    }

    @Test
    public void password() {
        user.setPassword("123");
        assertEquals("123", user.getPassword());
    }

    @Test
    public void getAuthorities() {
        assertEquals(2, user.getAuthorities().size());
    }

    @Test
    public void overrideParams() {
        assertEquals(user.getName(), user.getUsername());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(-1822697145, user.hashCode());
    }

}