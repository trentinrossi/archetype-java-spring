package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByUserIdAndPassword(String userId, String password);
    
    boolean existsByUserId(String userId);
    
    Page<User> findByUserIdGreaterThanEqual(String userId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userId >= :startUserId ORDER BY u.userId")
    Page<User> findUsersStartingFrom(@Param("startUserId") String startUserId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType ORDER BY u.userId")
    Page<User> findByUserType(@Param("userType") String userType, Pageable pageable);
}
