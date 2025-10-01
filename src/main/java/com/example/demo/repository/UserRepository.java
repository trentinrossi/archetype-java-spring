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
    
    Optional<User> findBySecUsrId(String secUsrId);
    
    boolean existsBySecUsrId(String secUsrId);
    
    Page<User> findBySecUsrType(String secUsrType, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.secUsrFname) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    Page<User> findBySecUsrFnameContaining(@Param("firstName") String firstName, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.secUsrLname) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    Page<User> findBySecUsrLnameContaining(@Param("lastName") String lastName, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE " +
           "(:secUsrId IS NULL OR u.secUsrId = :secUsrId) AND " +
           "(:secUsrFname IS NULL OR LOWER(u.secUsrFname) LIKE LOWER(CONCAT('%', :secUsrFname, '%'))) AND " +
           "(:secUsrLname IS NULL OR LOWER(u.secUsrLname) LIKE LOWER(CONCAT('%', :secUsrLname, '%'))) AND " +
           "(:secUsrType IS NULL OR u.secUsrType = :secUsrType)")
    Page<User> findUsersWithFilters(@Param("secUsrId") String secUsrId,
                                   @Param("secUsrFname") String secUsrFname,
                                   @Param("secUsrLname") String secUsrLname,
                                   @Param("secUsrType") String secUsrType,
                                   Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId >= :startUserId ORDER BY u.secUsrId")
    Page<User> findUsersStartingFromId(@Param("startUserId") String startUserId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE " +
           "u.secUsrId >= :startUserId AND " +
           "(:secUsrFname IS NULL OR LOWER(u.secUsrFname) LIKE LOWER(CONCAT('%', :secUsrFname, '%'))) AND " +
           "(:secUsrLname IS NULL OR LOWER(u.secUsrLname) LIKE LOWER(CONCAT('%', :secUsrLname, '%'))) AND " +
           "(:secUsrType IS NULL OR u.secUsrType = :secUsrType) " +
           "ORDER BY u.secUsrId")
    Page<User> findUsersStartingFromIdWithFilters(@Param("startUserId") String startUserId,
                                                 @Param("secUsrFname") String secUsrFname,
                                                 @Param("secUsrLname") String secUsrLname,
                                                 @Param("secUsrType") String secUsrType,
                                                 Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.secUsrFname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.secUsrLname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.secUsrId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE " +
           "u.secUsrId >= :startUserId AND " +
           "(LOWER(u.secUsrFname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.secUsrLname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.secUsrId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY u.secUsrId")
    Page<User> findBySearchTermStartingFromId(@Param("startUserId") String startUserId,
                                             @Param("searchTerm") String searchTerm,
                                             Pageable pageable);
    
    long countBySecUsrType(String secUsrType);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.secUsrId >= :startUserId")
    long countUsersStartingFromId(@Param("startUserId") String startUserId);
    
    List<User> findTop10ByOrderBySecUsrIdAsc();
    
    @Query("SELECT u FROM User u WHERE u.secUsrId >= :startUserId ORDER BY u.secUsrId")
    List<User> findTop10UsersStartingFromId(@Param("startUserId") String startUserId, Pageable pageable);
}