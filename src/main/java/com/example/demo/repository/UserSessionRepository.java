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
    
    Optional<UserSession> findTopByUserIdOrderByIdDesc(String userId);
    
    List<UserSession> findAllByUserId(String userId);
    
    Optional<UserSession> findByTransactionId(String transactionId);
    
    List<UserSession> findAllByTransactionId(String transactionId);
    
    @Query("SELECT us FROM UserSession us WHERE us.userId = :userId AND us.reenterFlag = true")
    List<UserSession> findActiveSessionsByUserId(@Param("userId") String userId);
    
    Optional<UserSession> findByUserIdAndProgramName(String userId, String programName);
    
    List<UserSession> findAllByUserIdAndProgramName(String userId, String programName);
    
    boolean existsByUserId(String userId);
    
    boolean existsByUserIdAndProgramName(String userId, String programName);
    
    boolean existsByTransactionId(String transactionId);
    
    List<UserSession> findByUserType(String userType);
    
    List<UserSession> findByProgramName(String programName);
    
    List<UserSession> findByFromProgram(String fromProgram);
    
    List<UserSession> findByToProgram(String toProgram);
    
    List<UserSession> findByFromTransactionId(String fromTransactionId);
    
    List<UserSession> findByReenterFlag(Boolean reenterFlag);
    
    @Query("SELECT us FROM UserSession us WHERE us.userId = :userId AND us.transactionId = :transactionId")
    Optional<UserSession> findByUserIdAndTransactionId(@Param("userId") String userId, @Param("transactionId") String transactionId);
    
    @Query("SELECT us FROM UserSession us WHERE us.userId = :userId AND us.programName = :programName AND us.reenterFlag = true")
    Optional<UserSession> findActiveSessionByUserIdAndProgramName(@Param("userId") String userId, @Param("programName") String programName);
    
    @Query("SELECT us FROM UserSession us WHERE us.userType = :userType AND us.reenterFlag = true")
    List<UserSession> findActiveSessionsByUserType(@Param("userType") String userType);
    
    @Query("SELECT COUNT(us) FROM UserSession us WHERE us.userId = :userId")
    long countByUserId(@Param("userId") String userId);
    
    @Query("SELECT COUNT(us) FROM UserSession us WHERE us.userId = :userId AND us.reenterFlag = true")
    long countActiveSessionsByUserId(@Param("userId") String userId);
    
    @Query("SELECT COUNT(us) FROM UserSession us WHERE us.userType = :userType")
    long countByUserType(@Param("userType") String userType);
    
    @Query("SELECT COUNT(us) FROM UserSession us WHERE us.programName = :programName")
    long countByProgramName(@Param("programName") String programName);
    
    Page<UserSession> findByUserId(String userId, Pageable pageable);
    
    Page<UserSession> findByUserType(String userType, Pageable pageable);
    
    Page<UserSession> findByProgramName(String programName, Pageable pageable);
    
    @Query("SELECT us FROM UserSession us WHERE us.userId = :userId AND us.programName = :programName AND us.transactionId = :transactionId")
    Optional<UserSession> findByUserIdAndProgramNameAndTransactionId(
        @Param("userId") String userId, 
        @Param("programName") String programName, 
        @Param("transactionId") String transactionId
    );
    
    @Query("SELECT us FROM UserSession us WHERE us.fromProgram = :fromProgram AND us.toProgram = :toProgram")
    List<UserSession> findByProgramTransition(@Param("fromProgram") String fromProgram, @Param("toProgram") String toProgram);
    
    @Query("SELECT us FROM UserSession us WHERE us.userId = :userId ORDER BY us.id DESC")
    List<UserSession> findLatestSessionsByUserId(@Param("userId") String userId, Pageable pageable);
    
    void deleteByUserId(String userId);
    
    void deleteByUserIdAndProgramName(String userId, String programName);
    
    @Query("SELECT DISTINCT us.userId FROM UserSession us WHERE us.programName = :programName")
    List<String> findDistinctUserIdsByProgramName(@Param("programName") String programName);
    
    @Query("SELECT DISTINCT us.programName FROM UserSession us WHERE us.userId = :userId")
    List<String> findDistinctProgramNamesByUserId(@Param("userId") String userId);
}
