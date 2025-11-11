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

import java.util.List;

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
        @ApiResponse(responseCode = "404", description = "Admin user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<AdminUserResponseDto> getAdminUserById(@PathVariable String userId) {
        log.info("Retrieving admin user with ID: {}", userId);
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
    public ResponseEntity<AdminUserResponseDto> createAdminUser(@RequestBody CreateAdminUserRequestDto request) {
        log.info("Creating new admin user with ID: {}", request.getUserId());
        AdminUserResponseDto response = adminUserService.createAdminUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing admin user", description = "Update admin user details by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin user updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Admin user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<AdminUserResponseDto> updateAdminUser(
            @PathVariable String userId,
            @RequestBody UpdateAdminUserRequestDto request) {
        log.info("Updating admin user with ID: {}", userId);
        AdminUserResponseDto response = adminUserService.updateAdminUser(userId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an admin user", description = "Delete an admin user by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Admin user deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Admin user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable String userId) {
        log.info("Deleting admin user with ID: {}", userId);
        adminUserService.deleteAdminUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Authenticate admin user", description = "Authenticate an admin user and set their authentication status to true")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin user authenticated successfully"),
        @ApiResponse(responseCode = "404", description = "Admin user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{userId}/authenticate")
    public ResponseEntity<AdminUserResponseDto> authenticateAdminUser(@PathVariable String userId) {
        log.info("Authenticating admin user with ID: {}", userId);
        AdminUserResponseDto response = adminUserService.authenticateAdminUser(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deauthenticate admin user", description = "Deauthenticate an admin user and set their authentication status to false")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin user deauthenticated successfully"),
        @ApiResponse(responseCode = "404", description = "Admin user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{userId}/deauthenticate")
    public ResponseEntity<AdminUserResponseDto> deauthenticateAdminUser(@PathVariable String userId) {
        log.info("Deauthenticating admin user with ID: {}", userId);
        AdminUserResponseDto response = adminUserService.deauthenticateAdminUser(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check authentication status", description = "Check if an admin user is currently authenticated")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication status retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Admin user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}/is-authenticated")
    public ResponseEntity<Boolean> isAdminUserAuthenticated(@PathVariable String userId) {
        log.info("Checking authentication status for admin user with ID: {}", userId);
        Boolean isAuthenticated = adminUserService.isAdminUserAuthenticated(userId);
        return ResponseEntity.ok(isAuthenticated);
    }

    @Operation(summary = "Get all authenticated admin users", description = "Retrieve a list of all admin users who are currently authenticated")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of authenticated admin users"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/authenticated")
    public ResponseEntity<List<AdminUserResponseDto>> getAllAuthenticatedAdminUsers() {
        log.info("Retrieving all authenticated admin users");
        List<AdminUserResponseDto> authenticatedUsers = adminUserService.getAllAuthenticatedAdminUsers();
        return ResponseEntity.ok(authenticatedUsers);
    }

    @Operation(summary = "Get all unauthenticated admin users", description = "Retrieve a list of all admin users who are not currently authenticated")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of unauthenticated admin users"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/unauthenticated")
    public ResponseEntity<List<AdminUserResponseDto>> getAllUnauthenticatedAdminUsers() {
        log.info("Retrieving all unauthenticated admin users");
        List<AdminUserResponseDto> unauthenticatedUsers = adminUserService.getAllUnauthenticatedAdminUsers();
        return ResponseEntity.ok(unauthenticatedUsers);
    }

    @Operation(summary = "Deauthenticate all admin users", description = "Deauthenticate all admin users in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All admin users deauthenticated successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/deauthenticate-all")
    public ResponseEntity<Void> deauthenticateAllAdminUsers() {
        log.info("Deauthenticating all admin users");
        adminUserService.deauthenticateAllAdminUsers();
        return ResponseEntity.ok().build();
    }
}
