package com.example.demo.controller;

import com.example.demo.dto.CreateMerchantRequestDto;
import com.example.demo.dto.MerchantResponseDto;
import com.example.demo.dto.UpdateMerchantRequestDto;
import com.example.demo.service.MerchantService;
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

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Merchant Management", description = "APIs for managing merchants")
@RequestMapping("/api/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    @Operation(summary = "Get all merchants", description = "Retrieve a paginated list of all merchants")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of merchants"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<MerchantResponseDto>> getAllMerchants(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<MerchantResponseDto> merchants = merchantService.getAllMerchants(pageable);
        return ResponseEntity.ok(merchants);
    }

    @Operation(summary = "Get merchant by ID", description = "Retrieve a merchant by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of merchant"),
        @ApiResponse(responseCode = "404", description = "Merchant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponseDto> getMerchantById(@PathVariable String id) {
        return merchantService.getMerchantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new merchant", description = "Create a new merchant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Merchant created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<MerchantResponseDto> createMerchant(
            @Valid @RequestBody CreateMerchantRequestDto request) {
        MerchantResponseDto response = merchantService.createMerchant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing merchant", description = "Update merchant details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Merchant updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Merchant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MerchantResponseDto> updateMerchant(
            @PathVariable String id,
            @Valid @RequestBody UpdateMerchantRequestDto request) {
        MerchantResponseDto response = merchantService.updateMerchant(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a merchant", description = "Delete a merchant by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Merchant deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Merchant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMerchant(@PathVariable String id) {
        merchantService.deleteMerchant(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search merchants by name", description = "Search merchants by name containing search term")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search"),
        @ApiResponse(responseCode = "400", description = "Invalid search term"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<MerchantResponseDto>> searchMerchantsByName(
            @RequestParam String searchTerm,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<MerchantResponseDto> merchants = merchantService.searchMerchantsByName(searchTerm, pageable);
        return ResponseEntity.ok(merchants);
    }

    @Operation(summary = "Get merchants by city", description = "Retrieve all merchants in a specific city")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of merchants"),
        @ApiResponse(responseCode = "400", description = "Invalid city"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/city/{city}")
    public ResponseEntity<Page<MerchantResponseDto>> getMerchantsByCity(
            @PathVariable String city,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<MerchantResponseDto> merchants = merchantService.getMerchantsByCity(city, pageable);
        return ResponseEntity.ok(merchants);
    }

    @Operation(summary = "Validate merchant exists", description = "BR008: Validate that merchant ID exists in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Merchant exists"),
        @ApiResponse(responseCode = "404", description = "Merchant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/validate/{id}")
    public ResponseEntity<Void> validateMerchantExists(@PathVariable String id) {
        merchantService.validateMerchantExists(id);
        return ResponseEntity.ok().build();
    }
}
