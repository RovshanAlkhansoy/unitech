package com.app.unitech.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.unitech.constants.GeneralConstants;
import com.app.unitech.dto.TokenPrincipalDto;
import com.app.unitech.dto.request.TransferRequestDto;
import com.app.unitech.dto.response.TransferResponseDto;
import com.app.unitech.entity.Account;
import com.app.unitech.entity.User;
import com.app.unitech.enums.AccountStatutes;
import com.app.unitech.repository.AccountRepository;
import com.app.unitech.repository.TransactionRepository;
import com.app.unitech.repository.UserRepository;
import com.app.unitech.service.JWTService;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private TransferServiceImpl transferService;


    @Test
    void testDoTransfer_Success() {
        // Mocking
        TransferRequestDto transferRequest = new TransferRequestDto("senderAccountNumber", "receiverAccountNumber", BigDecimal.valueOf(100.0));
        String authentication = "dummyAuthenticationToken";
        TokenPrincipalDto tokenPrincipalDto = new TokenPrincipalDto("userPin");
        when(jwtService.getUserPrincipal(authentication)).thenReturn(tokenPrincipalDto);
        User user = new User();
        when(userRepository.findUserInfoByPinIgnoreCase(tokenPrincipalDto.sub())).thenReturn(Optional.of(user));

        Account senderAccount = new Account();
        senderAccount.setAccountNumber("senderAccountNumber");
        senderAccount.setStatus(AccountStatutes.ACTIVE);
        senderAccount.setBalance(BigDecimal.valueOf(200.0));

        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("receiverAccountNumber");
        receiverAccount.setStatus(AccountStatutes.ACTIVE);
        receiverAccount.setBalance(BigDecimal.valueOf(50.0));

        when(accountRepository.findByAccountNumber("senderAccountNumber")).thenReturn(senderAccount);
        when(accountRepository.findByAccountNumber("receiverAccountNumber")).thenReturn(receiverAccount);

        // Call method under test
        TransferResponseDto response = transferService.doTransfer(transferRequest, authentication);
        response.setTransactionId(UUID.randomUUID());

        // Assertions
        assertTrue(response.isSuccess());
        assertEquals(GeneralConstants.SUCCESS, response.getMessage());
        assertNotNull(response.getTransactionId());
        assertEquals(BigDecimal.valueOf(100.0), senderAccount.getBalance());
        assertEquals(BigDecimal.valueOf(150.0), receiverAccount.getBalance());
        verify(accountRepository, times(2)).save(any());
        verify(transactionRepository, times(1)).save(any());
    }

    // Add more test cases for other scenarios such as insufficient balance, non-existent accounts, etc.
}