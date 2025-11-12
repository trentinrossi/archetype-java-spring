package com.example.demo.repository;

import com.example.demo.entity.AccountCrossReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountCrossReferenceRepository extends JpaRepository<AccountCrossReference, String> {
    
    Optional<AccountCrossReference> findByCardNumber(String cardNumber);
    
    boolean existsByCardNumber(String cardNumber);
    
    @Query("SELECT a FROM AccountCrossReference a WHERE a.cardNumber LIKE CONCAT(:prefix, '%')")
    List<AccountCrossReference> findByCardNumberPrefix(@Param("prefix") String prefix);
    
    @Query("SELECT a FROM AccountCrossReference a ORDER BY a.cardNumber ASC")
    List<AccountCrossReference> findAllOrderedByCardNumber();
}
