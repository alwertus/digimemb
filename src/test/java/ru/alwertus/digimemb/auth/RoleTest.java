package ru.alwertus.digimemb.auth;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void testEquals_false() {
        Assert.assertNotEquals(new Role("role1"), new User());
    }

    @Test
    void getAuthority() {
        String roleName = "role1";
        Assert.assertEquals(roleName, new Role(roleName).getAuthority());
    }
}