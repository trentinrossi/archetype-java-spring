package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.UserService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users with optional filtering by User ID prefix")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<UserListDTO>> getAllUsers(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String secUsrIdStartsWith) {
        log.info("Retrieving users with pagination: page={}, size={}, filter={}", 
                pageable.getPageNumber(), pageable.getPageSize(), secUsrIdStartsWith);
        
        UserSearchDTO searchDTO = new UserSearchDTO(secUsrIdStartsWith);
        Page<UserListDTO> users = userService.getAllUsers(pageable.getPageNumber(), searchDTO);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        log.info("Retrieving user with ID: {}", id);
        
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new user", description = "Create a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or duplicate User ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO request) {
        log.info("Creating new user with ID: {}", request.getSecUsrId());
        
        UserResponseDTO response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update an existing user", description = "Update user details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, 
                                                     @Valid @RequestBody UserUpdateDTO request) {
        log.info("Updating user with ID: {}", id);
        
        UserResponseDTO response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete a user", description = "Delete a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("Deleting user with ID: {}", id);
        
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}