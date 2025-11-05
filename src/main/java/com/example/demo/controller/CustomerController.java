package com.example.demo.controller;

import com.example.demo.dto.CustomerCreateDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.CustomerUpdateDTO;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;
    
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String customerId) {
        CustomerDTO customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }
    
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByLastName(@PathVariable String lastName) {
        List<CustomerDTO> customers = customerService.getCustomersByLastName(lastName);
        return ResponseEntity.ok(customers);
    }
    
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerCreateDTO createDTO) {
        CustomerDTO created = customerService.createCustomer(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable String customerId,
            @Valid @RequestBody CustomerUpdateDTO updateDTO) {
        CustomerDTO updated = customerService.updateCustomer(customerId, updateDTO);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
