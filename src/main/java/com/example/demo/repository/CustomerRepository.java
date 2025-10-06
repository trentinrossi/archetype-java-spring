package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    
    // Basic finder methods
    Optional<Customer> findByCustomerId(String customerId);
    
    boolean existsByCustomerId(String customerId);
    
    Page<Customer> findByCustomerStatus(String customerStatus, Pageable pageable);
    
    Page<Customer> findByCustomerType(String customerType, Pageable pageable);
    
    // Sequential reading methods based on CBCUS01C functionality
    @Query("SELECT c FROM Customer c WHERE c.customerId >= :startingCustomerId ORDER BY c.customerId ASC")
    List<Customer> findCustomersSequentiallyFromStarting(@Param("startingCustomerId") String startingCustomerId, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId >= :startingCustomerId AND c.customerStatus = :status ORDER BY c.customerId ASC")
    List<Customer> findCustomersSequentiallyByStatus(@Param("startingCustomerId") String startingCustomerId, @Param("status") String status, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId >= :startingCustomerId AND c.customerType = :customerType ORDER BY c.customerId ASC")
    List<Customer> findCustomersSequentiallyByType(@Param("startingCustomerId") String startingCustomerId, @Param("customerType") String customerType, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId >= :startingCustomerId AND c.customerStatus = :status AND c.customerType = :customerType ORDER BY c.customerId ASC")
    List<Customer> findCustomersSequentiallyByStatusAndType(@Param("startingCustomerId") String startingCustomerId, @Param("status") String status, @Param("customerType") String customerType, Pageable pageable);
    
    // Name-based searches
    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    Page<Customer> findByFirstNameContaining(@Param("firstName") String firstName, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    Page<Customer> findByLastNameContaining(@Param("lastName") String lastName, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) AND LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    Page<Customer> findByFirstNameAndLastNameContaining(@Param("firstName") String firstName, @Param("lastName") String lastName, Pageable pageable);
    
    // Contact information searches
    @Query("SELECT c FROM Customer c WHERE c.emailAddress = :email")
    Optional<Customer> findByEmailAddress(@Param("email") String email);
    
    @Query("SELECT c FROM Customer c WHERE c.phoneNumber = :phone")
    List<Customer> findByPhoneNumber(@Param("phone") String phone);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.emailAddress) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<Customer> findByEmailAddressContaining(@Param("email") String email, Pageable pageable);
    
    // Address-based searches
    @Query("SELECT c FROM Customer c WHERE LOWER(c.city) = LOWER(:city)")
    Page<Customer> findByCity(@Param("city") String city, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.state) = LOWER(:state)")
    Page<Customer> findByState(@Param("state") String state, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.zipCode = :zipCode")
    Page<Customer> findByZipCode(@Param("zipCode") String zipCode, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.city) = LOWER(:city) AND LOWER(c.state) = LOWER(:state)")
    Page<Customer> findByCityAndState(@Param("city") String city, @Param("state") String state, Pageable pageable);
    
    // Status-based queries
    @Query("SELECT c FROM Customer c WHERE c.customerStatus IN :statuses")
    Page<Customer> findByCustomerStatusIn(@Param("statuses") List<String> statuses, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerType IN :types")
    Page<Customer> findByCustomerTypeIn(@Param("types") List<String> types, Pageable pageable);
    
    // Active customers
    @Query("SELECT c FROM Customer c WHERE c.customerStatus IN ('A', 'Y')")
    Page<Customer> findActiveCustomers(Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerStatus NOT IN ('A', 'Y')")
    Page<Customer> findInactiveCustomers(Pageable pageable);
    
    // Credit rating queries
    @Query("SELECT c FROM Customer c WHERE c.creditRating = :rating")
    Page<Customer> findByCreditRating(@Param("rating") String rating, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.creditRating IN ('AAA', 'AA', 'A')")
    Page<Customer> findGoodCreditCustomers(Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.creditRating IN ('D', 'F')")
    Page<Customer> findPoorCreditCustomers(Pageable pageable);
    
    // Personal vs Business customers
    @Query("SELECT c FROM Customer c WHERE c.customerType IN ('P', '01')")
    Page<Customer> findPersonalCustomers(Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerType IN ('B', '02')")
    Page<Customer> findBusinessCustomers(Pageable pageable);
    
    // Date of birth queries
    @Query("SELECT c FROM Customer c WHERE c.dateOfBirth = :dob")
    List<Customer> findByDateOfBirth(@Param("dob") String dob);
    
    @Query("SELECT c FROM Customer c WHERE c.dateOfBirth >= :fromDate AND c.dateOfBirth <= :toDate")
    List<Customer> findByDateOfBirthBetween(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
    
    // Employer-based queries
    @Query("SELECT c FROM Customer c WHERE LOWER(c.employerName) LIKE LOWER(CONCAT('%', :employer, '%'))")
    Page<Customer> findByEmployerNameContaining(@Param("employer") String employer, Pageable pageable);
    
    // Income-based queries
    @Query("SELECT c FROM Customer c WHERE CAST(c.annualIncome AS DOUBLE) >= :minIncome")
    Page<Customer> findByMinimumIncome(@Param("minIncome") Double minIncome, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE CAST(c.annualIncome AS DOUBLE) >= :minIncome AND CAST(c.annualIncome AS DOUBLE) <= :maxIncome")
    Page<Customer> findByIncomeRange(@Param("minIncome") Double minIncome, @Param("maxIncome") Double maxIncome, Pageable pageable);
    
    // Recent customers
    @Query("SELECT c FROM Customer c WHERE c.createdAt >= :since")
    List<Customer> findRecentCustomers(@Param("since") LocalDateTime since);
    
    @Query("SELECT c FROM Customer c WHERE c.updatedAt >= :since")
    List<Customer> findRecentlyUpdatedCustomers(@Param("since") LocalDateTime since);
    
    // Count methods
    long countByCustomerStatus(String customerStatus);
    
    long countByCustomerType(String customerType);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.customerStatus = :status AND c.customerType = :type")
    long countByCustomerStatusAndType(@Param("status") String status, @Param("type") String type);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.customerId >= :startingCustomerId")
    long countCustomersFromStarting(@Param("startingCustomerId") String startingCustomerId);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.customerId >= :startingCustomerId AND c.customerStatus = :status")
    long countCustomersFromStartingByStatus(@Param("startingCustomerId") String startingCustomerId, @Param("status") String status);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.customerId >= :startingCustomerId AND c.customerType = :type")
    long countCustomersFromStartingByType(@Param("startingCustomerId") String startingCustomerId, @Param("type") String type);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE LOWER(c.city) = LOWER(:city)")
    long countByCity(@Param("city") String city);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE LOWER(c.state) = LOWER(:state)")
    long countByState(@Param("state") String state);
    
    // Distinct values
    @Query("SELECT DISTINCT c.customerStatus FROM Customer c ORDER BY c.customerStatus")
    List<String> findDistinctCustomerStatuses();
    
    @Query("SELECT DISTINCT c.customerType FROM Customer c WHERE c.customerType IS NOT NULL ORDER BY c.customerType")
    List<String> findDistinctCustomerTypes();
    
    @Query("SELECT DISTINCT c.creditRating FROM Customer c WHERE c.creditRating IS NOT NULL ORDER BY c.creditRating")
    List<String> findDistinctCreditRatings();
    
    @Query("SELECT DISTINCT c.state FROM Customer c WHERE c.state IS NOT NULL ORDER BY c.state")
    List<String> findDistinctStates();
    
    @Query("SELECT DISTINCT c.city FROM Customer c WHERE c.city IS NOT NULL ORDER BY c.city")
    List<String> findDistinctCities();
    
    // Navigation methods
    @Query("SELECT MAX(c.customerId) FROM Customer c")
    Optional<String> findMaxCustomerId();
    
    @Query("SELECT MIN(c.customerId) FROM Customer c")
    Optional<String> findMinCustomerId();
    
    @Query("SELECT c FROM Customer c WHERE c.customerId > :customerId ORDER BY c.customerId ASC")
    Optional<Customer> findNextCustomer(@Param("customerId") String customerId);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId < :customerId ORDER BY c.customerId DESC")
    Optional<Customer> findPreviousCustomer(@Param("customerId") String customerId);
    
    // Sequential reading with multiple filters for CBCUS01C-like functionality
    @Query("SELECT c FROM Customer c WHERE c.customerId >= :startingCustomerId AND c.customerStatus IN :statuses ORDER BY c.customerId ASC")
    List<Customer> findCustomersSequentiallyByStatusIn(@Param("startingCustomerId") String startingCustomerId, @Param("statuses") List<String> statuses, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId >= :startingCustomerId AND c.customerType IN :types ORDER BY c.customerId ASC")
    List<Customer> findCustomersSequentiallyByTypeIn(@Param("startingCustomerId") String startingCustomerId, @Param("types") List<String> types, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId >= :startingCustomerId AND c.creditRating IN :ratings ORDER BY c.customerId ASC")
    List<Customer> findCustomersSequentiallyByCreditRatingIn(@Param("startingCustomerId") String startingCustomerId, @Param("ratings") List<String> ratings, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId >= :startingCustomerId AND LOWER(c.state) = LOWER(:state) ORDER BY c.customerId ASC")
    List<Customer> findCustomersSequentiallyByState(@Param("startingCustomerId") String startingCustomerId, @Param("state") String state, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId >= :startingCustomerId AND LOWER(c.city) = LOWER(:city) ORDER BY c.customerId ASC")
    List<Customer> findCustomersSequentiallyByCity(@Param("startingCustomerId") String startingCustomerId, @Param("city") String city, Pageable pageable);
    
    // Complex sequential queries with multiple filters
    @Query("SELECT c FROM Customer c WHERE " +
           "c.customerId >= :startingCustomerId AND " +
           "(:status IS NULL OR c.customerStatus = :status) AND " +
           "(:type IS NULL OR c.customerType = :type) AND " +
           "(:creditRating IS NULL OR c.creditRating = :creditRating) AND " +
           "(:state IS NULL OR LOWER(c.state) = LOWER(:state)) " +
           "ORDER BY c.customerId ASC")
    List<Customer> findCustomersSequentiallyWithFilters(@Param("startingCustomerId") String startingCustomerId,
                                                       @Param("status") String status,
                                                       @Param("type") String type,
                                                       @Param("creditRating") String creditRating,
                                                       @Param("state") String state,
                                                       Pageable pageable);
}