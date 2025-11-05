package com.example.demo.controller;

import com.example.demo.dto.BillPaymentDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable String transactionId) {
        TransactionDTO transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }
    
    @GetMapping("/card/{cardNumber}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCardNumber(@PathVariable String cardNumber) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByCardNumber(cardNumber);
        return ResponseEntity.ok(transactions);
    }
    
    @PostMapping("/bill-payment")
    public ResponseEntity<BillPaymentDTO> processBillPayment(@Valid @RequestBody BillPaymentDTO paymentDTO) {
        BillPaymentDTO result = transactionService.processBillPayment(paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
