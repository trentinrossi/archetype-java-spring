package com.example.demo.service;

import com.example.demo.dto.CreateCustomerRequestDto;
import com.example.demo.dto.CustomerResponseDto;
import com.example.demo.dto.UpdateCustomerRequestDto;
import com.example.demo.entity.Customer;
import com.example.demo.enums.CustomerStatus;
import com.example.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service class for Customer operations
 * Implements business logic for customer management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    /**
     * Create a new customer
     */
    @Transactional
    public CustomerResponseDto createCustomer(CreateCustomerRequestDto request) {
        log.info("Creating new customer with ID: {}", request.getCustomerId());
        
        // Validate customer ID format (must be 9 numeric characters)
        if (!request.getCustomerId().matches("\\d{9}")) {
            throw new IllegalArgumentException("Customer ID must be 9 numeric characters");
        }
        
        // Check if customer already exists
        if (customerRepository.existsById(request.getCustomerId())) {
            throw new IllegalArgumentException("Customer with ID " + request.getCustomerId() + " already exists");
        }
        
        // Check if email already exists
        if (request.getEmail() != null && customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Customer with email " + request.getEmail() + " already exists");
        }
        
        // Check if SSN already exists
        if (request.getSsn() != null && customerRepository.existsBySsn(request.getSsn())) {
            throw new IllegalArgumentException("Customer with SSN already exists");
        }
        
        Customer customer = new Customer();
        customer.setCustomerId(request.getCustomerId());
        customer.setFirstName(request.getFirstName());
        customer.setMiddleInitial(request.getMiddleInitial());
        customer.setLastName(request.getLastName());
        customer.setAddressLine1(request.getAddressLine1());
        customer.setAddressLine2(request.getAddressLine2());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setZipCode(request.getZipCode());
        customer.setCountry(request.getCountry());
        customer.setHomePhone(request.getHomePhone());
        customer.setWorkPhone(request.getWorkPhone());
        customer.setMobilePhone(request.getMobilePhone());
        customer.setEmail(request.getEmail());
        customer.setSsn(request.getSsn());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setCreditScore(request.getCreditScore());
        customer.setFicoScore(request.getFicoScore());
        customer.setGovernmentId(request.getGovernmentId());
        customer.setGovernmentIdType(request.getGovernmentIdType());
        customer.setCustomerSince(request.getCustomerSince() != null ? request.getCustomerSince() : LocalDate.now());
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setVipStatus(request.getVipStatus() != null ? request.getVipStatus() : false);
        customer.setPreferredContactMethod(request.getPreferredContactMethod());
        customer.setOccupation(request.getOccupation());
        customer.setAnnualIncome(request.getAnnualIncome());
        customer.setEmployerName(request.getEmployerName());
        customer.setYearsAtEmployer(request.getYearsAtEmployer());
        
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getCustomerId());
        
        return convertToResponse(savedCustomer);
    }
    
    /**
     * Get customer by ID
     */
    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerById(String customerId) {
        log.debug("Fetching customer with ID: {}", customerId);
        return customerRepository.findById(customerId).map(this::convertToResponse);
    }
    
    /**
     * Update customer
     */
    @Transactional
    public CustomerResponseDto updateCustomer(String customerId, UpdateCustomerRequestDto request) {
        log.info("Updating customer with ID: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
        
        // Update only non-null fields
        if (request.getFirstName() != null) customer.setFirstName(request.getFirstName());
        if (request.getMiddleInitial() != null) customer.setMiddleInitial(request.getMiddleInitial());
        if (request.getLastName() != null) customer.setLastName(request.getLastName());
        if (request.getAddressLine1() != null) customer.setAddressLine1(request.getAddressLine1());
        if (request.getAddressLine2() != null) customer.setAddressLine2(request.getAddressLine2());
        if (request.getCity() != null) customer.setCity(request.getCity());
        if (request.getState() != null) customer.setState(request.getState());
        if (request.getZipCode() != null) customer.setZipCode(request.getZipCode());
        if (request.getCountry() != null) customer.setCountry(request.getCountry());
        if (request.getHomePhone() != null) customer.setHomePhone(request.getHomePhone());
        if (request.getWorkPhone() != null) customer.setWorkPhone(request.getWorkPhone());
        if (request.getMobilePhone() != null) customer.setMobilePhone(request.getMobilePhone());
        if (request.getEmail() != null) {
            // Check if new email already exists for another customer
            customerRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
                if (!existing.getCustomerId().equals(customerId)) {
                    throw new IllegalArgumentException("Email already in use by another customer");
                }
            });
            customer.setEmail(request.getEmail());
        }
        if (request.getSsn() != null) customer.setSsn(request.getSsn());
        if (request.getDateOfBirth() != null) customer.setDateOfBirth(request.getDateOfBirth());
        if (request.getCreditScore() != null) customer.setCreditScore(request.getCreditScore());
        if (request.getFicoScore() != null) customer.setFicoScore(request.getFicoScore());
        if (request.getGovernmentId() != null) customer.setGovernmentId(request.getGovernmentId());
        if (request.getGovernmentIdType() != null) customer.setGovernmentIdType(request.getGovernmentIdType());
        if (request.getStatus() != null) customer.setStatus(request.getStatus());
        if (request.getVipStatus() != null) customer.setVipStatus(request.getVipStatus());
        if (request.getPreferredContactMethod() != null) customer.setPreferredContactMethod(request.getPreferredContactMethod());
        if (request.getOccupation() != null) customer.setOccupation(request.getOccupation());
        if (request.getAnnualIncome() != null) customer.setAnnualIncome(request.getAnnualIncome());
        if (request.getEmployerName() != null) customer.setEmployerName(request.getEmployerName());
        if (request.getYearsAtEmployer() != null) customer.setYearsAtEmployer(request.getYearsAtEmployer());
        
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully with ID: {}", updatedCustomer.getCustomerId());
        
        return convertToResponse(updatedCustomer);
    }
    
    /**
     * Delete customer
     */
    @Transactional
    public void deleteCustomer(String customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found with ID: " + customerId);
        }
        
        customerRepository.deleteById(customerId);
        log.info("Customer deleted successfully with ID: {}", customerId);
    }
    
    /**
     * Get all customers with pagination
     */
    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getAllCustomers(Pageable pageable) {
        log.debug("Fetching all customers with pagination");
        return customerRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    /**
     * Search customers by name
     */
    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> searchCustomersByName(String searchTerm, Pageable pageable) {
        log.debug("Searching customers by name: {}", searchTerm);
        return customerRepository.findByNameContaining(searchTerm, pageable).map(this::convertToResponse);
    }
    
    /**
     * Get customers by status
     */
    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getCustomersByStatus(CustomerStatus status, Pageable pageable) {
        log.debug("Fetching customers with status: {}", status);
        return customerRepository.findByStatus(status, pageable).map(this::convertToResponse);
    }
    
    /**
     * Get VIP customers
     */
    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getVipCustomers(Pageable pageable) {
        log.debug("Fetching VIP customers");
        return customerRepository.findByVipStatusTrue(pageable).map(this::convertToResponse);
    }
    
    /**
     * Get customers by state
     */
    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getCustomersByState(String state, Pageable pageable) {
        log.debug("Fetching customers in state: {}", state);
        return customerRepository.findByState(state, pageable).map(this::convertToResponse);
    }
    
    /**
     * Get customers by credit score range
     */
    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getCustomersByCreditScoreRange(Integer minScore, Integer maxScore, Pageable pageable) {
        log.debug("Fetching customers with credit score between {} and {}", minScore, maxScore);
        return customerRepository.findByCreditScoreRange(minScore, maxScore, pageable).map(this::convertToResponse);
    }
    
    /**
     * Convert Customer entity to CustomerResponseDto
     */
    private CustomerResponseDto convertToResponse(Customer customer) {
        CustomerResponseDto response = new CustomerResponseDto();
        response.setCustomerId(customer.getCustomerId());
        response.setFirstName(customer.getFirstName());
        response.setMiddleInitial(customer.getMiddleInitial());
        response.setLastName(customer.getLastName());
        response.setFullName(customer.getFullName());
        response.setAddressLine1(customer.getAddressLine1());
        response.setAddressLine2(customer.getAddressLine2());
        response.setCity(customer.getCity());
        response.setState(customer.getState());
        response.setZipCode(customer.getZipCode());
        response.setCountry(customer.getCountry());
        response.setFormattedAddress(customer.getFormattedAddress());
        response.setHomePhone(customer.getHomePhone());
        response.setWorkPhone(customer.getWorkPhone());
        response.setMobilePhone(customer.getMobilePhone());
        response.setEmail(customer.getEmail());
        response.setSsn(maskSsn(customer.getSsn()));
        response.setDateOfBirth(customer.getDateOfBirth());
        response.setAge(customer.getAge());
        response.setCreditScore(customer.getCreditScore());
        response.setFicoScore(customer.getFicoScore());
        response.setGovernmentId(customer.getGovernmentId());
        response.setGovernmentIdType(customer.getGovernmentIdType());
        response.setCustomerSince(customer.getCustomerSince());
        response.setStatus(customer.getStatus());
        response.setStatusDisplayName(customer.getStatus().getDisplayName());
        response.setVipStatus(customer.getVipStatus());
        response.setPreferredContactMethod(customer.getPreferredContactMethod());
        response.setOccupation(customer.getOccupation());
        response.setAnnualIncome(customer.getAnnualIncome());
        response.setEmployerName(customer.getEmployerName());
        response.setYearsAtEmployer(customer.getYearsAtEmployer());
        response.setAccountCount(customer.getAccounts() != null ? customer.getAccounts().size() : 0);
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }
    
    /**
     * Mask SSN for security
     */
    private String maskSsn(String ssn) {
        if (ssn == null || ssn.length() < 4) {
            return "***-**-****";
        }
        return "***-**-" + ssn.substring(ssn.length() - 4);
    }
}
