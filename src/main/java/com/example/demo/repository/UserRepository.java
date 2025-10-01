package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    Page<User> findByUserType(String userType, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userId LIKE CONCAT(:prefix, '%')")
    Page<User> findByUserIdStartingWith(@Param("prefix") String prefix, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :since")
    List<User> findRecentUsers(@Param("since") LocalDateTime since);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND u.createdAt >= :since")
    List<User> findByUserTypeAndCreatedAtAfter(@Param("userType") String userType, @Param("since") LocalDateTime since);
    
    long countByUserType(String userType);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :since")
    long countUsersCreatedSince(@Param("since") LocalDateTime since);
    
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
    
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    Page<User> findAllOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType IN :userTypes")
    Page<User> findByUserTypeIn(@Param("userTypes") List<String> userTypes, Pageable pageable);
    
    void deleteByUserId(String userId);
}