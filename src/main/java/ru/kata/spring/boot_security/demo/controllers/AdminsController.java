package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminsController {

    private UserService userService;

    private RoleService roleService;


    public AdminsController() {
    }

    @Autowired
    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

    }

    @GetMapping
    public String index(@ModelAttribute("newUser") User newUser, Principal principal, Model model) {
        Optional<User> optionalAdmin = userService.findByUsername(principal.getName());
        User admin = optionalAdmin.get();
        System.out.println("*************************ADMIN PARAMS*********************");
        System.out.println(admin.getUsername());
        System.out.println(admin.getEmail());

        model.addAttribute("admin", admin);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin";
    }


    @GetMapping("/new")
    public String newUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        List<Role> allRoles = roleService.getAllRoles();  // Получение списка ролей из  сервиса
        model.addAttribute("allRoles", allRoles);
        return "new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "new";
        }

        Set<Role> userRoles = user.getRoles().stream().collect(Collectors.toSet());

        Set<Role> additionalRoles = new HashSet<>();

        for (Role role : userRoles) {

            switch (role.getName()) {
                case "ROLE_ADMIN":
                    additionalRoles.add(roleService.getRoleByName("ROLE_ADMIN"));
                    additionalRoles.add(roleService.getRoleByName("ROLE_USER"));
                    break;
                case "ROLE_USER":
                    additionalRoles.add(roleService.getRoleByName("ROLE_USER"));
                    break;
                // Дополнительные кейсы по необходимости
            }
        }

        userRoles.addAll(additionalRoles);
        user.setRoles(userRoles);


        userService.save(user);
        return "redirect:/admin";

    }

    /*------------UPDATE METHODS-----------------------*/

    @GetMapping("/edit")
    public String showByID(@RequestParam("id") Long id, Model model) {

        User user = userService.getUserByID(id);
        model.addAttribute("user", user);

        // Передаем список ролей в модель
        List<Role> allRoles = roleService.getAllRoles();
        model.addAttribute("allRoles", allRoles);

        return "edit";
    }


    @PatchMapping("/edit")
    public String update(@RequestParam("id") Long id, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "edit";
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    /*------------------------------------------*/

    @DeleteMapping("delete")
    public String delete(@RequestParam("id") Long id) {

        userService.deleteUserById(id);
        return "redirect:/admin";
    }

}