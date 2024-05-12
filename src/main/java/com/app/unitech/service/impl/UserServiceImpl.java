package com.app.unitech.service.impl;

import com.app.unitech.dto.request.UserRequestDto;
import com.app.unitech.entity.User;
import com.app.unitech.exception.UserAlreadyExistException;
import com.app.unitech.repository.UserRepository;
import com.app.unitech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    public void saveUser(UserRequestDto userRequestDto) {
        if (userRepository.findUserInfoByPinIgnoreCase(userRequestDto.getPin()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        var user = User.builder()
                .pin(userRequestDto.getPin())
                .password(bCryptPasswordEncoder.encode(userRequestDto.getPassword()))
                .build();
        userRepository.save(user);
    }

}