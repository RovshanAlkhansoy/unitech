package com.app.unitech.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.unitech.dto.request.UserRequestDto;
import com.app.unitech.entity.User;
import com.app.unitech.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(mockUserRepository, mockPasswordEncoder);
    }

    @Test
    void testSaveUser() {
        // Given
        UserRequestDto userRequestDto = new UserRequestDto("username", "password");

        // When
        when(mockPasswordEncoder.encode(userRequestDto.getPassword())).thenReturn("encodedPassword");
        userService.saveUser(userRequestDto);

        // Then
        verify(mockUserRepository, times(1)).save(any(User.class));
    }

}