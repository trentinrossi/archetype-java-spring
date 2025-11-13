package com.example.demo.controller;

import com.example.demo.dto.UserSessionResponseDto;
import com.example.demo.dto.CreateUserSessionRequestDto;
import com.example.demo.dto.UpdateUserSessionRequestDto;
import com.example.demo.service.UserSessionService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "User Session Management", description = "APIs for managing user sessions in CardDemo application")
@RequestMapping("/api/user-sessions")
public class UserSessionController {

    private final UserSessionService userSessionService;

    @Operation(summary = "Get all user sessions", description = "Retrieve a paginated list of all user sessions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user sessions"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<UserSessionResponseDto>> getAllUserSessions(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all user sessions with pagination: {}", pageable);
        Page<UserSessionResponseDto> userSessions = userSessionService.getAllUserSessions(pageable);
        return ResponseEntity.ok(userSessions);
    }

    @Operation(summary = "Get user session by ID", description = "Retrieve a user session by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user session"),
        @ApiResponse(responseCode = "404", description = "User session not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserSessionResponseDto> getUserSessionById(@PathVariable Long id) {
        log.info("Retrieving user session by ID: {}", id);
        return userSessionService.getUserSessionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get user session by user ID", description = "Retrieve the most recent user session for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user session"),
        @ApiResponse(responseCode = "404", description = "User session not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserSessionResponseDto> getUserSessionByUserId(@PathVariable String userId) {
        log.info("Retrieving user session for user: {}", userId);
        return userSessionService.getUserSessionByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new user session", description = "Create a new user session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User session created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserSessionResponseDto> createUserSession(
            @Valid @RequestBody CreateUserSessionRequestDto request) {
        log.info("Creating new user session for user: {}", request.getUserId());
        UserSessionResponseDto response = userSessionService.createUserSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing user session", description = "Update user session details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User session updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User session not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserSessionResponseDto> updateUserSession(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserSessionRequestDto request) {
        log.info("Updating user session with ID: {}", id);
        UserSessionResponseDto response = userSessionService.updateUserSession(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a user session", description = "Delete a user session by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User session deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User session not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSession(@PathVariable Long id) {
        log.info("Deleting user session with ID: {}", id);
        userSessionService.deleteUserSession(id);
        return ResponseEntity.noContent().build();
    }
}
