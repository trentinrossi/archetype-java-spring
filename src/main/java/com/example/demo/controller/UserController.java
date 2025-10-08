package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Security Administration", description = "APIs for user security administration and authentication")
@RequestMapping("/api")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "Add new user (COUSR01C)", description = "Create a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or user already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        UserResponseDTO response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update user (COUSR02C)", description = "Update an existing user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User ID", required = true) @PathVariable String userId,
            @Valid @RequestBody UserUpdateDTO request) {
        log.info("Updating user with ID: {}", userId);
        UserResponseDTO response = userService.updateUser(userId, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete user with confirmation (COUSR03C)", description = "Delete a user by ID with confirmation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "User cannot be deleted due to business rules"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true) @PathVariable String userId) {
        log.info("Deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "List all users with pagination (COUSR00C)", description = "Retrieve a paginated list of all users with optional filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users")
    public ResponseEntity<UserListDTO> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable,
            @Parameter(description = "Filter by user type (A for Admin, R for Regular)") @RequestParam(required = false) String userType) {
        log.info("Retrieving users with pagination, page: {}, size: {}, userType filter: {}", 
                pageable.getPageNumber(), pageable.getPageSize(), userType);
        
        UserListDTO response;
        if (userType != null && !userType.trim().isEmpty()) {
            response = userService.getUsersByType(userType, pageable);
        } else {
            response = userService.getAllUsers(pageable);
        }
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "User ID", required = true) @PathVariable String userId) {
        log.info("Retrieving user with ID: {}", userId);
        Optional<UserResponseDTO> response = userService.getUserById(userId);
        return response.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "User authentication (COSGN00C)", description = "Authenticate user credentials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "401", description = "Authentication failed - invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/auth/signin")
    public ResponseEntity<UserAuthResponseDTO> authenticateUser(@Valid @RequestBody UserAuthDTO request) {
        log.info("Authenticating user with ID: {}", request.getUserId());
        UserAuthResponseDTO response = userService.authenticateUser(request);
        if (response.isAuthenticated()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    @Operation(summary = "Search users by name", description = "Search users by first name or last name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search results"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/search")
    public ResponseEntity<UserListDTO> searchUsers(
            @Parameter(description = "Search term for name") @RequestParam String searchTerm,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Searching users with term: {}", searchTerm);
        UserListDTO response = userService.searchUsersByName(searchTerm, pageable);
        return ResponseEntity.ok(response);
    }
}