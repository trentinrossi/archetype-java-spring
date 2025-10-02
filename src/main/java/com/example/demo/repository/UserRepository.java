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
    
    Optional<User> findByUserIdAndPassword(String userId, String password);
    
    boolean existsByUserId(String userId);
    
    List<User> findByUserType(String userType);
    
    Optional<User> findByUserId(String userId);
    
    Page<User> findByUserType(String userType, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> findByUserTypeAndNameContaining(@Param("userType") String userType, 
                                               @Param("searchTerm") String searchTerm, 
                                               Pageable pageable);
    
    long countByUserType(String userType);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'A'")
    long countAdminUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'R'")
    long countRegularUsers();
}