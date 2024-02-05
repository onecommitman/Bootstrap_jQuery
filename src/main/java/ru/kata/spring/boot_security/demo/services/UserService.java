package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> findByUsername(String username);

    List<User> getAllUsers();

    User getUserByID(Long id);

    void updateUser(User user);

    void save(User user);

    void deleteUserById(Long id);
}
