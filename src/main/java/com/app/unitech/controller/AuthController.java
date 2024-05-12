package com.app.unitech.controller;

import com.app.unitech.dto.request.AuthRequestDto;
import com.app.unitech.dto.request.UserRequestDto;
import com.app.unitech.dto.response.JwtResponseDto;
import com.app.unitech.service.JWTService;
import com.app.unitech.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final JWTService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping(value = "/register")
    public void saveUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        userService.saveUser(userRequestDto);
    }

    @PostMapping("/login")
    public JwtResponseDto authenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getPin(), authRequestDto.getPassword()));
        if (authentication.isAuthenticated()) {
            return JwtResponseDto.builder()
                    .accessToken(jwtService.generateToken(authRequestDto.getPin()))
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid user request..!!");
        }
    }


}
