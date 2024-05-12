package com.app.unitech.service.impl;

import com.app.unitech.constants.GeneralConstants;
import com.app.unitech.dto.request.TransferRequestDto;
import com.app.unitech.dto.response.TransferResponseDto;
import com.app.unitech.entity.Account;
import com.app.unitech.entity.Transaction;
import com.app.unitech.entity.User;
import com.app.unitech.enums.AccountStatutes;
import com.app.unitech.enums.TransactionStatuses;
import com.app.unitech.exception.InsufficientBalanceException;
import com.app.unitech.exception.MoneyReceiverAccountException;
import com.app.unitech.exception.MoneySenderAccountException;
import com.app.unitech.exception.SameAccountException;
import com.app.unitech.exception.UserNotFoundWithGivenPin;
import com.app.unitech.repository.AccountRepository;
import com.app.unitech.repository.TransactionRepository;
import com.app.unitech.repository.UserRepository;
import com.app.unitech.service.JWTService;
import com.app.unitech.service.TransferService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    @Transactional
    @Override
    public TransferResponseDto doTransfer(TransferRequestDto transferRequest, String authentication) {

        var tokenPrincipalDto = jwtService.getUserPrincipal(authentication);
        var user = userRepository.findUserInfoByPinIgnoreCase(tokenPrincipalDto.sub())
                .stream().findFirst().orElseThrow(UserNotFoundWithGivenPin::new);

        // Göndərən Account mövcuddur və Active dir yoxlanması
        Account sender = accountRepository.findByAccountNumber(transferRequest.getFromAccountNumber());
        if (sender == null || !sender.getStatus().equals(AccountStatutes.ACTIVE)) {
            throw new MoneySenderAccountException();
        }

        // Göndəriləcək Account mövcuddur və Active dir yoxlanması
        Account receiver = accountRepository.findByAccountNumber(transferRequest.getToAccountNumber());
        if (receiver == null || !receiver.getStatus().equals(AccountStatutes.ACTIVE)) {
            throw new MoneyReceiverAccountException();
        }

        // Accountlar ferqlidir ya yox
        if (transferRequest.getFromAccountNumber().equals(transferRequest.getToAccountNumber())) {
            throw new SameAccountException();
        }

        // Göndərən Account hesabında kifayət məbləğ varmı
        BigDecimal transferAmount = transferRequest.getAmount();
        if (sender.getBalance().compareTo(transferAmount) < 0) {
            throw new InsufficientBalanceException();
        }

        // Transfer gerçəkləşməsi
        return transfer(sender, receiver, transferRequest.getAmount(), user);

    }

    public TransferResponseDto transfer(Account sender, Account receiver, BigDecimal amount, User user) {

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .fromAccount(sender.getAccountNumber())
                .toAccount(receiver.getAccountNumber())
                .status(TransactionStatuses.SUCCESS)
                .createdAt(LocalDateTime.now())
                .userId(user)
                .build();

        transactionRepository.save(transaction);

        return TransferResponseDto.builder()
                .success(true)
                .transactionId(transaction.getId())
                .message(GeneralConstants.SUCCESS)
                .build();

    }

}
