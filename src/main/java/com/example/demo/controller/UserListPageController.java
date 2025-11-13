package com.example.demo.controller;

import com.example.demo.dto.UserListPageResponseDto;
import com.example.demo.dto.CreateUserListPageRequestDto;
import com.example.demo.dto.UpdateUserListPageRequestDto;
import com.example.demo.service.UserListPageService;
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
@Tag(name = "User List Page Management", description = "APIs for managing paginated user lists in CardDemo application")
@RequestMapping("/api/user-list-pages")
public class UserListPageController {

    private final UserListPageService userListPageService;

    @Operation(summary = "Get all user list pages", description = "Retrieve a paginated list of all user list pages")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user list pages"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<UserListPageResponseDto>> getAllUserListPages(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all user list pages with pagination: {}", pageable);
        Page<UserListPageResponseDto> userListPages = userListPageService.getAllUserListPages(pageable);
        return ResponseEntity.ok(userListPages);
    }

    @Operation(summary = "Get user list page by ID", description = "Retrieve a user list page by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user list page"),
        @ApiResponse(responseCode = "404", description = "User list page not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserListPageResponseDto> getUserListPageById(@PathVariable Long id) {
        log.info("Retrieving user list page by ID: {}", id);
        return userListPageService.getUserListPageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get user list page by page number", description = "Retrieve a user list page by its page number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user list page"),
        @ApiResponse(responseCode = "404", description = "User list page not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<UserListPageResponseDto> getUserListPageByPageNumber(@PathVariable Long pageNumber) {
        log.info("Retrieving user list page by page number: {}", pageNumber);
        return userListPageService.getUserListPageByPageNumber(pageNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new user list page", description = "Create a new user list page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User list page created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserListPageResponseDto> createUserListPage(
            @Valid @RequestBody CreateUserListPageRequestDto request) {
        log.info("Creating new user list page: {}", request);
        UserListPageResponseDto response = userListPageService.createUserListPage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing user list page", description = "Update user list page details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User list page updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User list page not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserListPageResponseDto> updateUserListPage(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserListPageRequestDto request) {
        log.info("Updating user list page with ID: {}", id);
        UserListPageResponseDto response = userListPageService.updateUserListPage(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a user list page", description = "Delete a user list page by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User list page deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User list page not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserListPage(@PathVariable Long id) {
        log.info("Deleting user list page with ID: {}", id);
        userListPageService.deleteUserListPage(id);
        return ResponseEntity.noContent().build();
    }
}
