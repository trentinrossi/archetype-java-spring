package com.example.demo.service;

import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.dto.AccountResponseDto;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());

        validateAccountRequest(request);
        validateAccountIdNotExists(request.getAccountId());
        validateCustomerExists(request.getCustomerId());

        Account account = new Account();
        account.setAccountId(request.getAccountId());
        account.setActiveStatus(request.getActiveStatus());
        account.setCurrentBalance(request.getCurrentBalance());
        account.setCreditLimit(request.getCreditLimit());
        account.setCashCreditLimit(request.getCashCreditLimit());
        account.setOpenDate(request.getOpenDate());
        account.setExpirationDate(request.getExpirationDate());
        account.setReissueDate(request.getReissueDate());
        account.setCurrentCycleCredit(request.getCurrentCycleCredit());
        account.setCurrentCycleDebit(request.getCurrentCycleDebit());
        account.setGroupId(request.getGroupId());
        account.setCustomerId(request.getCustomerId());
        account.setAccountStatus(request.getAccountStatus());

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAccountId());
        return convertToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(Long accountId) {
        log.info("Retrieving account with ID: {}", accountId);
        
        validateAccountIdFormat(accountId);
        
        return accountRepository.findById(accountId).map(account -> {
            log.info("Account retrieved successfully with ID: {}", accountId);
            return convertToResponse(account);
        });
    }

    @Transactional
    public AccountResponseDto updateAccount(Long accountId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", accountId);

        validateAccountIdFormat(accountId);
        
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Account not found with ID: {}", accountId);
                    return new IllegalArgumentException(
                        String.format("Account not found with ID: %d", accountId)
                    );
                });

        validateUpdateRequest(request);

        if (request.getActiveStatus() != null) {
            existingAccount.setActiveStatus(request.getActiveStatus());
        }
        if (request.getCurrentBalance() != null) {
            existingAccount.setCurrentBalance(request.getCurrentBalance());
        }
        if (request.getCreditLimit() != null) {
            existingAccount.setCreditLimit(request.getCreditLimit());
        }
        if (request.getCashCreditLimit() != null) {
            existingAccount.setCashCreditLimit(request.getCashCreditLimit());
        }
        if (request.getOpenDate() != null) {
            existingAccount.setOpenDate(request.getOpenDate());
        }
        if (request.getExpirationDate() != null) {
            existingAccount.setExpirationDate(request.getExpirationDate());
        }
        if (request.getReissueDate() != null) {
            existingAccount.setReissueDate(request.getReissueDate());
        }
        if (request.getCurrentCycleCredit() != null) {
            existingAccount.setCurrentCycleCredit(request.getCurrentCycleCredit());
        }
        if (request.getCurrentCycleDebit() != null) {
            existingAccount.setCurrentCycleDebit(request.getCurrentCycleDebit());
        }
        if (request.getGroupId() != null) {
            existingAccount.setGroupId(request.getGroupId());
        }
        if (request.getCustomerId() != null) {
            validateCustomerExists(request.getCustomerId());
            existingAccount.setCustomerId(request.getCustomerId());
        }
        if (request.getAccountStatus() != null) {
            existingAccount.setAccountStatus(request.getAccountStatus());
        }

        Account updatedAccount = accountRepository.save(existingAccount);
        log.info("Account updated successfully with ID: {}", accountId);
        return convertToResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        log.info("Deleting account with ID: {}", accountId);
        
        validateAccountIdFormat(accountId);
        
        if (!accountRepository.existsById(accountId)) {
            log.error("Account not found with ID: {}", accountId);
            throw new IllegalArgumentException(
                String.format("Account not found with ID: %d", accountId)
            );
        }
        
        accountRepository.deleteById(accountId);
        log.info("Account deleted successfully with ID: {}", accountId);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.info("Retrieving all accounts with pagination");
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }

    private void validateAccountIdFormat(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account number is required");
        }
        
        String accountIdStr = String.valueOf(accountId);
        if (accountIdStr.length() != 11) {
            throw new IllegalArgumentException("Account number must be 11 digits");
        }
        
        if (accountId == 0L) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
    }

    private void validateAccountIdNotExists(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            throw new IllegalArgumentException(
                String.format("Account with ID %d already exists", accountId)
            );
        }
    }

    private void validateCustomerExists(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException(
                String.format("Customer with ID %d does not exist", customerId)
            );
        }
    }

    private void validateAccountRequest(CreateAccountRequestDto request) {
        validateActiveStatus(request.getActiveStatus());
        validateBalanceFields(request.getCurrentBalance(), request.getCreditLimit(), 
                             request.getCashCreditLimit(), request.getCurrentCycleCredit(), 
                             request.getCurrentCycleDebit());
        validateDateFields(request.getOpenDate(), request.getExpirationDate(), request.getReissueDate());
        validateAccountStatus(request.getAccountStatus());
    }

    private void validateUpdateRequest(UpdateAccountRequestDto request) {
        if (request.getActiveStatus() != null) {
            validateActiveStatus(request.getActiveStatus());
        }
        if (request.getCurrentBalance() != null || request.getCreditLimit() != null || 
            request.getCashCreditLimit() != null || request.getCurrentCycleCredit() != null || 
            request.getCurrentCycleDebit() != null) {
            // Validate individual balance fields if provided
        }
        if (request.getOpenDate() != null || request.getExpirationDate() != null || 
            request.getReissueDate() != null) {
            // Validate individual date fields if provided
        }
        if (request.getAccountStatus() != null) {
            validateAccountStatus(request.getAccountStatus());
        }
    }

    private void validateActiveStatus(String activeStatus) {
        if (activeStatus == null || activeStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Active status is required");
        }
        
        if (!activeStatus.equals("Y") && !activeStatus.equals("N")) {
            throw new IllegalArgumentException("Account status must be Y or N");
        }
    }

    private void validateBalanceFields(BigDecimal currentBalance, BigDecimal creditLimit, 
                                       BigDecimal cashCreditLimit, BigDecimal currentCycleCredit, 
                                       BigDecimal currentCycleDebit) {
        if (currentBalance == null || currentBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current balance must be a valid positive number");
        }
        
        if (creditLimit == null || creditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Credit limit must be a valid positive number");
        }
        
        if (cashCreditLimit == null || cashCreditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cash credit limit must be a valid positive number");
        }
        
        if (currentCycleCredit == null || currentCycleCredit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current cycle credit must be a valid positive number");
        }
        
        if (currentCycleDebit == null || currentCycleDebit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current cycle debit must be a valid positive number");
        }
    }

    private void validateDateFields(LocalDate openDate, LocalDate expirationDate, LocalDate reissueDate) {
        if (openDate == null) {
            throw new IllegalArgumentException("Invalid open date format or value");
        }
        
        if (expirationDate == null) {
            throw new IllegalArgumentException("Invalid expiration date format or value");
        }
        
        if (reissueDate == null) {
            throw new IllegalArgumentException("Invalid reissue date format or value");
        }
        
        int openYear = openDate.getYear();
        int expirationYear = expirationDate.getYear();
        int reissueYear = reissueDate.getYear();
        
        if (openYear < 1900 || openYear > 2099) {
            throw new IllegalArgumentException("Invalid open date format or value");
        }
        
        if (expirationYear < 1900 || expirationYear > 2099) {
            throw new IllegalArgumentException("Invalid expiration date format or value");
        }
        
        if (reissueYear < 1900 || reissueYear > 2099) {
            throw new IllegalArgumentException("Invalid reissue date format or value");
        }
    }

    private void validateAccountStatus(String accountStatus) {
        if (accountStatus == null || accountStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Account status is required");
        }
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(account.getAccountId());
        response.setActiveStatus(account.getActiveStatus());
        response.setCurrentBalance(account.getCurrentBalance());
        response.setCreditLimit(account.getCreditLimit());
        response.setCashCreditLimit(account.getCashCreditLimit());
        response.setOpenDate(account.getOpenDate());
        response.setExpirationDate(account.getExpirationDate());
        response.setReissueDate(account.getReissueDate());
        response.setCurrentCycleCredit(account.getCurrentCycleCredit());
        response.setCurrentCycleDebit(account.getCurrentCycleDebit());
        response.setGroupId(account.getGroupId());
        response.setCustomerId(account.getCustomerId());
        response.setAccountStatus(account.getAccountStatus());
        response.setAvailableCredit(account.getAvailableCredit());
        response.setAvailableCashCredit(account.getAvailableCashCredit());
        response.setNetCycleAmount(account.getCurrentCycleNetActivity());
        response.setIsActive(account.isActive());
        response.setIsExpired(account.isExpired());
        response.setHasGroupAssignment(account.hasGroupAssignment());
        response.setActiveStatusDisplay(account.isActive() ? "Active" : "Inactive");
        response.setAccountStatusDisplay(account.getAccountStatus());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
