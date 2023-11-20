package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    ///CRUD operations

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserByID(Long id) {
        Optional <User> optionalUser = Optional.ofNullable(userRepository.findUserById(id));
        return optionalUser.get();
    }

    @Transactional
    public void updateUser(User user) {

        String oldPassword;
        User tempUser;
        if(user.getPassword() == null) {
            tempUser = userRepository.findUserById(user.getId());
            oldPassword = tempUser.getPassword();
            user.setPassword(oldPassword);
            userRepository.save(user);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }

    @Transactional(readOnly = true)
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()) {
            return (User) optionalUser.get();
        } else throw new UsernameNotFoundException((String.format("User '%s' not found", username)));
    }
}