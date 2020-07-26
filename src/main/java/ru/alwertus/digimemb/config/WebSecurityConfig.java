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
        auth//.authenticationProvider(authProvider);
            .userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String test = "test2";

        switch (test) {
            case "test2":
                http.csrf().disable()
                        .httpBasic()    // аутентификация средствами HTTP протокола (для RESTfull API). Из-за него появляется окно запроса user-password
                            .and()
//                        .formLogin()
//                            .loginPage("/").permitAll()
//                            .failureUrl("/login?login_error=t")
//                            .and()
//                        .logout()
//                            .logoutUrl("static/j_spring_security_logout")
//                            .and()
                        .authorizeRequests()
//                            .antMatchers(HttpMethod.GET, "/","/login**","/login").permitAll()
                            .antMatchers(HttpMethod.POST, "/infopages").authenticated()
                            .antMatchers(HttpMethod.POST, "/signin").permitAll()
                ;
                break;
            case "test1":
                http
                        .csrf().disable().authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/") //"/index** ", "/static/** ** ", "/** .js", "/** .json", "/** .ico")
                        .permitAll()
                        .anyRequest()
                        .authenticated();
                break;
            case "test0":
                http
                        .csrf().disable().authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/index** ", "/static/** ** ", "/** .js", "/** .json", "/** .ico")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                        .and()
                        .formLogin()
                        .loginPage("/")
                        .loginProcessingUrl("/")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/");
                break;
            case "was1":
                http
                        .httpBasic()
                        .and()
                        .authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/", "/login", "/error").permitAll()
                        .antMatchers(HttpMethod.POST, "/login").permitAll()
//                .antMatchers(HttpMethod.GET, "/books").hasRole("USER")
                        .and()
                        .csrf().disable()
                        .formLogin().disable();
                break;
            case "was0":
                http
                        .authorizeRequests()
                        .anyRequest()
                        .authenticated()
                        .and()
                        .httpBasic();
                break;
        }




    }

}