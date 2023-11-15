package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final SuccessUserHandler successUserHandler;
    private UserService userService;


    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Autowired
    public void setUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.antMatchers().permitAll()
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/index").authenticated()
                .antMatchers("/admin/**/**").hasRole("ADMIN") // ROLE_ADMIN, ROLE_SUPERADMIN
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .successHandler(successUserHandler);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }

    /*@Bean
    public FilterRegistrationBean hiddenHttpMethodFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new HiddenHttpMethodFilter());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/"));
        return filterRegistrationBean;
    }*/
}