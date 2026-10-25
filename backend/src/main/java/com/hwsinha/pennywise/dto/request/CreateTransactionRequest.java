package com.hwsinha.pennywise.dto.request;

import com.hwsinha.pennywise.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransactionRequest(
        @NotBlank
        String title,
        @NotNull
        @Positive
        BigDecimal amount,
        @NotNull
        TransactionType type,
        @NotBlank
        String category,
        @NotNull
        LocalDate transactionDate) {
}
