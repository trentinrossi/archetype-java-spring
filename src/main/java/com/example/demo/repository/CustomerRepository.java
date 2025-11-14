package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByCustomerId(Long customerId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Customer c WHERE c.customerId = :customerId")
    Optional<Customer> findByCustomerIdForUpdate(@Param("customerId") Long customerId);
    
    boolean existsByCustomerId(Long customerId);
    
    Optional<Customer> findBySsn(Long ssn);
    
    boolean existsBySsn(Long ssn);
    
    List<Customer> findByFirstNameAndLastName(String firstName, String lastName);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(c.middleName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Customer> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    Optional<Customer> findByPhoneNumber1(String phoneNumber1);
    
    Optional<Customer> findByPhoneNumber2(String phoneNumber2);
    
    @Query("SELECT c FROM Customer c WHERE c.phoneNumber1 = :phoneNumber OR c.phoneNumber2 = :phoneNumber")
    List<Customer> findByAnyPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    Optional<Customer> findByPrimaryPhoneNumber(String primaryPhoneNumber);
    
    Optional<Customer> findBySecondaryPhoneNumber(String secondaryPhoneNumber);
    
    @Query("SELECT c FROM Customer c WHERE c.primaryPhoneNumber = :phoneNumber OR c.secondaryPhoneNumber = :phoneNumber")
    List<Customer> findByAnyPhoneNumberAlternate(@Param("phoneNumber") String phoneNumber);
    
    List<Customer> findByStateCode(String stateCode);
    
    List<Customer> findByCountryCode(String countryCode);
    
    List<Customer> findByStateCodeAndCountryCode(String stateCode, String countryCode);
    
    List<Customer> findByZipCode(String zipCode);
    
    @Query("SELECT c FROM Customer c WHERE c.stateCode = :stateCode AND c.zipCode = :zipCode")
    List<Customer> findByStateCodeAndZipCode(@Param("stateCode") String stateCode, @Param("zipCode") String zipCode);
    
    List<Customer> findByCity(String city);
    
    @Query("SELECT c FROM Customer c WHERE c.addressLine3 = :city")
    List<Customer> findByAddressLine3(String city);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%'))")
    List<Customer> findByCityContaining(@Param("city") String city);
    
    Optional<Customer> findByGovernmentIssuedId(String governmentIssuedId);
    
    boolean existsByGovernmentIssuedId(String governmentIssuedId);
    
    Optional<Customer> findByEftAccountId(String eftAccountId);
    
    boolean existsByEftAccountId(String eftAccountId);
    
    List<Customer> findByPrimaryCardholderIndicator(String primaryCardholderIndicator);
    
    long countByPrimaryCardholderIndicator(String primaryCardholderIndicator);
    
    @Query("SELECT c FROM Customer c WHERE c.ficoScore >= :minScore AND c.ficoScore <= :maxScore")
    List<Customer> findByFicoScoreRange(@Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);
    
    @Query("SELECT c FROM Customer c WHERE c.ficoCreditScore >= :minScore AND c.ficoCreditScore <= :maxScore")
    List<Customer> findByFicoCreditScoreRange(@Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);
    
    @Query("SELECT c FROM Customer c WHERE c.ficoScore >= :minScore")
    List<Customer> findByFicoScoreGreaterThanEqual(@Param("minScore") Integer minScore);
    
    @Query("SELECT c FROM Customer c WHERE c.ficoScore < :maxScore")
    List<Customer> findByFicoScoreLessThan(@Param("maxScore") Integer maxScore);
    
    @Query("SELECT c FROM Customer c WHERE c.dateOfBirth >= :startDate AND c.dateOfBirth <= :endDate")
    List<Customer> findByDateOfBirthBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT c FROM Customer c WHERE c.dateOfBirth <= :maxDate")
    List<Customer> findByDateOfBirthBefore(@Param("maxDate") LocalDate maxDate);
    
    @Query("SELECT c FROM Customer c WHERE c.dateOfBirth >= :minDate")
    List<Customer> findByDateOfBirthAfter(@Param("minDate") LocalDate minDate);
    
    @Query("SELECT c FROM Customer c WHERE YEAR(CURRENT_DATE) - YEAR(c.dateOfBirth) >= :minAge")
    List<Customer> findByMinimumAge(@Param("minAge") Integer minAge);
    
    @Query("SELECT c FROM Customer c WHERE YEAR(CURRENT_DATE) - YEAR(c.dateOfBirth) >= 18")
    List<Customer> findEligibleCustomers();
    
    @Query("SELECT c FROM Customer c WHERE c.lastName = :lastName AND c.dateOfBirth = :dateOfBirth")
    List<Customer> findByLastNameAndDateOfBirth(@Param("lastName") String lastName, @Param("dateOfBirth") LocalDate dateOfBirth);
    
    @Query("SELECT c FROM Customer c WHERE c.firstName = :firstName AND c.lastName = :lastName AND c.dateOfBirth = :dateOfBirth")
    List<Customer> findByFullNameAndDateOfBirth(@Param("firstName") String firstName, 
                                                  @Param("lastName") String lastName, 
                                                  @Param("dateOfBirth") LocalDate dateOfBirth);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.stateCode = :stateCode")
    long countByStateCode(@Param("stateCode") String stateCode);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.countryCode = :countryCode")
    long countByCountryCode(@Param("countryCode") String countryCode);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.city = :city")
    long countByCity(@Param("city") String city);
    
    @Query("SELECT c FROM Customer c WHERE c.addressLine1 LIKE %:searchTerm% " +
           "OR c.addressLine2 LIKE %:searchTerm% " +
           "OR c.addressLine3 LIKE %:searchTerm%")
    List<Customer> findByAddressContaining(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT c FROM Customer c WHERE c.stateCode = :stateCode AND c.city = :city")
    List<Customer> findByStateCodeAndCity(@Param("stateCode") String stateCode, @Param("city") String city);
    
    @Query("SELECT DISTINCT c.stateCode FROM Customer c ORDER BY c.stateCode")
    List<String> findDistinctStateCodes();
    
    @Query("SELECT DISTINCT c.countryCode FROM Customer c ORDER BY c.countryCode")
    List<String> findDistinctCountryCodes();
    
    @Query("SELECT DISTINCT c.city FROM Customer c WHERE c.stateCode = :stateCode ORDER BY c.city")
    List<String> findDistinctCitiesByStateCode(@Param("stateCode") String stateCode);
    
    @Query("SELECT c FROM Customer c WHERE c.eftAccountId IS NOT NULL")
    List<Customer> findCustomersWithEftAccount();
    
    @Query("SELECT c FROM Customer c WHERE c.eftAccountId IS NULL")
    List<Customer> findCustomersWithoutEftAccount();
    
    @Query("SELECT c FROM Customer c WHERE c.phoneNumber2 IS NOT NULL")
    List<Customer> findCustomersWithSecondaryPhone();
    
    @Query("SELECT c FROM Customer c WHERE c.secondaryPhoneNumber IS NOT NULL")
    List<Customer> findCustomersWithSecondaryPhoneAlternate();
    
    @Query("SELECT c FROM Customer c WHERE c.middleName IS NOT NULL AND c.middleName <> ''")
    List<Customer> findCustomersWithMiddleName();
    
    @Query("SELECT c FROM Customer c WHERE c.governmentIssuedId IS NOT NULL AND c.governmentIssuedId <> ''")
    List<Customer> findCustomersWithGovernmentId();
    
    Page<Customer> findAll(Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.primaryCardholderIndicator = 'Y'")
    List<Customer> findPrimaryCardholders();
    
    @Query("SELECT c FROM Customer c WHERE c.primaryCardholderIndicator = 'N'")
    List<Customer> findSecondaryCardholders();
}
