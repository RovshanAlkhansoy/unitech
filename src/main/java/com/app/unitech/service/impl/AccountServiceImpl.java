package com.app.unitech.service.impl;

import com.app.unitech.dto.request.AccountCreateRequestDto;
import com.app.unitech.dto.response.AccountResponseDto;
import com.app.unitech.entity.Account;
import com.app.unitech.enums.AccountStatutes;
import com.app.unitech.exception.UserNotFoundWithGivenPin;
import com.app.unitech.repository.AccountRepository;
import com.app.unitech.repository.UserRepository;
import com.app.unitech.service.AccountService;
import com.app.unitech.service.JWTService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    @Transactional
    @Override
    public AccountResponseDto createAccount(AccountCreateRequestDto accountCreateRequestDto, String authentication) {

        var tokenPrincipalDto = jwtService.getUserPrincipal(authentication);
        var user = userRepository.findUserInfoByPinIgnoreCase(tokenPrincipalDto.sub())
                .stream().findFirst().orElseThrow(UserNotFoundWithGivenPin::new);

        Account account = Account.builder()
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .accountNumber(accountCreateRequestDto.getAccountNumber())
                .balance(accountCreateRequestDto.getBalance())
                .userId(user)
                .status(AccountStatutes.DEACTIVATED)
                .build();

        return fromAccountEntityToAccountResponse(accountRepository.save(account));

    }

    @Override
    public List<AccountResponseDto> getAllAccounts(String authentication) {

        var tokenPrincipalDto = jwtService.getUserPrincipal(authentication);
        var user = userRepository.findUserInfoByPinIgnoreCase(tokenPrincipalDto.sub())
                .stream().findFirst().orElseThrow(UserNotFoundWithGivenPin::new);

        return accountRepository
                .findAllByUserId(user)
                .stream()
                .filter(account -> account.getStatus().equals(AccountStatutes.ACTIVE))
                .map(this::fromAccountEntityToAccountResponse)
                .toList();

    }

    private AccountResponseDto fromAccountEntityToAccountResponse(Account account) {
        return AccountResponseDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .status(account.getStatus())
                .build();
    }

}
