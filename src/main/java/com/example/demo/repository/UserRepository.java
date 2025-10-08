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
    
    List<User> findByUserType(String userType);
    
    @Query("SELECT u FROM User u WHERE u.userType = 'A'")
    Page<User> findAdminUsers(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = 'R'")
    Page<User> findRegularUsers(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT(:firstName, '%'))")
    Page<User> findByFirstNameStartingWith(@Param("firstName") String firstName, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.lastName) LIKE LOWER(CONCAT(:lastName, '%'))")
    Page<User> findByLastNameStartingWith(@Param("lastName") String lastName, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    Page<User> findByFullNameContaining(@Param("fullName") String fullName, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.password = :password")
    Optional<User> findByUserIdAndPassword(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT u FROM User u ORDER BY u.userId")
    Page<User> findAllOrderedByUserId(Pageable pageable);
    
    @Query("SELECT u FROM User u ORDER BY u.lastName, u.firstName")
    Page<User> findAllOrderedByName(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType ORDER BY u.userId")
    Page<User> findByUserTypeOrderedByUserId(@Param("userType") String userType, Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'A'")
    long countAdminUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'R'")
    long countRegularUsers();
    
    long countByUserType(String userType);
    
    @Query("SELECT u FROM User u WHERE u.userId > :startUserId ORDER BY u.userId")
    Page<User> findUsersAfterUserId(@Param("startUserId") String startUserId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userId < :endUserId ORDER BY u.userId DESC")
    Page<User> findUsersBeforeUserId(@Param("endUserId") String endUserId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userId BETWEEN :startUserId AND :endUserId ORDER BY u.userId")
    Page<User> findUsersBetweenUserIds(@Param("startUserId") String startUserId, @Param("endUserId") String endUserId, Pageable pageable);
}