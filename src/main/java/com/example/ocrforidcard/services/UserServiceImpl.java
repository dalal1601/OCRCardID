package com.example.ocrforidcard.services;

import com.example.ocrforidcard.dao.entities.User;
import com.example.ocrforidcard.dao.entities.Role;
import com.example.ocrforidcard.dao.repositories.RoleRepository;
import com.example.ocrforidcard.dao.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            logger.error("Attempt to save user with existing username: {}", user.getUsername());
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        logger.info("Saving new user: {}", user.getUsername());
        return userRepository.save(user);
    }


    public User findUserByUsername(String username) {
        return userRepository.findUserByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
            throw new RuntimeException("Role " + rolename + " not found");
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
        // Fetch the user by ID and handle the case where the user is not found
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID " + id + " not found"));
    }



}
