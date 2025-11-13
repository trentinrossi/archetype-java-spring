package com.example.demo.repository;

import com.example.demo.entity.UserListPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserListPageRepository extends JpaRepository<UserListPage, Long> {
    
    Optional<UserListPage> findByPageNumber(Long pageNumber);
    
    boolean existsByPageNumber(Long pageNumber);
    
    List<UserListPage> findByHasNextPageTrue();
    
    List<UserListPage> findAllByOrderByPageNumberAsc();
    
    @Query("SELECT u FROM UserListPage u WHERE u.pageNumber = :pageNumber")
    Optional<UserListPage> findPageByNumber(@Param("pageNumber") Long pageNumber);
    
    @Query("SELECT MAX(u.pageNumber) FROM UserListPage u")
    Long findMaxPageNumber();
    
    @Query("SELECT COUNT(u) FROM UserListPage u WHERE u.hasNextPage = true")
    long countPagesWithNext();
    
    void deleteByPageNumber(Long pageNumber);
}
