package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.example.demo.dto.*;
import com.example.demo.service.UserSecurityService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Security Management", description = "APIs for CardDemo authentication and user security management")
public class UserSecurityController {
    
    private final UserSecurityService userSecurityService;
    
    @Operation(summary = "User Sign-on", description = "Authenticate user with COSGN00C business logic - ENTER key for authentication, PF3 key for exit")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "401", description = "Authentication failed - Wrong password or user not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or invalid key pressed"),
        @ApiResponse(responseCode = "403", description = "User account is inactive"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/api/auth/signon")
    public ResponseEntity<SignonResponseDTO> signon(@Valid @RequestBody SignonRequestDTO request) {
        log.info("Signon attempt for user: {}", request.getUserId());
        
        try {
            SignonResponseDTO response = userSecurityService.authenticateUser(request);
            
            if (response.isSuccess()) {
                log.info("Authentication successful for user: {}", request.getUserId());
                return ResponseEntity.ok(response);
            } else {
                log.warn("Authentication failed for user: {} - {}", request.getUserId(), response.getMessage());
                if ("Wrong Password".equals(response.getMessage()) || "User not found".equals(response.getMessage())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                } else if ("User account is inactive".equals(response.getMessage())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
                } else {
                    return ResponseEntity.badRequest().body(response);
                }
            }
        } catch (Exception e) {
            log.error("Error during authentication for user: {}", request.getUserId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(SignonResponseDTO.failure("Internal server error"));
        }
    }
    
    @Operation(summary = "Handle PF3 Exit", description = "Handle PF3 key press for graceful exit with thank you message")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exit successful with thank you message"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/api/auth/exit")
    public ResponseEntity<SignonResponseDTO> handleExit() {
        log.info("PF3 exit key pressed");
        return ResponseEntity.ok(SignonResponseDTO.success(null, null));
    }
    
    @Operation(summary = "Handle Invalid Key", description = "Handle invalid key press during authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid key pressed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/api/auth/invalid-key")
    public ResponseEntity<SignonResponseDTO> handleInvalidKey() {
        log.warn("Invalid key pressed during authentication");
        return ResponseEntity.badRequest().body(SignonResponseDTO.invalidKey());
    }
    
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all user security records")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/api/users")
    public ResponseEntity<Page<UserSecurityDTO>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all users with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<UserSecurityDTO> users = userSecurityService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Get user by ID", description = "Retrieve a user security record by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/api/users/{userId}")
    public ResponseEntity<UserSecurityDTO> getUserById(@PathVariable String userId) {
        log.info("Retrieving user by ID: {}", userId);
        return userSecurityService.getUserById(userId.toUpperCase())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Create a new user", description = "Create a new user security record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "User already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/api/users")
    public ResponseEntity<UserSecurityDTO> createUser(@Valid @RequestBody CreateUserSecurityRequest request) {
        log.info("Creating new user: {}", request.getUserId());
        
        try {
            UserSecurityDTO response = userSecurityService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to create user: {} - {}", request.getUserId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Error creating user: {}", request.getUserId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Update an existing user", description = "Update user security details by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/api/users/{userId}")
    public ResponseEntity<UserSecurityDTO> updateUser(@PathVariable String userId, 
                                                     @Valid @RequestBody UpdateUserSecurityRequest request) {
        log.info("Updating user: {}", userId);
        
        try {
            UserSecurityDTO response = userSecurityService.updateUser(userId.toUpperCase(), request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to update user: {} - {}", userId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Delete a user", description = "Delete a user security record by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/api/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        log.info("Deleting user: {}", userId);
        
        try {
            userSecurityService.deleteUser(userId.toUpperCase());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Failed to delete user: {} - {}", userId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Activate user", description = "Activate a user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User activated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/api/users/{userId}/activate")
    public ResponseEntity<UserSecurityDTO> activateUser(@PathVariable String userId) {
        log.info("Activating user: {}", userId);
        
        try {
            UserSecurityDTO response = userSecurityService.activateUser(userId.toUpperCase());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to activate user: {} - {}", userId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error activating user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Deactivate user", description = "Deactivate a user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/api/users/{userId}/deactivate")
    public ResponseEntity<UserSecurityDTO> deactivateUser(@PathVariable String userId) {
        log.info("Deactivating user: {}", userId);
        
        try {
            UserSecurityDTO response = userSecurityService.deactivateUser(userId.toUpperCase());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to deactivate user: {} - {}", userId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deactivating user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Change user password", description = "Change password for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/api/users/{userId}/change-password")
    public ResponseEntity<UserSecurityDTO> changePassword(@PathVariable String userId, 
                                                         @Valid @RequestBody ChangePasswordRequest request) {
        log.info("Changing password for user: {}", userId);
        
        try {
            UserSecurityDTO response = userSecurityService.changePassword(userId.toUpperCase(), request.getNewPassword());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to change password for user: {} - {}", userId, e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            log.error("Error changing password for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Validate authentication", description = "Validate if user credentials are correct without full authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validation successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/api/auth/validate")
    public ResponseEntity<ValidationResponseDTO> validateAuthentication(@Valid @RequestBody SignonRequestDTO request) {
        log.info("Validating authentication for user: {}", request.getUserId());
        
        try {
            String validationError = userSecurityService.validateCredentials(request);
            
            if (validationError == null) {
                return ResponseEntity.ok(ValidationResponseDTO.success());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ValidationResponseDTO.failure(validationError));
            }
        } catch (Exception e) {
            log.error("Error during validation for user: {}", request.getUserId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ValidationResponseDTO.failure("Internal server error"));
        }
    }
}