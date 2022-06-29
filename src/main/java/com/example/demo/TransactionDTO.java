package com.example.demo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TransactionDTO {
    private Double amount;
    private Long fromAccountId;
    private Long toAccountId;
}
