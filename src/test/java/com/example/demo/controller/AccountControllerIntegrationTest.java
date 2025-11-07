package com.example.demo.controller;

import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for AccountController.
 * Tests the complete flow from HTTP request to database.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccountControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private AccountRepository accountRepository;
    
    private Account testAccount;
    
    @BeforeEach
    void setUp() {
        // Clean up before each test
        accountRepository.deleteAll();
        
        // Create test account
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
        
        accountRepository.save(testAccount);
    }
    
    @Test
    void testGetAllAccounts_Success() throws Exception {
        mockMvc.perform(get("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].acctId").value(10000000001L))
                .andExpect(jsonPath("$.content[0].acctActiveStatus").value("A"))
                .andExpect(jsonPath("$.content[0].isActive").value(true));
    }
    
    @Test
    void testGetAccountById_Success() throws Exception {
        mockMvc.perform(get("/api/accounts/{acctId}", 10000000001L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctId").value(10000000001L))
                .andExpect(jsonPath("$.acctActiveStatus").value("A"))
                .andExpect(jsonPath("$.acctCurrBal").value(1500.50))
                .andExpect(jsonPath("$.acctCreditLimit").value(5000.00))
                .andExpect(jsonPath("$.acctGroupId").value("GRP001"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.availableCredit").exists())
                .andExpect(jsonPath("$.netCycleAmount").exists());
    }
    
    @Test
    void testGetAccountById_NotFound() throws Exception {
        mockMvc.perform(get("/api/accounts/{acctId}", 99999999999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testCreateAccount_Success() throws Exception {
        CreateAccountRequestDto request = new CreateAccountRequestDto();
        request.setAcctId(10000000002L);
        request.setAcctActiveStatus("A");
        request.setAcctCurrBal(new BigDecimal("2000.00"));
        request.setAcctCreditLimit(new BigDecimal("6000.00"));
        request.setAcctCashCreditLimit(new BigDecimal("1200.00"));
        request.setAcctOpenDate(LocalDate.of(2024, 2, 1));
        request.setAcctExpirationDate(LocalDate.of(2027, 2, 1));
        request.setAcctCurrCycCredit(new BigDecimal("300.00"));
        request.setAcctCurrCycDebit(new BigDecimal("200.00"));
        request.setAcctGroupId("GRP002");
        
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.acctId").value(10000000002L))
                .andExpect(jsonPath("$.acctActiveStatus").value("A"))
                .andExpect(jsonPath("$.acctCurrBal").value(2000.00));
    }
    
    @Test
    void testCreateAccount_DuplicateId_BadRequest() throws Exception {
        CreateAccountRequestDto request = new CreateAccountRequestDto();
        request.setAcctId(10000000001L); // Already exists
        request.setAcctActiveStatus("A");
        request.setAcctCurrBal(new BigDecimal("2000.00"));
        request.setAcctCreditLimit(new BigDecimal("6000.00"));
        request.setAcctCashCreditLimit(new BigDecimal("1200.00"));
        request.setAcctOpenDate(LocalDate.of(2024, 2, 1));
        request.setAcctExpirationDate(LocalDate.of(2027, 2, 1));
        request.setAcctCurrCycCredit(new BigDecimal("300.00"));
        request.setAcctCurrCycDebit(new BigDecimal("200.00"));
        
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("already exists")));
    }
    
    @Test
    void testCreateAccount_InvalidAccountId_BadRequest() throws Exception {
        CreateAccountRequestDto request = new CreateAccountRequestDto();
        request.setAcctId(123L); // Invalid: not 11 digits
        request.setAcctActiveStatus("A");
        request.setAcctCurrBal(new BigDecimal("2000.00"));
        request.setAcctCreditLimit(new BigDecimal("6000.00"));
        request.setAcctCashCreditLimit(new BigDecimal("1200.00"));
        request.setAcctOpenDate(LocalDate.of(2024, 2, 1));
        request.setAcctExpirationDate(LocalDate.of(2027, 2, 1));
        request.setAcctCurrCycCredit(new BigDecimal("300.00"));
        request.setAcctCurrCycDebit(new BigDecimal("200.00"));
        
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testUpdateAccount_Success() throws Exception {
        UpdateAccountRequestDto request = new UpdateAccountRequestDto();
        request.setAcctActiveStatus("I");
        request.setAcctCurrBal(new BigDecimal("2500.00"));
        
        mockMvc.perform(put("/api/accounts/{acctId}", 10000000001L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acctId").value(10000000001L));
    }
    
    @Test
    void testUpdateAccount_NotFound() throws Exception {
        UpdateAccountRequestDto request = new UpdateAccountRequestDto();
        request.setAcctActiveStatus("I");
        
        mockMvc.perform(put("/api/accounts/{acctId}", 99999999999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }
    
    @Test
    void testDeleteAccount_Success() throws Exception {
        mockMvc.perform(delete("/api/accounts/{acctId}", 10000000001L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        // Verify account was deleted
        mockMvc.perform(get("/api/accounts/{acctId}", 10000000001L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testDeleteAccount_NotFound() throws Exception {
        mockMvc.perform(delete("/api/accounts/{acctId}", 99999999999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }
    
    @Test
    void testProcessAccountsSequentially_Success() throws Exception {
        // Create additional accounts
        Account account2 = new Account();
        account2.setAcctId(10000000002L);
        account2.setAcctActiveStatus("A");
        account2.setAcctCurrBal(new BigDecimal("3000.00"));
        account2.setAcctCreditLimit(new BigDecimal("7000.00"));
        account2.setAcctCashCreditLimit(new BigDecimal("1400.00"));
        account2.setAcctOpenDate(LocalDate.of(2024, 2, 1));
        account2.setAcctExpirationDate(LocalDate.of(2027, 2, 1));
        account2.setAcctCurrCycCredit(new BigDecimal("400.00"));
        account2.setAcctCurrCycDebit(new BigDecimal("300.00"));
        accountRepository.save(account2);
        
        mockMvc.perform(get("/api/accounts/process-sequential")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].acctId").value(10000000001L))
                .andExpect(jsonPath("$[1].acctId").value(10000000002L));
    }
    
    @Test
    void testGetActiveAccounts_Success() throws Exception {
        // Create inactive account
        Account inactiveAccount = new Account();
        inactiveAccount.setAcctId(10000000003L);
        inactiveAccount.setAcctActiveStatus("I");
        inactiveAccount.setAcctCurrBal(new BigDecimal("0.00"));
        inactiveAccount.setAcctCreditLimit(new BigDecimal("2000.00"));
        inactiveAccount.setAcctCashCreditLimit(new BigDecimal("400.00"));
        inactiveAccount.setAcctOpenDate(LocalDate.of(2023, 1, 1));
        inactiveAccount.setAcctExpirationDate(LocalDate.of(2026, 1, 1));
        inactiveAccount.setAcctCurrCycCredit(new BigDecimal("0.00"));
        inactiveAccount.setAcctCurrCycDebit(new BigDecimal("0.00"));
        accountRepository.save(inactiveAccount);
        
        mockMvc.perform(get("/api/accounts/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].acctActiveStatus").value("A"));
    }
    
    @Test
    void testGetInactiveAccounts_Success() throws Exception {
        // Create inactive account
        Account inactiveAccount = new Account();
        inactiveAccount.setAcctId(10000000003L);
        inactiveAccount.setAcctActiveStatus("I");
        inactiveAccount.setAcctCurrBal(new BigDecimal("0.00"));
        inactiveAccount.setAcctCreditLimit(new BigDecimal("2000.00"));
        inactiveAccount.setAcctCashCreditLimit(new BigDecimal("400.00"));
        inactiveAccount.setAcctOpenDate(LocalDate.of(2023, 1, 1));
        inactiveAccount.setAcctExpirationDate(LocalDate.of(2026, 1, 1));
        inactiveAccount.setAcctCurrCycCredit(new BigDecimal("0.00"));
        inactiveAccount.setAcctCurrCycDebit(new BigDecimal("0.00"));
        accountRepository.save(inactiveAccount);
        
        mockMvc.perform(get("/api/accounts/inactive")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].acctActiveStatus").value("I"));
    }
    
    @Test
    void testGetAccountsByGroupId_Success() throws Exception {
        mockMvc.perform(get("/api/accounts/group/{groupId}", "GRP001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].acctGroupId").value("GRP001"));
    }
    
    @Test
    void testGetTotalBalance_Success() throws Exception {
        mockMvc.perform(get("/api/accounts/total-balance")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1500.50));
    }
    
    @Test
    void testGetActiveAccountCount_Success() throws Exception {
        mockMvc.perform(get("/api/accounts/count/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }
    
    @Test
    void testGetInactiveAccountCount_Success() throws Exception {
        mockMvc.perform(get("/api/accounts/count/inactive")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0));
    }
}
