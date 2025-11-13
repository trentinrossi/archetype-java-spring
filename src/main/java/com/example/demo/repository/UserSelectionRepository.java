package com.example.demo.repository;

import com.example.demo.entity.UserSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSelectionRepository extends JpaRepository<UserSelection, Long> {
    
    Optional<UserSelection> findBySelectedUserId(String selectedUserId);
    
    List<UserSelection> findBySelectionFlag(String selectionFlag);
    
    boolean existsBySelectedUserId(String selectedUserId);
    
    @Query("SELECT u FROM UserSelection u WHERE u.selectionFlag = 'U'")
    List<UserSelection> findUpdateSelections();
    
    @Query("SELECT u FROM UserSelection u WHERE u.selectionFlag = 'D'")
    List<UserSelection> findDeleteSelections();
    
    @Query("SELECT COUNT(u) FROM UserSelection u WHERE u.selectionFlag = :flag")
    long countBySelectionFlag(@Param("flag") String flag);
    
    void deleteBySelectedUserId(String selectedUserId);
}
