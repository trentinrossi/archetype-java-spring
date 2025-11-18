package com.example.demo.repository;

import com.example.demo.entity.CreditCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    
    Optional<CreditCard> findByCardNumber(String cardNumber);
    
    boolean existsByCardNumber(String cardNumber);
    
    List<CreditCard> findByAccount_AccountId(Long accountId);
    
    Page<CreditCard> findByAccount_AccountId(Long accountId, Pageable pageable);
    
    @Query("SELECT c FROM CreditCard c WHERE c.account.accountId = :accountId AND c.cardNumber = :cardNumber")
    Optional<CreditCard> findByAccountIdAndCardNumber(@Param("accountId") Long accountId, @Param("cardNumber") String cardNumber);
    
    @Query("SELECT c FROM CreditCard c WHERE c.cardStatus = :status")
    List<CreditCard> findByCardStatus(@Param("status") String status);
    
    Page<CreditCard> findByCardStatus(String status, Pageable pageable);
    
    @Query("SELECT c FROM CreditCard c WHERE c.expirationDate < :date")
    List<CreditCard> findExpiredCards(@Param("date") LocalDate date);
    
    @Query("SELECT c FROM CreditCard c WHERE c.expirationDate BETWEEN :startDate AND :endDate")
    List<CreditCard> findCardsExpiringBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT c FROM CreditCard c WHERE c.account.accountId = :accountId OR c.cardNumber = :cardNumber")
    List<CreditCard> searchByAccountOrCard(@Param("accountId") Long accountId, @Param("cardNumber") String cardNumber);
    
    @Query("SELECT c FROM CreditCard c WHERE " +
           "(:accountId IS NULL OR c.account.accountId = :accountId) AND " +
           "(:cardNumber IS NULL OR c.cardNumber = :cardNumber)")
    Page<CreditCard> searchCards(@Param("accountId") Long accountId, 
                                  @Param("cardNumber") String cardNumber, 
                                  Pageable pageable);
    
    @Query("SELECT c FROM CreditCard c WHERE c.embossedName LIKE %:name%")
    List<CreditCard> findByEmbossedNameContaining(@Param("name") String name);
    
    @Query("SELECT c FROM CreditCard c WHERE c.customer.id = :customerId")
    List<CreditCard> findByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT COUNT(c) FROM CreditCard c WHERE c.account.accountId = :accountId")
    long countByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT COUNT(c) FROM CreditCard c WHERE c.cardStatus = 'Y'")
    long countActiveCards();
    
    @Query("SELECT COUNT(c) FROM CreditCard c WHERE c.expirationDate < CURRENT_DATE")
    long countExpiredCards();
    
    @Query("SELECT c FROM CreditCard c WHERE c.account.user.id = :userId")
    List<CreditCard> findCardsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT c FROM CreditCard c WHERE c.account.user.userType = 'ADMIN'")
    List<CreditCard> findAllCardsForAdmin();
}
