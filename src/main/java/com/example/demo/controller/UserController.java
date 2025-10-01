package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users in the CardDemo application")
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users with default 10 per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Unable to lookup User")
    })
    @GetMapping
    public ResponseEntity<UserPageResponse> getAllUsers(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            log.info("Getting all users - page: {}, size: {}", page, size);
            UserPageResponse response = userService.getAllUsers(page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting all users: {}", e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Get users starting from ID", description = "Retrieve users starting from a specific User ID with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Unable to lookup User")
    })
    @GetMapping("/from/{startUserId}")
    public ResponseEntity<UserPageResponse> getUsersStartingFromId(
            @Parameter(description = "Starting User ID", example = "USR001", required = true)
            @PathVariable String startUserId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            log.info("Getting users starting from ID: {} - page: {}, size: {}", startUserId, page, size);
            UserPageResponse response = userService.getUsersStartingFromId(startUserId, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting users starting from ID: {}", e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User ID NOT found"),
        @ApiResponse(responseCode = "400", description = "User ID can NOT be empty"),
        @ApiResponse(responseCode = "500", description = "Unable to lookup User")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", example = "USR001", required = true)
            @PathVariable String id) {
        
        try {
            log.info("Getting user by ID: {}", id);
            return userService.getUserById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error getting user by ID: {}", e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Create a new user", description = "Create a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User has been added"),
        @ApiResponse(responseCode = "400", description = "User ID already exist or validation error"),
        @ApiResponse(responseCode = "500", description = "Unable to Add User")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        
        try {
            log.info("Creating new user with ID: {}", request.getSecUsrId());
            UserResponse response = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            throw e;
        }
    }
    
    @Operation(summary = "Update an existing user", description = "Update user details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User has been updated"),
        @ApiResponse(responseCode = "400", description = "User ID NOT found or Please modify to update or validation error"),
        @ApiResponse(responseCode = "500", description = "Unable to Update User")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "User ID", example = "USR001", required = true)
            @PathVariable String id, 
            @Valid @RequestBody UpdateUserRequest request) {
        
        try {
            log.info("Updating user with ID: {}", id);
            UserResponse response = userService.updateUser(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            throw e;
        }
    }
    
    @Operation(summary = "Delete a user", description = "Delete a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User has been deleted"),
        @ApiResponse(responseCode = "400", description = "User ID can NOT be empty"),
        @ApiResponse(responseCode = "404", description = "User ID NOT found"),
        @ApiResponse(responseCode = "500", description = "Unable to lookup User")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDeleteResponse> deleteUser(
            @Parameter(description = "User ID", example = "USR001", required = true)
            @PathVariable String id) {
        
        try {
            log.info("Deleting user with ID: {}", id);
            UserDeleteResponse response = userService.deleteUser(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Search users", description = "Search users with filters and pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search results"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "500", description = "Unable to lookup User")
    })
    @GetMapping("/search")
    public ResponseEntity<UserPageResponse> searchUsers(
            @Parameter(description = "User ID to search", example = "USR001")
            @RequestParam(required = false) String secUsrId,
            @Parameter(description = "First name to search", example = "John")
            @RequestParam(required = false) String secUsrFname,
            @Parameter(description = "Last name to search", example = "Doe")
            @RequestParam(required = false) String secUsrLname,
            @Parameter(description = "User type to search", example = "A")
            @RequestParam(required = false) String secUsrType,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            log.info("Searching users with filters - secUsrId: {}, secUsrFname: {}, secUsrLname: {}, secUsrType: {}, page: {}, size: {}", 
                    secUsrId, secUsrFname, secUsrLname, secUsrType, page, size);
            
            UserSearchRequest searchRequest = new UserSearchRequest(secUsrId, secUsrFname, secUsrLname, secUsrType);
            UserPageResponse response = userService.searchUsers(searchRequest, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching users: {}", e.getMessage());
            throw e;
        }
    }
}