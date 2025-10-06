package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.dto.*;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("Creating new customer with ID: {}", request.getCustomerId());
        
        // Validate customer doesn't already exist
        if (customerRepository.existsByCustomerId(request.getCustomerId())) {
            throw new IllegalArgumentException("Customer with ID " + request.getCustomerId() + " already exists");
        }
        
        // Create customer entity
        Customer customer = new Customer();
        customer.setCustomerId(request.getCustomerId());
        customer.setCustomerData(request.getCustomerData());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setMiddleInitial(request.getMiddleInitial());
        customer.setAddressLine1(request.getAddressLine1());
        customer.setAddressLine2(request.getAddressLine2());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setZipCode(request.getZipCode());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setEmailAddress(request.getEmailAddress());
        customer.setCustomerStatus(request.getCustomerStatus() != null ? request.getCustomerStatus() : "A");
        customer.setCustomerType(request.getCustomerType());
        customer.setCreditRating(request.getCreditRating());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setSocialSecurityNumber(request.getSocialSecurityNumber());
        customer.setEmployerName(request.getEmployerName());
        customer.setAnnualIncome(request.getAnnualIncome());
        
        Customer savedCustomer = customerRepository.save(customer);
        
        log.info("Customer created successfully: {}", savedCustomer.getCustomerId());
        return convertToResponse(savedCustomer);
    }
    
    @Transactional(readOnly = true)
    public Optional<CustomerResponse> getCustomerById(String customerId) {
        log.info("Retrieving customer by ID: {}", customerId);
        
        return customerRepository.findByCustomerId(customerId)
                .map(this::convertToResponse);
    }
    
    @Transactional
    public CustomerResponse updateCustomer(String customerId, UpdateCustomerRequest request) {
        log.info("Updating customer with ID: {}", customerId);
        
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        
        // Update fields if provided
        if (request.getCustomerData() != null) {
            customer.setCustomerData(request.getCustomerData());
        }
        if (request.getFirstName() != null) {
            customer.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            customer.setLastName(request.getLastName());
        }
        if (request.getMiddleInitial() != null) {
            customer.setMiddleInitial(request.getMiddleInitial());
        }
        if (request.getAddressLine1() != null) {
            customer.setAddressLine1(request.getAddressLine1());
        }
        if (request.getAddressLine2() != null) {
            customer.setAddressLine2(request.getAddressLine2());
        }
        if (request.getCity() != null) {
            customer.setCity(request.getCity());
        }
        if (request.setState() != null) {
            customer.setState(request.getState());
        }
        if (request.getZipCode() != null) {
            customer.setZipCode(request.getZipCode());
        }
        if (request.getPhoneNumber() != null) {
            customer.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getEmailAddress() != null) {
            customer.setEmailAddress(request.getEmailAddress());
        }
        if (request.getCustomerStatus() != null) {
            customer.setCustomerStatus(request.getCustomerStatus());
        }
        if (request.getCustomerType() != null) {
            customer.setCustomerType(request.getCustomerType());
        }
        if (request.getCreditRating() != null) {
            customer.setCreditRating(request.getCreditRating());
        }
        if (request.getEmployerName() != null) {
            customer.setEmployerName(request.getEmployerName());
        }
        if (request.getAnnualIncome() != null) {
            customer.setAnnualIncome(request.getAnnualIncome());
        }
        
        Customer updatedCustomer = customerRepository.save(customer);
        
        log.info("Customer updated successfully: {}", updatedCustomer.getCustomerId());
        return convertToResponse(updatedCustomer);
    }
    
    @Transactional
    public void deleteCustomer(String customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        
        if (!customerRepository.existsByCustomerId(customerId)) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        
        customerRepository.deleteById(customerId);
        log.info("Customer deleted successfully: {}", customerId);
    }
    
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        log.info("Retrieving all customers with pagination");
        
        return customerRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getCustomersByStatus(String status, Pageable pageable) {
        log.info("Retrieving customers by status: {}", status);
        
        return customerRepository.findByCustomerStatus(status, pageable)
                .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getCustomersByType(String customerType, Pageable pageable) {
        log.info("Retrieving customers by type: {}", customerType);
        
        return customerRepository.findByCustomerType(customerType, pageable)
                .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public CustomerSequentialReadResponse sequentialReadCustomers(CustomerSequentialReadRequest request) {
        log.info("Sequential read starting from customer: {} with {} records", 
                request.getStartingCustomerId(), 
                request.getRecordCount() != null ? request.getRecordCount() : 10);
        
        String startingCustomerId = request.getStartingCustomerId() != null ? request.getStartingCustomerId() : "000000000";
        int recordCount = request.getRecordCount() != null ? request.getRecordCount() : 10;
        
        Pageable pageable = PageRequest.of(0, recordCount + 1, Sort.by("customerId"));
        List<Customer> customers;
        
        // Apply filters based on request
        if (request.getStatusFilter() != null && request.getCustomerTypeFilter() != null) {
            customers = customerRepository.findCustomersSequentiallyByStatusAndType(
                    startingCustomerId, request.getStatusFilter(), request.getCustomerTypeFilter(), pageable);
        } else if (request.getStatusFilter() != null) {
            customers = customerRepository.findCustomersSequentiallyByStatus(
                    startingCustomerId, request.getStatusFilter(), pageable);
        } else if (request.getCustomerTypeFilter() != null) {
            customers = customerRepository.findCustomersSequentiallyByType(
                    startingCustomerId, request.getCustomerTypeFilter(), pageable);
        } else {
            customers = customerRepository.findCustomersSequentiallyFromStarting(startingCustomerId, pageable);
        }
        
        // Apply additional filters
        if (Boolean.FALSE.equals(request.getIncludeInactive())) {
            customers = customers.stream()
                    .filter(Customer::isActive)
                    .collect(Collectors.toList());
        }
        
        if (Boolean.TRUE.equals(request.getPersonalCustomersOnly())) {
            customers = customers.stream()
                    .filter(Customer::isPersonalCustomer)
                    .collect(Collectors.toList());
        }
        
        if (Boolean.TRUE.equals(request.getBusinessCustomersOnly())) {
            customers = customers.stream()
                    .filter(Customer::isBusinessCustomer)
                    .collect(Collectors.toList());
        }
        
        // Determine if there are more records
        boolean hasMoreRecords = customers.size() > recordCount;
        if (hasMoreRecords) {
            customers = customers.subList(0, recordCount);
        }
        
        // Convert to response DTOs
        List<CustomerResponse> customerResponses = customers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        // Build response
        CustomerSequentialReadResponse response = new CustomerSequentialReadResponse();
        response.setStartingCustomerId(startingCustomerId);
        response.setRecordsRequested(recordCount);
        response.setRecordsReturned(customerResponses.size());
        response.setHasMoreRecords(hasMoreRecords);
        response.setNextCustomerId(hasMoreRecords && !customers.isEmpty() ? 
                getNextCustomerId(customers.get(customers.size() - 1).getCustomerId()) : null);
        response.setCustomers(customerResponses);
        response.setTotalMatchingCustomers(getTotalMatchingCustomers(request, startingCustomerId));
        response.setAppliedFilters(buildAppliedFiltersString(request));
        
        return response;
    }
    
    @Transactional(readOnly = true)
    public List<CustomerResponse> getActiveCustomers() {
        log.info("Retrieving all active customers");
        
        Pageable pageable = PageRequest.of(0, 1000, Sort.by("customerId"));
        return customerRepository.findActiveCustomers(pageable)
                .getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CustomerResponse> getInactiveCustomers() {
        log.info("Retrieving all inactive customers");
        
        Pageable pageable = PageRequest.of(0, 1000, Sort.by("customerId"));
        return customerRepository.findInactiveCustomers(pageable)
                .getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CustomerResponse> getPersonalCustomers() {
        log.info("Retrieving all personal customers");
        
        Pageable pageable = PageRequest.of(0, 1000, Sort.by("customerId"));
        return customerRepository.findPersonalCustomers(pageable)
                .getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CustomerResponse> getBusinessCustomers() {
        log.info("Retrieving all business customers");
        
        Pageable pageable = PageRequest.of(0, 1000, Sort.by("customerId"));
        return customerRepository.findBusinessCustomers(pageable)
                .getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CustomerResponse> getGoodCreditCustomers() {
        log.info("Retrieving customers with good credit rating");
        
        Pageable pageable = PageRequest.of(0, 1000, Sort.by("customerId"));
        return customerRepository.findGoodCreditCustomers(pageable)
                .getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CustomerResponse> getPoorCreditCustomers() {
        log.info("Retrieving customers with poor credit rating");
        
        Pageable pageable = PageRequest.of(0, 1000, Sort.by("customerId"));
        return customerRepository.findPoorCreditCustomers(pageable)
                .getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<CustomerResponse> searchCustomersByName(String firstName, String lastName, Pageable pageable) {
        log.info("Searching customers by name: {} {}", firstName, lastName);
        
        if (firstName != null && lastName != null) {
            return customerRepository.findByFirstNameAndLastNameContaining(firstName, lastName, pageable)
                    .map(this::convertToResponse);
        } else if (firstName != null) {
            return customerRepository.findByFirstNameContaining(firstName, pageable)
                    .map(this::convertToResponse);
        } else if (lastName != null) {
            return customerRepository.findByLastNameContaining(lastName, pageable)
                    .map(this::convertToResponse);
        } else {
            return Page.empty(pageable);
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<CustomerResponse> findCustomerByEmail(String email) {
        log.info("Finding customer by email: {}", email);
        
        return customerRepository.findByEmailAddress(email)
                .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public List<CustomerResponse> findCustomersByPhone(String phone) {
        log.info("Finding customers by phone: {}", phone);
        
        return customerRepository.findByPhoneNumber(phone)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public void openCustomerFile() {
        log.info("Opening customer file for sequential processing - CBCUS01C functionality");
        // Simulated file opening operation based on CBCUS01C program
        // In the original COBOL program, this would open the VSAM file
    }
    
    @Transactional(readOnly = true)
    public List<CustomerResponse> readCustomersSequentially(int recordCount) {
        log.info("Reading {} customers sequentially - CBCUS01C functionality", recordCount);
        
        Pageable pageable = PageRequest.of(0, recordCount, Sort.by("customerId"));
        return customerRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public void closeCustomerFile() {
        log.info("Closing customer file after sequential processing - CBCUS01C functionality");
        // Simulated file closing operation based on CBCUS01C program
        // In the original COBOL program, this would close the VSAM file
    }
    
    @Transactional(readOnly = true)
    public void displayCustomerRecord(CustomerResponse customer) {
        log.info("Displaying customer record - CBCUS01C functionality");
        log.info("Customer ID: {}", customer.getCustomerId());
        log.info("Full Name: {}", customer.getFullName());
        log.info("Status: {}", customer.getCustomerStatusDescription());
        log.info("Address: {}", customer.getFullAddress());
        log.info("Phone: {}", customer.getPhoneNumber());
        log.info("Email: {}", customer.getEmailAddress());
        log.info("Type: {}", customer.getCustomerType());
        log.info("Credit Rating: {}", customer.getCreditRating());
        log.info("-------------------------------------------------");
    }
    
    @Transactional(readOnly = true)
    public List<String> getDistinctStatuses() {
        return customerRepository.findDistinctCustomerStatuses();
    }
    
    @Transactional(readOnly = true)
    public List<String> getDistinctTypes() {
        return customerRepository.findDistinctCustomerTypes();
    }
    
    @Transactional(readOnly = true)
    public List<String> getDistinctCreditRatings() {
        return customerRepository.findDistinctCreditRatings();
    }
    
    @Transactional(readOnly = true)
    public List<String> getDistinctStates() {
        return customerRepository.findDistinctStates();
    }
    
    @Transactional(readOnly = true)
    public List<String> getDistinctCities() {
        return customerRepository.findDistinctCities();
    }
    
    @Transactional(readOnly = true)
    public long countCustomersByStatus(String status) {
        return customerRepository.countByCustomerStatus(status);
    }
    
    @Transactional(readOnly = true)
    public long countCustomersByType(String type) {
        return customerRepository.countByCustomerType(type);
    }
    
    @Transactional(readOnly = true)
    public long countCustomersByCity(String city) {
        return customerRepository.countByCity(city);
    }
    
    @Transactional(readOnly = true)
    public long countCustomersByState(String state) {
        return customerRepository.countByState(state);
    }
    
    private CustomerResponse convertToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setCustomerId(customer.getCustomerId());
        response.setCustomerData(customer.getCustomerData());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setMiddleInitial(customer.getMiddleInitial());
        response.setFullName(customer.getFullName());
        response.setAddressLine1(customer.getAddressLine1());
        response.setAddressLine2(customer.getAddressLine2());
        response.setCity(customer.getCity());
        response.setState(customer.getState());
        response.setZipCode(customer.getZipCode());
        response.setFullAddress(customer.getFullAddress());
        response.setPhoneNumber(customer.getPhoneNumber());
        response.setEmailAddress(customer.getEmailAddress());
        response.setCustomerStatus(customer.getCustomerStatus());
        response.setCustomerStatusDescription(customer.getCustomerStatusDescription());
        response.setCustomerType(customer.getCustomerType());
        response.setCreditRating(customer.getCreditRating());
        response.setDateOfBirth(customer.getDateOfBirth());
        response.setMaskedSSN(customer.getMaskedSSN());
        response.setEmployerName(customer.getEmployerName());
        response.setAnnualIncome(customer.getAnnualIncome());
        response.setIsActive(customer.isActive());
        response.setIsInactive(customer.isInactive());
        response.setIsSuspended(customer.isSuspended());
        response.setIsClosed(customer.isClosed());
        response.setHasValidEmail(customer.hasValidEmail());
        response.setHasValidPhone(customer.hasValidPhone());
        response.setIsPersonalCustomer(customer.isPersonalCustomer());
        response.setIsBusinessCustomer(customer.isBusinessCustomer());
        response.setHasGoodCreditRating(customer.hasGoodCreditRating());
        response.setHasPoorCreditRating(customer.hasPoorCreditRating());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        
        return response;
    }
    
    private String getNextCustomerId(String currentCustomerId) {
        // Simple increment logic for customer ID
        try {
            long id = Long.parseLong(currentCustomerId.replaceAll("[^0-9]", ""));
            return String.format("CUST%05d", id + 1);
        } catch (NumberFormatException e) {
            return currentCustomerId;
        }
    }
    
    private Long getTotalMatchingCustomers(CustomerSequentialReadRequest request, String startingCustomerId) {
        if (request.getStatusFilter() != null && request.getCustomerTypeFilter() != null) {
            return customerRepository.countByCustomerStatusAndType(
                    request.getStatusFilter(), request.getCustomerTypeFilter());
        } else if (request.getStatusFilter() != null) {
            return customerRepository.countCustomersFromStartingByStatus(startingCustomerId, request.getStatusFilter());
        } else if (request.getCustomerTypeFilter() != null) {
            return customerRepository.countCustomersFromStartingByType(startingCustomerId, request.getCustomerTypeFilter());
        } else {
            return customerRepository.countCustomersFromStarting(startingCustomerId);
        }
    }
    
    private String buildAppliedFiltersString(CustomerSequentialReadRequest request) {
        List<String> filters = new ArrayList<>();
        
        if (request.getStatusFilter() != null) {
            filters.add("Status: " + request.getStatusFilter());
        }
        if (request.getCustomerTypeFilter() != null) {
            filters.add("Type: " + request.getCustomerTypeFilter());
        }
        if (request.getCreditRatingFilter() != null) {
            filters.add("Rating: " + request.getCreditRatingFilter());
        }
        if (Boolean.FALSE.equals(request.getIncludeInactive())) {
            filters.add("Active Only");
        }
        if (Boolean.TRUE.equals(request.getPersonalCustomersOnly())) {
            filters.add("Personal Only");
        }
        if (Boolean.TRUE.equals(request.getBusinessCustomersOnly())) {
            filters.add("Business Only");
        }
        
        return filters.isEmpty() ? "No filters applied" : String.join(", ", filters);
    }
}