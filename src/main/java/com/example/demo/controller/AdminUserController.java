package com.example.demo.controller;

import com.example.demo.dto.AdminUserResponseDto;
import com.example.demo.dto.CreateAdminUserRequestDto;
import com.example.demo.dto.UpdateAdminUserRequestDto;
import com.example.demo.service.AdminUserService;
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
@Tag(name = "Admin User Management", description = "APIs for managing admin users in CardDemo application")
@RequestMapping("/api/admin-users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(summary = "Get all admin users", description = "Retrieve a paginated list of all admin users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<AdminUserResponseDto>> getAllAdminUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all admin users with pagination: {}", pageable);
        Page<AdminUserResponseDto> adminUsers = adminUserService.getAllAdminUsers(pageable);
        return ResponseEntity.ok(adminUsers);
    }

    @Operation(summary = "Get admin user by ID", description = "Retrieve an admin user by their user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin user"),
        @ApiResponse(responseCode = "400", description = "User ID cannot be empty or spaces"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<AdminUserResponseDto> getAdminUserById(@PathVariable String userId) {
        log.info("Retrieving admin user by ID: {}", userId);
        return adminUserService.getAdminUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new admin user", description = "Create a new admin user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Admin user created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AdminUserResponseDto> createAdminUser(
            @Valid @RequestBody CreateAdminUserRequestDto request) {
        log.info("Creating new admin user with ID: {}", request.getUserId());
        AdminUserResponseDto response = adminUserService.createAdminUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing admin user", description = "Update admin user details by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin user updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<AdminUserResponseDto> updateAdminUser(
            @PathVariable String userId,
            @Valid @RequestBody UpdateAdminUserRequestDto request) {
        log.info("Updating admin user with ID: {}", userId);
        AdminUserResponseDto response = adminUserService.updateAdminUser(userId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an admin user", description = "Delete an admin user by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Admin user deleted successfully"),
        @ApiResponse(responseCode = "400", description = "User ID cannot be empty or spaces"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable String userId) {
        log.info("Deleting admin user with ID: {}", userId);
        adminUserService.deleteAdminUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Authenticate admin user", description = "Check if admin user is authenticated")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication status retrieved"),
        @ApiResponse(responseCode = "400", description = "User ID cannot be empty or spaces"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}/authenticate")
    public ResponseEntity<Boolean> authenticateAdminUser(@PathVariable String userId) {
        log.info("Authenticating admin user with ID: {}", userId);
        boolean isAuthenticated = adminUserService.authenticateAdminUser(userId);
        return ResponseEntity.ok(isAuthenticated);
    }

    @Operation(summary = "Validate admin access", description = "Validate if user has admin access privileges")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin access validated successfully"),
        @ApiResponse(responseCode = "400", description = "User ID cannot be empty or spaces"),
        @ApiResponse(responseCode = "401", description = "User does not have admin access"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{userId}/validate-access")
    public ResponseEntity<Void> validateAdminAccess(@PathVariable String userId) {
        log.info("Validating admin access for user ID: {}", userId);
        adminUserService.validateAdminAccess(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Set authentication status", description = "Set authentication status for an admin user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication status updated successfully"),
        @ApiResponse(responseCode = "400", description = "User ID cannot be empty or spaces"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{userId}/authentication-status")
    public ResponseEntity<AdminUserResponseDto> setAuthenticationStatus(
            @PathVariable String userId,
            @RequestParam boolean status) {
        log.info("Setting authentication status for user ID: {} to {}", userId, status);
        adminUserService.setAuthenticationStatus(userId, status);
        return adminUserService.getAdminUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
