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
            throw new IllegalArgumentException("Customer ID must be 9 digits numeric and exist in customer file");
        }

        Customer customer = new Customer();
        customer.setCustomerId(request.getCustomerId());
        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        customer.setAddressLine1(request.getAddressLine1());
        customer.setAddressLine2(request.getAddressLine2());
        customer.setAddressLine3(request.getAddressLine3());
        customer.setStateCode(request.getStateCode());
        customer.setCountryCode(request.getCountryCode());
        customer.setZipCode(request.getZipCode());
        customer.setFicoCreditScore(request.getFicoCreditScore());

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getCustomerId());
        return convertToResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerById(Long id) {
        log.info("Retrieving customer by ID: {}", id);
        return customerRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerByCustomerId(Long customerId) {
        log.info("Retrieving customer by customer ID: {}", customerId);
        return customerRepository.findByCustomerId(customerId).map(this::convertToResponse);
    }

    @Transactional
    public CustomerResponseDto updateCustomer(Long id, UpdateCustomerRequestDto request) {
        log.info("Updating customer with ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (request.getFirstName() != null) {
            customer.setFirstName(request.getFirstName());
        }

        if (request.getMiddleName() != null) {
            customer.setMiddleName(request.getMiddleName());
        }

        if (request.getLastName() != null) {
            customer.setLastName(request.getLastName());
        }

        if (request.getAddressLine1() != null) {
            customer.setAddressLine1(request.getAddressLine1());
        }

        if (request.getAddressLine2() != null) {
            customer.setAddressLine2(request.getAddressLine2());
        }

        if (request.getAddressLine3() != null) {
            customer.setAddressLine3(request.getAddressLine3());
        }

        if (request.getStateCode() != null) {
            customer.setStateCode(request.getStateCode());
        }

        if (request.getCountryCode() != null) {
            customer.setCountryCode(request.getCountryCode());
        }

        if (request.getZipCode() != null) {
            customer.setZipCode(request.getZipCode());
        }

        if (request.getFicoCreditScore() != null) {
            customer.setFicoCreditScore(request.getFicoCreditScore());
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
    public Page<CustomerResponseDto> getAllCustomers(Pageable pageable) {
        log.info("Retrieving all customers with pagination");
        return customerRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public boolean customerExists(Long customerId) {
        return customerRepository.existsByCustomerId(customerId);
    }

    private CustomerResponseDto convertToResponse(Customer customer) {
        CustomerResponseDto response = new CustomerResponseDto();
        response.setCustomerId(customer.getCustomerId());
        response.setFirstName(customer.getFirstName());
        response.setMiddleName(customer.getMiddleName());
        response.setLastName(customer.getLastName());
        response.setFullName(customer.getFullName());
        response.setAddressLine1(customer.getAddressLine1());
        response.setAddressLine2(customer.getAddressLine2());
        response.setAddressLine3(customer.getAddressLine3());
        response.setStateCode(customer.getStateCode());
        response.setCountryCode(customer.getCountryCode());
        response.setZipCode(customer.getZipCode());
        response.setCompleteAddress(customer.getCompleteAddress());
        response.setFicoCreditScore(customer.getFicoCreditScore());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }
}
