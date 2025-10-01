package com.example.demo.controller;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UserSearchRequest;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "User Management", description = "APIs for managing users")
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users (COUSR00C)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Retrieving all users with pagination: page={}, size={}", 
                pageable.getPageNumber(), pageable.getPageSize());
        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Search users", description = "Search users with filtering by User ID (COUSR00C)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search of users"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @Valid UserSearchRequest searchRequest,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Searching users with criteria: {}, page={}, size={}", 
                searchRequest, pageable.getPageNumber(), pageable.getPageSize());
        Page<UserResponse> users = userService.searchUsers(searchRequest, pageable);
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID for editing (COUSR02C)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        log.info("Retrieving user by ID: {}", id);
        return userService.getUserById(id)
                .map(user -> {
                    log.info("User found: {}", user.getId());
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    log.warn("User not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @Operation(summary = "Create a new user", description = "Create a new user (COUSR01C)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or validation errors"),
        @ApiResponse(responseCode = "409", description = "User with the same ID already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Creating new user with ID: {}", request.getId());
        try {
            UserResponse response = userService.createUser(request);
            log.info("User created successfully: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.warn("User creation failed - duplicate ID: {}", request.getId());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @Operation(summary = "Update an existing user", description = "Update user details by ID (COUSR02C)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or validation errors"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, 
                                                  @Valid @RequestBody UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);
        try {
            UserResponse response = userService.updateUser(id, request);
            log.info("User updated successfully: {}", response.getId());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("User update failed - user not found: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Delete a user", description = "Delete a user by ID (COUSR03C)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("Deleting user with ID: {}", id);
        try {
            userService.deleteUser(id);
            log.info("User deleted successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("User deletion failed - user not found: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}