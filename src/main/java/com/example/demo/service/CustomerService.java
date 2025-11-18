package com.example.demo.service;

import com.example.demo.dto.CreateCustomerRequestDto;
import com.example.demo.dto.UpdateCustomerRequestDto;
import com.example.demo.dto.CustomerResponseDto;
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
        
        if (customerRepository.existsByCustomerId(request.getCustomerId())) {
            throw new IllegalArgumentException("Customer with ID already exists");
        }
        
        if (request.getEmail() != null && customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Customer with email already exists");
        }
        
        Customer customer = new Customer();
        customer.setCustomerId(request.getCustomerId());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        
        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerById(Long id) {
        log.info("Retrieving customer with ID: {}", id);
        return customerRepository.findById(id)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerByCustomerId(String customerId) {
        log.info("Retrieving customer with customer ID: {}", customerId);
        return customerRepository.findByCustomerId(customerId)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getAllCustomers(Pageable pageable) {
        log.info("Retrieving all customers with pagination");
        return customerRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Transactional
    public CustomerResponseDto updateCustomer(Long id, UpdateCustomerRequestDto request) {
        log.info("Updating customer with ID: {}", id);
        
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        
        if (request.getFirstName() != null) {
            customer.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            customer.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            if (customerRepository.existsByEmail(request.getEmail()) && 
                !request.getEmail().equals(customer.getEmail())) {
                throw new IllegalArgumentException("Customer with email already exists");
            }
            customer.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            customer.setPhoneNumber(request.getPhoneNumber());
        }
        
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToResponse(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);
        
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer not found");
        }
        
        customerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> searchCustomers(String searchTerm, Pageable pageable) {
        log.info("Searching customers with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term is required");
        }
        
        return customerRepository.searchCustomers(searchTerm, pageable)
                .map(this::convertToResponse);
    }

    private CustomerResponseDto convertToResponse(Customer customer) {
        CustomerResponseDto response = new CustomerResponseDto();
        response.setId(customer.getId());
        response.setCustomerId(customer.getCustomerId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setFullName(customer.getFullName());
        response.setEmail(customer.getEmail());
        response.setPhoneNumber(customer.getPhoneNumber());
        response.setAccountCount(customer.getAccountCount());
        response.setCreditCardCount(customer.getCreditCardCount());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }
}
