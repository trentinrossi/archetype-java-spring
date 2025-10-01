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
    
    Page<User> findBySecUsrIdStartingWith(String prefix, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId LIKE CONCAT(:prefix, '%') ORDER BY u.secUsrId")
    List<User> findBySecUsrIdStartingWithOrderBySecUsrId(@Param("prefix") String prefix);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId >= :startUsrId ORDER BY u.secUsrId")
    Page<User> findUsersStartingFromId(@Param("startUsrId") String startUsrId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId <= :endUsrId ORDER BY u.secUsrId DESC")
    Page<User> findUsersEndingAtId(@Param("endUsrId") String endUsrId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId > :afterUsrId ORDER BY u.secUsrId")
    Page<User> findUsersAfter(@Param("afterUsrId") String afterUsrId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId < :beforeUsrId ORDER BY u.secUsrId DESC")
    Page<User> findUsersBefore(@Param("beforeUsrId") String beforeUsrId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId LIKE CONCAT(:prefix, '%') AND u.secUsrId >= :startUsrId ORDER BY u.secUsrId")
    Page<User> findByPrefixStartingFromId(@Param("prefix") String prefix, @Param("startUsrId") String startUsrId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId LIKE CONCAT(:prefix, '%') AND u.secUsrId > :afterUsrId ORDER BY u.secUsrId")
    Page<User> findByPrefixAfter(@Param("prefix") String prefix, @Param("afterUsrId") String afterUsrId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.secUsrId LIKE CONCAT(:prefix, '%') AND u.secUsrId < :beforeUsrId ORDER BY u.secUsrId DESC")
    Page<User> findByPrefixBefore(@Param("prefix") String prefix, @Param("beforeUsrId") String beforeUsrId, Pageable pageable);
    
    long countBySecUsrIdStartingWith(String prefix);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.secUsrId >= :startUsrId")
    long countUsersStartingFromId(@Param("startUsrId") String startUsrId);
}