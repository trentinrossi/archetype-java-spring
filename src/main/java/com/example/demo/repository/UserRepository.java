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

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<User> findByUserType(UserType userType);
    
    Page<User> findByUserType(UserType userType, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND SIZE(u.accounts) > 0")
    List<User> findUsersByTypeWithAccounts(@Param("userType") UserType userType);
    
    @Query("SELECT u FROM User u WHERE u.userType = 'ADMIN'")
    List<User> findAllAdminUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'REGULAR'")
    List<User> findAllRegularUsers();
    
    long countByUserType(UserType userType);
}
