package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MoneyAppRoute extends RouteBuilder {
    public static final String GET_ALL_ACCOUNT_ROUTE = "direct:get-all-account";
    public static final String GET_ACCOUNT_BY_ID_ROUTE = "direct:get-account-by-id";
    public static final String CREATE_ACCOUNT_ROUTE = "direct:create-account";
    public static final String GET_ACCOUNT_TRANSACTION_ROUTE = "direct:get-account-transactions";
    public static final String PERFORM_TRANSFER_TRANSACTION_ROUTE = "direct:perform-transfer-transactions";
    public static final String PERFORM_DEPOSIT_TRANSACTION_ROUTE = "direct:perform-deposit-transactions";

    @Override
    public void configure() {

        onException(Exception.class)
                .handled(true)
                .to("log:GeneralError?level=ERROR")
                .logStackTrace(true)
                .process(exchange -> {
                    Throwable exception = ((Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT)).getCause();
                    log.error("Exception occurred", exception);
                    exchange.getIn().setBody(new ErrorResponse()
                            .setCode("500")
                            .setMessage(exception.getMessage()));
                });

        from(GET_ALL_ACCOUNT_ROUTE).routeId("get-all-account-route")
                .log(LoggingLevel.INFO, "retrieving all accounts")
                .bean(MoneyAppService.class, "getAllAccounts")
                .bean(MoneyAppService.class, "formatAccountResponse");

        from(GET_ACCOUNT_BY_ID_ROUTE).routeId("get-account-id-route")
                .log(LoggingLevel.INFO, "retrieving account by id")
                .bean(MoneyAppService.class, "getAccountById")
                .bean(MoneyAppService.class, "cleanAccountResponse");

        from(CREATE_ACCOUNT_ROUTE).routeId("create-account-route")
                .log(LoggingLevel.INFO, "creating accounts")
                .bean(MoneyAppService.class, "createAccount");

        from(GET_ACCOUNT_TRANSACTION_ROUTE).routeId("get-account-transaction-route")
                .log(LoggingLevel.INFO, "retrieving account transactions by id")
                .bean(MoneyAppService.class, "getAllTransactions")
                .bean(MoneyAppService.class, "formatTransactionResponse");

        from(PERFORM_TRANSFER_TRANSACTION_ROUTE).routeId("perform-transaction-route")
                .log(LoggingLevel.INFO, "performing transfer")
                .bean(MoneyAppService.class, "doTransfer");

        from(PERFORM_DEPOSIT_TRANSACTION_ROUTE).routeId("perform-deposit-transaction-route")
                .log(LoggingLevel.INFO, "performing deposit")
                .bean(MoneyAppService.class, "doDeposit");
    }
}
