package com.example.ocrforidcard.web;

import com.example.ocrforidcard.dao.entities.User;
import com.example.ocrforidcard.dao.repositories.UserRepository;
import com.example.ocrforidcard.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserRestController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;


    @GetMapping(path = "/users")
    @PreAuthorize("hasAuthority('ADMIN')") // Only admins can access this
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')") // Both roles can access this
    public User getUserById(@PathVariable Long id, Principal principal) {
        User user = userService.getUserById(id);

        // Allow admin to see all users, but restrict regular users to their own data
        if (!principal.getName().equals(user.getUsername()) && !isAdmin(principal)) {
            throw new AccessDeniedException("You are not authorized to view this user.");
        }

        return user;
    }

    private boolean isAdmin(Principal principal) {
        // Check if the current user has the ADMIN role
        User currentUser = userService.findUserByUsername(principal.getName());
        return currentUser.getRoles().stream()
                .anyMatch(role -> role.getRole().equals("ADMIN"));
    }
}