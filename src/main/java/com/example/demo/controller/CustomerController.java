package com.example.demo.controller;

import com.example.demo.dto.CustomerResponseDto;
import com.example.demo.dto.CreateCustomerRequestDto;
import com.example.demo.dto.UpdateCustomerRequestDto;
import com.example.demo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> getAllCustomers(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all customers with pagination: {}", pageable);
        Page<CustomerResponseDto> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable String id) {
        log.info("Retrieving customer by ID: {}", id);
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CreateCustomerRequestDto request) {
        log.info("Creating new customer with ID: {}", request.getCustomerId());
        CustomerResponseDto response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable String id,
            @Valid @RequestBody UpdateCustomerRequestDto request) {
        log.info("Updating customer with ID: {}", id);
        CustomerResponseDto response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        log.info("Deleting customer with ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
