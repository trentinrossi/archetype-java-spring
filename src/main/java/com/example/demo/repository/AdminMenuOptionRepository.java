package com.example.demo.repository;

import com.example.demo.entity.AdminMenuOption;
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
    
    List<AdminMenuOption> findByIsActiveTrue();
    
    List<AdminMenuOption> findByIsActiveFalse();
    
    Page<AdminMenuOption> findByIsActive(Boolean isActive, Pageable pageable);
    
    Optional<AdminMenuOption> findByProgramName(String programName);
    
    List<AdminMenuOption> findAllByOrderByOptionNumberAsc();
    
    List<AdminMenuOption> findByIsActiveTrueOrderByOptionNumberAsc();
    
    boolean existsByProgramName(String programName);
    
    @Query("SELECT a FROM AdminMenuOption a WHERE a.isActive = true ORDER BY a.optionNumber ASC")
    List<AdminMenuOption> findActiveMenuOptionsOrdered();
    
    @Query("SELECT a FROM AdminMenuOption a WHERE LOWER(a.optionName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<AdminMenuOption> findByOptionNameContaining(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(a) FROM AdminMenuOption a WHERE a.isActive = true")
    long countActiveOptions();
    
    @Query("SELECT COUNT(a) FROM AdminMenuOption a WHERE a.isActive = false")
    long countInactiveOptions();
    
    @Query("SELECT a FROM AdminMenuOption a WHERE a.optionNumber BETWEEN :startNumber AND :endNumber ORDER BY a.optionNumber ASC")
    List<AdminMenuOption> findByOptionNumberRange(@Param("startNumber") Integer startNumber, @Param("endNumber") Integer endNumber);
}
