package com.example.demo.controller;

import com.example.demo.dto.CreateUserSessionRequestDto;
import com.example.demo.dto.UpdateUserSessionRequestDto;
import com.example.demo.dto.UserSessionResponseDto;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Session Management", description = "APIs for managing user sessions in CardDemo Admin Menu")
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

    @Operation(summary = "Get user session by ID", description = "Retrieve a user session by their ID")
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

    @Operation(summary = "Create a new user session", description = "Create a new user session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User session created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserSessionResponseDto> createUserSession(@RequestBody CreateUserSessionRequestDto request) {
        log.info("Creating new user session for transaction: {}", request.getTransactionId());
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
    public ResponseEntity<UserSessionResponseDto> updateUserSession(@PathVariable Long id,
                                                                     @RequestBody UpdateUserSessionRequestDto request) {
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

    @Operation(summary = "Get user session by transaction ID", description = "Retrieve a user session by transaction ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user session"),
        @ApiResponse(responseCode = "404", description = "User session not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<UserSessionResponseDto> getUserSessionByTransactionId(@PathVariable String transactionId) {
        log.info("Retrieving user session by transaction ID: {}", transactionId);
        return userSessionService.getUserSessionByTransactionId(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get user sessions by program name", description = "Retrieve user sessions by program name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user sessions"),
        @ApiResponse(responseCode = "400", description = "Invalid program name"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/program/{programName}")
    public ResponseEntity<List<UserSessionResponseDto>> getSessionsByProgramName(@PathVariable String programName) {
        log.info("Retrieving user sessions by program name: {}", programName);
        List<UserSessionResponseDto> userSessions = userSessionService.getSessionsByProgramName(programName);
        return ResponseEntity.ok(userSessions);
    }

    @Operation(summary = "Get user sessions by reenter flag", description = "Retrieve user sessions by reenter flag")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user sessions"),
        @ApiResponse(responseCode = "400", description = "Invalid reenter flag"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/reenter/{reenterFlag}")
    public ResponseEntity<List<UserSessionResponseDto>> getSessionsByReenterFlag(@PathVariable Boolean reenterFlag) {
        log.info("Retrieving user sessions by reenter flag: {}", reenterFlag);
        List<UserSessionResponseDto> userSessions = userSessionService.getSessionsByReenterFlag(reenterFlag);
        return ResponseEntity.ok(userSessions);
    }

    @Operation(summary = "Get user sessions with calling context", description = "Retrieve user sessions that have calling context (from_program and from_transaction)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user sessions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/with-calling-context")
    public ResponseEntity<List<UserSessionResponseDto>> getSessionsWithCallingContext() {
        log.info("Retrieving user sessions with calling context");
        List<UserSessionResponseDto> userSessions = userSessionService.getSessionsWithCallingContext();
        return ResponseEntity.ok(userSessions);
    }

    @Operation(summary = "Get user sessions with program context", description = "Retrieve user sessions that have program context")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user sessions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/with-program-context")
    public ResponseEntity<List<UserSessionResponseDto>> getSessionsWithProgramContext() {
        log.info("Retrieving user sessions with program context");
        List<UserSessionResponseDto> userSessions = userSessionService.getSessionsWithProgramContext();
        return ResponseEntity.ok(userSessions);
    }

    @Operation(summary = "Get user sessions by from program", description = "Retrieve user sessions by from program name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user sessions"),
        @ApiResponse(responseCode = "400", description = "Invalid from program name"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/from-program/{fromProgram}")
    public ResponseEntity<List<UserSessionResponseDto>> getSessionsByFromProgram(@PathVariable String fromProgram) {
        log.info("Retrieving user sessions by from program: {}", fromProgram);
        List<UserSessionResponseDto> userSessions = userSessionService.getSessionsByFromProgram(fromProgram);
        return ResponseEntity.ok(userSessions);
    }

    @Operation(summary = "Get user sessions by from transaction", description = "Retrieve user sessions by from transaction ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user sessions"),
        @ApiResponse(responseCode = "400", description = "Invalid from transaction ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/from-transaction/{fromTransaction}")
    public ResponseEntity<List<UserSessionResponseDto>> getSessionsByFromTransaction(@PathVariable String fromTransaction) {
        log.info("Retrieving user sessions by from transaction: {}", fromTransaction);
        List<UserSessionResponseDto> userSessions = userSessionService.getSessionsByFromTransaction(fromTransaction);
        return ResponseEntity.ok(userSessions);
    }

    @Operation(summary = "Clear calling context", description = "Clear the calling context (from_program and from_transaction) for a user session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calling context cleared successfully"),
        @ApiResponse(responseCode = "404", description = "User session not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/clear-calling-context")
    public ResponseEntity<UserSessionResponseDto> clearCallingContext(@PathVariable Long id) {
        log.info("Clearing calling context for user session ID: {}", id);
        UserSessionResponseDto response = userSessionService.clearCallingContext(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Set reenter flag", description = "Set the reenter flag for a user session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reenter flag set successfully"),
        @ApiResponse(responseCode = "404", description = "User session not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/set-reenter-flag")
    public ResponseEntity<UserSessionResponseDto> setReenterFlag(@PathVariable Long id, @RequestParam Boolean reenterFlag) {
        log.info("Setting reenter flag to {} for user session ID: {}", reenterFlag, id);
        UserSessionResponseDto response = userSessionService.setReenterFlag(id, reenterFlag);
        return ResponseEntity.ok(response);
    }
}
