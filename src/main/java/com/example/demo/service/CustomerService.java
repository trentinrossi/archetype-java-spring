package com.example.demo.service;

import com.example.demo.dto.CustomerCreateDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.CustomerUpdateDTO;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(String customerId) {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
        return mapToDTO(customer);
    }
    
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByLastName(String lastName) {
        return customerRepository.findByLastName(lastName).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CustomerDTO createCustomer(CustomerCreateDTO createDTO) {
        if (customerRepository.existsByCustomerId(createDTO.getCustomerId())) {
            throw new RuntimeException("Customer already exists: " + createDTO.getCustomerId());
        }
        
        Customer customer = new Customer();
        customer.setCustomerId(createDTO.getCustomerId());
        customer.setFirstName(createDTO.getFirstName().toUpperCase());
        customer.setMiddleName(createDTO.getMiddleName() != null ? createDTO.getMiddleName().toUpperCase() : null);
        customer.setLastName(createDTO.getLastName().toUpperCase());
        customer.setAddressLine1(createDTO.getAddressLine1());
        customer.setAddressLine2(createDTO.getAddressLine2());
        customer.setCity(createDTO.getCity());
        customer.setStateCode(createDTO.getStateCode().toUpperCase());
        customer.setZipCode(createDTO.getZipCode());
        customer.setCountryCode(createDTO.getCountryCode().toUpperCase());
        customer.setPhoneNumber1(createDTO.getPhoneNumber1());
        customer.setPhoneNumber2(createDTO.getPhoneNumber2());
        customer.setSsn(createDTO.getSsn());
        customer.setGovernmentIssuedId(createDTO.getGovernmentIssuedId());
        customer.setDateOfBirth(createDTO.getDateOfBirth());
        customer.setEftAccountId(createDTO.getEftAccountId());
        customer.setPrimaryCardHolderIndicator(createDTO.getPrimaryCardHolderIndicator());
        customer.setFicoCreditScore(createDTO.getFicoCreditScore());
        
        Customer saved = customerRepository.save(customer);
        return mapToDTO(saved);
    }
    
    @Transactional
    public CustomerDTO updateCustomer(String customerId, CustomerUpdateDTO updateDTO) {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
        
        customer.setFirstName(updateDTO.getFirstName().toUpperCase());
        customer.setMiddleName(updateDTO.getMiddleName() != null ? updateDTO.getMiddleName().toUpperCase() : null);
        customer.setLastName(updateDTO.getLastName().toUpperCase());
        customer.setAddressLine1(updateDTO.getAddressLine1());
        customer.setAddressLine2(updateDTO.getAddressLine2());
        customer.setCity(updateDTO.getCity());
        customer.setStateCode(updateDTO.getStateCode().toUpperCase());
        customer.setZipCode(updateDTO.getZipCode());
        customer.setCountryCode(updateDTO.getCountryCode().toUpperCase());
        customer.setPhoneNumber1(updateDTO.getPhoneNumber1());
        customer.setPhoneNumber2(updateDTO.getPhoneNumber2());
        customer.setSsn(updateDTO.getSsn());
        customer.setGovernmentIssuedId(updateDTO.getGovernmentIssuedId());
        customer.setDateOfBirth(updateDTO.getDateOfBirth());
        customer.setEftAccountId(updateDTO.getEftAccountId());
        customer.setPrimaryCardHolderIndicator(updateDTO.getPrimaryCardHolderIndicator());
        customer.setFicoCreditScore(updateDTO.getFicoCreditScore());
        
        Customer saved = customerRepository.save(customer);
        return mapToDTO(saved);
    }
    
    @Transactional
    public void deleteCustomer(String customerId) {
        if (!customerRepository.existsByCustomerId(customerId)) {
            throw new RuntimeException("Customer not found: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }
    
    private CustomerDTO mapToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setMiddleName(customer.getMiddleName());
        dto.setLastName(customer.getLastName());
        dto.setFullName(customer.getFullName());
        dto.setAddressLine1(customer.getAddressLine1());
        dto.setAddressLine2(customer.getAddressLine2());
        dto.setCity(customer.getCity());
        dto.setStateCode(customer.getStateCode());
        dto.setZipCode(customer.getZipCode());
        dto.setCountryCode(customer.getCountryCode());
        dto.setPhoneNumber1(customer.getPhoneNumber1());
        dto.setPhoneNumber2(customer.getPhoneNumber2());
        dto.setGovernmentIssuedId(customer.getGovernmentIssuedId());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setEftAccountId(customer.getEftAccountId());
        dto.setPrimaryCardHolderIndicator(customer.getPrimaryCardHolderIndicator());
        dto.setFicoCreditScore(customer.getFicoCreditScore());
        dto.setPrimaryCardHolder(customer.isPrimaryCardHolder());
        return dto;
    }
}
