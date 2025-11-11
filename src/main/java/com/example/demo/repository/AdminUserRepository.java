package com.example.demo.repository;

import com.example.demo.entity.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, String> {
    
    Optional<AdminUser> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    List<AdminUser> findByAuthenticationStatus(Boolean authenticationStatus);
    
    Page<AdminUser> findByAuthenticationStatus(Boolean authenticationStatus, Pageable pageable);
    
    @Query("SELECT a FROM AdminUser a WHERE a.authenticationStatus = true")
    List<AdminUser> findAuthenticatedUsers();
    
    @Query("SELECT a FROM AdminUser a WHERE a.authenticationStatus = false")
    List<AdminUser> findUnauthenticatedUsers();
    
    long countByAuthenticationStatus(Boolean authenticationStatus);
    
    @Query("SELECT COUNT(a) FROM AdminUser a WHERE a.authenticationStatus = true")
    long countAuthenticatedUsers();
    
    @Query("SELECT a FROM AdminUser a WHERE a.userId = :userId AND a.authenticationStatus = true")
    Optional<AdminUser> findAuthenticatedUserById(@Param("userId") String userId);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AdminUser a WHERE a.userId = :userId AND a.authenticationStatus = true")
    boolean isUserAuthenticated(@Param("userId") String userId);
}
