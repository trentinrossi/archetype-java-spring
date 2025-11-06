package com.example.demo.service;

import com.example.demo.dto.CreateCustomerRequestDto;
import com.example.demo.dto.CustomerResponseDto;
import com.example.demo.dto.UpdateCustomerRequestDto;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    @Transactional
    public CustomerResponseDto createCustomer(CreateCustomerRequestDto request) {
        log.info("Creating new customer with ID: {}", request.getCustomerId());
        
        validateCustomerId(request.getCustomerId());
        
        Long customerIdLong = Long.parseLong(request.getCustomerId());
        
        if (customerRepository.existsByCustomerId(customerIdLong)) {
            log.error("Customer with ID {} already exists", request.getCustomerId());
            throw new IllegalArgumentException("Customer with ID already exists");
        }
        
        if (request.getCustomerData() == null || request.getCustomerData().trim().isEmpty()) {
            log.error("Customer data is required");
            throw new IllegalArgumentException("Customer data is required");
        }
        
        if (request.getCustomerData().length() > 491) {
            log.error("Customer data exceeds maximum length of 491 characters");
            throw new IllegalArgumentException("Customer data exceeds maximum length of 491 characters");
        }
        
        Customer customer = new Customer();
        customer.setCustomerId(customerIdLong);
        customer.setCustomerData(request.getCustomerData());
        
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getCustomerId());
        
        return convertToResponse(savedCustomer);
    }
    
    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerById(Long customerId) {
        log.debug("Retrieving customer by customer ID: {}", customerId);
        return customerRepository.findByCustomerId(customerId).map(this::convertToResponse);
    }
    
    @Transactional
    public CustomerResponseDto updateCustomer(Long customerId, UpdateCustomerRequestDto request) {
        log.info("Updating customer with ID: {}", customerId);
        
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", customerId);
                    return new IllegalArgumentException("Customer not found");
                });
        
        if (request.getCustomerData() != null) {
            if (request.getCustomerData().trim().isEmpty()) {
                log.error("Customer data cannot be empty");
                throw new IllegalArgumentException("Customer data cannot be empty");
            }
            
            if (request.getCustomerData().length() > 491) {
                log.error("Customer data exceeds maximum length of 491 characters");
                throw new IllegalArgumentException("Customer data exceeds maximum length of 491 characters");
            }
            customer.setCustomerData(request.getCustomerData());
        }
        
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully with ID: {}", updatedCustomer.getCustomerId());
        
        return convertToResponse(updatedCustomer);
    }
    
    @Transactional
    public void deleteCustomer(Long customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", customerId);
                    return new IllegalArgumentException("Customer not found");
                });
        
        customerRepository.delete(customer);
        log.info("Customer deleted successfully with ID: {}", customerId);
    }
    
    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getAllCustomers(Pageable pageable) {
        log.debug("Retrieving all customers with pagination");
        return customerRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByCustomerId(Long customerId) {
        log.debug("Checking existence of customer with ID: {}", customerId);
        return customerRepository.existsByCustomerId(customerId);
    }
    
    private void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            log.error("Customer ID is required");
            throw new IllegalArgumentException("Customer ID is required");
        }
        
        if (customerId.length() != 9) {
            log.error("Invalid customer ID format: must be 9 characters. Provided: {}", customerId);
            throw new IllegalArgumentException("Invalid customer ID format: must be 9 characters");
        }
        
        if (!customerId.matches("\\d{9}")) {
            log.error("Invalid customer ID format: must be numeric. Provided: {}", customerId);
            throw new IllegalArgumentException("Invalid customer ID format: must be numeric");
        }
    }
    
    private CustomerResponseDto convertToResponse(Customer customer) {
        CustomerResponseDto response = new CustomerResponseDto();
        response.setCustomerId(String.valueOf(customer.getCustomerId()));
        response.setCustomerData(customer.getCustomerData());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }
}
