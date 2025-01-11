package com.example.ocrforidcard.services;

import com.example.ocrforidcard.dao.entities.User;
import com.example.ocrforidcard.dao.entities.Role;
import com.example.ocrforidcard.dao.repositories.RoleRepository;
import com.example.ocrforidcard.dao.repositories.UserRepository;
import com.example.ocrforidcard.exceptions.UserNotFoundException;
import com.example.ocrforidcard.exceptions.RoleNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    // Constructor injection
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            logger.error("Attempt to save user with existing username: {}", user.getUsername());
            throw new UserNotFoundException("Username already exists");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        logger.info("Saving new user: {}", user.getUsername());
        return userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsernameWithRoles(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User addRoleToUser(String username, String rolename) {
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByRole(rolename);

        if (role == null) {
            throw new RoleNotFoundException("Role " + rolename + " not found");
        }

        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }
}
