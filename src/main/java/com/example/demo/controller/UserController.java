package com.example.demo.controller;

import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.dto.UpdateUserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.enums.UserType;
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

/**
 * REST Controller for User management
 * Business Rule BR001: Admin users can view all credit cards when no context is passed.
 * Non-admin users can only view cards associated with their specific account.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing system users and permissions")
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/users - Fetching all users");
        Page<UserResponseDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String userId) {
        log.info("GET /api/users/{} - Fetching user", userId);
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Create a new user", 
               description = "Create a new user with specified type (ADMIN or REGULAR) and account access")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - User ID must not exceed 20 characters"),
        @ApiResponse(responseCode = "409", description = "User already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody CreateUserRequestDto request) {
        log.info("POST /api/users - Creating new user with type: {}", request.getUserType());
        try {
            UserResponseDto response = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Error creating user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Update an existing user", 
               description = "Update user details including type and account access")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UpdateUserRequestDto request) {
        log.info("PUT /api/users/{} - Updating user", userId);
        try {
            UserResponseDto response = userService.updateUser(userId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Delete a user", description = "Delete a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        log.info("DELETE /api/users/{} - Deleting user", userId);
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error deleting user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Get users by type", 
               description = "Retrieve users filtered by type (ADMIN or REGULAR)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid user type"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/type/{userType}")
    public ResponseEntity<Page<UserResponseDto>> getUsersByType(
            @PathVariable UserType userType,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/users/type/{} - Fetching users by type", userType);
        Page<UserResponseDto> users = userService.getUsersByType(userType, pageable);
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Check user access to account", 
               description = "Check if a user has access to a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Access check completed"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}/access/{accountId}")
    public ResponseEntity<Boolean> hasAccessToAccount(
            @PathVariable String userId,
            @PathVariable String accountId) {
        log.info("GET /api/users/{}/access/{} - Checking account access", userId, accountId);
        try {
            boolean hasAccess = userService.hasAccessToAccount(userId, accountId);
            return ResponseEntity.ok(hasAccess);
        } catch (IllegalArgumentException e) {
            log.error("Error checking access: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Check if user is admin", 
               description = "Check if a user has admin privileges")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin check completed"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}/is-admin")
    public ResponseEntity<Boolean> isAdmin(@PathVariable String userId) {
        log.info("GET /api/users/{}/is-admin - Checking admin status", userId);
        try {
            boolean isAdmin = userService.isAdmin(userId);
            return ResponseEntity.ok(isAdmin);
        } catch (IllegalArgumentException e) {
            log.error("Error checking admin status: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Get users by account ID", 
               description = "Retrieve users with access to a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<UserResponseDto>> getUsersByAccountId(
            @PathVariable String accountId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/users/account/{} - Fetching users with access to account", accountId);
        Page<UserResponseDto> users = userService.getUsersByAccountId(accountId, pageable);
        return ResponseEntity.ok(users);
    }
}
