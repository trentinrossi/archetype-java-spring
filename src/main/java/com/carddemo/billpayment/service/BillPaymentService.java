package com.carddemo.billpayment.service;

import com.carddemo.billpayment.dto.BillPaymentRequestDto;
import com.carddemo.billpayment.dto.BillPaymentResponseDto;
import com.carddemo.billpayment.entity.Account;
import com.carddemo.billpayment.entity.Transaction;
import com.carddemo.billpayment.repository.AccountRepository;
import com.carddemo.billpayment.repository.CardCrossReferenceRepository;
import com.carddemo.billpayment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Bill Payment Service - Orchestrates the complete bill payment workflow
 * Implements all business rules for bill payment processing
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BillPaymentService {

    private final AccountRepository accountRepository;
    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Process bill payment following the complete workflow:
     * BR001: Account Validation
     * BR002: Balance Check
     * BR003: Payment Confirmation
     * BR004: Full Balance Payment
     * BR005: Transaction ID Generation
     * BR006: Bill Payment Transaction Recording
     * BR007: Account Balance Update
     */
    @Transactional
    public BillPaymentResponseDto processBillPayment(BillPaymentRequestDto request) {
        log.info("Processing bill payment for account: {}", request.getAccountId());

        // BR001: Account Validation - Validates that the entered account ID exists in the system
        if (request.getAccountId() == null || request.getAccountId().trim().isEmpty()) {
            throw new IllegalArgumentException("Acct ID can NOT be empty...");
        }

        Account account = accountRepository.findByAcctId(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));

        log.info("Account validated successfully: {}", request.getAccountId());

        // BR002: Balance Check - Verifies that the account has a positive balance to pay
        if (account.getAcctCurrBal().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Account has no balance to pay: {}", request.getAccountId());
            throw new IllegalArgumentException("You have nothing to pay...");
        }

        log.info("Balance check passed. Current balance: {}", account.getAcctCurrBal());

        // Validate card number
        if (request.getCardNumber() == null || request.getCardNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }

        // Validate card is associated with account
        boolean cardValid = cardCrossReferenceRepository.existsByXrefAcctIdAndXrefCardNum(
                request.getAccountId(), request.getCardNumber());
        
        if (!cardValid) {
            log.warn("Card number not associated with account: {}", request.getAccountId());
            throw new IllegalArgumentException("Card number is not associated with this account");
        }

        log.info("Card validation passed for account: {}", request.getAccountId());

        // BR003: Payment Confirmation - Requires user confirmation before processing payment
        if (request.getConfirmation() == null || request.getConfirmation().trim().isEmpty()) {
            log.info("Confirmation required for payment. Returning account information.");
            return createConfirmationResponse(account, request.getCardNumber());
        }

        String confirmation = request.getConfirmation().trim().toUpperCase();

        if (confirmation.equals("N")) {
            log.info("Payment cancelled by user for account: {}", request.getAccountId());
            return createCancelledResponse(account, request.getCardNumber());
        }

        if (!confirmation.equals("Y")) {
            throw new IllegalArgumentException("Invalid value. Valid values are (Y/N)...");
        }

        log.info("Payment confirmed by user for account: {}", request.getAccountId());

        // BR004: Full Balance Payment - Payment processes the full current account balance
        BigDecimal paymentAmount = account.getAcctCurrBal();
        BigDecimal previousBalance = account.getAcctCurrBal();

        log.info("Processing full balance payment of {} for account: {}", paymentAmount, request.getAccountId());

        // BR005: Transaction ID Generation - Generates unique sequential transaction ID
        // BR006: Bill Payment Transaction Recording - Records bill payment with specific transaction attributes
        Transaction transaction = Transaction.createBillPaymentTransaction(
                paymentAmount, request.getCardNumber(), account);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Bill payment transaction recorded with ID: {}", savedTransaction.getTranId());

        // BR007: Account Balance Update - Updates account balance after successful payment
        account.processFullBalancePayment();
        accountRepository.save(account);
        log.info("Account balance updated to zero for account: {}", request.getAccountId());

        // Create success response
        BillPaymentResponseDto response = new BillPaymentResponseDto(
                "SUCCESS",
                "Bill payment processed successfully",
                account.getAcctId(),
                paymentAmount,
                previousBalance,
                account.getAcctCurrBal(),
                savedTransaction.getTranId(),
                request.getCardNumber()
        );

        log.info("Bill payment completed successfully for account: {} with transaction ID: {}", 
                 request.getAccountId(), savedTransaction.getTranId());

        return response;
    }

    /**
     * Get account information for bill payment confirmation
     */
    @Transactional(readOnly = true)
    public BillPaymentResponseDto getAccountForPaymentConfirmation(String accountId, String cardNumber) {
        log.info("Getting account information for payment confirmation: {}", accountId);

        // BR001: Account Validation
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Acct ID can NOT be empty...");
        }

        Account account = accountRepository.findByAcctId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));

        // BR002: Balance Check
        if (account.getAcctCurrBal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("You have nothing to pay...");
        }

        // Validate card
        if (cardNumber != null && !cardNumber.trim().isEmpty()) {
            boolean cardValid = cardCrossReferenceRepository.existsByXrefAcctIdAndXrefCardNum(
                    accountId, cardNumber);
            
            if (!cardValid) {
                throw new IllegalArgumentException("Card number is not associated with this account");
            }
        }

        return createConfirmationResponse(account, cardNumber);
    }

    private BillPaymentResponseDto createConfirmationResponse(Account account, String cardNumber) {
        BillPaymentResponseDto response = new BillPaymentResponseDto();
        response.setStatus("PENDING_CONFIRMATION");
        response.setMessage("Please confirm payment. Current balance: " + 
                          String.format("$%.2f", account.getAcctCurrBal()));
        response.setAccountId(account.getAcctId());
        response.setPaymentAmount(account.getAcctCurrBal());
        response.setPreviousBalance(account.getAcctCurrBal());
        response.setNewBalance(BigDecimal.ZERO);
        response.setMaskedCardNumber(maskCardNumber(cardNumber));
        return response;
    }

    private BillPaymentResponseDto createCancelledResponse(Account account, String cardNumber) {
        BillPaymentResponseDto response = new BillPaymentResponseDto();
        response.setStatus("CANCELLED");
        response.setMessage("Payment cancelled by user");
        response.setAccountId(account.getAcctId());
        response.setPaymentAmount(BigDecimal.ZERO);
        response.setPreviousBalance(account.getAcctCurrBal());
        response.setNewBalance(account.getAcctCurrBal());
        response.setMaskedCardNumber(maskCardNumber(cardNumber));
        return response;
    }

    private String maskCardNumber(String cardNum) {
        if (cardNum == null || cardNum.length() != 16) {
            return "****************";
        }
        return "************" + cardNum.substring(12);
    }
}
