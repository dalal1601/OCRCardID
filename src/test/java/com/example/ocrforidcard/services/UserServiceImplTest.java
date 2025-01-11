package com.example.ocrforidcard.services;

import com.example.ocrforidcard.dao.entities.User;
import com.example.ocrforidcard.dao.repositories.UserRepository;
import com.example.ocrforidcard.dao.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    void saveUser() {
        // Arrange
        when(userRepository.save(user)).thenReturn(user);
        when(bCryptPasswordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");  // Mock the encoding

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
        verify(bCryptPasswordEncoder, times(1)).encode("password");  // Ensure the password encoding is called
    }







    @Test
    void findAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        // Act
        Iterable<User> users = userService.findAllUsers();

        // Assert
        assertNotNull(users);
        assertTrue(users.iterator().hasNext());
        verify(userRepository, times(1)).findAll();
    }


}
