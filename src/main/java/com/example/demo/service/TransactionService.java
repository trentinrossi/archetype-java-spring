package com.example.demo.service;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
import com.example.demo.dto.UpdateTransactionRequestDto;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public TransactionResponseDto createTransaction(CreateTransactionRequestDto request) {
        log.info("Creating new transaction with type: {}", request.getTranTypeCd());

        if (transactionRepository.existsByTranId(request.getTranId())) {
            throw new IllegalArgumentException("Tran ID already exist...");
        }

        Transaction transaction = new Transaction();
        transaction.setTranId(request.getTranId());
        transaction.setTranTypeCd(request.getTranTypeCd());
        transaction.setTranCatCd(request.getTranCatCd());
        transaction.setTranSource(request.getTranSource());
        transaction.setTranDesc(request.getTranDesc());
        transaction.setTranAmt(request.getTranAmt());
        transaction.setTranCardNum(request.getTranCardNum());
        transaction.setTranMerchantId(request.getTranMerchantId());
        transaction.setTranMerchantName(request.getTranMerchantName());
        transaction.setTranMerchantCity(request.getTranMerchantCity());
        transaction.setTranMerchantZip(request.getTranMerchantZip());
        transaction.setTranOrigTs(request.getTranOrigTs());
        transaction.setTranProcTs(request.getTranProcTs());
        transaction.setTranAcctId(request.getTranAcctId());

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully with ID: {}", savedTransaction.getTranId());
        return convertToResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public Optional<TransactionResponseDto> getTransactionById(Long id) {
        log.info("Retrieving transaction with ID: {}", id);
        return transactionRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<TransactionResponseDto> getTransactionByTranId(Long tranId) {
        log.info("Retrieving transaction with transaction ID: {}", tranId);
        return transactionRepository.findByTranId(tranId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByAccountId(String accountId) {
        log.info("Retrieving transactions for account ID: {}", accountId);
        return transactionRepository.findByTranAcctId(accountId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByCardNumber(String cardNumber) {
        log.info("Retrieving transactions for card number: {}", cardNumber);
        return transactionRepository.findByTranCardNum(cardNumber).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponseDto updateTransaction(Long id, UpdateTransactionRequestDto request) {
        log.info("Updating transaction with ID: {}", id);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        if (request.getTranTypeCd() != null) {
            transaction.setTranTypeCd(request.getTranTypeCd());
        }
        if (request.getTranCatCd() != null) {
            transaction.setTranCatCd(request.getTranCatCd());
        }
        if (request.getTranSource() != null) {
            transaction.setTranSource(request.getTranSource());
        }
        if (request.getTranDesc() != null) {
            transaction.setTranDesc(request.getTranDesc());
        }
        if (request.getTranAmt() != null) {
            transaction.setTranAmt(request.getTranAmt());
        }
        if (request.getTranMerchantId() != null) {
            transaction.setTranMerchantId(request.getTranMerchantId());
        }
        if (request.getTranMerchantName() != null) {
            transaction.setTranMerchantName(request.getTranMerchantName());
        }
        if (request.getTranMerchantCity() != null) {
            transaction.setTranMerchantCity(request.getTranMerchantCity());
        }
        if (request.getTranMerchantZip() != null) {
            transaction.setTranMerchantZip(request.getTranMerchantZip());
        }
        if (request.getTranProcTs() != null) {
            transaction.setTranProcTs(request.getTranProcTs());
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction updated successfully with ID: {}", updatedTransaction.getTranId());
        return convertToResponse(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        log.info("Deleting transaction with ID: {}", id);

        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction not found");
        }

        transactionRepository.deleteById(id);
        log.info("Transaction deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        log.info("Retrieving all transactions with pagination");
        return transactionRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public Long generateNextTransactionId() {
        log.info("Generating next transaction ID (BR005)");

        Optional<Long> highestIdOpt = transactionRepository.findMaxTranId();

        if (highestIdOpt.isEmpty()) {
            log.info("No existing transactions found, starting with ID: 1");
            return 1L;
        }

        Long highestId = highestIdOpt.get();
        Long nextId = highestId + 1;
        log.info("Highest existing transaction ID: {}, Generated next transaction ID: {}", highestId, nextId);

        if (transactionRepository.existsByTranId(nextId)) {
            log.error("Duplicate key error: Transaction ID {} already exists", nextId);
            throw new IllegalStateException("Tran ID already exist...");
        }

        return nextId;
    }

    @Transactional
    public TransactionResponseDto createBillPaymentTransaction(String accountId, String cardNum, BigDecimal amount) {
        log.info("Creating bill payment transaction for account ID: {} (BR006)", accountId);

        Long nextTranId = generateNextTransactionId();
        LocalDateTime currentTimestamp = generateCurrentTimestamp();

        Transaction transaction = Transaction.createBillPaymentTransaction(nextTranId, amount, cardNum, accountId);

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Bill payment transaction created successfully with ID: {}", savedTransaction.getTranId());

        return convertToResponse(savedTransaction);
    }

    public LocalDateTime generateCurrentTimestamp() {
        log.debug("Generating current timestamp (BR008)");
        LocalDateTime now = LocalDateTime.now();
        log.debug("Generated timestamp: {}", now);
        return now;
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getBillPaymentTransactions(Pageable pageable) {
        log.info("Retrieving bill payment transactions");
        return transactionRepository.findBillPaymentTransactions(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getBillPaymentTransactionsByAccountId(String accountId, Pageable pageable) {
        log.info("Retrieving bill payment transactions for account ID: {}", accountId);
        return transactionRepository.findBillPaymentTransactionsByTranAcctId(accountId, pageable).map(this::convertToResponse);
    }

    private TransactionResponseDto convertToResponse(Transaction transaction) {
        TransactionResponseDto response = new TransactionResponseDto();
        response.setId(transaction.getId());
        response.setTranId(transaction.getTranId());
        response.setTranTypeCd(transaction.getTranTypeCd());
        response.setTranCatCd(transaction.getTranCatCd());
        response.setTranSource(transaction.getTranSource());
        response.setTranDesc(transaction.getTranDesc());
        response.setTranAmt(transaction.getTranAmt());
        response.setTranAmtFormatted(transaction.getFormattedTransactionAmount());
        response.setTranCardNum(transaction.getTranCardNum());
        response.setTranMerchantId(transaction.getTranMerchantId());
        response.setTranMerchantName(transaction.getTranMerchantName());
        response.setTranMerchantCity(transaction.getTranMerchantCity());
        response.setTranMerchantZip(transaction.getTranMerchantZip());
        response.setTranOrigTs(transaction.getTranOrigTs());
        response.setTranProcTs(transaction.getTranProcTs());
        response.setTranAcctId(transaction.getTranAcctId());
        response.setIsBillPayment(transaction.isBillPaymentTransaction());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }
}
