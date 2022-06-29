package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@RestApiController("transaction")
public class MoneyAppTransactionController {
    private final ProducerTemplate producerTemplate;

    @GetMapping("/{accountId}")
    public Object getAccountTransaction(@PathVariable("accountId") Long accountId) {
        return producerTemplate.requestBody(MoneyAppRoute.GET_ACCOUNT_TRANSACTION_ROUTE, accountId, Object.class);
    }

    @PostMapping("/transfer")
    public Object doTransfer(@RequestBody TransactionDTO transaction) {
        return producerTemplate.requestBody(MoneyAppRoute.PERFORM_TRANSFER_TRANSACTION_ROUTE, transaction, Object.class);
    }

    @PostMapping("/deposit")
    public Object doDeposit(@RequestBody TransactionDTO transaction) {
        return producerTemplate.requestBody(MoneyAppRoute.PERFORM_DEPOSIT_TRANSACTION_ROUTE, transaction, Object.class);
    }
}
