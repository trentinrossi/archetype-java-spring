package com.example.demo.controller;

import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.UpdateUserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users in CardDemo Admin Menu system")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "Get all users",
        description = "Retrieve a paginated list of all users in the CardDemo system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of users",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all users with pagination: {}", pageable);
        Page<UserResponseDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(
        summary = "Get user by ID",
        description = "Retrieve a specific user by their unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of user",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User ID NOT found..."
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unable to lookup User..."
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "User database ID", required = true)
            @PathVariable Long id) {
        log.info("Retrieving user by ID: {}", id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Get user by User ID",
        description = "Retrieve a specific user by their user ID (8 character identifier)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of user",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User ID NOT found..."
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unable to lookup User..."
        )
    })
    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<UserResponseDto> getUserByUserId(
            @Parameter(description = "User ID (8 characters)", required = true)
            @PathVariable String userId) {
        log.info("Retrieving user by User ID: {}", userId);
        return userService.getUserByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Create a new user",
        description = "Create a new user in the CardDemo system with authentication credentials"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data - validation errors"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "User ID already exist..."
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody CreateUserRequestDto request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        UserResponseDto response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Update an existing user",
        description = "Update user details by database ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data - validation errors"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User ID NOT found..."
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unable to Update User..."
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @Parameter(description = "User database ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequestDto request) {
        log.info("Updating user with ID: {}", id);
        UserResponseDto response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete a user",
        description = "Delete a user by their database ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "User deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User ID NOT found..."
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unable to Update User..."
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User database ID", required = true)
            @PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "User authentication",
        description = "Authenticate user with user ID and password for admin menu access (BR001 - User Authentication Check)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Authentication successful",
            content = @Content(schema = @Schema(implementation = LoginResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Please enter User ID ... / Please enter Password ..."
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Wrong Password. Try again ... / User not found. Try again ..."
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticateUser(
            @Valid @RequestBody LoginRequestDto request) {
        log.info("Authenticating user: {}", request.getUserId());
        LoginResponseDto response = userService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Check admin authentication",
        description = "Verify that user is authenticated as admin before accessing admin menu (BR001 - Admin Authentication Requirement)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User is authenticated as admin"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "User is not authenticated or not an admin"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/by-user-id/{userId}/admin-check")
    public ResponseEntity<Void> checkAdminAuthentication(
            @Parameter(description = "User ID (8 characters)", required = true)
            @PathVariable String userId) {
        log.info("Checking admin authentication for user: {}", userId);
        userService.checkAdminAuthentication(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Search users",
        description = "Search users by name (first name or last name)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful search results",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid search parameters"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDto>> searchUsers(
            @Parameter(description = "Search term for name search")
            @RequestParam String searchTerm,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Searching users with term: {}", searchTerm);
        Page<UserResponseDto> users = userService.searchUsers(searchTerm, pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(
        summary = "Filter users by type",
        description = "Filter users by user type (admin or regular)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of filtered users",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "User Type can NOT be empty..."
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/filter-by-type")
    public ResponseEntity<Page<UserResponseDto>> filterUsersByType(
            @Parameter(description = "User type (A=Admin, R=Regular)", required = true)
            @RequestParam String userType,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Filtering users by type: {}", userType);
        Page<UserResponseDto> users = userService.getUsersByType(userType, pageable);
        return ResponseEntity.ok(users);
    }
}
