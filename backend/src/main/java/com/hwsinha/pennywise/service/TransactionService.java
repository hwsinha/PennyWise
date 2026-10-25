package com.hwsinha.pennywise.service;

import com.hwsinha.pennywise.dto.request.CreateTransactionRequest;
import com.hwsinha.pennywise.dto.response.CategoryExpenseResponse;
import com.hwsinha.pennywise.dto.response.MonthlySummaryResponse;
import com.hwsinha.pennywise.dto.response.TransactionResponse;
import com.hwsinha.pennywise.dto.response.YearlySummaryResponse;
import com.hwsinha.pennywise.entity.Transaction;
import com.hwsinha.pennywise.entity.TransactionType;
import com.hwsinha.pennywise.exception.ResourceNotFoundException;
import com.hwsinha.pennywise.mapper.TransactionMapper;
import com.hwsinha.pennywise.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        var transaction = transactionMapper.toEntity(request);
        var savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(savedTransaction);
    }

    public TransactionResponse getTransactionById(long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction with id: " + id + " not found"
                ));
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction with id: " + id + " not found"
                ));
        transactionRepository.delete(transaction);
    }

    @Transactional
    public TransactionResponse updateTransaction(Long id,
                                                 CreateTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction with id: " + id + " not found"
                ));
        transactionMapper.updateEntity(transaction, request);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toResponse(updatedTransaction);
    }

    public MonthlySummaryResponse getMonthlySummary(
            int year, int month
    ) {
        validateYearAndMonth(year, month);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1);

        BigDecimal totalIncome = transactionRepository.sumAmountByTypeAndDateRange(
                TransactionType.INCOME, startDate, endDate
        );

        BigDecimal totalExpense = transactionRepository.sumAmountByTypeAndDateRange(
                TransactionType.EXPENSE, startDate, endDate
        );
        BigDecimal netBalance = totalIncome.subtract(totalExpense);
        return new MonthlySummaryResponse(
                year,
                month,
                totalIncome,
                totalExpense,
                netBalance
        );
    }

    public List<CategoryExpenseResponse> getMonthlyExpenseByCategory(
            int year, int month
    ) {
        validateYearAndMonth(year, month);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1);

        return transactionRepository.findCategoryTotalsByTypeAndDateRange(
                        TransactionType.EXPENSE, startDate, endDate
                ).stream()
                .map(row -> new CategoryExpenseResponse(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    public List<TransactionResponse> getTransactionBetweenDates(
            LocalDate startDate, LocalDate endDate
    ){
        if(startDate==null || endDate==null){
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if(startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        return transactionRepository
                .findByTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateDesc(
                        startDate, endDate.plusDays(1)
                ).stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    public YearlySummaryResponse getYearlySummary(int year) {
        if (year < 1) {
            throw new IllegalArgumentException(
                    "Year must be greater than or equal to 1"
            );
        }

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = startDate.plusYears(1);

        BigDecimal totalIncome =
                transactionRepository.sumAmountByTypeAndDateRange(
                        TransactionType.INCOME,
                        startDate,
                        endDate
                );

        BigDecimal totalExpenses =
                transactionRepository.sumAmountByTypeAndDateRange(
                        TransactionType.EXPENSE,
                        startDate,
                        endDate
                );

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        return new YearlySummaryResponse(
                year,
                totalIncome,
                totalExpenses,
                netBalance
        );
    }

    public List<CategoryExpenseResponse> getYearlyExpenseByCategory(int year) {
        if (year < 1) {
            throw new IllegalArgumentException(
                    "Year must be greater than or equal to 1"
            );
        }

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = startDate.plusYears(1);

        return transactionRepository.findCategoryTotalsByTypeAndDateRange(
                        TransactionType.EXPENSE,
                        startDate,
                        endDate
                )
                .stream()
                .map(row -> new CategoryExpenseResponse(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    public Page<TransactionResponse> getAllTransactions(
            TransactionType type,
            String category,
            Pageable pageable
    ) {
        Page<Transaction> transactions;

        if (type != null && category != null && !category.isBlank()) {
            transactions = transactionRepository
                    .findByTypeAndCategoryIgnoreCase(type, category, pageable);
        } else if (type != null) {
            transactions = transactionRepository.findByType(type, pageable);
        } else if (category != null && !category.isBlank()) {
            transactions = transactionRepository
                    .findByCategoryIgnoreCase(category, pageable);
        } else {
            transactions = transactionRepository.findAll(pageable);
        }

        return transactions.map(transactionMapper::toResponse);
    }



    private void validateYearAndMonth(int year, int month) {
        if (year < 1) {
            throw new IllegalArgumentException(
                    "Year must be greater than or equal to 1"
            );
        }

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException(
                    "Month must be between 1 and 12"
            );
        }
    }
}
