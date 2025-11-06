package com.example.demo.controller;

import com.example.demo.dto.CreateCustomerRequestDto;
import com.example.demo.dto.CustomerResponseDto;
import com.example.demo.dto.UpdateCustomerRequestDto;
import com.example.demo.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customer master data including demographic information, addresses, phone numbers, SSN, and credit scores")
@RequestMapping("/api/customers")
public class CustomerController {
    
    private final CustomerService customerService;
    
    @Operation(summary = "Get all customers", description = "Retrieve a paginated list of all customers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of customers"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> getAllCustomers(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all customers with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<CustomerResponseDto> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Get customer by ID", description = "Retrieve a customer by their ID using random access pattern")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of customer"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        log.info("Retrieving customer by ID: {}", id);
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new customer", description = "Create a new customer with demographic information, addresses, contact information, SSN, and credit scores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - customer ID must be 9 characters in numeric format"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CreateCustomerRequestDto request) {
        log.info("Creating new customer with ID: {}", request.getCustomerId());
        CustomerResponseDto response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update an existing customer", description = "Update customer details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - customer ID must be 9 characters in numeric format"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable Long id, 
                                                  @RequestBody UpdateCustomerRequestDto request) {
        log.info("Updating customer with ID: {}", id);
        CustomerResponseDto response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete a customer", description = "Delete a customer by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.info("Deleting customer with ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}