package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UsersController {

    private UserService userService;

    public UsersController() {
    }

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("")
    public String show (Model model, Principal principal){
        Optional<User> opUser = userService.findByUsername(principal.getName());
        User user = opUser.get();
        model.addAttribute("user", user);
        model.addAttribute("userRoles", user.getRolesString());
        return "user";
    }
    /*@GetMapping()
    public String showUserInfo(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Получаем текущего пользователя из Spring Security
        User user = (User) userDetails;
        System.out.println(user.getUsername());

        Optional<ru.kata.spring.boot_security.demo.models.User> opUser = userService.findByUsername(user.getUsername());

        ru.kata.spring.boot_security.demo.models.User currentUser = opUser.get();

        // Передаем данные в модель
        model.addAttribute("user", currentUser);

        // Возвращаем имя Thymeleaf шаблона
        return "user";
    }*/

}
