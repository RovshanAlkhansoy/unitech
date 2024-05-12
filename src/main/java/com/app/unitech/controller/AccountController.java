package com.app.unitech.controller;

import static com.app.unitech.constants.GeneralConstants.AUTHORIZATION;

import com.app.unitech.dto.request.AccountCreateRequestDto;
import com.app.unitech.dto.response.AccountResponseDto;
import com.app.unitech.dto.response.BaseResponseDto;
import com.app.unitech.service.AccountService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/account")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public BaseResponseDto<AccountResponseDto> createAccount(
            @RequestBody @Valid AccountCreateRequestDto accountCreateRequestDto,
            @RequestHeader(name = AUTHORIZATION) String authentication) {
        AccountResponseDto accountResponseDto = accountService.createAccount(accountCreateRequestDto, authentication);
        return BaseResponseDto.success(accountResponseDto);
    }

    @GetMapping
    public BaseResponseDto<List<AccountResponseDto>> getAllAccounts(
            @RequestHeader(name = AUTHORIZATION) String authentication) {
        List<AccountResponseDto> accountResponseDto = accountService.getAllAccounts(authentication);
        return BaseResponseDto.success(accountResponseDto);
    }

}
