package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.password = :password")
    Optional<User> findByUserIdAndPassword(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT u FROM User u WHERE UPPER(u.userId) = UPPER(:userId)")
    Optional<User> findByUserIdIgnoreCase(@Param("userId") String userId);
    
    @Query("SELECT u FROM User u WHERE UPPER(u.userId) = UPPER(:userId) AND u.password = :password")
    Optional<User> findByUserIdAndPasswordIgnoreCase(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE UPPER(u.userId) = UPPER(:userId)")
    boolean existsByUserIdIgnoreCase(@Param("userId") String userId);
    
    @Query("SELECT u FROM User u WHERE UPPER(u.firstName) LIKE UPPER(CONCAT('%', :searchTerm, '%')) " +
           "OR UPPER(u.lastName) LIKE UPPER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE UPPER(u.firstName) LIKE UPPER(CONCAT('%', :firstName, '%'))")
    List<User> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);
    
    @Query("SELECT u FROM User u WHERE UPPER(u.lastName) LIKE UPPER(CONCAT('%', :lastName, '%'))")
    List<User> findByLastNameContainingIgnoreCase(@Param("lastName") String lastName);
    
    Page<User> findByUserType(String userType, Pageable pageable);
    
    List<User> findByUserType(String userType);
    
    long countByUserType(String userType);
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :since")
    List<User> findRecentUsers(@Param("since") LocalDateTime since);
    
    @Query("SELECT u FROM User u WHERE u.updatedAt >= :since")
    List<User> findRecentlyUpdatedUsers(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'A'")
    long countAdminUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'U'")
    long countRegularUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'A'")
    List<User> findAllAdminUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'U'")
    List<User> findAllRegularUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'A'")
    Page<User> findAllAdminUsers(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = 'U'")
    Page<User> findAllRegularUsers(Pageable pageable);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userId = :userId AND u.password = :password")
    boolean validateUserCredentials(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE UPPER(u.userId) = UPPER(:userId) AND u.password = :password")
    boolean validateUserCredentialsIgnoreCase(@Param("userId") String userId, @Param("password") String password);
}