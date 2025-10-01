package com.example.demo.controller;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UserListResponse;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users based on CICS programs COUSR00C, COUSR01C, COUSR02C, COUSR03C")
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "Get paginated list of users (COUSR00C)", 
               description = "Retrieve a paginated list of users with maximum 10 per page. Supports filtering by starting User ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<UserListResponse>> getAllUsers(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (maximum 10)", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Starting User ID for filtering", example = "USR00001")
            @RequestParam(required = false) String startingUserId) {
        
        log.info("Getting users - page: {}, size: {}, startingUserId: {}", page, size, startingUserId);
        
        if (size > 10) {
            size = 10;
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserListResponse> users = userService.getAllUsers(startingUserId, pageable);
        
        log.info("Retrieved {} users out of {} total", users.getNumberOfElements(), users.getTotalElements());
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Get user by ID for update (COUSR02C)", 
               description = "Retrieve a user by their ID for update screen display")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{secUsrId}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", example = "USR00001", required = true)
            @PathVariable String secUsrId) {
        
        log.info("Getting user by ID: {}", secUsrId);
        
        return userService.getUserById(secUsrId)
                .map(user -> {
                    log.info("User found: {}", user.getSecUsrId());
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    log.warn("User not found with ID: {}", secUsrId);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @Operation(summary = "Create a new user (COUSR01C)", 
               description = "Create a new user with validation of all mandatory fields")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or duplicate User ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "User creation request", required = true)
            @Valid @RequestBody CreateUserRequest request) {
        
        log.info("Creating new user with ID: {}", request.getSecUsrId());
        
        try {
            UserResponse response = userService.createUser(request);
            log.info("User created successfully: {}", response.getSecUsrId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Error creating user: {}", e.getMessage());
            throw e;
        }
    }
    
    @Operation(summary = "Update an existing user (COUSR02C)", 
               description = "Update user details by ID with validation of changes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or no changes detected"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{secUsrId}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "User ID", example = "USR00001", required = true)
            @PathVariable String secUsrId,
            @Parameter(description = "User update request", required = true)
            @Valid @RequestBody UpdateUserRequest request) {
        
        log.info("Updating user with ID: {}", secUsrId);
        
        try {
            UserResponse response = userService.updateUser(secUsrId, request);
            log.info("User updated successfully: {}", response.getSecUsrId());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error updating user: {}", e.getMessage());
            throw e;
        }
    }
    
    @Operation(summary = "Delete a user (COUSR03C)", 
               description = "Delete a user by ID after confirmation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{secUsrId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "USR00001", required = true)
            @PathVariable String secUsrId) {
        
        log.info("Deleting user with ID: {}", secUsrId);
        
        try {
            userService.deleteUser(secUsrId);
            log.info("User deleted successfully: {}", secUsrId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error deleting user: {}", e.getMessage());
            throw e;
        }
    }
}