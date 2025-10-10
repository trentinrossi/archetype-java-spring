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
    
    Page<User> findByUserType(String userType, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE " +
           "(:userId IS NULL OR LOWER(u.userId) LIKE LOWER(CONCAT(:userId, '%'))) AND " +
           "(:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:userType IS NULL OR u.userType = :userType)")
    Page<User> findUsersWithFilters(@Param("userId") String userId,
                                   @Param("firstName") String firstName,
                                   @Param("lastName") String lastName,
                                   @Param("userType") String userType,
                                   Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.userId) LIKE LOWER(CONCAT(:userIdPrefix, '%'))")
    Page<User> findByUserIdStartingWithIgnoreCase(@Param("userIdPrefix") String userIdPrefix, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    Page<User> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    Page<User> findByLastNameContainingIgnoreCase(@Param("lastName") String lastName, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.userId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByNameOrUserIdContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    List<User> findByUserTypeOrderByUserIdAsc(String userType);
    
    List<User> findByUserTypeOrderByFirstNameAsc(String userType);
    
    List<User> findByUserTypeOrderByLastNameAsc(String userType);
    
    long countByUserType(String userType);
    
    @Query("SELECT COUNT(u) FROM User u WHERE " +
           "(:userId IS NULL OR LOWER(u.userId) LIKE LOWER(CONCAT(:userId, '%'))) AND " +
           "(:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:userType IS NULL OR u.userType = :userType)")
    long countUsersWithFilters(@Param("userId") String userId,
                              @Param("firstName") String firstName,
                              @Param("lastName") String lastName,
                              @Param("userType") String userType);
    
    void deleteByUserId(String userId);
}