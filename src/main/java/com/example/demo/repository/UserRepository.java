package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByUserId(String userId);
    
    Optional<User> findByUserIdAndPassword(String userId, String password);
    
    boolean existsByUserId(String userId);
    
    List<User> findByUserType(String userType);
    
    Page<User> findByUserType(String userType, Pageable pageable);
    
    void deleteByUserId(String userId);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType ORDER BY u.createdAt DESC")
    List<User> findByUserTypeOrderByCreatedAtDesc(@Param("userType") String userType);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = :userType")
    long countByUserType(@Param("userType") String userType);
    
    @Query("SELECT u FROM User u WHERE u.userType = 'A'")
    List<User> findAllAdminUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'R'")
    List<User> findAllRegularUsers();
}