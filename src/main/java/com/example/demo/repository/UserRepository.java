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
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    Optional<User> findByUserIdAndPassword(String userId, String password);
    
    List<User> findByUserType(String userType);
    
    Page<User> findByUserType(String userType, Pageable pageable);
    
    List<User> findByAuthenticated(Boolean authenticated);
    
    Page<User> findByAuthenticated(Boolean authenticated, Pageable pageable);
    
    List<User> findByUserTypeAndAuthenticated(String userType, Boolean authenticated);
    
    Page<User> findByUserTypeAndAuthenticated(String userType, Boolean authenticated, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> findByNameContaining(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.password = :password")
    Optional<User> authenticateUser(@Param("userId") String userId, @Param("password") String password);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.userId) = LOWER(:userId)")
    Optional<User> findByUserIdIgnoreCase(@Param("userId") String userId);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE LOWER(u.userId) = LOWER(:userId)")
    boolean existsByUserIdIgnoreCase(@Param("userId") String userId);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND u.authenticated = true")
    List<User> findAuthenticatedUsersByType(@Param("userType") String userType);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND u.authenticated = true")
    Page<User> findAuthenticatedUsersByType(@Param("userType") String userType, Pageable pageable);
    
    long countByUserType(String userType);
    
    long countByAuthenticated(Boolean authenticated);
    
    long countByUserTypeAndAuthenticated(String userType, Boolean authenticated);
    
    @Query("SELECT u FROM User u WHERE " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND u.userType = :userType")
    Page<User> searchUsersByTypeAndName(@Param("searchTerm") String searchTerm, 
                                         @Param("userType") String userType, 
                                         Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
    
    List<User> findByFirstNameContainingIgnoreCase(String firstName);
    
    List<User> findByLastNameContainingIgnoreCase(String lastName);
    
    Page<User> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);
    
    Page<User> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) " +
           "AND LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<User> findByFirstNameAndLastNameContaining(@Param("firstName") String firstName, 
                                                     @Param("lastName") String lastName);
    
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) " +
           "AND LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    Page<User> findByFirstNameAndLastNameContaining(@Param("firstName") String firstName, 
                                                     @Param("lastName") String lastName, 
                                                     Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'A'")
    long countAdminUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType != 'A'")
    long countRegularUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'A'")
    List<User> findAllAdminUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'A'")
    Page<User> findAllAdminUsers(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType != 'A'")
    List<User> findAllRegularUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType != 'A'")
    Page<User> findAllRegularUsers(Pageable pageable);
}
