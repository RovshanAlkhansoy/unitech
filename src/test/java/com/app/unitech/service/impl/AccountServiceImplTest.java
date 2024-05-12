package com.app.unitech.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.unitech.dto.TokenPrincipalDto;
import com.app.unitech.dto.request.AccountCreateRequestDto;
import com.app.unitech.dto.response.AccountResponseDto;
import com.app.unitech.entity.Account;
import com.app.unitech.entity.User;
import com.app.unitech.enums.AccountStatutes;
import com.app.unitech.repository.AccountRepository;
import com.app.unitech.repository.UserRepository;
import com.app.unitech.service.JWTService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AccountServiceImpl accountService;


    @Test
    void testCreateAccount() {

        AccountCreateRequestDto requestDto = new AccountCreateRequestDto("123456789",BigDecimal.valueOf(100.0));
        String authentication = "dummyAuthenticationToken";
        TokenPrincipalDto tokenPrincipalDto = new TokenPrincipalDto("userPin");
        when(jwtService.getUserPrincipal(authentication)).thenReturn(tokenPrincipalDto);
        User user = new User();
        when(userRepository.findUserInfoByPinIgnoreCase(tokenPrincipalDto.sub())).thenReturn(Optional.of(user));

        // Creating account entity
        Account account = new Account();
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        account.setAccountNumber("123456789");
        account.setBalance(BigDecimal.valueOf(100.0));
        account.setStatus(AccountStatutes.DEACTIVATED);

        // Mocking save operation
        when(accountRepository.save(any())).thenReturn(account);

        // Performing the test
        AccountResponseDto responseDto = accountService.createAccount(requestDto, authentication);

        // Verifying interactions and assertions
        verify(userRepository).findUserInfoByPinIgnoreCase("userPin");
        verify(accountRepository).save(any());
        assertEquals(account.getAccountNumber(), responseDto.getAccountNumber());
        assertEquals(account.getBalance(), responseDto.getBalance());
        assertEquals(account.getCreatedAt(), responseDto.getCreatedAt());
        assertEquals(account.getUpdatedAt(), responseDto.getUpdatedAt());
        assertEquals(account.getStatus(), responseDto.getStatus());
    }

    @Test
    void testGetAllAccounts() {
        // Mocking necessary dependencies
        String authentication = "dummyAuthenticationToken";
        TokenPrincipalDto tokenPrincipalDto = new TokenPrincipalDto("userPin");
        when(jwtService.getUserPrincipal(authentication)).thenReturn(tokenPrincipalDto);

        User user = new User();
        when(userRepository.findUserInfoByPinIgnoreCase(tokenPrincipalDto.sub())).thenReturn(Optional.of(user));

        // Creating account entity
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456789");
        account.setBalance((BigDecimal.valueOf(100.0)));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        account.setStatus(AccountStatutes.ACTIVE);

        // Mocking find all accounts operation
        when(accountRepository.findAllByUserId(any())).thenReturn(List.of(account));

        // Performing the test
        List<AccountResponseDto> responseDtoList = accountService.getAllAccounts(authentication);

        // Verifying interactions and assertions
        verify(jwtService).getUserPrincipal(authentication);
        verify(userRepository).findUserInfoByPinIgnoreCase("userPin");
        verify(accountRepository).findAllByUserId(any());
        assertEquals(1, responseDtoList.size());
        AccountResponseDto responseDto = responseDtoList.getFirst();
        assertEquals(account.getId(), responseDto.getId());
        assertEquals(account.getAccountNumber(), responseDto.getAccountNumber());
        assertEquals(account.getBalance(), responseDto.getBalance());
        assertEquals(account.getCreatedAt(), responseDto.getCreatedAt());
        assertEquals(account.getUpdatedAt(), responseDto.getUpdatedAt());
        assertEquals(account.getStatus(), responseDto.getStatus());
    }

}