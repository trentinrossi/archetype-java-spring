package com.example.demo.controller;

import com.example.demo.dto.UserSelectionResponseDto;
import com.example.demo.dto.CreateUserSelectionRequestDto;
import com.example.demo.dto.UpdateUserSelectionRequestDto;
import com.example.demo.service.UserSelectionService;
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
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Selection Management", description = "APIs for managing user selections (update/delete) in CardDemo application")
@RequestMapping("/api/user-selections")
public class UserSelectionController {

    private final UserSelectionService userSelectionService;

    @Operation(summary = "Get all user selections", description = "Retrieve a paginated list of all user selections")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user selections"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<UserSelectionResponseDto>> getAllUserSelections(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all user selections with pagination: {}", pageable);
        Page<UserSelectionResponseDto> userSelections = userSelectionService.getAllUserSelections(pageable);
        return ResponseEntity.ok(userSelections);
    }

    @Operation(summary = "Get user selection by ID", description = "Retrieve a user selection by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user selection"),
        @ApiResponse(responseCode = "404", description = "User selection not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserSelectionResponseDto> getUserSelectionById(@PathVariable Long id) {
        log.info("Retrieving user selection by ID: {}", id);
        return userSelectionService.getUserSelectionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get user selection by user ID", description = "Retrieve a user selection by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user selection"),
        @ApiResponse(responseCode = "404", description = "User selection not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserSelectionResponseDto> getUserSelectionByUserId(@PathVariable String userId) {
        log.info("Retrieving user selection for user: {}", userId);
        return userSelectionService.getUserSelectionByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all update selections", description = "Retrieve all user selections marked for update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of update selections"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/updates")
    public ResponseEntity<List<UserSelectionResponseDto>> getUpdateSelections() {
        log.info("Retrieving all update selections");
        List<UserSelectionResponseDto> selections = userSelectionService.getUpdateSelections();
        return ResponseEntity.ok(selections);
    }

    @Operation(summary = "Get all delete selections", description = "Retrieve all user selections marked for deletion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of delete selections"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/deletes")
    public ResponseEntity<List<UserSelectionResponseDto>> getDeleteSelections() {
        log.info("Retrieving all delete selections");
        List<UserSelectionResponseDto> selections = userSelectionService.getDeleteSelections();
        return ResponseEntity.ok(selections);
    }

    @Operation(summary = "Create a new user selection", description = "Create a new user selection")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User selection created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserSelectionResponseDto> createUserSelection(
            @Valid @RequestBody CreateUserSelectionRequestDto request) {
        log.info("Creating new user selection: {}", request);
        UserSelectionResponseDto response = userSelectionService.createUserSelection(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing user selection", description = "Update user selection details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User selection updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User selection not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserSelectionResponseDto> updateUserSelection(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserSelectionRequestDto request) {
        log.info("Updating user selection with ID: {}", id);
        UserSelectionResponseDto response = userSelectionService.updateUserSelection(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a user selection", description = "Delete a user selection by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User selection deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User selection not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSelection(@PathVariable Long id) {
        log.info("Deleting user selection with ID: {}", id);
        userSelectionService.deleteUserSelection(id);
        return ResponseEntity.noContent().build();
    }
}
