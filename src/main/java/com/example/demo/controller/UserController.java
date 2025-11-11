package com.example.demo.controller;

import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.dto.UpdateUserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.enums.UserType;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController
 * 
 * REST API controller for User management.
 * Provides endpoints for CRUD operations and permission checking.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing system users")
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
        log.info("Fetching all users with pagination");
        Page<UserResponseDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "User ID (alphanumeric, max 20 characters)", required = true)
            @PathVariable String userId) {
        log.info("Fetching user with ID: {}", userId);
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new user", description = "Create a new system user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - user type must be ADMIN or REGULAR"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody CreateUserRequestDto request) {
        log.info("Creating new user");
        UserResponseDto response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing user", description = "Update user details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @Parameter(description = "User ID (alphanumeric, max 20 characters)", required = true)
            @PathVariable String userId,
            @Valid @RequestBody UpdateUserRequestDto request) {
        log.info("Updating user with ID: {}", userId);
        UserResponseDto response = userService.updateUser(userId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a user", description = "Delete a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID (alphanumeric, max 20 characters)", required = true)
            @PathVariable String userId) {
        log.info("Deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get users by type", 
               description = "Retrieve users filtered by user type. Implements BR001 - User Permission Based Card Access.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid user type"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/type/{userType}")
    public ResponseEntity<Page<UserResponseDto>> getUsersByType(
            @Parameter(description = "User type (ADMIN or REGULAR)", required = true)
            @PathVariable String userType,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Fetching users by type: {}", userType);
        UserType type = UserType.fromCode(userType);
        Page<UserResponseDto> users = userService.getUsersByType(type, pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Check if user is admin", 
               description = "Check if a user has admin privileges. Implements BR001 - User Permission Based Card Access.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}/is-admin")
    public ResponseEntity<Boolean> isAdminUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        log.info("Checking if user {} is admin", userId);
        boolean isAdmin = userService.isAdminUser(userId);
        return ResponseEntity.ok(isAdmin);
    }

    @Operation(summary = "Check if user can view all cards", 
               description = "Check if a user can view all credit cards without context. Implements BR001 - User Permission Based Card Access.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}/can-view-all-cards")
    public ResponseEntity<Boolean> canViewAllCards(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        log.info("Checking if user {} can view all cards", userId);
        boolean canView = userService.canViewAllCards(userId);
        return ResponseEntity.ok(canView);
    }

    @Operation(summary = "Check if user requires account context", 
               description = "Check if a user requires account context to view cards. Implements BR001 - User Permission Based Card Access.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}/requires-account-context")
    public ResponseEntity<Boolean> requiresAccountContext(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        log.info("Checking if user {} requires account context", userId);
        boolean requires = userService.requiresAccountContext(userId);
        return ResponseEntity.ok(requires);
    }

    @Operation(summary = "Get accessible account IDs", 
               description = "Get list of account IDs accessible by a user. Implements BR001 - User Permission Based Card Access. Admin users get all accounts, regular users get only their assigned accounts.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of account IDs"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}/accessible-accounts")
    public ResponseEntity<List<String>> getAccessibleAccountIds(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        log.info("Fetching accessible account IDs for user: {}", userId);
        List<String> accountIds = userService.getAccessibleAccountIds(userId);
        return ResponseEntity.ok(accountIds);
    }
}
