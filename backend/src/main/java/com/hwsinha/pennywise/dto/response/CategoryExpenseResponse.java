package com.hwsinha.pennywise.dto.response;

import java.math.BigDecimal;

public record CategoryExpenseResponse(
        String category,
        BigDecimal totalAmount) {
}
