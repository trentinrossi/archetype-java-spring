package com.example.demo.controller;

import com.example.demo.dto.UserCreateDto;
import com.example.demo.dto.UserDeleteDto;
import com.example.demo.dto.UserDetailDto;
import com.example.demo.dto.UserListDto;
import com.example.demo.dto.UserUpdateDto;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users with optional filtering by user ID prefix")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<UserListDto>> getAllUsers(
            @PageableDefault(size = 10) Pageable pageable,
            @Parameter(description = "Filter users by user ID prefix", example = "USR")
            @RequestParam(required = false) String userIdPrefix) {
        log.info("Getting all users with page: {}, userIdPrefix: {}", pageable, userIdPrefix);
        Page<UserListDto> users = userService.getAllUsers(pageable, userIdPrefix);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Create a new user", description = "Create a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "User with ID already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserDetailDto> createUser(@Valid @RequestBody UserCreateDto request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        UserDetailDto response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailDto> getUserById(
            @Parameter(description = "User ID", example = "USR00001")
            @PathVariable String userId) {
        log.info("Getting user by ID: {}", userId);
        UserDetailDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
    
    @Operation(summary = "Update an existing user", description = "Update user details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<UserDetailDto> updateUser(
            @Parameter(description = "User ID", example = "USR00001")
            @PathVariable String userId, 
            @Valid @RequestBody UserUpdateDto request) {
        log.info("Updating user with ID: {}", userId);
        UserDetailDto response = userService.updateUser(userId, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get user for deletion confirmation", description = "Retrieve user details for deletion confirmation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User details retrieved for deletion confirmation"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}/delete")
    public ResponseEntity<UserDeleteDto> getUserForDeletion(
            @Parameter(description = "User ID", example = "USR00001")
            @PathVariable String userId) {
        log.info("Getting user for deletion confirmation: {}", userId);
        UserDeleteDto user = userService.getUserForDeletion(userId);
        return ResponseEntity.ok(user);
    }
    
    @Operation(summary = "Delete a user", description = "Delete a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "USR00001")
            @PathVariable String userId) {
        log.info("Deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}