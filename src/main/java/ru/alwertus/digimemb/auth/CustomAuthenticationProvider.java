package ru.alwertus.digimemb.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

//@Log4j2
//@Component
public class CustomAuthenticationProvider/* implements AuthenticationProvider */{
    /*@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        Object oTmp = authentication.getCredentials();
        String passw = (oTmp == null ? "" : oTmp.toString());

        log.info("ENTER TO AUTHENTICATE >> " + name + ":" + passw);
        Authentication current = new UsernamePasswordAuthenticationToken(name, passw, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(current);
//        return null;
//        return new UsernamePasswordAuthenticationToken(name, passw, new ArrayList<>());
        return current;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
*/
}

