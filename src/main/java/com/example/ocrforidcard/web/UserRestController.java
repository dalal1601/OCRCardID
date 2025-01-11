package com.example.ocrforidcard.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.ocrforidcard.dao.entities.User;
import com.example.ocrforidcard.dao.repositories.UserRepository;
import com.example.ocrforidcard.security.SecurityParams;
import com.example.ocrforidcard.services.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserRestController {

    private final UserRepository userRepository;
    private final UserService userService;

    // Constructor injection
    public UserRestController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

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

    @PostMapping("/register")
    public Map<String, String> registerAndGenerateToken(@RequestBody User user) {
        user.setEnabled(true);
        User savedUser = userService.saveUser(user);
        userService.addRoleToUser(user.getUsername(), "USER");

        // Générer un JWT pour l'utilisateur enregistré
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityParams.EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SecurityParams.SECRET));

        Map<String, String> response = new HashMap<>();
        response.put("username", savedUser.getUsername());
        response.put("token", SecurityParams.PREFIX + token);
        return response;
    }
}
