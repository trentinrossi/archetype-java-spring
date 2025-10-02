package com.example.demo.repository;

import com.example.demo.entity.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity, String> {
    
    @Query("SELECT u FROM UserSecurity u WHERE UPPER(u.userId) = UPPER(:userId)")
    Optional<UserSecurity> findByUserIdIgnoreCase(@Param("userId") String userId);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserSecurity u WHERE UPPER(u.userId) = UPPER(:userId)")
    boolean existsByUserIdIgnoreCase(@Param("userId") String userId);
    
    @Query("SELECT u FROM UserSecurity u WHERE u.userType = :userType")
    List<UserSecurity> findByUserType(@Param("userType") String userType);
    
    @Query("SELECT u FROM UserSecurity u WHERE u.userType = :userType")
    Page<UserSecurity> findByUserType(@Param("userType") String userType, Pageable pageable);
    
    @Query("SELECT u FROM UserSecurity u WHERE u.userType = 'A'")
    List<UserSecurity> findAllAdminUsers();
    
    @Query("SELECT u FROM UserSecurity u WHERE u.userType = 'R'")
    List<UserSecurity> findAllRegularUsers();
    
    @Query("SELECT u FROM UserSecurity u WHERE UPPER(u.userId) = UPPER(:userId) AND u.password = :password")
    Optional<UserSecurity> findByUserIdAndPasswordIgnoreCase(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserSecurity u WHERE UPPER(u.userId) = UPPER(:userId) AND u.password = :password")
    boolean validateUserCredentials(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT u FROM UserSecurity u WHERE UPPER(u.firstName) LIKE UPPER(CONCAT('%', :searchTerm, '%')) " +
           "OR UPPER(u.lastName) LIKE UPPER(CONCAT('%', :searchTerm, '%'))")
    Page<UserSecurity> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM UserSecurity u WHERE u.userType = :userType " +
           "AND (UPPER(u.firstName) LIKE UPPER(CONCAT('%', :searchTerm, '%')) " +
           "OR UPPER(u.lastName) LIKE UPPER(CONCAT('%', :searchTerm, '%')))")
    Page<UserSecurity> findByUserTypeAndNameContainingIgnoreCase(@Param("userType") String userType, 
                                                                 @Param("searchTerm") String searchTerm, 
                                                                 Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM UserSecurity u WHERE u.userType = :userType")
    long countByUserType(@Param("userType") String userType);
    
    @Query("SELECT COUNT(u) FROM UserSecurity u WHERE u.userType = 'A'")
    long countAdminUsers();
    
    @Query("SELECT COUNT(u) FROM UserSecurity u WHERE u.userType = 'R'")
    long countRegularUsers();
    
    @Query("SELECT u FROM UserSecurity u WHERE u.userType IN ('A', 'R')")
    List<UserSecurity> findAllValidUsers();
    
    @Query("SELECT u FROM UserSecurity u WHERE u.userType NOT IN ('A', 'R')")
    List<UserSecurity> findAllInvalidUsers();
}