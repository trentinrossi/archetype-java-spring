package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Security Management", description = "APIs for user authentication and management")
public class UserSecurityController {
    
    private final UserSecurityService userSecurityService;
    
    @Operation(summary = "User authentication", description = "Authenticate user with credentials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "401", description = "Authentication failed"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/api/auth/signin")
    public ResponseEntity<SignonResponse> signin(@Valid @RequestBody SignonRequest request) {
        log.info("Authentication attempt for user: {}", request.getUserId());
        
        SignonResponse response = userSecurityService.authenticateUser(request);
        
        if (response.isSuccess()) {
            log.info("Authentication successful for user: {}", request.getUserId());
            return ResponseEntity.ok(response);
        } else {
            log.warn("Authentication failed for user: {}", request.getUserId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    @Operation(summary = "User registration", description = "Register a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "409", description = "User already exists"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/api/users/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        log.info("User registration attempt for user ID: {}", request.getUserId());
        
        UserRegistrationResponse response = userSecurityService.registerUser(request);
        
        if (response.isSuccess()) {
            log.info("User registration successful for user ID: {}", request.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            log.warn("User registration failed for user ID: {}", request.getUserId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
    
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/api/users/{userId}")
    public ResponseEntity<UserSecurityDTO> getUserById(@PathVariable String userId) {
        log.info("Retrieving user by ID: {}", userId);
        
        return userSecurityService.getUserById(userId)
                .map(user -> {
                    log.info("User found: {}", userId);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    log.warn("User not found: {}", userId);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/api/users")
    public ResponseEntity<Page<UserSecurityDTO>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all users with pagination: page={}, size={}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        Page<UserSecurityDTO> users = userSecurityService.getAllUsers(pageable);
        
        log.info("Retrieved {} users", users.getTotalElements());
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Get users by type", description = "Retrieve users by user type with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user type or request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/api/users/type/{userType}")
    public ResponseEntity<Page<UserSecurityDTO>> getUsersByType(
            @PathVariable String userType,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving users by type: {} with pagination: page={}, size={}", 
                userType, pageable.getPageNumber(), pageable.getPageSize());
        
        Page<UserSecurityDTO> users = userSecurityService.getUsersByType(userType, pageable);
        log.info("Retrieved {} users of type {}", users.getTotalElements(), userType);
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Update user", description = "Update user information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/api/users/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserRegistrationRequest request) {
        log.info("Updating user: {}", userId);
        
        boolean updated = userSecurityService.updateUser(userId, request);
        
        if (updated) {
            log.info("User updated successfully: {}", userId);
            return ResponseEntity.ok("User updated successfully");
        } else {
            log.warn("User not found for update: {}", userId);
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Delete user", description = "Delete a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/api/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        log.info("Deleting user: {}", userId);
        
        boolean deleted = userSecurityService.deleteUser(userId);
        
        if (deleted) {
            log.info("User deleted successfully: {}", userId);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("User not found for deletion: {}", userId);
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Check if user exists", description = "Check if a user exists by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User existence check completed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/api/users/exists/{userId}")
    public ResponseEntity<Boolean> userExists(@PathVariable String userId) {
        log.info("Checking if user exists: {}", userId);
        
        boolean exists = userSecurityService.userExists(userId);
        
        log.info("User {} exists: {}", userId, exists);
        return ResponseEntity.ok(exists);
    }
    
    @Operation(summary = "Count users by type", description = "Get count of users by user type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User count retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user type"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/api/users/count/{userType}")
    public ResponseEntity<Long> countUsersByType(@PathVariable String userType) {
        log.info("Counting users by type: {}", userType);
        
        long count = userSecurityService.countUsersByType(userType);
        log.info("Count of users with type {}: {}", userType, count);
        return ResponseEntity.ok(count);
    }
}