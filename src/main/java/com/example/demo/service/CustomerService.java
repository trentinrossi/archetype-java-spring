package com.example.demo.service;

import com.example.demo.dto.CustomerDTO;
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
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(String customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        return convertToDTO(customer);
    }
    
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByLastName(String lastName) {
        return customerRepository.findByLastName(lastName).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = convertToEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }
    
    @Transactional
    public CustomerDTO updateCustomer(String customerId, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        existingCustomer.setFirstName(customerDTO.getFirstName());
        existingCustomer.setMiddleName(customerDTO.getMiddleName());
        existingCustomer.setLastName(customerDTO.getLastName());
        existingCustomer.setAddressLine1(customerDTO.getAddressLine1());
        existingCustomer.setAddressLine2(customerDTO.getAddressLine2());
        existingCustomer.setAddressLine3(customerDTO.getAddressLine3());
        existingCustomer.setStateCode(customerDTO.getStateCode());
        existingCustomer.setCountryCode(customerDTO.getCountryCode());
        existingCustomer.setZipCode(customerDTO.getZipCode());
        existingCustomer.setPhoneNumber1(customerDTO.getPhoneNumber1());
        existingCustomer.setPhoneNumber2(customerDTO.getPhoneNumber2());
        existingCustomer.setSsn(customerDTO.getSsn());
        existingCustomer.setGovernmentIssuedId(customerDTO.getGovernmentIssuedId());
        existingCustomer.setDateOfBirth(customerDTO.getDateOfBirth());
        existingCustomer.setEftAccountId(customerDTO.getEftAccountId());
        existingCustomer.setPrimaryCardholderIndicator(customerDTO.getPrimaryCardholderIndicator());
        existingCustomer.setFicoCreditScore(customerDTO.getFicoCreditScore());
        
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToDTO(updatedCustomer);
    }
    
    @Transactional
    public void deleteCustomer(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }
    
    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setMiddleName(customer.getMiddleName());
        dto.setLastName(customer.getLastName());
        dto.setAddressLine1(customer.getAddressLine1());
        dto.setAddressLine2(customer.getAddressLine2());
        dto.setAddressLine3(customer.getAddressLine3());
        dto.setStateCode(customer.getStateCode());
        dto.setCountryCode(customer.getCountryCode());
        dto.setZipCode(customer.getZipCode());
        dto.setPhoneNumber1(customer.getPhoneNumber1());
        dto.setPhoneNumber2(customer.getPhoneNumber2());
        dto.setSsn(customer.getSsn());
        dto.setGovernmentIssuedId(customer.getGovernmentIssuedId());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setEftAccountId(customer.getEftAccountId());
        dto.setPrimaryCardholderIndicator(customer.getPrimaryCardholderIndicator());
        dto.setFicoCreditScore(customer.getFicoCreditScore());
        return dto;
    }
    
    private Customer convertToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        customer.setFirstName(dto.getFirstName());
        customer.setMiddleName(dto.getMiddleName());
        customer.setLastName(dto.getLastName());
        customer.setAddressLine1(dto.getAddressLine1());
        customer.setAddressLine2(dto.getAddressLine2());
        customer.setAddressLine3(dto.getAddressLine3());
        customer.setStateCode(dto.getStateCode());
        customer.setCountryCode(dto.getCountryCode());
        customer.setZipCode(dto.getZipCode());
        customer.setPhoneNumber1(dto.getPhoneNumber1());
        customer.setPhoneNumber2(dto.getPhoneNumber2());
        customer.setSsn(dto.getSsn());
        customer.setGovernmentIssuedId(dto.getGovernmentIssuedId());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setEftAccountId(dto.getEftAccountId());
        customer.setPrimaryCardholderIndicator(dto.getPrimaryCardholderIndicator());
        customer.setFicoCreditScore(dto.getFicoCreditScore());
        return customer;
    }
}
