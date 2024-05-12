package com.app.unitech.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.app.unitech.entity.User;
import com.app.unitech.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

class UserDetailServiceImplTest {

    private UserDetailServiceImpl userDetailService;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailService = new UserDetailServiceImpl(mockUserRepository);
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Given
        String pin = "123456";
        User user = new User();
        user.setPin("123456");
        when(mockUserRepository.findUserInfoByPinIgnoreCase(pin)).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = userDetailService.loadUserByUsername(pin);

        // Then
        assertEquals(user.getPin(), userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Given
        String pin = "123456";
        when(mockUserRepository.findUserInfoByPinIgnoreCase(pin)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class, () -> userDetailService.loadUserByUsername(pin));
    }

}