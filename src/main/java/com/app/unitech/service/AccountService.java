package com.app.unitech.service;

import com.app.unitech.dto.request.AccountCreateRequestDto;
import com.app.unitech.dto.response.AccountResponseDto;
import java.util.List;

public interface AccountService {

    AccountResponseDto createAccount(AccountCreateRequestDto accountCreateRequestDto, String auth);

    List<AccountResponseDto> getAllAccounts(String authentication);

}
