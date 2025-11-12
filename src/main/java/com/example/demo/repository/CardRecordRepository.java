package com.example.demo.repository;

import com.example.demo.entity.CardRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRecordRepository extends JpaRepository<CardRecord, String> {
    
    Optional<CardRecord> findByCardNumber(String cardNumber);
    
    boolean existsByCardNumber(String cardNumber);
    
    @Query("SELECT c FROM CardRecord c WHERE c.cardNumber LIKE CONCAT(:prefix, '%')")
    List<CardRecord> findByCardNumberPrefix(@Param("prefix") String prefix);
    
    @Query("SELECT c FROM CardRecord c WHERE LENGTH(c.cardNumber) = 16")
    List<CardRecord> findAllValidCardNumbers();
    
    @Query("SELECT COUNT(c) FROM CardRecord c WHERE LENGTH(c.cardNumber) = 16")
    long countValidCardNumbers();
    
    @Query("SELECT c FROM CardRecord c WHERE c.cardNumber LIKE %:searchTerm%")
    Page<CardRecord> findByCardNumberContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT c FROM CardRecord c WHERE c.cardData LIKE %:searchTerm%")
    Page<CardRecord> findByCardDataContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    List<CardRecord> findByCardNumberIn(List<String> cardNumbers);
    
    @Query("SELECT c FROM CardRecord c ORDER BY c.cardNumber ASC")
    List<CardRecord> findAllOrderedByCardNumber();
}
