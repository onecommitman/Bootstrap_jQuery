package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exception_handling.NoSuchUserException;
import ru.kata.spring.boot_security.demo.exception_handling.UserIncorrectData;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RESTController {
    private UserService userService;

    private RoleService roleService;


    public RESTController() {
    }

    @Autowired
    public RESTController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin/current")
    public User getCurrentAdmin(@AuthenticationPrincipal User user) {
        return user;
    }

    @GetMapping("/admin/")
    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user")
    public User getAuthUser(@AuthenticationPrincipal User user) {
        return user;
    }

    @GetMapping("/admin/{id}")
    public User showUserByID(@PathVariable Long id) {
        User user = userService.getUserByID(id);
        if(user == null) {
            throw new NoSuchUserException("There is no user with id " + id + "in database.");
        }
        return userService.getUserByID(id);
    }

    @PostMapping("/admin/")
    public User addNewUser(@RequestBody User user) {
        userService.save(user);
        return user;
    }

    @PutMapping("/admin/{id}")
    public User updateUser(@RequestBody User user) {
        userService.updateUser(user);
        //userService.deleteUserById(id);
        return user;
    }

    @DeleteMapping("/admin/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "User with ID = " + id + " was deleted.";
    }
    @ExceptionHandler
    public ResponseEntity<UserIncorrectData>handleException(NoSuchUserException exception) {
        UserIncorrectData data = new UserIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/roles/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }
}
