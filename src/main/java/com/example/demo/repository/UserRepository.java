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
    
    @Query("SELECT u FROM User u WHERE UPPER(u.userId) LIKE UPPER(CONCAT(:userIdPrefix, '%')) ORDER BY u.userId")
    Page<User> findByUserIdStartingWithIgnoreCase(@Param("userIdPrefix") String userIdPrefix, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE UPPER(u.userId) >= UPPER(:startUserId) ORDER BY u.userId")
    Page<User> findByUserIdGreaterThanEqualIgnoreCase(@Param("startUserId") String startUserId, Pageable pageable);
    
    @Query("SELECT u FROM User u ORDER BY u.userId")
    Page<User> findAllOrderByUserId(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE UPPER(u.userId) LIKE UPPER(CONCAT('%', :searchTerm, '%')) " +
           "OR UPPER(u.firstName) LIKE UPPER(CONCAT('%', :searchTerm, '%')) " +
           "OR UPPER(u.lastName) LIKE UPPER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY u.userId")
    Page<User> findBySearchTermIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType ORDER BY u.userId")
    Page<User> findByUserTypeOrderByUserId(@Param("userType") String userType, Pageable pageable);
    
    @Query(value = "SELECT * FROM usrsec WHERE UPPER(sec_usr_id) >= UPPER(?1) ORDER BY sec_usr_id LIMIT ?2", 
           nativeQuery = true)
    List<User> findUsersStartingFromUserIdWithLimit(String startUserId, int limit);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = :userType")
    long countByUserType(@Param("userType") String userType);
    
    @Query("SELECT u FROM User u WHERE UPPER(u.userId) LIKE UPPER(CONCAT(:prefix, '%')) " +
           "AND UPPER(u.userId) >= UPPER(:startUserId) ORDER BY u.userId")
    Page<User> findByUserIdPrefixAndStartingFromIgnoreCase(@Param("prefix") String prefix, 
                                                          @Param("startUserId") String startUserId, 
                                                          Pageable pageable);
}