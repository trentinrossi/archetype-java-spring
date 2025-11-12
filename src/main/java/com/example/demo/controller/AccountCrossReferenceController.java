package com.example.demo.controller;

import com.example.demo.dto.CreateAccountCrossReferenceRequestDto;
import com.example.demo.dto.UpdateAccountCrossReferenceRequestDto;
import com.example.demo.dto.AccountCrossReferenceResponseDto;
import com.example.demo.service.AccountCrossReferenceService;
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
@Tag(name = "Account Cross Reference Management", description = "APIs for managing account cross references")
@RequestMapping("/api/account-cross-references")
public class AccountCrossReferenceController {

    private final AccountCrossReferenceService service;

    @Operation(summary = "Get all account cross references", description = "Retrieve a paginated list of all account cross references")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<AccountCrossReferenceResponseDto>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        Page<AccountCrossReferenceResponseDto> result = service.getAll(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get account cross reference by card number", description = "Retrieve an account cross reference by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{cardNumber}")
    public ResponseEntity<AccountCrossReferenceResponseDto> getByCardNumber(@PathVariable String cardNumber) {
        return service.getByCardNumber(cardNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new account cross reference", description = "Create a new account cross reference")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountCrossReferenceResponseDto> create(@RequestBody CreateAccountCrossReferenceRequestDto request) {
        AccountCrossReferenceResponseDto response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing account cross reference", description = "Update account cross reference by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated successfully"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{cardNumber}")
    public ResponseEntity<AccountCrossReferenceResponseDto> update(@PathVariable String cardNumber, @RequestBody UpdateAccountCrossReferenceRequestDto request) {
        AccountCrossReferenceResponseDto response = service.update(cardNumber, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an account cross reference", description = "Delete an account cross reference by card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<Void> delete(@PathVariable String cardNumber) {
        service.delete(cardNumber);
        return ResponseEntity.noContent().build();
    }
}
