package com.example.demo.controller;

import com.example.demo.dto.SignonRequest;
import com.example.demo.dto.SignonResponse;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.dto.UserCreateResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.service.UserSecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Security Management", description = "APIs for user authentication and management")
@RequestMapping("/api")
public class UserController {
    
    private final UserSecurityService userSecurityService;
    
    @Operation(summary = "User authentication", description = "Authenticate user with credentials (COSGN00C)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/auth/signin")
    public ResponseEntity<SignonResponse> signIn(@Valid @RequestBody SignonRequest request) {
        log.info("Authentication attempt for user: {}", request.getUserId());
        SignonResponse response = userSecurityService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Create a new user", description = "Create a new user in the system (COUSR01C)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "User ID already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/users")
    public ResponseEntity<UserCreateResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        UserCreateResponse response = userSecurityService.createUser(request);
        
        if ("SUCCESS".equals(response.getResult())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
        log.info("Retrieving user with ID: {}", userId);
        return userSecurityService.getUserById(userId.toUpperCase())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Update an existing user", description = "Update user details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String userId, 
                                                  @Valid @RequestBody UserCreateRequest request) {
        log.info("Updating user with ID: {}", userId);
        try {
            UserResponse response = userSecurityService.updateUser(userId.toUpperCase(), request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("User not found for update: {}", userId);
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Delete a user", description = "Delete a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        log.info("Deleting user with ID: {}", userId);
        try {
            userSecurityService.deleteUser(userId.toUpperCase());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("User not found for deletion: {}", userId);
            return ResponseEntity.notFound().build();
        }
    }
}