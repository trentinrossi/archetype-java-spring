package com.example.demo.repository;

import com.example.demo.entity.DateValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DateValidationRepository extends JpaRepository<DateValidation, Long> {
    
    Optional<DateValidation> findByInputDate(String inputDate);
    
    Page<DateValidation> findByInputDate(String inputDate, Pageable pageable);
    
    List<DateValidation> findByInputDateAndFormatPattern(String inputDate, String formatPattern);
    
    Page<DateValidation> findByFormatPattern(String formatPattern, Pageable pageable);
    
    Page<DateValidation> findByValidationResult(Boolean validationResult, Pageable pageable);
    
    Page<DateValidation> findBySeverityCode(String severityCode, Pageable pageable);
    
    Page<DateValidation> findByErrorType(String errorType, Pageable pageable);
    
    Page<DateValidation> findByMessageNumber(Integer messageNumber, Pageable pageable);
    
    List<DateValidation> findByValidationResultFalse();
    
    List<DateValidation> findByValidationResultTrue();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.validationResult = false AND dv.severityCode IN ('ERROR', 'CRITICAL')")
    List<DateValidation> findCriticalFailures();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.validationResult = false AND dv.severityCode IN ('WARNING', 'WARN')")
    List<DateValidation> findWarnings();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.createdAt BETWEEN :startDate AND :endDate")
    Page<DateValidation> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate, 
                                        Pageable pageable);
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.createdAt >= :since ORDER BY dv.createdAt DESC")
    List<DateValidation> findValidationHistory(@Param("since") LocalDateTime since);
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.errorType = 'INVALID_DATE'")
    List<DateValidation> findInvalidDateErrors();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.errorType = 'INSUFFICIENT_DATA'")
    List<DateValidation> findInsufficientDataErrors();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.errorType = 'BAD_DATE_VALUE'")
    List<DateValidation> findBadDateValueErrors();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.errorType = 'INVALID_ERA'")
    List<DateValidation> findInvalidEraErrors();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.errorType = 'UNSUPPORTED_RANGE'")
    List<DateValidation> findUnsupportedRangeErrors();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.errorType = 'INVALID_MONTH'")
    List<DateValidation> findInvalidMonthErrors();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.errorType = 'BAD_PICTURE_STRING'")
    List<DateValidation> findBadPictureStringErrors();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.errorType = 'NON_NUMERIC_DATA'")
    List<DateValidation> findNonNumericDataErrors();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.errorType = 'YEAR_IN_ERA_ZERO'")
    List<DateValidation> findYearInEraZeroErrors();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.messageNumber = :messageNumber")
    List<DateValidation> findByMessageNumber(@Param("messageNumber") Integer messageNumber);
    
    @Query("SELECT dv FROM DateValidation dv WHERE LOWER(dv.resultMessage) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<DateValidation> findByResultMessageContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.lillianDateOutput IS NOT NULL")
    List<DateValidation> findWithLillianDateOutput();
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.lillianDateOutput BETWEEN :startDate AND :endDate")
    List<DateValidation> findByLillianDateRange(@Param("startDate") Long startDate, @Param("endDate") Long endDate);
    
    long countByValidationResult(Boolean validationResult);
    
    long countBySeverityCode(String severityCode);
    
    long countByErrorType(String errorType);
    
    @Query("SELECT COUNT(dv) FROM DateValidation dv WHERE dv.createdAt >= :since")
    long countValidationsSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT dv.severityCode, COUNT(dv) FROM DateValidation dv GROUP BY dv.severityCode")
    List<Object[]> countBySeverityCodeGrouped();
    
    @Query("SELECT dv.errorType, COUNT(dv) FROM DateValidation dv WHERE dv.errorType IS NOT NULL GROUP BY dv.errorType")
    List<Object[]> countByErrorTypeGrouped();
    
    boolean existsByInputDateAndFormatPattern(String inputDate, String formatPattern);
    
    @Query("SELECT dv FROM DateValidation dv WHERE dv.inputDate = :inputDate AND dv.formatPattern = :formatPattern ORDER BY dv.createdAt DESC")
    List<DateValidation> findLatestValidationByInputAndFormat(@Param("inputDate") String inputDate, 
                                                             @Param("formatPattern") String formatPattern, 
                                                             Pageable pageable);
}