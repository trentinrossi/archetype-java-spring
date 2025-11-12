package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    
    Optional<Customer> findByCustomerId(String customerId);
    
    boolean existsByCustomerId(String customerId);
    
    Optional<Customer> findBySsn(String ssn);
    
    List<Customer> findByLastName(String lastName);
    
    List<Customer> findByPrimaryCardHolderIndicator(String indicator);
    
    @Query("SELECT c FROM Customer c WHERE c.ficoCreditScore >= :minScore")
    List<Customer> findByFicoCreditScoreGreaterThanOrEqual(@Param("minScore") Integer minScore);
    
    @Query("SELECT c FROM Customer c WHERE c.stateCode = :stateCode")
    List<Customer> findByStateCode(@Param("stateCode") String stateCode);
    
    @Query("SELECT c FROM Customer c WHERE c.city = :city")
    List<Customer> findByCity(@Param("city") String city);
}
