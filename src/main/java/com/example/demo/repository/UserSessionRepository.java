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
    
    Optional<UserSession> findByUserId(String userId);
    
    List<UserSession> findByUserIdOrderByCreatedAtDesc(String userId);
    
    Optional<UserSession> findTopByUserIdOrderByCreatedAtDesc(String userId);
    
    List<UserSession> findByUserType(String userType);
    
    Page<UserSession> findByUserType(String userType, Pageable pageable);
    
    List<UserSession> findByTransactionId(String transactionId);
    
    List<UserSession> findByProgramName(String programName);
    
    boolean existsByUserId(String userId);
    
    @Query("SELECT u FROM UserSession u WHERE u.userType = 'A'")
    List<UserSession> findAdminSessions();
    
    @Query("SELECT u FROM UserSession u WHERE u.userType IN ('U', 'R')")
    List<UserSession> findRegularUserSessions();
    
    @Query("SELECT u FROM UserSession u WHERE u.reenterFlag = true")
    List<UserSession> findReenteringSessions();
    
    @Query("SELECT u FROM UserSession u WHERE u.userId = :userId AND u.transactionId = :transactionId")
    Optional<UserSession> findByUserIdAndTransactionId(@Param("userId") String userId, @Param("transactionId") String transactionId);
    
    @Query("SELECT COUNT(u) FROM UserSession u WHERE u.userType = :userType")
    long countByUserType(@Param("userType") String userType);
    
    void deleteByUserId(String userId);
}
