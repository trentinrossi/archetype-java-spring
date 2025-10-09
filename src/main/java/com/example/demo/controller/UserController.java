package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.UserSecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Security Administration", description = "APIs for user security administration and authentication")
@RequestMapping("/api")
public class UserController {
    
    private final UserSecurityService userSecurityService;
    
    @Operation(summary = "User authentication", description = "Authenticate user with credentials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/auth/signin")
    public ResponseEntity<Map<String, Object>> signIn(@Valid @RequestBody SignonDTO signonRequest) {
        log.info("Authentication attempt for user: {}", signonRequest.getUserId());
        
        try {
            boolean authenticated = userSecurityService.authenticateUser(signonRequest);
            Map<String, Object> response = new HashMap<>();
            
            if (authenticated) {
                Optional<UserDTO> userOpt = userSecurityService.getUserById(signonRequest.getUserId());
                if (userOpt.isPresent()) {
                    response.put("authenticated", true);
                    response.put("user", userOpt.get());
                    response.put("message", "Authentication successful");
                    return ResponseEntity.ok(response);
                }
            }
            
            response.put("authenticated", false);
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("authenticated", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @Operation(summary = "Get all users with pagination", description = "Retrieve a paginated list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users")
    public ResponseEntity<UserListDTO> getAllUsers(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        log.info("Retrieving users - page: {}, size: {}", page, size);
        
        try {
            UserListDTO users = userSecurityService.getUserList(page, size);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            log.error("Invalid parameters for user list: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "User ID", example = "USR001", required = true)
            @PathVariable String id) {
        log.info("Retrieving user by ID: {}", id);
        
        Optional<UserDTO> userOpt = userSecurityService.getUserById(id);
        return userOpt.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Create a new user", description = "Add a new user to the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "User already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO createRequest) {
        log.info("Creating new user with ID: {}", createRequest.getUserId());
        
        try {
            UserDTO createdUser = userSecurityService.addUser(createRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            log.error("User creation failed: {}", e.getMessage());
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Update an existing user", description = "Update user details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "User ID", example = "USR001", required = true)
            @PathVariable String id,
            @Valid @RequestBody UpdateUserDTO updateRequest) {
        log.info("Updating user with ID: {}", id);
        
        try {
            UserDTO updatedUser = userSecurityService.updateUser(id, updateRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            log.error("User update failed: {}", e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Delete a user", description = "Delete a user by ID with confirmation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Confirmation required"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @Parameter(description = "User ID", example = "USR001", required = true)
            @PathVariable String id,
            @Parameter(description = "Confirmation flag", example = "true")
            @RequestParam(defaultValue = "false") boolean confirm) {
        log.info("Deleting user with ID: {}, confirmed: {}", id, confirm);
        
        Map<String, Object> response = new HashMap<>();
        
        if (!confirm) {
            // Return confirmation details for the user
            boolean canDelete = userSecurityService.confirmDeleteUser(id);
            if (canDelete) {
                Optional<UserDTO> userOpt = userSecurityService.getUserById(id);
                if (userOpt.isPresent()) {
                    UserDTO user = userOpt.get();
                    response.put("confirmationRequired", true);
                    response.put("user", user);
                    response.put("message", "Please confirm deletion of user: " + user.getFullName());
                    return ResponseEntity.ok(response);
                }
            }
            response.put("error", "User not found");
            return ResponseEntity.notFound().build();
        }
        
        try {
            userSecurityService.deleteUser(id);
            response.put("deleted", true);
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("User deletion failed: {}", e.getMessage());
            if (e.getMessage().contains("not found")) {
                response.put("error", "User not found");
                return ResponseEntity.notFound().build();
            }
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @Operation(summary = "Get admin users for selection", description = "Retrieve list of admin users for selection purposes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin users"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/selection/admin")
    public ResponseEntity<List<UserSelectionDTO>> getAdminUsersForSelection() {
        log.info("Retrieving admin users for selection");
        List<UserSelectionDTO> adminUsers = userSecurityService.getAdminUsersForSelection();
        return ResponseEntity.ok(adminUsers);
    }
    
    @Operation(summary = "Get regular users for selection", description = "Retrieve list of regular users for selection purposes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of regular users"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/selection/regular")
    public ResponseEntity<List<UserSelectionDTO>> getRegularUsersForSelection() {
        log.info("Retrieving regular users for selection");
        List<UserSelectionDTO> regularUsers = userSecurityService.getRegularUsersForSelection();
        return ResponseEntity.ok(regularUsers);
    }
    
    @Operation(summary = "Get all users for selection", description = "Retrieve list of all users for selection purposes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/selection")
    public ResponseEntity<List<UserSelectionDTO>> getAllUsersForSelection() {
        log.info("Retrieving all users for selection");
        List<UserSelectionDTO> users = userSecurityService.getUsersForSelection();
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "Get next page of users", description = "Forward pagination - get users after the specified user ID (COBOL-style)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of next users"),
        @ApiResponse(responseCode = "404", description = "No more users found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/next/{lastUserId}")
    public ResponseEntity<UserListDTO> getNextUsers(
            @Parameter(description = "Last user ID from current page", example = "USR020", required = true)
            @PathVariable String lastUserId,
            @Parameter(description = "Number of items per page", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting next users after ID: {}, size: {}", lastUserId, size);
        
        try {
            UserListDTO nextUsers = userSecurityService.getNextPage(lastUserId, size);
            return ResponseEntity.ok(nextUsers);
        } catch (IllegalArgumentException e) {
            log.error("Error getting next users: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Get previous page of users", description = "Backward pagination - get users before the specified user ID (COBOL-style)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of previous users"),
        @ApiResponse(responseCode = "404", description = "No previous users found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/previous/{firstUserId}")
    public ResponseEntity<UserListDTO> getPreviousUsers(
            @Parameter(description = "First user ID from current page", example = "USR001", required = true)
            @PathVariable String firstUserId,
            @Parameter(description = "Number of items per page", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting previous users before ID: {}, size: {}", firstUserId, size);
        
        try {
            UserListDTO previousUsers = userSecurityService.getPreviousPage(firstUserId, size);
            return ResponseEntity.ok(previousUsers);
        } catch (IllegalArgumentException e) {
            log.error("Error getting previous users: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Get user statistics", description = "Retrieve comprehensive user statistics and metrics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user statistics"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/stats")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        log.info("Retrieving user statistics");
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalUsers", userSecurityService.getTotalUserCount());
        statistics.put("adminUsers", userSecurityService.getAdminUserCount());
        statistics.put("regularUsers", userSecurityService.getRegularUserCount());
        statistics.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.ok(statistics);
    }
    
    @Operation(summary = "Get menu navigation data", description = "Retrieve menu navigation support data for user interface")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of menu data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/menu")
    public ResponseEntity<Map<String, Object>> getMenuNavigationData(
            @Parameter(description = "Current user ID for context", example = "USR001")
            @RequestParam(required = false) String currentUserId) {
        log.info("Retrieving menu navigation data for user: {}", currentUserId);
        
        Map<String, Object> menuData = new HashMap<>();
        
        // Basic menu structure
        menuData.put("mainMenuOptions", List.of(
            Map.of("id", "1", "name", "List Users", "description", "View all users with pagination"),
            Map.of("id", "2", "name", "Add User", "description", "Add new user to system"),
            Map.of("id", "3", "name", "Update User", "description", "Update existing user information"),
            Map.of("id", "4", "name", "Delete User", "description", "Delete user from system")
        ));
        
        // User context if provided
        if (currentUserId != null) {
            Optional<UserDTO> currentUserOpt = userSecurityService.getUserById(currentUserId);
            if (currentUserOpt.isPresent()) {
                UserDTO currentUser = currentUserOpt.get();
                menuData.put("currentUser", currentUser);
                menuData.put("userType", currentUser.getUserType());
                menuData.put("hasAdminAccess", currentUser.isAdmin());
                
                // Admin-specific options
                if (currentUser.isAdmin()) {
                    menuData.put("adminMenuOptions", List.of(
                        Map.of("id", "A1", "name", "User Administration", "description", "Full user management"),
                        Map.of("id", "A2", "name", "System Statistics", "description", "View system statistics"),
                        Map.of("id", "A3", "name", "User Reports", "description", "Generate user reports")
                    ));
                }
            }
        }
        
        menuData.put("timestamp", java.time.LocalDateTime.now());
        return ResponseEntity.ok(menuData);
    }
    
    @Operation(summary = "Check user access", description = "Check if user has required access level")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Access check completed"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/{id}/access")
    public ResponseEntity<Map<String, Object>> checkUserAccess(
            @Parameter(description = "User ID", example = "USR001", required = true)
            @PathVariable String id,
            @Parameter(description = "Required access type (A=Admin, R=Regular)", example = "A")
            @RequestParam(required = false) String requiredType) {
        log.info("Checking access for user: {} with required type: {}", id, requiredType);
        
        Map<String, Object> accessInfo = new HashMap<>();
        
        Optional<UserDTO> userOpt = userSecurityService.getUserById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        UserDTO user = userOpt.get();
        accessInfo.put("userId", user.getUserId());
        accessInfo.put("userType", user.getUserType());
        accessInfo.put("hasAdminAccess", user.isAdmin());
        accessInfo.put("hasRegularAccess", user.isRegular());
        
        if (requiredType != null) {
            boolean hasAccess = userSecurityService.hasUserAccess(id, requiredType);
            accessInfo.put("hasRequiredAccess", hasAccess);
            accessInfo.put("requiredType", requiredType);
        }
        
        return ResponseEntity.ok(accessInfo);
    }
}