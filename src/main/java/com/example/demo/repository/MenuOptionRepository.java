package com.example.demo.repository;

import com.example.demo.entity.MenuOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {
    
    Optional<MenuOption> findByOptionNumber(Integer optionNumber);
    
    List<MenuOption> findByUserTypeRequired(String userTypeRequired);
    
    Page<MenuOption> findByUserTypeRequired(String userTypeRequired, Pageable pageable);
    
    List<MenuOption> findByIsActive(Boolean isActive);
    
    Page<MenuOption> findByIsActive(Boolean isActive, Pageable pageable);
    
    List<MenuOption> findByUserTypeRequiredAndIsActive(String userTypeRequired, Boolean isActive);
    
    Page<MenuOption> findByUserTypeRequiredAndIsActive(String userTypeRequired, Boolean isActive, Pageable pageable);
    
    @Query("SELECT mo FROM MenuOption mo WHERE mo.isActive = true ORDER BY mo.displayOrder ASC")
    List<MenuOption> findAllActiveOrderedByDisplay();
    
    @Query("SELECT mo FROM MenuOption mo WHERE mo.isActive = true ORDER BY mo.displayOrder ASC")
    Page<MenuOption> findAllActiveOrderedByDisplay(Pageable pageable);
    
    @Query("SELECT mo FROM MenuOption mo WHERE mo.userTypeRequired = :userType AND mo.isActive = true ORDER BY mo.displayOrder ASC")
    List<MenuOption> findActiveByUserTypeOrdered(@Param("userType") String userType);
    
    @Query("SELECT mo FROM MenuOption mo WHERE mo.userTypeRequired = :userType AND mo.isActive = true ORDER BY mo.displayOrder ASC")
    Page<MenuOption> findActiveByUserTypeOrdered(@Param("userType") String userType, Pageable pageable);
    
    @Query("SELECT mo FROM MenuOption mo WHERE mo.programName LIKE 'DUMMY%'")
    List<MenuOption> findComingSoonOptions();
    
    @Query("SELECT mo FROM MenuOption mo WHERE mo.programName NOT LIKE 'DUMMY%' AND mo.isActive = true")
    List<MenuOption> findImplementedOptions();
    
    @Query("SELECT COUNT(mo) FROM MenuOption mo WHERE mo.userTypeRequired = :userType AND mo.isActive = true")
    long countActiveByUserType(@Param("userType") String userType);
    
    @Query("SELECT COUNT(mo) FROM MenuOption mo WHERE mo.isActive = true")
    long countActive();
    
    @Query("SELECT COUNT(mo) FROM MenuOption mo WHERE mo.programName LIKE 'DUMMY%'")
    long countComingSoon();
    
    @Query("SELECT mo FROM MenuOption mo WHERE mo.optionNumber = :optionNumber AND mo.isActive = true")
    Optional<MenuOption> findActiveByOptionNumber(@Param("optionNumber") Integer optionNumber);
    
    @Query("SELECT mo FROM MenuOption mo WHERE mo.optionNumber BETWEEN :start AND :end ORDER BY mo.displayOrder ASC")
    List<MenuOption> findByOptionNumberRange(@Param("start") Integer start, @Param("end") Integer end);
    
    @Query("SELECT mo FROM MenuOption mo WHERE LOWER(mo.optionName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<MenuOption> searchByOptionName(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT mo FROM MenuOption mo WHERE LOWER(mo.optionName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<MenuOption> searchByOptionName(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    boolean existsByOptionNumber(Integer optionNumber);
    
    boolean existsByProgramName(String programName);
    
    List<MenuOption> findByProgramName(String programName);
    
    @Query("SELECT DISTINCT mo.userTypeRequired FROM MenuOption mo WHERE mo.isActive = true")
    List<String> findDistinctActiveUserTypes();
    
    @Query("SELECT MAX(mo.optionNumber) FROM MenuOption mo")
    Integer findMaxOptionNumber();
    
    @Query("SELECT MAX(mo.displayOrder) FROM MenuOption mo")
    Integer findMaxDisplayOrder();
}
