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
import java.util.List;


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
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }


    @GetMapping("/new")
    public String newUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "new";
        }
        userService.save(user);
        return "redirect:/admin";

    }



    /*------------UPDATE METHODS-----------------------*/

    @GetMapping("/edit")
    public String showByID(@RequestParam("id") Long id, Model model) {
        System.out.println("********************************************* Загрузка юзера из БД ******************************************************");
        //model.addAttribute("user", userService.getUserByID(id));
        List<Role> roles = roleService.getAllRoles(); // Получение списка ролей из сервиса
        Role role = new Role();
        model.addAttribute("roleForm", role); // Добавление объекта Role в модель
        model.addAttribute("roles", roles);
        //model.addAttribute("roles+", roleService.getAllRoles());
        System.out.println("----------------------_SETTED ROLE IS----------------------------------");
        System.out.println(role.getName());
        System.out.println("-----------------------USERS ROLES-------------------------------------");
        System.out.println(roleService.getAllRoles());
        System.out.println("Загружен юзер из БД для редактирования...");
        return "edit";
    }


    @PatchMapping("/edit")
    public String update(@RequestParam("id") Long id, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "edit";
        }
        System.out.printf("\n*********************************************** USER WITH ID = %d WAS UPDATED ***********************************", id);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    /*------------------------------------------*/

    @DeleteMapping
    public String delete(@RequestParam("id") Long id) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("Deleting user with id" + id);
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

}