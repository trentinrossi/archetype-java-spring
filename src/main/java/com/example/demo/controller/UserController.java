package com.example.demo.controller;

import com.example.demo.dto.*;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Security Management", description = "APIs for user authentication and management")
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "User sign in", description = "Authenticate user with credentials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Authentication failed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody UserSignOnRequest request, BindingResult bindingResult) {
        log.info("User sign in attempt for userId: {}", request.getUserId());
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(errors);
        }
        
        try {
            UserSignOnResponse response = userService.signOnUser(request);
            if (response.isSuccess()) {
                log.info("User sign in successful for userId: {}", request.getUserId());
                return ResponseEntity.ok(response);
            } else {
                log.warn("User sign in failed for userId: {}", request.getUserId());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            log.error("Error during sign in for userId: {}", request.getUserId(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Authentication failed");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @Operation(summary = "Create a new user", description = "Create a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "User already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateRequest request, BindingResult bindingResult) {
        log.info("Creating new user with userId: {}", request.getUserId());
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(errors);
        }
        
        try {
            UserResponse response = userService.createUser(request);
            log.info("User created successfully with userId: {}", request.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.warn("User creation failed for userId: {}", request.getUserId(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "User creation failed");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (Exception e) {
            log.error("Error creating user with userId: {}", request.getUserId(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "Failed to create user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @Operation(summary = "Update an existing user", description = "Update user details by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, 
                                       @Valid @RequestBody UserUpdateRequest request, 
                                       BindingResult bindingResult) {
        log.info("Updating user with userId: {}", userId);
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(errors);
        }
        
        try {
            request.setUserId(userId);
            UserResponse response = userService.updateUser(request);
            log.info("User updated successfully with userId: {}", userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("User update failed for userId: {}", userId, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            log.error("Error updating user with userId: {}", userId, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "Failed to update user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @Operation(summary = "Delete a user", description = "Delete a user by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        log.info("Deleting user with userId: {}", userId);
        
        try {
            UserDeleteRequest deleteRequest = new UserDeleteRequest(userId);
            userService.deleteUser(deleteRequest);
            log.info("User deleted successfully with userId: {}", userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("User deletion failed for userId: {}", userId, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            log.error("Error deleting user with userId: {}", userId, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "Failed to delete user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        log.info("Retrieving user with userId: {}", userId);
        
        try {
            return userService.getUserById(userId)
                    .map(user -> {
                        log.info("User found with userId: {}", userId);
                        return ResponseEntity.ok(user);
                    })
                    .orElseGet(() -> {
                        log.warn("User not found with userId: {}", userId);
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "User not found");
                        error.put("message", "User with ID " + userId + " does not exist");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                    });
        } catch (Exception e) {
            log.error("Error retrieving user with userId: {}", userId, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "Failed to retrieve user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<?> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all users with pagination - page: {}, size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        try {
            Page<UserResponse> users = userService.getAllUsers(pageable);
            log.info("Retrieved {} users out of {} total users", 
                    users.getNumberOfElements(), users.getTotalElements());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error retrieving users", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "Failed to retrieve users");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}