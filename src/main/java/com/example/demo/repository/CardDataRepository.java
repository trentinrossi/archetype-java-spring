package com.example.demo.repository;

import com.example.demo.entity.CardData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Card Data Repository
 * Repository interface for CardData entity operations
 * Based on COBOL CBACT02C.cbl business rules for sequential file access
 */
@Repository
public interface CardDataRepository extends JpaRepository<CardData, String> {

    /**
     * Find card data by card number (primary key)
     * Equivalent to indexed file access by FDCARDNUM
     */
    Optional<CardData> findByCardNumber(String cardNumber);

    /**
     * Check if card data exists by card number
     */
    boolean existsByCardNumber(String cardNumber);

    /**
     * Find all card data ordered by card number (sequential access simulation)
     * Mimics the sequential reading behavior from COBOL program
     */
    @Query("SELECT c FROM CardData c ORDER BY c.cardNumber ASC")
    List<CardData> findAllOrderedByCardNumber();

    /**
     * Find all card data with pagination, ordered by card number
     * For batch processing with pagination support
     */
    @Query("SELECT c FROM CardData c ORDER BY c.cardNumber ASC")
    Page<CardData> findAllOrderedByCardNumber(Pageable pageable);

    /**
     * Find card data by partial card number match
     * Useful for search operations
     */
    @Query("SELECT c FROM CardData c WHERE c.cardNumber LIKE :cardNumberPattern ORDER BY c.cardNumber ASC")
    List<CardData> findByCardNumberContaining(@Param("cardNumberPattern") String cardNumberPattern);

    /**
     * Find card data by card data content
     * Search within the FDCARDDATA field
     */
    @Query("SELECT c FROM CardData c WHERE c.cardData LIKE %:searchTerm% ORDER BY c.cardNumber ASC")
    List<CardData> findByCardDataContaining(@Param("searchTerm") String searchTerm);

    /**
     * Count total number of card records
     * Useful for batch processing statistics
     */
    @Query("SELECT COUNT(c) FROM CardData c")
    long countAllCardData();

    /**
     * Find card data within a range of card numbers
     * Useful for batch processing specific ranges
     */
    @Query("SELECT c FROM CardData c WHERE c.cardNumber BETWEEN :startCardNumber AND :endCardNumber ORDER BY c.cardNumber ASC")
    List<CardData> findByCardNumberRange(@Param("startCardNumber") String startCardNumber, 
                                        @Param("endCardNumber") String endCardNumber);

    /**
     * Find the first N card records ordered by card number
     * Useful for batch processing with limits
     */
    @Query("SELECT c FROM CardData c ORDER BY c.cardNumber ASC")
    List<CardData> findTopNOrderedByCardNumber(Pageable pageable);
}