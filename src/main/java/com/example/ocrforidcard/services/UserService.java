package com.example.ocrforidcard.services;

import com.example.ocrforidcard.dao.entities.User;

import com.example.ocrforidcard.dao.entities.Role;
import java.util.List;

public interface UserService {
    User saveUser(User user);
    User findUserByUsername(String username);
    Role addRole(Role role);
    User addRoleToUser(String username,String rolename);
    List<User> findAllUsers();

    User getUserById(Long id);
}
