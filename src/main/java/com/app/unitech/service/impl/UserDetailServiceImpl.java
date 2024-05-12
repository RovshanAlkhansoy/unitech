package com.app.unitech.service.impl;

import com.app.unitech.dto.CustomUserDetailDto;
import com.app.unitech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        var user = userRepository.findUserInfoByPinIgnoreCase(pin).orElseThrow(() ->
                new ResponseStatusException(HttpStatusCode.valueOf(401)));
        return new CustomUserDetailDto(user);
    }

}
