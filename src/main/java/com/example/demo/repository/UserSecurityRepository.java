package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.UserSecurity;
import com.example.demo.entity.UserSecurity.UserType;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity, String> {
    
    @Query("SELECT u FROM UserSecurity u WHERE UPPER(u.userId) = UPPER(:userId)")
    Optional<UserSecurity> findByUserIdIgnoreCase(@Param("userId") String userId);
    
    @Query("SELECT u FROM UserSecurity u WHERE u.active = true")
    List<UserSecurity> findActiveUsers();
    
    @Query("SELECT u FROM UserSecurity u WHERE u.active = true")
    Page<UserSecurity> findActiveUsers(Pageable pageable);
    
    @Query("SELECT u FROM UserSecurity u WHERE u.userType = :userType")
    List<UserSecurity> findByUserType(@Param("userType") UserType userType);
    
    @Query("SELECT u FROM UserSecurity u WHERE u.userType = :userType")
    Page<UserSecurity> findByUserType(@Param("userType") UserType userType, Pageable pageable);
    
    @Query("SELECT u FROM UserSecurity u WHERE u.userType = :userType AND u.active = true")
    List<UserSecurity> findActiveUsersByUserType(@Param("userType") UserType userType);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserSecurity u WHERE UPPER(u.userId) = UPPER(:userId) AND u.active = true")
    boolean existsByUserIdAndActive(@Param("userId") String userId);
    
    @Query("SELECT u FROM UserSecurity u WHERE UPPER(u.userId) = UPPER(:userId) AND UPPER(u.password) = UPPER(:password)")
    Optional<UserSecurity> findByUserIdAndPasswordIgnoreCase(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT u FROM UserSecurity u WHERE UPPER(u.userId) = UPPER(:userId) AND UPPER(u.password) = UPPER(:password) AND u.active = true")
    Optional<UserSecurity> authenticateUser(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserSecurity u WHERE UPPER(u.userId) = UPPER(:userId) AND UPPER(u.password) = UPPER(:password) AND u.active = true")
    boolean validateAuthentication(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserSecurity u WHERE UPPER(u.userId) = UPPER(:userId)")
    boolean existsByUserIdIgnoreCase(@Param("userId") String userId);
    
    @Query("SELECT u FROM UserSecurity u WHERE u.programName = :programName")
    List<UserSecurity> findByProgramName(@Param("programName") String programName);
    
    @Query("SELECT u FROM UserSecurity u WHERE u.transactionId = :transactionId")
    List<UserSecurity> findByTransactionId(@Param("transactionId") String transactionId);
    
    @Query("SELECT COUNT(u) FROM UserSecurity u WHERE u.active = true")
    long countActiveUsers();
    
    @Query("SELECT COUNT(u) FROM UserSecurity u WHERE u.userType = :userType AND u.active = true")
    long countActiveUsersByType(@Param("userType") UserType userType);
    
    @Query("SELECT u FROM UserSecurity u WHERE u.active = false")
    List<UserSecurity> findInactiveUsers();
    
    @Query("SELECT u FROM UserSecurity u WHERE UPPER(u.userId) LIKE UPPER(CONCAT('%', :searchTerm, '%'))")
    Page<UserSecurity> findByUserIdContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
}