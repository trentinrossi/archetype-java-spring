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
    
    Optional<User> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    List<User> findByUserType(String userType);
    
    Page<User> findByUserType(String userType, Pageable pageable);
    
    List<User> findByAuthenticated(Boolean authenticated);
    
    Page<User> findByAuthenticated(Boolean authenticated, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.password = :password")
    Optional<User> findByUserIdAndPassword(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT u FROM User u WHERE u.userType = 'A'")
    List<User> findAdminUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType IN ('U', 'R')")
    List<User> findRegularUsers();
    
    @Query("SELECT u FROM User u WHERE u.authenticated = true")
    List<User> findAuthenticatedUsers();
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = :userType")
    long countByUserType(@Param("userType") String userType);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.authenticated = true")
    long countAuthenticatedUsers();
    
    void deleteByUserId(String userId);
}
