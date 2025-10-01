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
    
    Optional<User> findById(String id);
    
    boolean existsById(String id);
    
    Page<User> findByUserType(String userType, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.id >= :startId ORDER BY u.id")
    Page<User> findUsersStartingFromId(@Param("startId") String startId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.id >= :startId AND u.userType = :userType ORDER BY u.id")
    Page<User> findUsersStartingFromIdByUserType(@Param("startId") String startId, @Param("userType") String userType, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.id) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByNameOrIdContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.id > :lastId ORDER BY u.id")
    List<User> findNext10UsersAfterId(@Param("lastId") String lastId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.id < :firstId ORDER BY u.id DESC")
    List<User> findPrevious10UsersBeforeId(@Param("firstId") String firstId, Pageable pageable);
    
    long countByUserType(String userType);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.id >= :startId")
    long countUsersStartingFromId(@Param("startId") String startId);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.id >= :startId AND u.userType = :userType")
    long countUsersStartingFromIdByUserType(@Param("startId") String startId, @Param("userType") String userType);
    
    List<User> findTop10ByOrderByIdAsc();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'A' ORDER BY u.id")
    List<User> findAllAdminUsers();
    
    @Query("SELECT u FROM User u WHERE u.userType = 'U' ORDER BY u.id")
    List<User> findAllRegularUsers();
}