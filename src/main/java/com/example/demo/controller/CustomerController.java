package com.example.demo.controller;

import com.example.demo.dto.CreateCustomerRequestDto;
import com.example.demo.dto.CustomerResponseDto;
import com.example.demo.dto.UpdateCustomerRequestDto;
import com.example.demo.enums.CustomerStatus;
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
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Customer operations
 * Provides endpoints for customer management
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customers")
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
        log.info("GET /api/customers - Fetching all customers");
        Page<CustomerResponseDto> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }
    
    @Operation(summary = "Get customer by ID", description = "Retrieve a customer by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of customer"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable String customerId) {
        log.info("GET /api/customers/{} - Fetching customer", customerId);
        return customerService.getCustomerById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Create a new customer", description = "Create a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CreateCustomerRequestDto request) {
        log.info("POST /api/customers - Creating new customer");
        CustomerResponseDto response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update an existing customer", description = "Update customer details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable String customerId,
            @RequestBody UpdateCustomerRequestDto request) {
        log.info("PUT /api/customers/{} - Updating customer", customerId);
        CustomerResponseDto response = customerService.updateCustomer(customerId, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete a customer", description = "Delete a customer by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        log.info("DELETE /api/customers/{} - Deleting customer", customerId);
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Search customers by name", description = "Search customers by name (first or last)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<CustomerResponseDto>> searchCustomersByName(
            @RequestParam String name,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/customers/search?name={} - Searching customers", name);
        Page<CustomerResponseDto> customers = customerService.searchCustomersByName(name, pageable);
        return ResponseEntity.ok(customers);
    }
    
    @Operation(summary = "Get customers by status", description = "Retrieve customers by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<CustomerResponseDto>> getCustomersByStatus(
            @PathVariable CustomerStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/customers/status/{} - Fetching customers by status", status);
        Page<CustomerResponseDto> customers = customerService.getCustomersByStatus(status, pageable);
        return ResponseEntity.ok(customers);
    }
    
    @Operation(summary = "Get VIP customers", description = "Retrieve all VIP customers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/vip")
    public ResponseEntity<Page<CustomerResponseDto>> getVipCustomers(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/customers/vip - Fetching VIP customers");
        Page<CustomerResponseDto> customers = customerService.getVipCustomers(pageable);
        return ResponseEntity.ok(customers);
    }
    
    @Operation(summary = "Get customers by state", description = "Retrieve customers by state")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid state"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/state/{state}")
    public ResponseEntity<Page<CustomerResponseDto>> getCustomersByState(
            @PathVariable String state,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/customers/state/{} - Fetching customers by state", state);
        Page<CustomerResponseDto> customers = customerService.getCustomersByState(state, pageable);
        return ResponseEntity.ok(customers);
    }
    
    @Operation(summary = "Get customers by credit score range", description = "Retrieve customers by credit score range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/credit-score")
    public ResponseEntity<Page<CustomerResponseDto>> getCustomersByCreditScoreRange(
            @RequestParam Integer minScore,
            @RequestParam Integer maxScore,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/customers/credit-score?minScore={}&maxScore={} - Fetching customers by credit score range", 
                minScore, maxScore);
        Page<CustomerResponseDto> customers = customerService.getCustomersByCreditScoreRange(minScore, maxScore, pageable);
        return ResponseEntity.ok(customers);
    }
}
