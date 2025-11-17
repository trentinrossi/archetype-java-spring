package com.example.demo.repository;

import com.example.demo.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
    
    Optional<Merchant> findByMerchantId(String merchantId);
    
    boolean existsByMerchantId(String merchantId);
    
    List<Merchant> findByMerchantName(String merchantName);
    
    Page<Merchant> findByMerchantName(String merchantName, Pageable pageable);
    
    List<Merchant> findByMerchantCity(String merchantCity);
    
    Page<Merchant> findByMerchantCity(String merchantCity, Pageable pageable);
    
    List<Merchant> findByMerchantZip(String merchantZip);
    
    Page<Merchant> findByMerchantZip(String merchantZip, Pageable pageable);
    
    @Query("SELECT m FROM Merchant m WHERE LOWER(m.merchantName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Merchant> searchByMerchantName(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.merchant.merchantId = :merchantId")
    long countTransactionsByMerchantId(@Param("merchantId") String merchantId);
}
