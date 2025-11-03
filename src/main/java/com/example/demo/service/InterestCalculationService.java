package com.example.demo.service;

import com.example.demo.dto.InterestCalculationDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestCalculationService {
    
    private final TransactionCategoryBalanceRepository transactionCategoryBalanceRepository;
    private final AccountRepository accountRepository;
    private final DisclosureGroupRepository disclosureGroupRepository;
    private final TransactionRepository transactionRepository;
    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    
    private static final String DEFAULT_GROUP_ID = "DEFAULT";
    private static final String INTEREST_TRANSACTION_TYPE = "01";
    private static final String INTEREST_CATEGORY_CODE = "05";
    private static final String SYSTEM_SOURCE = "System";
    private static final BigDecimal DIVISOR = new BigDecimal("1200");
    
    @Transactional
    public List<InterestCalculationDTO> calculateMonthlyInterest(String processingDate) {
        List<InterestCalculationDTO> calculations = new ArrayList<>();
        List<TransactionCategoryBalance> balances = transactionCategoryBalanceRepository.findAll();
        
        String currentAccountId = null;
        BigDecimal totalInterest = BigDecimal.ZERO;
        
        for (TransactionCategoryBalance balance : balances) {
            if (!balance.getAccountId().equals(currentAccountId)) {
                if (currentAccountId != null) {
                    updateAccountBalance(currentAccountId, totalInterest);
                }
                currentAccountId = balance.getAccountId();
                totalInterest = BigDecimal.ZERO;
            }
            
            Account account = accountRepository.findById(balance.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found: " + balance.getAccountId()));
            
            BigDecimal interestRate = getInterestRate(
                account.getGroupId(),
                balance.getTransactionTypeCode(),
                balance.getTransactionCategoryCode()
            );
            
            if (interestRate.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal monthlyInterest = balance.getBalance()
                    .multiply(interestRate)
                    .divide(DIVISOR, 2, RoundingMode.HALF_UP);
                
                totalInterest = totalInterest.add(monthlyInterest);
                
                createInterestTransaction(
                    balance.getAccountId(),
                    monthlyInterest,
                    processingDate
                );
                
                InterestCalculationDTO calculation = new InterestCalculationDTO();
                calculation.setAccountId(balance.getAccountId());
                calculation.setBalance(balance.getBalance());
                calculation.setInterestRate(interestRate);
                calculation.setMonthlyInterest(monthlyInterest);
                calculation.setTransactionTypeCode(balance.getTransactionTypeCode());
                calculation.setTransactionCategoryCode(balance.getTransactionCategoryCode());
                calculations.add(calculation);
            }
        }
        
        if (currentAccountId != null) {
            updateAccountBalance(currentAccountId, totalInterest);
        }
        
        return calculations;
    }
    
    private BigDecimal getInterestRate(String groupId, String typeCode, String categoryCode) {
        return disclosureGroupRepository
            .findByAccountGroupIdAndTransactionTypeCodeAndTransactionCategoryCode(groupId, typeCode, categoryCode)
            .map(DisclosureGroup::getInterestRate)
            .orElseGet(() -> disclosureGroupRepository
                .findByAccountGroupIdAndTransactionTypeCodeAndTransactionCategoryCode(DEFAULT_GROUP_ID, typeCode, categoryCode)
                .map(DisclosureGroup::getInterestRate)
                .orElse(BigDecimal.ZERO));
    }
    
    private void createInterestTransaction(String accountId, BigDecimal amount, String processingDate) {
        List<CardCrossReference> crossRefs = cardCrossReferenceRepository.findByAccountId(accountId);
        if (crossRefs.isEmpty()) {
            return;
        }
        
        String cardNumber = crossRefs.get(0).getCardNumber();
        String transactionId = processingDate + String.format("%06d", System.currentTimeMillis() % 1000000);
        
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setCardNumber(cardNumber);
        transaction.setAccountId(accountId);
        transaction.setTransactionTypeCode(INTEREST_TRANSACTION_TYPE);
        transaction.setTransactionCategoryCode(INTEREST_CATEGORY_CODE);
        transaction.setTransactionSource(SYSTEM_SOURCE);
        transaction.setTransactionDescription("Int. for a/c " + accountId);
        transaction.setTransactionAmount(amount);
        transaction.setMerchantId(0L);
        transaction.setMerchantName("");
        transaction.setMerchantCity("");
        transaction.setMerchantZip("");
        transaction.setOriginalTimestamp(LocalDateTime.now());
        transaction.setProcessingTimestamp(LocalDateTime.now());
        
        transactionRepository.save(transaction);
    }
    
    private void updateAccountBalance(String accountId, BigDecimal totalInterest) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
        
        account.setCurrentBalance(account.getCurrentBalance().add(totalInterest));
        account.setCurrentCycleCredit(BigDecimal.ZERO);
        account.setCurrentCycleDebit(BigDecimal.ZERO);
        
        accountRepository.save(account);
    }
}
