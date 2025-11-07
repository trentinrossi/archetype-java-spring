package com.example.demo.service;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AccountService.
 * Tests business logic and validation rules.
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @InjectMocks
    private AccountService accountService;
    
    private Account testAccount;
    private CreateAccountRequestDto createRequest;
    
    @BeforeEach
    void setUp() {
        // Setup test account
        testAccount = new Account();
        testAccount.setAcctId(10000000001L);
        testAccount.setAcctActiveStatus("A");
        testAccount.setAcctCurrBal(new BigDecimal("1500.50"));
        testAccount.setAcctCreditLimit(new BigDecimal("5000.00"));
        testAccount.setAcctCashCreditLimit(new BigDecimal("1000.00"));
        testAccount.setAcctOpenDate(LocalDate.of(2024, 1, 15));
        testAccount.setAcctExpirationDate(LocalDate.of(2027, 1, 15));
        testAccount.setAcctCurrCycCredit(new BigDecimal("250.00"));
        testAccount.setAcctCurrCycDebit(new BigDecimal("150.00"));
        testAccount.setAcctGroupId("GRP001");
        
        // Setup create request
        createRequest = new CreateAccountRequestDto();
        createRequest.setAcctId(10000000001L);
        createRequest.setAcctActiveStatus("A");
        createRequest.setAcctCurrBal(new BigDecimal("1500.50"));
        createRequest.setAcctCreditLimit(new BigDecimal("5000.00"));
        createRequest.setAcctCashCreditLimit(new BigDecimal("1000.00"));
        createRequest.setAcctOpenDate(LocalDate.of(2024, 1, 15));
        createRequest.setAcctExpirationDate(LocalDate.of(2027, 1, 15));
        createRequest.setAcctCurrCycCredit(new BigDecimal("250.00"));
        createRequest.setAcctCurrCycDebit(new BigDecimal("150.00"));
        createRequest.setAcctGroupId("GRP001");
    }
    
    @Test
    void testCreateAccount_Success() {
        // Given
        when(accountRepository.existsByAcctId(anyLong())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        
        // When
        AccountResponseDto response = accountService.createAccount(createRequest);
        
        // Then
        assertNotNull(response);
        assertEquals(10000000001L, response.getAcctId());
        assertEquals("A", response.getAcctActiveStatus());
        assertEquals(new BigDecimal("1500.50"), response.getAcctCurrBal());
        assertTrue(response.getIsActive());
        
        verify(accountRepository, times(1)).existsByAcctId(10000000001L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    
    @Test
    void testCreateAccount_DuplicateId_ThrowsException() {
        // Given
        when(accountRepository.existsByAcctId(anyLong())).thenReturn(true);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> accountService.createAccount(createRequest)
        );
        
        assertTrue(exception.getMessage().contains("already exists"));
        verify(accountRepository, times(1)).existsByAcctId(10000000001L);
        verify(accountRepository, never()).save(any(Account.class));
    }
    
    @Test
    void testCreateAccount_InvalidAccountId_ThrowsException() {
        // Given
        createRequest.setAcctId(123L); // Invalid: not 11 digits
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> accountService.createAccount(createRequest)
        );
        
        assertTrue(exception.getMessage().contains("11-digit"));
        verify(accountRepository, never()).save(any(Account.class));
    }
    
    @Test
    void testCreateAccount_ExpirationBeforeOpen_ThrowsException() {
        // Given
        createRequest.setAcctExpirationDate(LocalDate.of(2023, 1, 1));
        when(accountRepository.existsByAcctId(anyLong())).thenReturn(false);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> accountService.createAccount(createRequest)
        );
        
        assertTrue(exception.getMessage().contains("expiration date must be after open date"));
        verify(accountRepository, never()).save(any(Account.class));
    }
    
    @Test
    void testCreateAccount_CashLimitExceedsCreditLimit_ThrowsException() {
        // Given
        createRequest.setAcctCashCreditLimit(new BigDecimal("6000.00"));
        when(accountRepository.existsByAcctId(anyLong())).thenReturn(false);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> accountService.createAccount(createRequest)
        );
        
        assertTrue(exception.getMessage().contains("Cash credit limit cannot exceed credit limit"));
        verify(accountRepository, never()).save(any(Account.class));
    }
    
    @Test
    void testGetAccountById_Success() {
        // Given
        when(accountRepository.findByAcctId(anyLong())).thenReturn(Optional.of(testAccount));
        
        // When
        Optional<AccountResponseDto> response = accountService.getAccountById(10000000001L);
        
        // Then
        assertTrue(response.isPresent());
        assertEquals(10000000001L, response.get().getAcctId());
        assertEquals("A", response.get().getAcctActiveStatus());
        
        verify(accountRepository, times(1)).findByAcctId(10000000001L);
    }
    
    @Test
    void testGetAccountById_NotFound() {
        // Given
        when(accountRepository.findByAcctId(anyLong())).thenReturn(Optional.empty());
        
        // When
        Optional<AccountResponseDto> response = accountService.getAccountById(10000000001L);
        
        // Then
        assertFalse(response.isPresent());
        verify(accountRepository, times(1)).findByAcctId(10000000001L);
    }
    
    @Test
    void testUpdateAccount_Success() {
        // Given
        UpdateAccountRequestDto updateRequest = new UpdateAccountRequestDto();
        updateRequest.setAcctActiveStatus("I");
        updateRequest.setAcctCurrBal(new BigDecimal("2000.00"));
        
        when(accountRepository.findByAcctId(anyLong())).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        
        // When
        AccountResponseDto response = accountService.updateAccount(10000000001L, updateRequest);
        
        // Then
        assertNotNull(response);
        verify(accountRepository, times(1)).findByAcctId(10000000001L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    
    @Test
    void testUpdateAccount_NotFound_ThrowsException() {
        // Given
        UpdateAccountRequestDto updateRequest = new UpdateAccountRequestDto();
        when(accountRepository.findByAcctId(anyLong())).thenReturn(Optional.empty());
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> accountService.updateAccount(10000000001L, updateRequest)
        );
        
        assertTrue(exception.getMessage().contains("not found"));
        verify(accountRepository, times(1)).findByAcctId(10000000001L);
        verify(accountRepository, never()).save(any(Account.class));
    }
    
    @Test
    void testDeleteAccount_Success() {
        // Given
        when(accountRepository.existsByAcctId(anyLong())).thenReturn(true);
        doNothing().when(accountRepository).deleteById(anyLong());
        
        // When
        accountService.deleteAccount(10000000001L);
        
        // Then
        verify(accountRepository, times(1)).existsByAcctId(10000000001L);
        verify(accountRepository, times(1)).deleteById(10000000001L);
    }
    
    @Test
    void testDeleteAccount_NotFound_ThrowsException() {
        // Given
        when(accountRepository.existsByAcctId(anyLong())).thenReturn(false);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> accountService.deleteAccount(10000000001L)
        );
        
        assertTrue(exception.getMessage().contains("not found"));
        verify(accountRepository, times(1)).existsByAcctId(10000000001L);
        verify(accountRepository, never()).deleteById(anyLong());
    }
    
    @Test
    void testGetAllAccounts_Success() {
        // Given
        List<Account> accounts = Arrays.asList(testAccount);
        Page<Account> accountPage = new PageImpl<>(accounts);
        Pageable pageable = PageRequest.of(0, 20);
        
        when(accountRepository.findAll(any(Pageable.class))).thenReturn(accountPage);
        
        // When
        Page<AccountResponseDto> response = accountService.getAllAccounts(pageable);
        
        // Then
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(10000000001L, response.getContent().get(0).getAcctId());
        
        verify(accountRepository, times(1)).findAll(any(Pageable.class));
    }
    
    @Test
    void testProcessAccountsSequentially_Success() {
        // Given
        List<Account> accounts = Arrays.asList(testAccount);
        when(accountRepository.findAllAccountsSequentially()).thenReturn(accounts);
        
        // When
        List<AccountResponseDto> response = accountService.processAccountsSequentially();
        
        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(10000000001L, response.get(0).getAcctId());
        
        verify(accountRepository, times(1)).findAllAccountsSequentially();
    }
    
    @Test
    void testProcessAccountsSequentially_EmptyList() {
        // Given
        when(accountRepository.findAllAccountsSequentially()).thenReturn(List.of());
        
        // When
        List<AccountResponseDto> response = accountService.processAccountsSequentially();
        
        // Then
        assertNotNull(response);
        assertTrue(response.isEmpty());
        
        verify(accountRepository, times(1)).findAllAccountsSequentially();
    }
    
    @Test
    void testGetActiveAccounts_Success() {
        // Given
        List<Account> accounts = Arrays.asList(testAccount);
        Page<Account> accountPage = new PageImpl<>(accounts);
        Pageable pageable = PageRequest.of(0, 20);
        
        when(accountRepository.findAllActiveAccounts(any(Pageable.class))).thenReturn(accountPage);
        
        // When
        Page<AccountResponseDto> response = accountService.getActiveAccounts(pageable);
        
        // Then
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertTrue(response.getContent().get(0).getIsActive());
        
        verify(accountRepository, times(1)).findAllActiveAccounts(any(Pageable.class));
    }
    
    @Test
    void testGetTotalBalance_Success() {
        // Given
        BigDecimal expectedTotal = new BigDecimal("10000.00");
        when(accountRepository.calculateTotalBalance()).thenReturn(expectedTotal);
        
        // When
        BigDecimal result = accountService.getTotalBalance();
        
        // Then
        assertEquals(expectedTotal, result);
        verify(accountRepository, times(1)).calculateTotalBalance();
    }
    
    @Test
    void testGetTotalBalance_NullReturnsZero() {
        // Given
        when(accountRepository.calculateTotalBalance()).thenReturn(null);
        
        // When
        BigDecimal result = accountService.getTotalBalance();
        
        // Then
        assertEquals(BigDecimal.ZERO, result);
        verify(accountRepository, times(1)).calculateTotalBalance();
    }
    
    @Test
    void testGetActiveAccountCount_Success() {
        // Given
        when(accountRepository.countActiveAccounts()).thenReturn(5L);
        
        // When
        long count = accountService.getActiveAccountCount();
        
        // Then
        assertEquals(5L, count);
        verify(accountRepository, times(1)).countActiveAccounts();
    }
    
    @Test
    void testConvertToResponse_AllFieldsMapped() {
        // When
        AccountResponseDto response = accountService.getAccountById(10000000001L)
                .orElse(null);
        
        when(accountRepository.findByAcctId(anyLong())).thenReturn(Optional.of(testAccount));
        response = accountService.getAccountById(10000000001L).orElse(null);
        
        // Then
        assertNotNull(response);
        assertEquals(testAccount.getAcctId(), response.getAcctId());
        assertEquals(testAccount.getAcctActiveStatus(), response.getAcctActiveStatus());
        assertEquals(testAccount.getAcctCurrBal(), response.getAcctCurrBal());
        assertEquals(testAccount.getAcctCreditLimit(), response.getAcctCreditLimit());
        assertEquals(testAccount.getAcctCashCreditLimit(), response.getAcctCashCreditLimit());
        assertEquals(testAccount.getAcctOpenDate(), response.getAcctOpenDate());
        assertEquals(testAccount.getAcctExpirationDate(), response.getAcctExpirationDate());
        assertEquals(testAccount.getAcctCurrCycCredit(), response.getAcctCurrCycCredit());
        assertEquals(testAccount.getAcctCurrCycDebit(), response.getAcctCurrCycDebit());
        assertEquals(testAccount.getAcctGroupId(), response.getAcctGroupId());
        
        // Verify computed fields
        assertNotNull(response.getAvailableCredit());
        assertNotNull(response.getAvailableCashCredit());
        assertNotNull(response.getNetCycleAmount());
        assertNotNull(response.getIsActive());
        assertNotNull(response.getIsExpired());
    }
}
