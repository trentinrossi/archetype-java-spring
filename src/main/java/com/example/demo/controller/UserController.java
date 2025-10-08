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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users with optional search")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers(
            @Parameter(description = "Search term for user names") @RequestParam(required = false) String search,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Getting all users with search: {} and pageable: {}", search, pageable);
        
        UserListResponse users;
        if (StringUtils.hasText(search)) {
            users = userService.searchUsers(search, pageable);
        } else {
            users = userService.getAllUsers(pageable);
        }
        
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID of the user to retrieve") @PathVariable String userId) {
        log.info("Getting user by ID: {}", userId);
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new user", description = "Create a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "User with ID already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        UserResponse response = userService.createUser(request);
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
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable String userId, 
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("Updating user with ID: {}", userId);
        UserResponse response = userService.updateUser(userId, request);
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
            @Parameter(description = "ID of the user to delete") @PathVariable String userId) {
        log.info("Deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search users by name", description = "Search users by first name or last name with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search results"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<UserListResponse> searchUsers(
            @Parameter(description = "Search term for user names", required = true) @RequestParam String name,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Searching users by name: {} with pageable: {}", name, pageable);
        UserListResponse users = userService.searchUsers(name, pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get users by type", description = "Retrieve users filtered by user type with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users by type"),
        @ApiResponse(responseCode = "400", description = "Invalid user type"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/type/{userType}")
    public ResponseEntity<UserListResponse> getUsersByType(
            @Parameter(description = "Type of users to retrieve (A=Admin, R=Regular)") @PathVariable String userType,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Getting users by type: {} with pageable: {}", userType, pageable);
        UserListResponse users = userService.getUsersByType(userType, pageable);
        return ResponseEntity.ok(users);
    }
}