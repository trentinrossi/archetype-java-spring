package com.example.demo.repository;

import com.example.demo.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    
    // Sequential file access patterns (COBOL equivalent)
    @Query("SELECT c FROM Card c ORDER BY c.sequenceNumber ASC, c.createdAt ASC")
    List<Card> findAllInSequentialOrder();
    
    @Query("SELECT c FROM Card c ORDER BY c.sequenceNumber ASC, c.createdAt ASC")
    Page<Card> findAllInSequentialOrder(Pageable pageable);
    
    @Query("SELECT c FROM Card c WHERE c.sequenceNumber >= :startSequence ORDER BY c.sequenceNumber ASC")
    List<Card> findFromSequenceNumber(@Param("startSequence") Long startSequence);
    
    @Query("SELECT c FROM Card c WHERE c.sequenceNumber BETWEEN :startSequence AND :endSequence ORDER BY c.sequenceNumber ASC")
    List<Card> findBySequenceNumberRange(@Param("startSequence") Long startSequence, @Param("endSequence") Long endSequence);
    
    // Card number lookup (FDCARDNUM field equivalent)
    Optional<Card> findByCardNumber(String cardNumber);
    
    boolean existsByCardNumber(String cardNumber);
    
    @Query("SELECT c FROM Card c WHERE c.cardNumber IN :cardNumbers ORDER BY c.cardNumber")
    List<Card> findByCardNumberIn(@Param("cardNumbers") List<String> cardNumbers);
    
    @Query("SELECT c FROM Card c WHERE c.cardNumber LIKE :pattern ORDER BY c.cardNumber")
    List<Card> findByCardNumberPattern(@Param("pattern") String pattern);
    
    @Query("SELECT c FROM Card c WHERE c.cardNumber >= :startCardNumber AND c.cardNumber <= :endCardNumber ORDER BY c.cardNumber")
    List<Card> findByCardNumberRange(@Param("startCardNumber") String startCardNumber, @Param("endCardNumber") String endCardNumber);
    
    // Status-based queries
    Page<Card> findByRecordStatus(String recordStatus, Pageable pageable);
    
    List<Card> findByRecordStatus(String recordStatus);
    
    List<Card> findByRecordStatusIn(List<String> recordStatuses);
    
    @Query("SELECT c FROM Card c WHERE c.recordStatus = 'A' ORDER BY c.sequenceNumber ASC")
    List<Card> findActiveCards();
    
    @Query("SELECT c FROM Card c WHERE c.recordStatus = 'A' ORDER BY c.sequenceNumber ASC")
    Page<Card> findActiveCards(Pageable pageable);
    
    @Query("SELECT c FROM Card c WHERE c.recordStatus = 'I' ORDER BY c.sequenceNumber ASC")
    List<Card> findInactiveCards();
    
    @Query("SELECT c FROM Card c WHERE c.recordStatus = 'S' ORDER BY c.sequenceNumber ASC")
    List<Card> findSuspendedCards();
    
    @Query("SELECT c FROM Card c WHERE c.recordStatus = 'D' ORDER BY c.sequenceNumber ASC")
    List<Card> findDeletedCards();
    
    long countByRecordStatus(String recordStatus);
    
    // Validation status queries
    Page<Card> findByValidationStatus(String validationStatus, Pageable pageable);
    
    List<Card> findByValidationStatus(String validationStatus);
    
    @Query("SELECT c FROM Card c WHERE c.validationStatus = 'V' ORDER BY c.sequenceNumber ASC")
    List<Card> findValidatedCards();
    
    @Query("SELECT c FROM Card c WHERE c.validationStatus = 'P' ORDER BY c.sequenceNumber ASC")
    List<Card> findValidationPendingCards();
    
    @Query("SELECT c FROM Card c WHERE c.validationStatus = 'F' ORDER BY c.sequenceNumber ASC")
    List<Card> findValidationFailedCards();
    
    long countByValidationStatus(String validationStatus);
    
    // Processing flag queries
    Page<Card> findByProcessingFlag(String processingFlag, Pageable pageable);
    
    List<Card> findByProcessingFlag(String processingFlag);
    
    @Query("SELECT c FROM Card c WHERE c.processingFlag = 'P' ORDER BY c.sequenceNumber ASC")
    List<Card> findPendingProcessingCards();
    
    @Query("SELECT c FROM Card c WHERE c.processingFlag = 'R' ORDER BY c.sequenceNumber ASC")
    List<Card> findProcessingCards();
    
    @Query("SELECT c FROM Card c WHERE c.processingFlag = 'C' ORDER BY c.sequenceNumber ASC")
    List<Card> findCompletedProcessingCards();
    
    long countByProcessingFlag(String processingFlag);
    
    // Error handling queries
    @Query("SELECT c FROM Card c WHERE c.errorCode IS NOT NULL AND c.errorCode != '' ORDER BY c.sequenceNumber ASC")
    List<Card> findCardsWithErrors();
    
    @Query("SELECT c FROM Card c WHERE c.errorCode IS NOT NULL AND c.errorCode != '' ORDER BY c.sequenceNumber ASC")
    Page<Card> findCardsWithErrors(Pageable pageable);
    
    List<Card> findByErrorCode(String errorCode);
    
    @Query("SELECT c FROM Card c WHERE c.errorMessage LIKE %:errorPattern% ORDER BY c.sequenceNumber ASC")
    List<Card> findByErrorMessageContaining(@Param("errorPattern") String errorPattern);
    
    @Query("SELECT c FROM Card c WHERE c.errorCode IS NULL OR c.errorCode = '' ORDER BY c.sequenceNumber ASC")
    List<Card> findCardsWithoutErrors();
    
    long countByErrorCodeIsNotNull();
    
    // Batch ID and sequence number queries
    Page<Card> findByBatchId(String batchId, Pageable pageable);
    
    List<Card> findByBatchId(String batchId);
    
    @Query("SELECT c FROM Card c WHERE c.batchId = :batchId ORDER BY c.sequenceNumber ASC")
    List<Card> findByBatchIdOrderBySequence(@Param("batchId") String batchId);
    
    List<Card> findByBatchIdIn(List<String> batchIds);
    
    @Query("SELECT c FROM Card c WHERE c.batchId = :batchId AND c.sequenceNumber = :sequenceNumber")
    Optional<Card> findByBatchIdAndSequenceNumber(@Param("batchId") String batchId, @Param("sequenceNumber") Long sequenceNumber);
    
    @Query("SELECT c FROM Card c WHERE c.batchId = :batchId AND c.sequenceNumber BETWEEN :startSeq AND :endSeq ORDER BY c.sequenceNumber ASC")
    List<Card> findByBatchIdAndSequenceNumberRange(@Param("batchId") String batchId, @Param("startSeq") Long startSeq, @Param("endSeq") Long endSeq);
    
    @Query("SELECT MAX(c.sequenceNumber) FROM Card c WHERE c.batchId = :batchId")
    Optional<Long> findMaxSequenceNumberByBatchId(@Param("batchId") String batchId);
    
    @Query("SELECT MIN(c.sequenceNumber) FROM Card c WHERE c.batchId = :batchId")
    Optional<Long> findMinSequenceNumberByBatchId(@Param("batchId") String batchId);
    
    long countByBatchId(String batchId);
    
    // Data integrity and hash-based queries
    List<Card> findByCardDataHash(String cardDataHash);
    
    @Query("SELECT c FROM Card c WHERE c.cardDataHash = :hash ORDER BY c.sequenceNumber ASC")
    List<Card> findByDataHash(@Param("hash") String hash);
    
    @Query("SELECT c FROM Card c WHERE c.cardDataHash IS NULL OR c.cardDataHash = '' ORDER BY c.sequenceNumber ASC")
    List<Card> findCardsWithoutDataHash();
    
    @Query("SELECT c FROM Card c WHERE c.dataLength != LENGTH(c.cardData) ORDER BY c.sequenceNumber ASC")
    List<Card> findCardsWithDataLengthMismatch();
    
    @Query("SELECT COUNT(c) FROM Card c WHERE c.cardDataHash = :hash")
    long countByCardDataHash(@Param("hash") String hash);
    
    // Time-based queries (created, updated, last accessed)
    @Query("SELECT c FROM Card c WHERE c.createdAt >= :since ORDER BY c.createdAt ASC")
    List<Card> findCreatedSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT c FROM Card c WHERE c.createdAt BETWEEN :start AND :end ORDER BY c.createdAt ASC")
    List<Card> findCreatedBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT c FROM Card c WHERE c.updatedAt >= :since ORDER BY c.updatedAt ASC")
    List<Card> findUpdatedSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT c FROM Card c WHERE c.updatedAt BETWEEN :start AND :end ORDER BY c.updatedAt ASC")
    List<Card> findUpdatedBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT c FROM Card c WHERE c.lastAccessed >= :since ORDER BY c.lastAccessed ASC")
    List<Card> findAccessedSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT c FROM Card c WHERE c.lastAccessed < :before ORDER BY c.lastAccessed ASC")
    List<Card> findAccessedBefore(@Param("before") LocalDateTime before);
    
    @Query("SELECT c FROM Card c WHERE c.lastAccessed IS NULL ORDER BY c.createdAt ASC")
    List<Card> findNeverAccessed();
    
    @Query("SELECT c FROM Card c WHERE c.processingDate >= :since ORDER BY c.processingDate ASC")
    List<Card> findProcessedSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT c FROM Card c WHERE c.processingDate BETWEEN :start AND :end ORDER BY c.processingDate ASC")
    List<Card> findProcessedBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    // Batch processing operations
    @Query("SELECT c FROM Card c WHERE c.batchId = :batchId AND c.recordStatus = 'A' AND (c.errorCode IS NULL OR c.errorCode = '') ORDER BY c.sequenceNumber ASC")
    List<Card> findReadyForProcessingByBatchId(@Param("batchId") String batchId);
    
    @Query("SELECT c FROM Card c WHERE c.recordStatus = 'A' AND c.validationStatus = 'V' AND (c.processingFlag IS NULL OR c.processingFlag = 'P') ORDER BY c.sequenceNumber ASC")
    List<Card> findReadyForBatchProcessing();
    
    @Query("SELECT c FROM Card c WHERE c.recordStatus = 'A' AND c.validationStatus = 'V' AND (c.processingFlag IS NULL OR c.processingFlag = 'P') ORDER BY c.sequenceNumber ASC")
    Page<Card> findReadyForBatchProcessing(Pageable pageable);
    
    @Query("SELECT c FROM Card c WHERE c.processingFlag = 'R' AND c.processingDate < :staleThreshold ORDER BY c.processingDate ASC")
    List<Card> findStaleProcessingCards(@Param("staleThreshold") LocalDateTime staleThreshold);
    
    // System-based queries
    List<Card> findBySourceSystem(String sourceSystem);
    
    List<Card> findByTargetSystem(String targetSystem);
    
    @Query("SELECT c FROM Card c WHERE c.sourceSystem = :sourceSystem AND c.targetSystem = :targetSystem ORDER BY c.sequenceNumber ASC")
    List<Card> findBySourceAndTargetSystem(@Param("sourceSystem") String sourceSystem, @Param("targetSystem") String targetSystem);
    
    long countBySourceSystem(String sourceSystem);
    
    long countByTargetSystem(String targetSystem);
    
    // Complex queries for COBOL-like operations
    @Query("SELECT c FROM Card c WHERE c.recordStatus = :status AND c.validationStatus = :validationStatus AND c.processingFlag = :processingFlag ORDER BY c.sequenceNumber ASC")
    List<Card> findByStatusCombination(@Param("status") String status, @Param("validationStatus") String validationStatus, @Param("processingFlag") String processingFlag);
    
    @Query("SELECT c FROM Card c WHERE c.batchId = :batchId AND c.recordStatus IN :statuses ORDER BY c.sequenceNumber ASC")
    List<Card> findByBatchIdAndStatusIn(@Param("batchId") String batchId, @Param("statuses") List<String> statuses);
    
    @Query("SELECT c FROM Card c WHERE c.cardData LIKE %:pattern% ORDER BY c.sequenceNumber ASC")
    List<Card> findByCardDataContaining(@Param("pattern") String pattern);
    
    @Query("SELECT c FROM Card c WHERE LENGTH(c.cardData) = :length ORDER BY c.sequenceNumber ASC")
    List<Card> findByCardDataLength(@Param("length") Integer length);
    
    @Query("SELECT c FROM Card c WHERE LENGTH(c.cardData) BETWEEN :minLength AND :maxLength ORDER BY c.sequenceNumber ASC")
    List<Card> findByCardDataLengthRange(@Param("minLength") Integer minLength, @Param("maxLength") Integer maxLength);
    
    // Bulk update operations
    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.processingFlag = :processingFlag WHERE c.batchId = :batchId")
    int updateProcessingFlagByBatchId(@Param("batchId") String batchId, @Param("processingFlag") String processingFlag);
    
    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.validationStatus = :validationStatus WHERE c.cardNumber IN :cardNumbers")
    int updateValidationStatusByCardNumbers(@Param("cardNumbers") List<String> cardNumbers, @Param("validationStatus") String validationStatus);
    
    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.recordStatus = :recordStatus WHERE c.batchId = :batchId")
    int updateRecordStatusByBatchId(@Param("batchId") String batchId, @Param("recordStatus") String recordStatus);
    
    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.lastAccessed = :accessTime WHERE c.cardNumber = :cardNumber")
    int updateLastAccessed(@Param("cardNumber") String cardNumber, @Param("accessTime") LocalDateTime accessTime);
    
    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.errorCode = :errorCode, c.errorMessage = :errorMessage WHERE c.cardNumber = :cardNumber")
    int updateErrorInfo(@Param("cardNumber") String cardNumber, @Param("errorCode") String errorCode, @Param("errorMessage") String errorMessage);
    
    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.errorCode = NULL, c.errorMessage = NULL WHERE c.batchId = :batchId")
    int clearErrorsByBatchId(@Param("batchId") String batchId);
    
    // Statistics and aggregation queries
    @Query("SELECT c.recordStatus, COUNT(c) FROM Card c GROUP BY c.recordStatus")
    List<Object[]> getRecordStatusStatistics();
    
    @Query("SELECT c.validationStatus, COUNT(c) FROM Card c GROUP BY c.validationStatus")
    List<Object[]> getValidationStatusStatistics();
    
    @Query("SELECT c.processingFlag, COUNT(c) FROM Card c GROUP BY c.processingFlag")
    List<Object[]> getProcessingFlagStatistics();
    
    @Query("SELECT c.batchId, COUNT(c) FROM Card c GROUP BY c.batchId ORDER BY COUNT(c) DESC")
    List<Object[]> getBatchStatistics();
    
    @Query("SELECT c.sourceSystem, COUNT(c) FROM Card c GROUP BY c.sourceSystem")
    List<Object[]> getSourceSystemStatistics();
    
    @Query("SELECT c.errorCode, COUNT(c) FROM Card c WHERE c.errorCode IS NOT NULL GROUP BY c.errorCode ORDER BY COUNT(c) DESC")
    List<Object[]> getErrorCodeStatistics();
    
    // Custom indexed file operations (COBOL equivalent)
    @Query("SELECT c FROM Card c WHERE c.cardNumber > :lastCardNumber ORDER BY c.cardNumber ASC")
    List<Card> findNextCardsAfter(@Param("lastCardNumber") String lastCardNumber, Pageable pageable);
    
    @Query("SELECT c FROM Card c WHERE c.sequenceNumber > :lastSequence ORDER BY c.sequenceNumber ASC")
    List<Card> findNextSequentialRecords(@Param("lastSequence") Long lastSequence, Pageable pageable);
    
    @Query("SELECT c FROM Card c WHERE c.batchId = :batchId AND c.sequenceNumber > :lastSequence ORDER BY c.sequenceNumber ASC")
    List<Card> findNextBatchRecords(@Param("batchId") String batchId, @Param("lastSequence") Long lastSequence, Pageable pageable);
    
    // Data validation queries
    @Query("SELECT c FROM Card c WHERE c.cardNumber IS NULL OR c.cardNumber = '' OR LENGTH(c.cardNumber) != 16")
    List<Card> findCardsWithInvalidCardNumbers();
    
    @Query("SELECT c FROM Card c WHERE c.cardData IS NULL OR c.cardData = '' OR LENGTH(c.cardData) > 134")
    List<Card> findCardsWithInvalidCardData();
    
    @Query("SELECT c FROM Card c WHERE c.recordStatus NOT IN ('A', 'I', 'S', 'D')")
    List<Card> findCardsWithInvalidRecordStatus();
    
    @Query("SELECT c FROM Card c WHERE c.validationStatus NOT IN ('V', 'P', 'F') AND c.validationStatus IS NOT NULL")
    List<Card> findCardsWithInvalidValidationStatus();
    
    @Query("SELECT c FROM Card c WHERE c.processingFlag NOT IN ('P', 'R', 'C') AND c.processingFlag IS NOT NULL")
    List<Card> findCardsWithInvalidProcessingFlag();
}