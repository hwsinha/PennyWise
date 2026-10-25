package com.hwsinha.pennywise.mapper;

import com.hwsinha.pennywise.dto.request.CreateTransactionRequest;
import com.hwsinha.pennywise.dto.response.TransactionResponse;
import com.hwsinha.pennywise.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(CreateTransactionRequest request) {
        Transaction transaction = new Transaction();

        transaction.setTitle(request.title());
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setCategory(request.category());
        transaction.setTransactionDate(request.transactionDate());

        return transaction;
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getTitle(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCategory(),
                transaction.getTransactionDate()
        );
    }

    public void updateEntity(Transaction transaction,
                             CreateTransactionRequest request){
        transaction.setTitle(request.title());
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setCategory(request.category());
        transaction.setTransactionDate(request.transactionDate());
    }
}
