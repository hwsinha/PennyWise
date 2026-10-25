package com.hwsinha.pennywise.service;

import com.hwsinha.pennywise.dto.request.CreateTransactionRequest;
import com.hwsinha.pennywise.dto.response.TransactionResponse;
import com.hwsinha.pennywise.entity.Transaction;
import com.hwsinha.pennywise.exception.ResourceNotFoundException;
import com.hwsinha.pennywise.mapper.TransactionMapper;
import com.hwsinha.pennywise.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    public TransactionResponse getTransactionById(long id) {
        Transaction transaction= transactionRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Transaction with id: " + id + " not found"
                ));
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction=transactionRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Transaction with id: " + id + " not found"
                ));
        transactionRepository.delete(transaction);
    }

    @Transactional
    public TransactionResponse updateTransaction(Long id,
                                                 CreateTransactionRequest request) {
        Transaction transaction= transactionRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Transaction with id: " + id + " not found"
                ));
        transactionMapper.updateEntity(transaction, request);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toResponse(updatedTransaction);
    }
}
