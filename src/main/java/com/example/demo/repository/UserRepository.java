package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    /**
     * Find user by User ID for authentication (READUSERSECFILE operation from COSGN00C)
     * @param userId the user ID to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUserId(String userId);
    
    /**
     * Check if user exists by User ID to prevent duplicates (duplicate key handling from COUSR01C)
     * @param userId the user ID to check
     * @return true if user exists, false otherwise
     */
    boolean existsByUserId(String userId);
    
    /**
     * Find user by User ID and password for authentication validation
     * @param userId the user ID
     * @param password the user password
     * @return Optional containing the user if credentials match
     */
    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.password = :password")
    Optional<User> findByUserIdAndPassword(@Param("userId") String userId, @Param("password") String password);
    
    /**
     * Find users by user type for administrative purposes
     * @param userType the user type (A=Admin, R=Regular)
     * @return list of users with specified type
     */
    @Query("SELECT u FROM User u WHERE u.userType = :userType")
    java.util.List<User> findByUserType(@Param("userType") String userType);
    
    /**
     * Count users by user type
     * @param userType the user type to count
     * @return number of users with specified type
     */
    long countByUserType(String userType);
    
    /**
     * Find users by first name and last name
     * @param firstName the first name
     * @param lastName the last name
     * @return list of users matching the name criteria
     */
    @Query("SELECT u FROM User u WHERE UPPER(u.firstName) = UPPER(:firstName) AND UPPER(u.lastName) = UPPER(:lastName)")
    java.util.List<User> findByFirstNameAndLastNameIgnoreCase(@Param("firstName") String firstName, @Param("lastName") String lastName);
}