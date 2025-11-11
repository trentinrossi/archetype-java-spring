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
 * UserRepository
 * 
 * Data access layer for User entity.
 * Provides methods for querying and managing system users.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    /**
     * Finds a user by user ID
     * 
     * @param userId The user ID to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUserId(String userId);
    
    /**
     * Checks if a user exists by user ID
     * 
     * @param userId The user ID to check
     * @return true if user exists, false otherwise
     */
    boolean existsByUserId(String userId);
    
    /**
     * Finds a user by user ID and user type
     * 
     * @param userId The user ID
     * @param userType The user type
     * @return Optional containing the user if found
     */
    Optional<User> findByUserIdAndUserType(String userId, UserType userType);
    
    /**
     * Finds all users by user type
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userType The user type to filter by
     * @return List of users with the specified type
     */
    List<User> findByUserType(UserType userType);
    
    /**
     * Finds all users by user type with pagination
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userType The user type to filter by
     * @param pageable Pagination information
     * @return Page of users with the specified type
     */
    Page<User> findByUserType(UserType userType, Pageable pageable);
    
    /**
     * Finds all admin users
     * Implements BR001: User Permission Based Card Access
     * 
     * @return List of admin users
     */
    @Query("SELECT u FROM User u WHERE u.userType = 'ADMIN'")
    List<User> findAllAdminUsers();
    
    /**
     * Finds all regular users
     * Implements BR001: User Permission Based Card Access
     * 
     * @return List of regular users
     */
    @Query("SELECT u FROM User u WHERE u.userType = 'REGULAR'")
    List<User> findAllRegularUsers();
    
    /**
     * Checks if a user is an admin
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID to check
     * @return true if user is admin, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
           "FROM User u WHERE u.userId = :userId AND u.userType = 'ADMIN'")
    boolean isAdminUser(@Param("userId") String userId);
    
    /**
     * Checks if a user is a regular user
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID to check
     * @return true if user is regular, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
           "FROM User u WHERE u.userId = :userId AND u.userType = 'REGULAR'")
    boolean isRegularUser(@Param("userId") String userId);
    
    /**
     * Finds users by user ID containing search term
     * 
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return Page of users matching the search term
     */
    @Query("SELECT u FROM User u WHERE UPPER(u.userId) LIKE UPPER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByUserIdContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Counts users by user type
     * 
     * @param userType The user type to count
     * @return Number of users with the specified type
     */
    long countByUserType(UserType userType);
    
    /**
     * Counts admin users
     * Implements BR001: User Permission Based Card Access
     * 
     * @return Number of admin users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'ADMIN'")
    long countAdminUsers();
    
    /**
     * Counts regular users
     * Implements BR001: User Permission Based Card Access
     * 
     * @return Number of regular users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'REGULAR'")
    long countRegularUsers();
    
    /**
     * Finds users by a list of user IDs
     * 
     * @param userIds List of user IDs
     * @return List of users
     */
    @Query("SELECT u FROM User u WHERE u.userId IN :userIds")
    List<User> findByUserIdIn(@Param("userIds") List<String> userIds);
    
    /**
     * Finds users with access to a specific account
     * Implements BR001: User Permission Based Card Access
     * 
     * @param accountId The account ID
     * @return List of users with access to the account
     */
    @Query("SELECT u FROM User u JOIN u.accounts a WHERE a.accountId = :accountId")
    List<User> findUsersWithAccessToAccount(@Param("accountId") String accountId);
    
    /**
     * Checks if a user exists by user ID and user type
     * 
     * @param userId The user ID
     * @param userType The user type
     * @return true if user exists, false otherwise
     */
    boolean existsByUserIdAndUserType(String userId, UserType userType);
}
