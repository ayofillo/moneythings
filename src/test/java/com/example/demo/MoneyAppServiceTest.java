package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class MoneyAppServiceTest {
    @InjectMocks
    private MoneyAppService moneyAppService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        Account account = new Account()
                .setId(1L)
                .setAccountName("deco")
                .setCreatedDate(LocalDateTime.now())
                .setBalance(2000.0);
        Account account2 = new Account()
                .setId(2L)
                .setAccountName("deco2")
                .setCreatedDate(LocalDateTime.now())
                .setBalance(2000.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));
        when(accountRepository.save(any())).thenReturn(new Account());
        when(transactionRepository.save(any())).thenReturn(new Transaction());
    }

    @DisplayName("Test fund transfer")
    @Test
    public void testAmountTransfer() {
        TransactionDTO transactionDTO = new TransactionDTO()
                .setAmount(400.0)
                .setFromAccountId(1L)
                .setToAccountId(2L);
        Integer response = moneyAppService.doTransfer(transactionDTO);

        Assertions.assertEquals(1, response);
    }

    @DisplayName("Test fund transfer - Invalid Account")
    @Test()
    public void testInvalidAccount() {
        TransactionDTO transactionDTO = new TransactionDTO()
                .setAmount(400.0)
                .setFromAccountId(1L)
                .setToAccountId(1L);
        MoneyAppException exception = Assertions.assertThrows(MoneyAppException.class, () -> moneyAppService.doTransfer(transactionDTO));
        Assertions.assertEquals("Cannot transfer to same account", exception.getMessage());
    }

    @DisplayName("Test fund transfer - Amount Failure")
    @Test()
    public void testInvalidAmount() {
        TransactionDTO transactionDTO = new TransactionDTO()
                .setAmount(-40000.0)
                .setFromAccountId(1L)
                .setToAccountId(2L);
        MoneyAppException exception = Assertions.assertThrows(MoneyAppException.class, () -> moneyAppService.doTransfer(transactionDTO));
        Assertions.assertEquals("Invalid transfer amount", exception.getMessage());
    }

    @DisplayName("Test fund transfer - balance Failure")
    @Test()
    public void testInsufficient() {
        TransactionDTO transactionDTO = new TransactionDTO()
                .setAmount(40000.0)
                .setFromAccountId(1L)
                .setToAccountId(2L);
        MoneyAppException exception = Assertions.assertThrows(MoneyAppException.class, () -> moneyAppService.doTransfer(transactionDTO));
        Assertions.assertEquals("Insufficient balance", exception.getMessage());
    }

    @DisplayName("Test deposit")
    @Test
    public void testAmountDeposit() {
        TransactionDTO transactionDTO = new TransactionDTO()
                .setAmount(400.0)
                .setFromAccountId(1L)
                .setToAccountId(2L);
        Integer response = moneyAppService.doDeposit(transactionDTO);

        Assertions.assertEquals(1, response);
    }
}
