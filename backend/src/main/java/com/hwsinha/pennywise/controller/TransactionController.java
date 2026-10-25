package com.hwsinha.pennywise.controller;

import com.hwsinha.pennywise.dto.request.CreateTransactionRequest;
import com.hwsinha.pennywise.dto.response.CategoryExpenseResponse;
import com.hwsinha.pennywise.dto.response.MonthlySummaryResponse;
import com.hwsinha.pennywise.dto.response.TransactionResponse;
import com.hwsinha.pennywise.dto.response.YearlySummaryResponse;
import com.hwsinha.pennywise.entity.TransactionType;
import com.hwsinha.pennywise.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @Valid @RequestBody CreateTransactionRequest createTransactionRequest) {
        return transactionService.createTransaction(createTransactionRequest);
    }


    @GetMapping("/{id}")
    public TransactionResponse getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }
    @GetMapping
    public Page<TransactionResponse> getAllTransactions(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String category,
            @PageableDefault(
                    size = 10,
                    sort = "transactionDate",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return transactionService.getAllTransactions(
                type, category, pageable
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }

    @PutMapping("/{id}")
    public TransactionResponse updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        return transactionService.updateTransaction(id, request);
    }

    @GetMapping("/summary")
    public MonthlySummaryResponse getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return transactionService.getMonthlySummary(year, month);
    }

    @GetMapping("/summary/categories")
    public List<CategoryExpenseResponse> getMonthlySummaryCategories(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return transactionService.getMonthlyExpenseByCategory(year, month);
    }

    @GetMapping("/range")
    public List<TransactionResponse> getTransactionBetweenDates(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return transactionService
                .getTransactionBetweenDates(startDate, endDate);
    }

    @GetMapping("/summary/yearly")
    public YearlySummaryResponse getYearlySummary(
            @RequestParam int year
    ) {
        return transactionService.getYearlySummary(year);
    }

    @GetMapping("/summary/yearly/categories")
    public List<CategoryExpenseResponse> getYearlyExpenseByCategory(
            @RequestParam int year
    ) {
        return transactionService.getYearlyExpenseByCategory(year);
    }

}
