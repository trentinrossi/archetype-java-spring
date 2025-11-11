package com.example.demo.repository;

import com.example.demo.entity.AdminMenuOption;
import com.example.demo.entity.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminMenuOptionRepository extends JpaRepository<AdminMenuOption, Long> {
    
    Optional<AdminMenuOption> findByOptionNumber(Integer optionNumber);
    
    boolean existsByOptionNumber(Integer optionNumber);
    
    List<AdminMenuOption> findByProgramName(String programName);
    
    boolean existsByProgramName(String programName);
    
    List<AdminMenuOption> findByIsActive(Boolean isActive);
    
    Page<AdminMenuOption> findByIsActive(Boolean isActive, Pageable pageable);
    
    List<AdminMenuOption> findByAdminUser(AdminUser adminUser);
    
    Page<AdminMenuOption> findByAdminUser(AdminUser adminUser, Pageable pageable);
    
    List<AdminMenuOption> findByAdminUserAndIsActive(AdminUser adminUser, Boolean isActive);
    
    Page<AdminMenuOption> findByAdminUserAndIsActive(AdminUser adminUser, Boolean isActive, Pageable pageable);
    
    List<AdminMenuOption> findByIsActiveTrueOrderByOptionNumberAsc();
    
    List<AdminMenuOption> findByIsActiveFalseOrderByOptionNumberAsc();
    
    List<AdminMenuOption> findAllByOrderByOptionNumberAsc();
    
    Page<AdminMenuOption> findAllByOrderByOptionNumberAsc(Pageable pageable);
    
    long countByIsActive(Boolean isActive);
    
    long countByAdminUser(AdminUser adminUser);
    
    long countByAdminUserAndIsActive(AdminUser adminUser, Boolean isActive);
    
    @Query("SELECT a FROM AdminMenuOption a WHERE a.isActive = true ORDER BY a.optionNumber ASC")
    List<AdminMenuOption> findAllActiveOptions();
    
    @Query("SELECT a FROM AdminMenuOption a WHERE a.isActive = true ORDER BY a.optionNumber ASC")
    Page<AdminMenuOption> findAllActiveOptions(Pageable pageable);
    
    @Query("SELECT a FROM AdminMenuOption a WHERE a.isActive = false ORDER BY a.optionNumber ASC")
    List<AdminMenuOption> findAllInactiveOptions();
    
    @Query("SELECT a FROM AdminMenuOption a WHERE LOWER(a.optionName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY a.optionNumber ASC")
    List<AdminMenuOption> findByOptionNameContaining(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT a FROM AdminMenuOption a WHERE LOWER(a.optionName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY a.optionNumber ASC")
    Page<AdminMenuOption> findByOptionNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT a FROM AdminMenuOption a WHERE a.isActive = :isActive AND LOWER(a.optionName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY a.optionNumber ASC")
    List<AdminMenuOption> findByIsActiveAndOptionNameContaining(@Param("isActive") Boolean isActive, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT a FROM AdminMenuOption a WHERE a.optionNumber BETWEEN :startNumber AND :endNumber ORDER BY a.optionNumber ASC")
    List<AdminMenuOption> findByOptionNumberRange(@Param("startNumber") Integer startNumber, @Param("endNumber") Integer endNumber);
    
    @Query("SELECT COUNT(a) FROM AdminMenuOption a WHERE a.isActive = true")
    long countActiveOptions();
    
    @Query("SELECT COUNT(a) FROM AdminMenuOption a WHERE a.isActive = false")
    long countInactiveOptions();
    
    boolean existsByOptionNumberAndIsActive(Integer optionNumber, Boolean isActive);
    
    Optional<AdminMenuOption> findByOptionNumberAndIsActive(Integer optionNumber, Boolean isActive);
}
