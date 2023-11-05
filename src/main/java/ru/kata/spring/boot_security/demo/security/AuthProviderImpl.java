package ru.kata.spring.boot_security.demo.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.services.AppUserDetailsService;

import java.util.Collections;


@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final AppUserDetailsService appUserDetailsService;

    @Autowired
    public AuthProviderImpl(AppUserDetailsService appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        UserDetails appUserDetails = appUserDetailsService.loadUserByUsername(username);
        String pasword = authentication.getCredentials().toString();
        if(!appUserDetails.getPassword().equals(pasword)) {
            throw new BadCredentialsException("Incorrect password!!!");
        }
        return new UsernamePasswordAuthenticationToken(appUserDetails, pasword, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
