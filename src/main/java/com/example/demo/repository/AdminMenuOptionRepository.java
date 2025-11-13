package com.example.demo.repository;

import com.example.demo.entity.AdminMenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminMenuOptionRepository extends JpaRepository<AdminMenuOption, Long> {
    
    Optional<AdminMenuOption> findByOptionNumber(Integer optionNumber);
    
    List<AdminMenuOption> findByIsActive(Boolean isActive);
    
    @Query("SELECT amo FROM AdminMenuOption amo WHERE amo.isActive = true ORDER BY amo.displayOrder ASC")
    List<AdminMenuOption> findAllActiveOrderedByDisplay();
    
    @Query("SELECT amo FROM AdminMenuOption amo WHERE amo.programName LIKE 'DUMMY%'")
    List<AdminMenuOption> findComingSoonOptions();
    
    boolean existsByOptionNumber(Integer optionNumber);
}
