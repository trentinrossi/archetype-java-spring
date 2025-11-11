package com.example.demo.repository;

import com.example.demo.entity.UserSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    
    Optional<UserSession> findByTransactionId(String transactionId);
    
    List<UserSession> findByProgramName(String programName);
    
    Page<UserSession> findByProgramName(String programName, Pageable pageable);
    
    List<UserSession> findByReenterFlag(Boolean reenterFlag);
    
    Page<UserSession> findByReenterFlag(Boolean reenterFlag, Pageable pageable);
    
    @Query("SELECT us FROM UserSession us WHERE us.fromProgram IS NOT NULL AND us.fromTransaction IS NOT NULL")
    List<UserSession> findSessionsWithCallingContext();
    
    List<UserSession> findByFromProgram(String fromProgram);
    
    List<UserSession> findByFromTransaction(String fromTransaction);
    
    Optional<UserSession> findByTransactionIdAndProgramName(String transactionId, String programName);
    
    List<UserSession> findByProgramNameAndReenterFlag(String programName, Boolean reenterFlag);
    
    @Query("SELECT us FROM UserSession us WHERE us.programContext IS NOT NULL")
    List<UserSession> findSessionsWithProgramContext();
    
    boolean existsByTransactionId(String transactionId);
    
    boolean existsByProgramName(String programName);
    
    long countByReenterFlag(Boolean reenterFlag);
    
    long countByProgramName(String programName);
    
    @Query("SELECT us FROM UserSession us WHERE us.fromProgram = :fromProgram AND us.fromTransaction = :fromTransaction")
    List<UserSession> findByFromProgramAndFromTransaction(@Param("fromProgram") String fromProgram, 
                                                           @Param("fromTransaction") String fromTransaction);
    
    @Query("SELECT us FROM UserSession us WHERE us.transactionId = :transactionId AND us.reenterFlag = :reenterFlag")
    Optional<UserSession> findByTransactionIdAndReenterFlag(@Param("transactionId") String transactionId, 
                                                             @Param("reenterFlag") Boolean reenterFlag);
    
    List<UserSession> findByProgramNameAndFromProgram(String programName, String fromProgram);
    
    @Query("SELECT COUNT(us) FROM UserSession us WHERE us.fromProgram IS NOT NULL")
    long countSessionsWithFromProgram();
    
    @Query("SELECT COUNT(us) FROM UserSession us WHERE us.programContext IS NOT NULL")
    long countSessionsWithProgramContext();
}
