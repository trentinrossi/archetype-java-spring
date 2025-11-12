package com.example.demo.service;

import com.example.demo.dto.CustomerResponseDto;
import com.example.demo.dto.CreateCustomerRequestDto;
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
        
        if (customerRepository.existsByCustomerId(request.getCustomerId())) {
            throw new IllegalArgumentException("Customer with this ID already exists");
        }
        
        Customer customer = new Customer();
        customer.setCustomerId(request.getCustomerId());
        customer.setSsn(request.getSsn());
        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        customer.setAddressLine1(request.getAddressLine1());
        customer.setAddressLine2(request.getAddressLine2());
        customer.setCity(request.getCity());
        customer.setStateCode(request.getStateCode());
        customer.setCountryCode(request.getCountryCode());
        customer.setZipCode(request.getZipCode());
        customer.setPhoneNumber1(request.getPhoneNumber1());
        customer.setPhoneNumber2(request.getPhoneNumber2());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGovernmentId(request.getGovernmentId());
        customer.setEftAccountId(request.getEftAccountId());
        customer.setPrimaryCardHolderIndicator(request.getPrimaryCardHolderIndicator());
        customer.setFicoCreditScore(request.getFicoCreditScore());
        
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getCustomerId());
        
        return convertToResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerById(String customerId) {
        log.info("Retrieving customer with ID: {}", customerId);
        return customerRepository.findByCustomerId(customerId).map(this::convertToResponse);
    }

    @Transactional
    public CustomerResponseDto updateCustomer(String customerId, UpdateCustomerRequestDto request) {
        log.info("Updating customer with ID: {}", customerId);
        
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        
        if (request.getFirstName() != null) customer.setFirstName(request.getFirstName());
        if (request.getMiddleName() != null) customer.setMiddleName(request.getMiddleName());
        if (request.getLastName() != null) customer.setLastName(request.getLastName());
        if (request.getAddressLine1() != null) customer.setAddressLine1(request.getAddressLine1());
        if (request.getAddressLine2() != null) customer.setAddressLine2(request.getAddressLine2());
        if (request.getCity() != null) customer.setCity(request.getCity());
        if (request.getStateCode() != null) customer.setStateCode(request.getStateCode());
        if (request.getCountryCode() != null) customer.setCountryCode(request.getCountryCode());
        if (request.getZipCode() != null) customer.setZipCode(request.getZipCode());
        if (request.getPhoneNumber1() != null) customer.setPhoneNumber1(request.getPhoneNumber1());
        if (request.getPhoneNumber2() != null) customer.setPhoneNumber2(request.getPhoneNumber2());
        if (request.getGovernmentId() != null) customer.setGovernmentId(request.getGovernmentId());
        if (request.getEftAccountId() != null) customer.setEftAccountId(request.getEftAccountId());
        if (request.getPrimaryCardHolderIndicator() != null) customer.setPrimaryCardHolderIndicator(request.getPrimaryCardHolderIndicator());
        if (request.getFicoCreditScore() != null) customer.setFicoCreditScore(request.getFicoCreditScore());
        
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully with ID: {}", updatedCustomer.getCustomerId());
        
        return convertToResponse(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(String customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        
        if (!customerRepository.existsByCustomerId(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }
        
        customerRepository.deleteById(customerId);
        log.info("Customer deleted successfully with ID: {}", customerId);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getAllCustomers(Pageable pageable) {
        log.info("Retrieving all customers with pagination");
        return customerRepository.findAll(pageable).map(this::convertToResponse);
    }

    private CustomerResponseDto convertToResponse(Customer customer) {
        CustomerResponseDto response = new CustomerResponseDto();
        response.setCustomerId(customer.getCustomerId());
        response.setSsn(customer.getSsn());
        response.setFormattedSsn(customer.getFormattedSsn());
        response.setFirstName(customer.getFirstName());
        response.setMiddleName(customer.getMiddleName());
        response.setLastName(customer.getLastName());
        response.setFullName(customer.getFullName());
        response.setAddressLine1(customer.getAddressLine1());
        response.setAddressLine2(customer.getAddressLine2());
        response.setCity(customer.getCity());
        response.setStateCode(customer.getStateCode());
        response.setCountryCode(customer.getCountryCode());
        response.setZipCode(customer.getZipCode());
        response.setPhoneNumber1(customer.getPhoneNumber1());
        response.setPhoneNumber2(customer.getPhoneNumber2());
        response.setDateOfBirth(customer.getDateOfBirth());
        response.setAge(customer.getAge());
        response.setGovernmentId(customer.getGovernmentId());
        response.setEftAccountId(customer.getEftAccountId());
        response.setPrimaryCardHolderIndicator(customer.getPrimaryCardHolderIndicator());
        response.setIsPrimaryCardHolder(customer.isPrimaryCardHolder());
        response.setFicoCreditScore(customer.getFicoCreditScore());
        response.setCustomerData(customer.getCustomerData());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }
}
