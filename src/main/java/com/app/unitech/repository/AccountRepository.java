package com.app.unitech.repository;

import com.app.unitech.entity.Account;
import com.app.unitech.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountNumber(String accountNumber);

    List<Account> findAllByUserId(User user);

}
