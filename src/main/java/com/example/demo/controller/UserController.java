package com.example.demo.controller;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UserSearchDTO;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users with COBOL-style operations")
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "List users with pagination and filtering", 
               description = "Retrieve a paginated list of users with optional filtering by User ID prefix. Supports PF7/PF8 style navigation.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters - check pagination or filter values"),
        @ApiResponse(responseCode = "500", description = "Internal system error - contact administrator")
    })
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @Parameter(description = "User ID prefix to filter by", example = "USR")
            @RequestParam(required = false) String userIdPrefix,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "userId")
            @RequestParam(defaultValue = "userId") String sortBy,
            @Parameter(description = "Sort direction", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("Retrieving users - page: {}, size: {}, userIdPrefix: {}", page, size, userIdPrefix);
        
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection);
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<UserResponse> users = userService.getAllUsers(userIdPrefix, pageable);
            
            log.info("Retrieved {} users out of {} total", users.getNumberOfElements(), users.getTotalElements());
            return ResponseEntity.ok(users);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid request parameters: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid sort direction or field - use ASC/DESC for direction");
        } catch (Exception e) {
            log.error("Error retrieving users: {}", e.getMessage());
            throw new RuntimeException("System error occurred while retrieving users");
        }
    }
    
    @Operation(summary = "Get user by exact User ID", 
               description = "Retrieve a specific user by their exact User ID match")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found - no record exists with specified User ID"),
        @ApiResponse(responseCode = "400", description = "Invalid User ID format - must be 8 characters or less"),
        @ApiResponse(responseCode = "500", description = "Internal system error - contact administrator")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID to retrieve", example = "USR00001", required = true)
            @PathVariable String userId) {
        
        log.info("Retrieving user with ID: {}", userId);
        
        try {
            if (userId == null || userId.trim().isEmpty()) {
                log.warn("Empty or null User ID provided");
                throw new IllegalArgumentException("User ID cannot be empty");
            }
            
            if (userId.length() > 8) {
                log.warn("User ID too long: {}", userId);
                throw new IllegalArgumentException("User ID cannot exceed 8 characters");
            }
            
            Optional<UserResponse> user = userService.getUserById(userId);
            
            if (user.isPresent()) {
                log.info("User found: {}", userId);
                return ResponseEntity.ok(user.get());
            } else {
                log.warn("User not found: {}", userId);
                return ResponseEntity.notFound().build();
            }
            
        } catch (UserService.UserNotFoundException e) {
            log.warn("User not found: {}", userId);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.error("Invalid User ID format: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error retrieving user {}: {}", userId, e.getMessage());
            throw new RuntimeException("System error occurred while retrieving user");
        }
    }
    
    @Operation(summary = "Create a new user", 
               description = "Add a new user to the system with validation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - check required fields and formats"),
        @ApiResponse(responseCode = "409", description = "User already exists - User ID must be unique"),
        @ApiResponse(responseCode = "500", description = "Internal system error - contact administrator")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "User creation request", required = true)
            @Valid @RequestBody CreateUserRequest request) {
        
        log.info("Creating new user with ID: {}", request.getUserId());
        
        try {
            UserResponse response = userService.createUser(request);
            log.info("User created successfully: {}", response.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (UserService.DuplicateUserIdException e) {
            log.error("User creation failed - duplicate ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            log.error("User creation failed - validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            throw new RuntimeException("System error occurred while creating user");
        }
    }
    
    @Operation(summary = "Update an existing user", 
               description = "Update user details by User ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - check field formats and constraints"),
        @ApiResponse(responseCode = "404", description = "User not found - no record exists with specified User ID"),
        @ApiResponse(responseCode = "500", description = "Internal system error - contact administrator")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "User ID to update", example = "USR00001", required = true)
            @PathVariable String userId,
            @Parameter(description = "User update request", required = true)
            @Valid @RequestBody UpdateUserRequest request) {
        
        log.info("Updating user with ID: {}", userId);
        
        try {
            if (userId == null || userId.trim().isEmpty()) {
                log.warn("Empty or null User ID provided for update");
                throw new IllegalArgumentException("User ID cannot be empty");
            }
            
            if (userId.length() > 8) {
                log.warn("User ID too long for update: {}", userId);
                throw new IllegalArgumentException("User ID cannot exceed 8 characters");
            }
            
            UserResponse response = userService.updateUser(userId, request);
            log.info("User updated successfully: {}", userId);
            return ResponseEntity.ok(response);
            
        } catch (UserService.UserNotFoundException e) {
            log.warn("User not found for update: {}", userId);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.error("User update failed - validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating user {}: {}", userId, e.getMessage());
            throw new RuntimeException("System error occurred while updating user");
        }
    }
    
    @Operation(summary = "Delete a user", 
               description = "Delete a user by User ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found - no record exists with specified User ID"),
        @ApiResponse(responseCode = "400", description = "Invalid User ID format - must be 8 characters or less"),
        @ApiResponse(responseCode = "500", description = "Internal system error - contact administrator")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID to delete", example = "USR00001", required = true)
            @PathVariable String userId) {
        
        log.info("Deleting user with ID: {}", userId);
        
        try {
            if (userId == null || userId.trim().isEmpty()) {
                log.warn("Empty or null User ID provided for deletion");
                throw new IllegalArgumentException("User ID cannot be empty");
            }
            
            if (userId.length() > 8) {
                log.warn("User ID too long for deletion: {}", userId);
                throw new IllegalArgumentException("User ID cannot exceed 8 characters");
            }
            
            userService.deleteUser(userId);
            log.info("User deleted successfully: {}", userId);
            return ResponseEntity.noContent().build();
            
        } catch (UserService.UserNotFoundException e) {
            log.warn("User not found for deletion: {}", userId);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.error("User deletion failed - validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error deleting user {}: {}", userId, e.getMessage());
            throw new RuntimeException("System error occurred while deleting user");
        }
    }
    
    @Operation(summary = "Advanced user search", 
               description = "Search users with multiple criteria and pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search criteria or pagination parameters"),
        @ApiResponse(responseCode = "500", description = "Internal system error - contact administrator")
    })
    @PostMapping("/search")
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @Parameter(description = "Search criteria", required = true)
            @Valid @RequestBody UserSearchDTO searchCriteria) {
        
        log.info("Searching users with criteria: {}", searchCriteria);
        
        try {
            Sort.Direction direction = Sort.Direction.fromString(searchCriteria.getSortDirection());
            Pageable pageable = PageRequest.of(
                searchCriteria.getPage(), 
                searchCriteria.getSize(), 
                Sort.by(direction, searchCriteria.getSortBy())
            );
            
            Page<UserResponse> users = userService.searchUsers(searchCriteria, pageable);
            
            log.info("Search returned {} users out of {} total", 
                users.getNumberOfElements(), users.getTotalElements());
            return ResponseEntity.ok(users);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid search parameters: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error searching users: {}", e.getMessage());
            throw new RuntimeException("System error occurred while searching users");
        }
    }
}