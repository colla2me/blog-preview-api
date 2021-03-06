package com.github.service;

import com.github.entity.User;
import com.github.dao.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockEncoder;

    @Mock
    UserMapper mockMapper;

    @InjectMocks
    UserService userService;

    @Test
    public void testSave() {
        when(mockEncoder.encode("password")).thenReturn("encodedPassword");
        userService.save("user", "password");
        verify(mockMapper).save("user", "encodedPassword");
    }

    @Test
    public void testGetUserByUsername() {
        userService.getUserByUsername("user");
        verify(mockMapper).findUserByUsername("user");
    }

    @Test
    public void throwExceptionWhenUserNotFound() {
        when(mockMapper.findUserByUsername("user")).thenReturn(null);
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("user"));
    }

    @Test
    public void testUserDetailsWhenUserFound() {
        when(mockMapper.findUserByUsername("user"))
                .thenReturn(new User(1, "user", "password"));
        UserDetails userDetails = userService.loadUserByUsername("user");
        Assertions.assertEquals("user", userDetails.getUsername());
        Assertions.assertEquals("password", userDetails.getPassword());
    }
}