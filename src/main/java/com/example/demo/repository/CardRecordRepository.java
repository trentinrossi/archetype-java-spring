package com.example.demo.repository;

import com.example.demo.entity.CardRecord;
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
    
    @Query("SELECT c FROM CardRecord c WHERE c.cardNumber = :cardNumber AND LENGTH(c.cardNumber) = 16")
    Optional<CardRecord> findByCardNumberWithLengthValidation(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT c FROM CardRecord c WHERE c.cardNumber LIKE %:pattern%")
    List<CardRecord> findByCardNumberPattern(@Param("pattern") String pattern);
    
    @Query("SELECT COUNT(c) FROM CardRecord c")
    long countAllCardRecords();
}
