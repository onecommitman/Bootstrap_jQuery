package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    ///CRUD operations

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByID(Long id) {
        Optional <User> optionalUser = Optional.ofNullable(userRepository.findUserById(id));
        return optionalUser.get();
    }
    @Override
    @Transactional
    public void updateUser(User user) {

        User tempUser;
        if(user.getPassword().equals("")) {
            tempUser = userRepository.findUserById(user.getId());
            user.setPassword(tempUser.getPassword()); // оставляем старый пароль
            userRepository.save(user);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }

    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()) {
            return (User) optionalUser.get();
        } else throw new UsernameNotFoundException((String.format("User '%s' not found", username)));
    }
}