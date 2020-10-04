package ru.alwertus.digimemb.auth;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    default User getCurrentUser() {
        return (User) getAuthentication().getPrincipal();
    }
}
