package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    boolean existsByAccountNumber(String accountNumber);
    
    List<Account> findByCustomerId(String customerId);
    
    Optional<Account> findByCustomerIdAndAccountNumber(String customerId, String accountNumber);
    
    Page<Account> findByStatus(String status, Pageable pageable);
    
    Page<Account> findByCustomerId(String customerId, Pageable pageable);
    
    List<Account> findByStatusAndExpiryDateBefore(String status, LocalDate expiryDate);
    
    List<Account> findByStatusAndExpiryDateAfter(String status, LocalDate expiryDate);
    
    Optional<Account> findByCardNumber(String cardNumber);
    
    boolean existsByCardNumber(String cardNumber);
    
    List<Account> findByCardActiveStatus(String cardActiveStatus);
    
    List<Account> findByGroupId(String groupId);
    
    @Query("SELECT a FROM Account a WHERE a.customerId = :customerId AND a.status = :status")
    List<Account> findActiveAccountsByCustomerId(@Param("customerId") String customerId, @Param("status") String status);
    
    @Query("SELECT a FROM Account a WHERE a.balance > :balance")
    List<Account> findAccountsWithBalanceGreaterThan(@Param("balance") BigDecimal balance);
    
    @Query("SELECT a FROM Account a WHERE a.balance < :balance")
    List<Account> findAccountsWithBalanceLessThan(@Param("balance") BigDecimal balance);
    
    @Query("SELECT a FROM Account a WHERE a.creditLimit > :creditLimit")
    List<Account> findAccountsWithCreditLimitGreaterThan(@Param("creditLimit") BigDecimal creditLimit);
    
    @Query("SELECT a FROM Account a WHERE a.balance > a.creditLimit")
    List<Account> findAccountsOverCreditLimit();
    
    @Query("SELECT a FROM Account a WHERE a.balance > a.cashCreditLimit")
    List<Account> findAccountsOverCashCreditLimit();
    
    @Query("SELECT a FROM Account a WHERE a.ficoCreditScore BETWEEN :minScore AND :maxScore")
    List<Account> findAccountsByFicoScoreRange(@Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);
    
    @Query("SELECT a FROM Account a WHERE a.custFicoCreditScore BETWEEN :minScore AND :maxScore")
    List<Account> findAccountsByCustomerFicoScoreRange(@Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);
    
    @Query("SELECT a FROM Account a WHERE a.openDate BETWEEN :startDate AND :endDate")
    List<Account> findAccountsByOpenDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE a.expiryDate BETWEEN :startDate AND :endDate")
    List<Account> findAccountsByExpiryDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE a.cardExpiryDate BETWEEN :startDate AND :endDate")
    List<Account> findAccountsByCardExpiryDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE LOWER(a.customerName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Account> findByCustomerNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE LOWER(a.custFirstName) LIKE LOWER(CONCAT('%', :firstName, '%')) " +
           "AND LOWER(a.custLastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Account> findByCustomerFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    
    @Query("SELECT a FROM Account a WHERE a.custPhone = :phoneNumber OR a.custPhoneHome = :phoneNumber OR a.custPhoneWork = :phoneNumber")
    List<Account> findByAnyPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    @Query("SELECT a FROM Account a WHERE a.custSsn = :ssn")
    Optional<Account> findByCustomerSsn(@Param("ssn") String ssn);
    
    @Query("SELECT a FROM Account a WHERE a.custAddrStateCd = :stateCode")
    List<Account> findByCustomerState(@Param("stateCode") String stateCode);
    
    @Query("SELECT a FROM Account a WHERE a.custAddrCountryCd = :countryCode")
    List<Account> findByCustomerCountry(@Param("countryCode") String countryCode);
    
    @Query("SELECT a FROM Account a WHERE a.custAddrZip = :zipCode")
    List<Account> findByCustomerZipCode(@Param("zipCode") String zipCode);
    
    @Query("SELECT a FROM Account a WHERE a.createdAt >= :since")
    List<Account> findRecentAccounts(@Param("since") LocalDateTime since);
    
    @Query("SELECT a FROM Account a WHERE a.updatedAt >= :since")
    List<Account> findRecentlyUpdatedAccounts(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.status = :status")
    long countByStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.customerId = :customerId")
    long countByCustomerId(@Param("customerId") String customerId);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.cardActiveStatus = :cardActiveStatus")
    long countByCardActiveStatus(@Param("cardActiveStatus") String cardActiveStatus);
    
    @Query("SELECT SUM(a.balance) FROM Account a WHERE a.customerId = :customerId")
    BigDecimal getTotalBalanceByCustomerId(@Param("customerId") String customerId);
    
    @Query("SELECT SUM(a.creditLimit) FROM Account a WHERE a.customerId = :customerId")
    BigDecimal getTotalCreditLimitByCustomerId(@Param("customerId") String customerId);
    
    @Query("SELECT AVG(a.balance) FROM Account a WHERE a.status = :status")
    BigDecimal getAverageBalanceByStatus(@Param("status") String status);
    
    @Query("SELECT AVG(a.ficoCreditScore) FROM Account a WHERE a.ficoCreditScore IS NOT NULL")
    Double getAverageFicoScore();
    
    @Query("SELECT AVG(a.custFicoCreditScore) FROM Account a WHERE a.custFicoCreditScore IS NOT NULL")
    Double getAverageCustomerFicoScore();
    
    @Query("SELECT a FROM Account a WHERE a.accountNumber LIKE :pattern")
    List<Account> findByAccountNumberPattern(@Param("pattern") String pattern);
    
    @Query("SELECT a FROM Account a WHERE a.customerId LIKE :pattern")
    List<Account> findByCustomerIdPattern(@Param("pattern") String pattern);
    
    @Query("SELECT DISTINCT a.custAddrStateCd FROM Account a WHERE a.custAddrStateCd IS NOT NULL ORDER BY a.custAddrStateCd")
    List<String> findDistinctStates();
    
    @Query("SELECT DISTINCT a.custAddrCountryCd FROM Account a WHERE a.custAddrCountryCd IS NOT NULL ORDER BY a.custAddrCountryCd")
    List<String> findDistinctCountries();
    
    @Query("SELECT DISTINCT a.groupId FROM Account a WHERE a.groupId IS NOT NULL ORDER BY a.groupId")
    List<String> findDistinctGroupIds();
    
    @Query("SELECT a FROM Account a WHERE a.currCycleCredit > :amount OR a.currCycleDebit > :amount")
    List<Account> findAccountsWithHighCycleActivity(@Param("amount") BigDecimal amount);
    
    @Query("SELECT a FROM Account a WHERE a.currCycleCredit IS NULL OR a.currCycleDebit IS NULL")
    List<Account> findAccountsWithMissingCycleData();
    
    @Query("SELECT a FROM Account a WHERE a.reissueDate IS NOT NULL AND a.reissueDate > :date")
    List<Account> findAccountsReissuedAfter(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.cardNumber IS NOT NULL AND a.cardActiveStatus = 'Y'")
    List<Account> findAccountsWithActiveCards();
    
    @Query("SELECT a FROM Account a WHERE a.cardNumber IS NOT NULL AND a.cardActiveStatus = 'N'")
    List<Account> findAccountsWithInactiveCards();
    
    @Query("SELECT a FROM Account a WHERE a.cardExpiryDate IS NOT NULL AND a.cardExpiryDate < :date")
    List<Account> findAccountsWithExpiredCards(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.cardExpiryDate IS NOT NULL AND a.cardExpiryDate BETWEEN :startDate AND :endDate")
    List<Account> findAccountsWithCardsExpiringBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE a.custGovtIssuedId = :govtId")
    Optional<Account> findByGovernmentIssuedId(@Param("govtId") String govtId);
    
    @Query("SELECT a FROM Account a WHERE a.custDobYyyyMmDd = :dob")
    List<Account> findByCustomerDateOfBirth(@Param("dob") String dob);
    
    @Query("SELECT a FROM Account a WHERE a.status = 'A' AND a.expiryDate > CURRENT_DATE")
    List<Account> findValidActiveAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.status = 'A' AND a.balance < a.creditLimit * 0.1")
    List<Account> findActiveAccountsWithLowUtilization();
    
    @Query("SELECT a FROM Account a WHERE a.status = 'A' AND a.balance > a.creditLimit * 0.9")
    List<Account> findActiveAccountsWithHighUtilization();
    
    @Query("SELECT a FROM Account a WHERE a.ficoCreditScore IS NOT NULL AND a.custFicoCreditScore IS NOT NULL " +
           "AND ABS(a.ficoCreditScore - a.custFicoCreditScore) > :threshold")
    List<Account> findAccountsWithFicoScoreDiscrepancy(@Param("threshold") Integer threshold);
    
    @Query("SELECT a FROM Account a WHERE LENGTH(a.accountNumber) != 11 OR a.accountNumber LIKE '0%'")
    List<Account> findAccountsWithInvalidAccountNumbers();
    
    @Query("SELECT a FROM Account a WHERE a.custSsn IS NOT NULL AND " +
           "(a.custSsn NOT LIKE '___-__-____' OR a.custSsn LIKE '000-%' OR a.custSsn LIKE '%-00-%' OR a.custSsn LIKE '%-0000')")
    List<Account> findAccountsWithInvalidSsn();
    
    @Query("SELECT a FROM Account a WHERE a.custPhone IS NOT NULL AND LENGTH(a.custPhone) != 10")
    List<Account> findAccountsWithInvalidPhoneNumbers();
    
    @Query("SELECT a FROM Account a WHERE a.custAddrZip IS NOT NULL AND " +
           "a.custAddrZip NOT LIKE '_____' AND a.custAddrZip NOT LIKE '_____-____'")
    List<Account> findAccountsWithInvalidZipCodes();
    
    @Query("SELECT a FROM Account a WHERE a.cardNumber IS NOT NULL AND LENGTH(a.cardNumber) != 16")
    List<Account> findAccountsWithInvalidCardNumbers();
    
    @Query("SELECT a FROM Account a WHERE a.cardCvvCd IS NOT NULL AND LENGTH(a.cardCvvCd) != 3")
    List<Account> findAccountsWithInvalidCvv();
}