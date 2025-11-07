package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity
 * Business Rule BR001: Admin users can view all credit cards when no context is passed.
 * Non-admin users can only view cards associated with their specific account.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    /**
     * Find user by user ID
     */
    Optional<User> findByUserId(String userId);
    
    /**
     * Check if user exists by user ID
     */
    boolean existsByUserId(String userId);
    
    /**
     * Find users by user type
     * Business Rule BR001: Distinguish between admin and regular users
     */
    Page<User> findByUserType(UserType userType, Pageable pageable);
    
    /**
     * Find all admin users
     * Business Rule BR001: Admin users have elevated permissions
     */
    @Query("SELECT u FROM User u WHERE u.userType = 'ADMIN'")
    List<User> findAllAdminUsers();
    
    /**
     * Find users with access to a specific account
     */
    @Query("SELECT u FROM User u JOIN u.accounts a WHERE a.accountId = :accountId")
    List<User> findUsersByAccountId(@Param("accountId") String accountId);
    
    /**
     * Check if user has access to account
     * Business Rule BR001: Non-admin users can only view cards associated with their specific account
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
           "FROM User u JOIN u.accounts a " +
           "WHERE u.userId = :userId AND a.accountId = :accountId")
    boolean hasAccessToAccount(@Param("userId") String userId, @Param("accountId") String accountId);
    
    /**
     * Find user with accounts eagerly loaded
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.accounts WHERE u.userId = :userId")
    Optional<User> findByUserIdWithAccounts(@Param("userId") String userId);
    
    /**
     * Count users by type
     */
    long countByUserType(UserType userType);
}
