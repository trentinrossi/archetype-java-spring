package com.example.demo.controller;

import com.example.demo.dto.CreateTransactionCategoryBalanceRequestDto;
import com.example.demo.dto.UpdateTransactionCategoryBalanceRequestDto;
import com.example.demo.dto.TransactionCategoryBalanceResponseDto;
import com.example.demo.service.TransactionCategoryBalanceService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Transaction Category Balance Management", description = "APIs for managing transaction category balances")
@RequestMapping("/api/transaction-category-balances")
public class TransactionCategoryBalanceController {

    private final TransactionCategoryBalanceService service;

    @Operation(summary = "Get all transaction category balances", description = "Retrieve a paginated list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<TransactionCategoryBalanceResponseDto>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        Page<TransactionCategoryBalanceResponseDto> result = service.getAll(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get transaction category balance by ID", description = "Retrieve by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionCategoryBalanceResponseDto> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new transaction category balance", description = "Create new record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TransactionCategoryBalanceResponseDto> create(@RequestBody CreateTransactionCategoryBalanceRequestDto request) {
        TransactionCategoryBalanceResponseDto response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing transaction category balance", description = "Update by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated successfully"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionCategoryBalanceResponseDto> update(@PathVariable Long id, @RequestBody UpdateTransactionCategoryBalanceRequestDto request) {
        TransactionCategoryBalanceResponseDto response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a transaction category balance", description = "Delete by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
