package com.hwsinha.pennywise.repository;

import com.hwsinha.pennywise.entity.Transaction;
import com.hwsinha.pennywise.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
                SELECT COALESCE(SUM(t.amount), 0)
                        FROM Transaction t
                        WHERE t.type = :type
                          AND t.transactionDate >= :startDate
                          AND t.transactionDate < :endDate
            """)
    BigDecimal sumAmountByTypeAndDateRange(
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT t.category, COALESCE(SUM(t.amount), 0)
                FROM Transaction t
                WHERE t.type = :type
                  AND t.transactionDate >= :startDate
                  AND t.transactionDate < :endDate
                GROUP BY t.category
                ORDER BY SUM(t.amount) DESC
            """)
    List<Object[]> findCategoryTotalsByTypeAndDateRange(@Param("type") TransactionType type,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate);

    List<Transaction> findByTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateDesc(
            LocalDate startDate, LocalDate endDate
    );

    Page<Transaction> findByType(
            TransactionType type,
            Pageable pageable
    );

    Page<Transaction> findByCategoryIgnoreCase(
            String category,
            Pageable pageable
    );

    Page<Transaction> findByTypeAndCategoryIgnoreCase(
            TransactionType type,
            String category,
            Pageable pageable
    );

}
