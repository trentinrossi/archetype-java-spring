package com.example.demo.controller;

import com.example.demo.dto.CreateUserSessionRequestDto;
import com.example.demo.dto.UpdateUserSessionRequestDto;
import com.example.demo.dto.UserSessionResponseDto;
import com.example.demo.service.UserSessionService;
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
@Tag(name = "User Session Management", description = "APIs for managing user sessions and program navigation")
@RequestMapping("/api/sessions")
public class UserSessionController {

    private final UserSessionService userSessionService;

    @Operation(
        summary = "Get all sessions",
        description = "Retrieve a paginated list of all user sessions"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of sessions",
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
    public ResponseEntity<Page<UserSessionResponseDto>> getAllSessions(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all sessions with pagination: {}", pageable);
        Page<UserSessionResponseDto> sessions = userSessionService.getAllSessions(pageable);
        return ResponseEntity.ok(sessions);
    }

    @Operation(
        summary = "Get session by ID",
        description = "Retrieve a specific session by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of session",
            content = @Content(schema = @Schema(implementation = UserSessionResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Session not found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserSessionResponseDto> getSessionById(
            @Parameter(description = "Session ID", required = true)
            @PathVariable Long id) {
        log.info("Retrieving session by ID: {}", id);
        return userSessionService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Get latest session by user ID",
        description = "Retrieve the most recent session for a specific user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of session",
            content = @Content(schema = @Schema(implementation = UserSessionResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Session not found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<UserSessionResponseDto> getLatestSessionByUserId(
            @Parameter(description = "User ID (8 characters)", required = true)
            @PathVariable String userId) {
        log.info("Retrieving latest session for user: {}", userId);
        return userSessionService.getLatestSessionByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Create a new session",
        description = "Create a new user session for program navigation and context tracking"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Session created successfully",
            content = @Content(schema = @Schema(implementation = UserSessionResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data - validation errors"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @PostMapping
    public ResponseEntity<UserSessionResponseDto> createSession(
            @Valid @RequestBody CreateUserSessionRequestDto request) {
        log.info("Creating new session for user: {}", request.getUserId());
        UserSessionResponseDto response = userSessionService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Update an existing session",
        description = "Update session details by session ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Session updated successfully",
            content = @Content(schema = @Schema(implementation = UserSessionResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data - validation errors"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Session not found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserSessionResponseDto> updateSession(
            @Parameter(description = "Session ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserSessionRequestDto request) {
        log.info("Updating session with ID: {}", id);
        UserSessionResponseDto response = userSessionService.updateSession(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete a session",
        description = "Delete a session by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Session deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Session not found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(
            @Parameter(description = "Session ID", required = true)
            @PathVariable Long id) {
        log.info("Deleting session with ID: {}", id);
        userSessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get sessions by user ID",
        description = "Retrieve all sessions for a specific user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of sessions",
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
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<UserSessionResponseDto>> getSessionsByUserId(
            @Parameter(description = "User ID (8 characters)", required = true)
            @PathVariable String userId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving sessions for user: {}", userId);
        Page<UserSessionResponseDto> sessions = userSessionService.getSessionsByUserId(userId, pageable);
        return ResponseEntity.ok(sessions);
    }

    @Operation(
        summary = "Get sessions by user type",
        description = "Retrieve all sessions for a specific user type (admin or regular)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of sessions",
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
    @GetMapping("/user-type/{userType}")
    public ResponseEntity<Page<UserSessionResponseDto>> getSessionsByUserType(
            @Parameter(description = "User type (A=Admin, R=Regular)", required = true)
            @PathVariable String userType,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving sessions for user type: {}", userType);
        Page<UserSessionResponseDto> sessions = userSessionService.getSessionsByUserType(userType, pageable);
        return ResponseEntity.ok(sessions);
    }

    @Operation(
        summary = "Transfer session to program",
        description = "Transfer a session to a different program (BR004 - Program Navigation)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Session transferred successfully",
            content = @Content(schema = @Schema(implementation = UserSessionResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Session not found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @PostMapping("/{id}/transfer")
    public ResponseEntity<UserSessionResponseDto> transferToProgram(
            @Parameter(description = "Session ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Target program name", required = true)
            @RequestParam String targetProgram,
            @Parameter(description = "Target transaction ID", required = true)
            @RequestParam String targetTransaction) {
        log.info("Transferring session {} to program: {}", id, targetProgram);
        UserSessionResponseDto response = userSessionService.transferToProgram(id, targetProgram, targetTransaction);
        return ResponseEntity.ok(response);
    }
}
