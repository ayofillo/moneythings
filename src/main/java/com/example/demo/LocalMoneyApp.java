package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Profile({"local", "aws"})
@Component
@RequiredArgsConstructor
@Slf4j
public class LocalMoneyApp implements ApplicationRunner {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (!CollectionUtils.isEmpty(accountRepository.findAll())) return;

        Account account = new Account()
                .setAccountName("deco")
                .setCreatedDate(LocalDateTime.now())
                .setBalance(2000.0);
        account = accountRepository.save(account);
        log.info("account {} saved...", account.getId());

        transactionRepository.saveAll(List.of(new Transaction()
                .setCreatedTime(LocalDateTime.now())
                .setTransactionType(TransactionType.DEPOSIT.name())
                .setDirection(TransactionDirection.INCOMING.name())
                .setAccount(account)
                .setTransactionAmount(2000.0)));

        log.info("Account {} updated...", account.getId());

        account = new Account()
                .setAccountName("ayodeji")
                .setCreatedDate(LocalDateTime.now())
                .setBalance(1000.0);
        account = accountRepository.save(account);
        log.info("account {} saved...", account.getId());


        transactionRepository.saveAll(List.of(new Transaction()
                .setCreatedTime(LocalDateTime.now())
                .setTransactionType(TransactionType.DEPOSIT.name())
                .setDirection(TransactionDirection.INCOMING.name())
                .setAccount(account)
                .setTransactionAmount(1000.0)));

        log.info("Account {} updated...", account.getId());
    }
}
