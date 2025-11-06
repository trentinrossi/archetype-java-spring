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

        if (customerRepository.existsByCustomerId(request.getCustomerId())) {
            throw new IllegalArgumentException("Customer with ID already exists");
        }

        Customer customer = new Customer();
        customer.setCustomerId(request.getCustomerId());
        customer.setCustomerData(request.getCustomerData());

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getCustomerId());
        return convertToResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerById(Long id) {
        log.debug("Retrieving customer by ID: {}", id);
        return customerRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerByCustomerId(String customerId) {
        log.debug("Retrieving customer by customer ID: {}", customerId);
        validateCustomerId(customerId);
        return customerRepository.findByCustomerId(customerId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getAllCustomers(Pageable pageable) {
        log.debug("Retrieving all customers with pagination");
        return customerRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public CustomerResponseDto updateCustomer(Long id, UpdateCustomerRequestDto request) {
        log.info("Updating customer with ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (request.getCustomerData() != null) {
            customer.setCustomerData(request.getCustomerData());
        }

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully with ID: {}", updatedCustomer.getCustomerId());
        return convertToResponse(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);

        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer not found");
        }

        customerRepository.deleteById(id);
        log.info("Customer deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public boolean existsByCustomerId(String customerId) {
        validateCustomerId(customerId);
        return customerRepository.existsByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public long countCustomers() {
        return customerRepository.count();
    }

    private void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid customer ID format");
        }

        if (customerId.length() != 9) {
            throw new IllegalArgumentException("Invalid customer ID format");
        }

        if (!customerId.matches("\\d{9}")) {
            throw new IllegalArgumentException("Invalid customer ID format");
        }
    }

    private CustomerResponseDto convertToResponse(Customer customer) {
        CustomerResponseDto response = new CustomerResponseDto();
        response.setId(customer.getId());
        response.setCustomerId(customer.getCustomerId());
        response.setCustomerData(customer.getCustomerData());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }
}