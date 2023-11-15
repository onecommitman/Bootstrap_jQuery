package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    ///CRUD operations

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByID(Long id) {
        Optional <User> optionalUser = Optional.ofNullable(userRepository.findUserById(id));
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&**************************************&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("Is USER is present?? " + optionalUser.isPresent());
        return optionalUser.get();
    }

    public void updateUser(User user) {
        userRepository.save(user);

    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}