package ru.alwertus.digimemb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.alwertus.digimemb.auth.UserService;

@ComponentScan(basePackages = {"ru.alwertus.digimemb"})
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic()    // аутентификация средствами HTTP протокола (для RESTfull API). Из-за него появляется окно запроса user-password
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/infopages").authenticated()
                .antMatchers(HttpMethod.POST, "/signin").permitAll()
                .antMatchers(HttpMethod.POST, "/rest").permitAll();
    }

}