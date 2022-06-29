package com.example.demo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String code;
    private String message;
}
