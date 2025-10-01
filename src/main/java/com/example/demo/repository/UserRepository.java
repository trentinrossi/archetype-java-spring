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
    
    Optional<User> findBySecUsrId(String secUsrId);
    
    boolean existsBySecUsrId(String secUsrId);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId >= :startUsrId ORDER BY u.secUsrId ASC")
    Page<User> findUsersFromStartingId(@Param("startUsrId") String startUsrId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId >= :startUsrId ORDER BY u.secUsrId ASC")
    List<User> findUsersFromStartingId(@Param("startUsrId") String startUsrId);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId <= :endUsrId ORDER BY u.secUsrId DESC")
    List<User> findUsersBeforeId(@Param("endUsrId") String endUsrId);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId LIKE :pattern ORDER BY u.secUsrId ASC")
    Page<User> findBySecUsrIdPattern(@Param("pattern") String pattern, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId LIKE :pattern ORDER BY u.secUsrId ASC")
    List<User> findBySecUsrIdPattern(@Param("pattern") String pattern);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.secUsrFname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.secUsrLname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.secUsrId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY u.secUsrId ASC")
    Page<User> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrType = :userType ORDER BY u.secUsrId ASC")
    Page<User> findBySecUsrType(@Param("userType") String userType, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId > :currentUsrId ORDER BY u.secUsrId ASC")
    List<User> findNextUsers(@Param("currentUsrId") String currentUsrId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId < :currentUsrId ORDER BY u.secUsrId DESC")
    List<User> findPreviousUsers(@Param("currentUsrId") String currentUsrId, Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.secUsrType = :userType")
    long countBySecUsrType(@Param("userType") String userType);
    
    @Query("SELECT u FROM User u ORDER BY u.secUsrId ASC")
    List<User> findFirst10Users(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId >= :startUsrId AND u.secUsrId <= :endUsrId ORDER BY u.secUsrId ASC")
    List<User> findUsersInRange(@Param("startUsrId") String startUsrId, @Param("endUsrId") String endUsrId);
    
    @Query("SELECT MIN(u.secUsrId) FROM User u")
    Optional<String> findFirstUserId();
    
    @Query("SELECT MAX(u.secUsrId) FROM User u")
    Optional<String> findLastUserId();
}