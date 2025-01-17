package com.example.ocrforidcard.security;

import com.example.ocrforidcard.dao.entities.User;
import com.example.ocrforidcard.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;

    // Constructor injection for UserService
    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("user not found");
        else {
            // Create the list of authorities
            List<GrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
                authorities.add(authority);
            });
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    authorities);
        }
    }
}
