package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MoneyAppService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Long createAccount(Account account) {
        account = accountRepository.save(
                account.setCreatedDate(LocalDateTime.now()));
        transactionRepository.save(new Transaction()
                .setAccount(account)
                .setCreatedTime(LocalDateTime.now())
                .setTransactionType(TransactionType.DEPOSIT.name())
                .setTransactionAmount(account.getBalance()));
        return account.getId();
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @SneakyThrows
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new MoneyAppException("No account found"));
    }

    public List<Account> formatAccountResponse(List<Account> accounts) {
        accounts.parallelStream().forEach(this::cleanAccountResponse);
        return accounts;
    }

    public List<Transaction> formatTransactionResponse(List<Transaction> transactions) {
        transactions.parallelStream().forEach(this::cleanTransactionResponse);
        return transactions;
    }

    public Account cleanAccountResponse(Account account) {
        account.getTransactions().parallelStream().forEach(txn -> txn.setAccount(null));
        return account;
    }

    public void cleanTransactionResponse(Transaction transaction) {
        transaction.getAccount().setTransactions(null);
    }

    @SneakyThrows
    public List<Transaction> getAllTransactions(Long accountId) {
        return transactionRepository.getAllByAccount(
                accountRepository.findById(accountId)
                        .orElseThrow(() -> new MoneyAppException("No account found"))
        );
    }

    @SneakyThrows
    @Transactional
    public Integer doTransfer(TransactionDTO transactionDTO) {
        Account fromAccount = accountRepository.findById(
                        transactionDTO.getFromAccountId())
                .orElseThrow(() ->
                        new MoneyAppException(("Sender account not found")));

        Account toAccount = accountRepository.findById(
                        transactionDTO.getToAccountId())
                .orElseThrow(() ->
                        new MoneyAppException(("Receiver account not found")));

        validateTransaction(fromAccount, toAccount, transactionDTO);

        accountRepository.saveAll(List.of(fromAccount.setBalance(fromAccount.getBalance() - transactionDTO.getAmount()),
                toAccount.setBalance(toAccount.getBalance() + transactionDTO.getAmount())
        ));

        transactionRepository.saveAll(List.of(new Transaction()
                        .setCreatedTime(LocalDateTime.now())
                        .setTransactionType(TransactionType.TRANSFER.name())
                        .setDirection(TransactionDirection.OUTGOING.name())
                        .setAccount(fromAccount)
                        .setTransactionAmount(transactionDTO.getAmount()),
                new Transaction()
                        .setCreatedTime(LocalDateTime.now())
                        .setTransactionType(TransactionType.TRANSFER.name())
                        .setDirection(TransactionDirection.INCOMING.name())
                        .setAccount(toAccount)
                        .setTransactionAmount(transactionDTO.getAmount())));

        return 1;
    }

    @SneakyThrows
    private void validateTransaction(Account fromAccount, Account toAccount, TransactionDTO transactionDTO) {
        if (fromAccount.getBalance() < transactionDTO.getAmount()) throw new MoneyAppException("Insufficient balance");
        if (transactionDTO.getAmount() <= 0) throw new MoneyAppException("Invalid transfer amount");
        if (fromAccount.getId().equals(toAccount.getId())) throw new MoneyAppException("Cannot transfer to same account");
    }

    @SneakyThrows
    @Transactional
    public Integer doDeposit(TransactionDTO transactionDTO) {
        if (transactionDTO.getAmount() <= 0) throw new MoneyAppException("Invalid deposit amount");

        Account account = accountRepository.findById(
                        transactionDTO.getFromAccountId())
                .orElseThrow(() ->
                        new MoneyAppException(("Account not found")));
        accountRepository.save(account.setBalance(account.getBalance() + transactionDTO.getAmount()));
        transactionRepository.save(new Transaction()
                .setCreatedTime(LocalDateTime.now())
                .setTransactionType(TransactionType.DEPOSIT.name())
                .setDirection(TransactionDirection.INCOMING.name())
                .setAccount(account)
                .setTransactionAmount(transactionDTO.getAmount()));
        return 1;
    }
}
