package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    Page<User> findByUserType(String userType, Pageable pageable);
    
    List<User> findByUserType(String userType);
    
    List<User> findByUserType(String userType, Sort sort);
    
    Page<User> findByUserIdGreaterThan(String userId, Pageable pageable);
    
    Page<User> findByUserIdLessThan(String userId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.password = :password")
    Optional<User> findByUserIdAndPassword(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.password = :password AND u.userType = :userType")
    Optional<User> authenticateUser(@Param("userId") String userId, @Param("password") String password, @Param("userType") String userType);
    
    @Query("SELECT u FROM User u WHERE UPPER(u.firstName) LIKE UPPER(CONCAT('%', :searchTerm, '%')) " +
           "OR UPPER(u.lastName) LIKE UPPER(CONCAT('%', :searchTerm, '%')) " +
           "OR UPPER(u.userId) LIKE UPPER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userId > :userId ORDER BY u.userId ASC")
    List<User> findUsersAfter(@Param("userId") String userId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userId < :userId ORDER BY u.userId DESC")
    List<User> findUsersBefore(@Param("userId") String userId, Pageable pageable);
    
    @Query("SELECT u FROM User u ORDER BY u.userId ASC")
    List<User> findFirstUsers(Pageable pageable);
    
    @Query("SELECT u FROM User u ORDER BY u.userId DESC")
    List<User> findLastUsers(Pageable pageable);
    
    @Query("SELECT MIN(u.userId) FROM User u")
    Optional<String> findFirstUserId();
    
    @Query("SELECT MAX(u.userId) FROM User u")
    Optional<String> findLastUserId();
    
    @Query("SELECT u FROM User u WHERE u.userId > :startUserId ORDER BY u.userId ASC")
    Page<User> findUsersForwardFrom(@Param("startUserId") String startUserId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userId < :endUserId ORDER BY u.userId DESC")
    Page<User> findUsersBackwardFrom(@Param("endUserId") String endUserId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND u.userId > :startUserId ORDER BY u.userId ASC")
    Page<User> findUsersByTypeForwardFrom(@Param("userType") String userType, @Param("startUserId") String startUserId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND u.userId < :endUserId ORDER BY u.userId DESC")
    Page<User> findUsersByTypeBackwardFrom(@Param("userType") String userType, @Param("endUserId") String endUserId, Pageable pageable);
    
    long countByUserType(String userType);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'A'")
    long countAdminUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'R'")
    long countRegularUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'A' ORDER BY u.userId")
    List<User> findAllAdminUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'R' ORDER BY u.userId")
    List<User> findAllRegularUsers();
    
    @Query("SELECT DISTINCT u.userType FROM User u ORDER BY u.userType")
    List<String> findDistinctUserTypes();
    
    @Query("SELECT u FROM User u WHERE u.userId BETWEEN :startUserId AND :endUserId ORDER BY u.userId")
    List<User> findUsersBetweenIds(@Param("startUserId") String startUserId, @Param("endUserId") String endUserId);
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= CURRENT_DATE ORDER BY u.createdAt DESC")
    List<User> findUsersCreatedToday();
    
    @Query("SELECT u FROM User u WHERE u.updatedAt >= CURRENT_DATE ORDER BY u.updatedAt DESC")
    List<User> findUsersUpdatedToday();
}