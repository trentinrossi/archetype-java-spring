package com.example.demo.repository;

import com.example.demo.entity.CardCrossReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardCrossReferenceRepository extends JpaRepository<CardCrossReference, Long> {
    
    Optional<CardCrossReference> findByCardNumber(String cardNumber);
    
    boolean existsByCardNumber(String cardNumber);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.crossReferenceData LIKE CONCAT('%', :searchTerm, '%')")
    Page<CardCrossReference> findByCrossReferenceDataContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardNumber IN :cardNumbers")
    List<CardCrossReference> findByCardNumberIn(@Param("cardNumbers") List<String> cardNumbers);
    
    @Query("SELECT c FROM CardCrossReference c ORDER BY c.cardNumber ASC")
    List<CardCrossReference> findAllOrderByCardNumber();
    
    @Query("SELECT c FROM CardCrossReference c ORDER BY c.cardNumber ASC")
    Page<CardCrossReference> findAllOrderByCardNumber(Pageable pageable);
}