package com.hwsinha.pennywise.dto.response;

import java.math.BigDecimal;

public record YearlySummaryResponse(
        int year,
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal netBalance
) {
}
