package com.hwsinha.pennywise.dto.response;

import com.hwsinha.pennywise.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(Long id,
                                  String title,
                                  BigDecimal amount,
                                  TransactionType type,
                                  String category,
                                  LocalDate transactionDate) {
}
