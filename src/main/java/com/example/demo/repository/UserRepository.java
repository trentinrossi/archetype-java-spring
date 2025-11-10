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
 * Repository interface for User entity.
 * Provides data access methods for user operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username
     * @param username the username
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     * @param email the email address
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by username
     * @param username the username
     * @return true if user exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if user exists by email
     * @param email the email address
     * @return true if user exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by user type
     * @param userType the type of user
     * @param pageable pagination information
     * @return page of users
     */
    Page<User> findByUserType(UserType userType, Pageable pageable);
    
    /**
     * Find users by account ID
     * @param accountId the account identifier
     * @return list of users associated with the account
     */
    List<User> findByAccountId(String accountId);
    
    /**
     * Find active users
     * @param active the active status
     * @param pageable pagination information
     * @return page of users
     */
    Page<User> findByActive(Boolean active, Pageable pageable);
    
    /**
     * Find admin users
     * @return list of admin users
     */
    @Query("SELECT u FROM User u WHERE u.userType = 'ADMIN'")
    List<User> findAdminUsers();
    
    /**
     * Find regular users by account ID
     * @param accountId the account identifier
     * @return list of regular users for the account
     */
    @Query("SELECT u FROM User u WHERE u.userType = 'REGULAR' AND u.accountId = :accountId")
    List<User> findRegularUsersByAccountId(@Param("accountId") String accountId);
    
    /**
     * Count users by user type
     * @param userType the type of user
     * @return count of users
     */
    long countByUserType(UserType userType);
    
    /**
     * Search users by name or username
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return page of users matching the search
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);
}
