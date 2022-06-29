package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestApiController("account")
@AllArgsConstructor
public class MoneyAppAccountController {
    private final ProducerTemplate producerTemplate;

    @GetMapping
    public Object getAllAccounts() {
        return producerTemplate.requestBody(MoneyAppRoute.GET_ALL_ACCOUNT_ROUTE, "", Object.class);
    }

    @GetMapping("/{accountId}")
    public Object getAccountById(@PathVariable("accountId") Long accountId) {
        return producerTemplate.requestBody(MoneyAppRoute.GET_ACCOUNT_BY_ID_ROUTE, accountId, Object.class);
    }

    @PostMapping
    public Object createAccount(@RequestBody Account account) {
        return producerTemplate.requestBody(MoneyAppRoute.CREATE_ACCOUNT_ROUTE, account, Object.class);
    }
}
