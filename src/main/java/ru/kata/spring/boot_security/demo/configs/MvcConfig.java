package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("admin");
        registry.addViewController("/user").setViewName("user");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/admin/edit**").setViewName("edit");
        registry.addViewController("/admin/new").setViewName("new");
        //registry.addViewController("/index").setViewName("hello");
        //если строка активирована, то открывается только после аутентификации
    }
}
