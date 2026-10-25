package com.hwsinha.pennywise.controller;

import com.hwsinha.pennywise.dto.request.CreateTransactionRequest;
import com.hwsinha.pennywise.dto.response.TransactionResponse;
import com.hwsinha.pennywise.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @Valid @RequestBody CreateTransactionRequest createTransactionRequest){
        return transactionService.createTransaction(createTransactionRequest);
    }

    @GetMapping
    public List<TransactionResponse> getTransactions(){
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransactionById(@PathVariable Long id){
        return transactionService.getTransactionById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable Long id){
        transactionService.deleteTransaction(id);
    }

    @PutMapping("/{id}")
    public TransactionResponse updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody CreateTransactionRequest request
    ){
        return transactionService.updateTransaction(id, request);
    }
}
