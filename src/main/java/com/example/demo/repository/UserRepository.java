package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByUserIdAndPassword(String userId, String password);
    
    Optional<User> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    Page<User> findAllByOrderByUserId(Pageable pageable);
    
    Page<User> findByUserTypeOrderByUserId(String userType, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY u.userId")
    Page<User> findBySearchTermIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType " +
           "AND (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY u.userId")
    Page<User> findByUserTypeAndSearchTermIgnoreCase(@Param("userType") String userType, 
                                                     @Param("searchTerm") String searchTerm, 
                                                     Pageable pageable);
    
    List<User> findByUserTypeOrderByUserId(String userType);
    
    long countByUserType(String userType);
    
    @Query("SELECT COUNT(u) FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    long countBySearchTermIgnoreCase(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.userId) = LOWER(:userId)")
    Optional<User> findByUserIdIgnoreCase(@Param("userId") String userId);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.userId) = LOWER(:userId) AND u.password = :password")
    Optional<User> findByUserIdIgnoreCaseAndPassword(@Param("userId") String userId, @Param("password") String password);
}