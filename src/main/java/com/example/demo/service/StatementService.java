package com.example.demo.service;

import com.example.demo.dto.CreateStatementRequestDto;
import com.example.demo.dto.StatementResponseDto;
import com.example.demo.entity.Statement;
import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Transaction;
import com.example.demo.enums.StatementStatus;
import com.example.demo.repository.StatementRepository;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementService {

    private final StatementRepository statementRepository;
    private final AccountRepository accountRepository;

    /**
     * BR002: Statement Generation Per Account
     * Generate a comprehensive statement for an account containing customer information,
     * account details, and all related transactions
     */
    @Transactional
    public StatementResponseDto generateStatement(CreateStatementRequestDto request) {
        log.info("Generating statement for account ID: {}", request.getAccountId());

        Account account = accountRepository.findByAccountId(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account ID must be 11 digits numeric and exist in account file"));

        Customer customer = account.getCustomer();
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found for account");
        }

        Statement statement = new Statement();
        
        // BR006: Customer Name Composition
        statement.composeCustomerName(
            customer.getFirstName(),
            customer.getMiddleName(),
            customer.getLastName()
        );
        
        // BR007: Complete Address Display
        statement.composeCompleteAddress(
            customer.getAddressLine1(),
            customer.getAddressLine2(),
            customer.getAddressLine3(),
            customer.getStateCode(),
            customer.getCountryCode(),
            customer.getZipCode()
        );
        
        statement.setAccountId(account.getAccountId());
        statement.setCurrentBalance(account.getCurrentBalance());
        statement.setFicoScore(customer.getFicoCreditScore());
        
        // BR003: Transaction Amount Summation
        BigDecimal totalTransactionAmount = account.calculateTotalTransactionAmount();
        statement.setTotalTransactionAmount(totalTransactionAmount);
        
        statement.setCustomer(customer);
        statement.setAccount(account);
        statement.setStatementPeriodStart(request.getStatementPeriodStart());
        statement.setStatementPeriodEnd(request.getStatementPeriodEnd());
        statement.setStatus(StatementStatus.GENERATED);
        
        // BR004: Dual Format Statement Output
        // BR008: HTML Statement Styling Standards
        statement.generateDualFormatStatements("Financial Services Bank", "123 Main Street, New York, NY 10001");
        
        Statement savedStatement = statementRepository.save(statement);
        log.info("Statement generated successfully for account ID: {}", savedStatement.getAccountId());
        return convertToResponse(savedStatement);
    }

    @Transactional(readOnly = true)
    public Optional<StatementResponseDto> getStatementById(Long id) {
        log.info("Retrieving statement by ID: {}", id);
        return statementRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<StatementResponseDto> getAllStatements(Pageable pageable) {
        log.info("Retrieving all statements with pagination");
        return statementRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<StatementResponseDto> getStatementsByAccountId(Long accountId, Pageable pageable) {
        log.info("Retrieving statements for account ID: {}", accountId);
        return statementRepository.findByAccountId(accountId, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<StatementResponseDto> getStatementsByCustomerId(Long customerId, Pageable pageable) {
        log.info("Retrieving statements for customer ID: {}", customerId);
        return statementRepository.findByCustomer_CustomerId(customerId, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<StatementResponseDto> getStatementsByStatus(StatementStatus status, Pageable pageable) {
        log.info("Retrieving statements with status: {}", status);
        return statementRepository.findByStatus(status, pageable).map(this::convertToResponse);
    }

    @Transactional
    public void deleteStatement(Long id) {
        log.info("Deleting statement with ID: {}", id);
        if (!statementRepository.existsById(id)) {
            throw new IllegalArgumentException("Statement not found");
        }
        statementRepository.deleteById(id);
        log.info("Statement deleted successfully with ID: {}", id);
    }

    /**
     * BR004: Get plain text format of statement
     */
    @Transactional(readOnly = true)
    public String getPlainTextStatement(Long id) {
        log.info("Retrieving plain text statement for ID: {}", id);
        Statement statement = statementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Statement not found"));
        return statement.getPlainTextContent();
    }

    /**
     * BR004: Get HTML format of statement
     */
    @Transactional(readOnly = true)
    public String getHtmlStatement(Long id) {
        log.info("Retrieving HTML statement for ID: {}", id);
        Statement statement = statementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Statement not found"));
        return statement.getHtmlContent();
    }

    /**
     * Update statement status
     */
    @Transactional
    public StatementResponseDto updateStatementStatus(Long id, StatementStatus status) {
        log.info("Updating statement status for ID: {} to {}", id, status);
        Statement statement = statementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Statement not found"));
        statement.setStatus(status);
        Statement updatedStatement = statementRepository.save(statement);
        return convertToResponse(updatedStatement);
    }

    /**
     * Regenerate statement formats
     */
    @Transactional
    public StatementResponseDto regenerateStatementFormats(Long id) {
        log.info("Regenerating statement formats for ID: {}", id);
        Statement statement = statementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Statement not found"));
        
        statement.generateDualFormatStatements("Financial Services Bank", "123 Main Street, New York, NY 10001");
        Statement updatedStatement = statementRepository.save(statement);
        return convertToResponse(updatedStatement);
    }

    private StatementResponseDto convertToResponse(Statement statement) {
        StatementResponseDto response = new StatementResponseDto();
        response.setId(statement.getId());
        response.setCustomerName(statement.getCustomerName());
        response.setCustomerAddress(statement.getCustomerAddress());
        response.setAccountId(statement.getAccountId());
        response.setCurrentBalance(statement.getCurrentBalance());
        response.setFicoScore(statement.getFicoScore());
        response.setTotalTransactionAmount(statement.getTotalTransactionAmount());
        response.setStatementPeriodStart(statement.getStatementPeriodStart());
        response.setStatementPeriodEnd(statement.getStatementPeriodEnd());
        response.setPlainTextContent(statement.getPlainTextContent());
        response.setHtmlContent(statement.getHtmlContent());
        response.setStatus(statement.getStatus());
        response.setStatusDisplayName(statement.getStatus().getDisplayName());
        response.setCreatedAt(statement.getCreatedAt());
        response.setUpdatedAt(statement.getUpdatedAt());
        return response;
    }
}
