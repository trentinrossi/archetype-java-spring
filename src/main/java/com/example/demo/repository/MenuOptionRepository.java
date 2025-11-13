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
    
    boolean existsByOptionNumber(Integer optionNumber);
    
    List<MenuOption> findByUserTypeRequired(String userTypeRequired);
    
    Page<MenuOption> findByUserTypeRequired(String userTypeRequired, Pageable pageable);
    
    List<MenuOption> findAllByOrderByOptionNumberAsc();
    
    @Query("SELECT m FROM MenuOption m WHERE m.userTypeRequired = 'A'")
    List<MenuOption> findAdminOnlyOptions();
    
    @Query("SELECT m FROM MenuOption m WHERE m.userTypeRequired IN ('U', 'R')")
    List<MenuOption> findUserAccessibleOptions();
    
    @Query("SELECT m FROM MenuOption m WHERE m.userTypeRequired = :userType OR m.userTypeRequired = 'U' ORDER BY m.optionNumber ASC")
    List<MenuOption> findAccessibleOptionsForUserType(@Param("userType") String userType);
    
    @Query("SELECT COUNT(m) FROM MenuOption m WHERE m.userTypeRequired = :userType")
    long countByUserTypeRequired(@Param("userType") String userType);
    
    Optional<MenuOption> findByProgramName(String programName);
    
    boolean existsByProgramName(String programName);
}
