package com.example.demo.service;

import com.example.demo.dto.PageResponseDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yy");
    
    @Transactional(readOnly = true)
    public PageResponseDTO<TransactionDTO> getAllTransactions(int page, int size, String startTransactionId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionId").ascending());
        
        Page<Transaction> transactionPage;
        if (startTransactionId != null && !startTransactionId.trim().isEmpty()) {
            transactionPage = transactionRepository.findTransactionsStartingFrom(startTransactionId, pageable);
        } else {
            transactionPage = transactionRepository.findAll(pageable);
        }
        
        List<TransactionDTO> transactionDTOs = transactionPage.getContent().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return new PageResponseDTO<>(
            transactionDTOs,
            transactionPage.getNumber(),
            transactionPage.getSize(),
            transactionPage.getTotalElements(),
            transactionPage.getTotalPages(),
            transactionPage.isFirst(),
            transactionPage.isLast(),
            transactionPage.hasNext(),
            transactionPage.hasPrevious()
        );
    }
    
    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return convertToDTO(transaction);
    }
    
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByDateRange(String startDate, String endDate) {
        LocalDateTime start = parseDate(startDate).atStartOfDay();
        LocalDateTime end = parseDate(endDate).atTime(LocalTime.MAX);
        
        List<Transaction> transactions = transactionRepository.findByDateRange(start, end);
        return transactions.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TransactionDTO> getMonthlyTransactions() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startDate = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);
        
        List<Transaction> transactions = transactionRepository.findByDateRange(startDate, endDate);
        return transactions.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TransactionDTO> getYearlyTransactions() {
        int currentYear = LocalDate.now().getYear();
        LocalDateTime startDate = LocalDate.of(currentYear, 1, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(currentYear, 12, 31).atTime(LocalTime.MAX);
        
        List<Transaction> transactions = transactionRepository.findByDateRange(startDate, endDate);
        return transactions.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PageResponseDTO<TransactionDTO> getTransactionsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        Page<Transaction> transactionPage = transactionRepository.findByUserId(userId, pageable);
        
        List<TransactionDTO> transactionDTOs = transactionPage.getContent().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return new PageResponseDTO<>(
            transactionDTOs,
            transactionPage.getNumber(),
            transactionPage.getSize(),
            transactionPage.getTotalElements(),
            transactionPage.getTotalPages(),
            transactionPage.isFirst(),
            transactionPage.isLast(),
            transactionPage.hasNext(),
            transactionPage.hasPrevious()
        );
    }
    
    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setTransactionDate(transaction.getTransactionDate().format(DATE_FORMATTER));
        dto.setTransactionDescription(transaction.getTransactionDescription());
        dto.setTransactionAmount(transaction.getTransactionAmount());
        dto.setUserId(transaction.getUserId());
        
        if (transaction.getUser() != null) {
            dto.setUserFullName(transaction.getUser().getFullName());
        }
        
        dto.setCreatedAt(transaction.getCreatedAt().format(DATE_TIME_FORMATTER));
        return dto;
    }
    
    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format. Expected: yyyy-MM-dd");
        }
    }
}
