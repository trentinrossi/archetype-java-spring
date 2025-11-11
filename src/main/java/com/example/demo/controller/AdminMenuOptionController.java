package com.example.demo.controller;

import com.example.demo.dto.AdminMenuOptionResponseDto;
import com.example.demo.dto.CreateAdminMenuOptionRequestDto;
import com.example.demo.dto.UpdateAdminMenuOptionRequestDto;
import com.example.demo.service.AdminMenuOptionService;
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
@Tag(name = "Admin Menu Options", description = "APIs for managing administrative menu options in CardDemo application")
@RequestMapping("/api/admin-menu-options")
public class AdminMenuOptionController {

    private final AdminMenuOptionService adminMenuOptionService;

    @Operation(summary = "Get all admin menu options", description = "Retrieve a paginated list of all administrative menu options")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin menu options"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<AdminMenuOptionResponseDto>> getAllAdminMenuOptions(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all admin menu options with pagination: {}", pageable);
        Page<AdminMenuOptionResponseDto> adminMenuOptions = adminMenuOptionService.getAllAdminMenuOptions(pageable);
        return ResponseEntity.ok(adminMenuOptions);
    }

    @Operation(summary = "Get admin menu option by ID", description = "Retrieve a specific admin menu option by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin menu option"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AdminMenuOptionResponseDto> getAdminMenuOptionById(@PathVariable Long id) {
        log.info("Retrieving admin menu option by ID: {}", id);
        return adminMenuOptionService.getAdminMenuOptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all active admin menu options", description = "Retrieve all administrative menu options that are currently active")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of active admin menu options"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active")
    public ResponseEntity<List<AdminMenuOptionResponseDto>> getActiveAdminMenuOptions() {
        log.info("Retrieving all active admin menu options");
        List<AdminMenuOptionResponseDto> activeOptions = adminMenuOptionService.getActiveAdminMenuOptions();
        return ResponseEntity.ok(activeOptions);
    }

    @Operation(summary = "Get all inactive admin menu options", description = "Retrieve all administrative menu options that are currently inactive")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of inactive admin menu options"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/inactive")
    public ResponseEntity<List<AdminMenuOptionResponseDto>> getInactiveAdminMenuOptions() {
        log.info("Retrieving all inactive admin menu options");
        List<AdminMenuOptionResponseDto> inactiveOptions = adminMenuOptionService.getInactiveAdminMenuOptions();
        return ResponseEntity.ok(inactiveOptions);
    }

    @Operation(summary = "Get admin menu option by option number", description = "Retrieve a specific admin menu option by its sequential option number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin menu option"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/option-number/{optionNumber}")
    public ResponseEntity<AdminMenuOptionResponseDto> getAdminMenuOptionByOptionNumber(@PathVariable Integer optionNumber) {
        log.info("Retrieving admin menu option by option number: {}", optionNumber);
        return adminMenuOptionService.getAdminMenuOptionByOptionNumber(optionNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get admin menu options by program name", description = "Retrieve admin menu options by their associated program name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin menu options"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/program/{programName}")
    public ResponseEntity<List<AdminMenuOptionResponseDto>> getAdminMenuOptionsByProgramName(@PathVariable String programName) {
        log.info("Retrieving admin menu options by program name: {}", programName);
        List<AdminMenuOptionResponseDto> options = adminMenuOptionService.getAdminMenuOptionsByProgramName(programName);
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "Get admin menu options by admin user", description = "Retrieve all admin menu options accessible by a specific admin user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin menu options"),
        @ApiResponse(responseCode = "404", description = "Admin user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/{adminUserId}")
    public ResponseEntity<List<AdminMenuOptionResponseDto>> getAdminMenuOptionsByAdminUser(@PathVariable String adminUserId) {
        log.info("Retrieving admin menu options for admin user ID: {}", adminUserId);
        List<AdminMenuOptionResponseDto> options = adminMenuOptionService.getAdminMenuOptionsByAdminUser(adminUserId);
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "Get active admin menu options by admin user", description = "Retrieve all active admin menu options accessible by a specific admin user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of active admin menu options"),
        @ApiResponse(responseCode = "404", description = "Admin user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/{adminUserId}/active")
    public ResponseEntity<List<AdminMenuOptionResponseDto>> getActiveAdminMenuOptionsByAdminUser(@PathVariable String adminUserId) {
        log.info("Retrieving active admin menu options for admin user ID: {}", adminUserId);
        List<AdminMenuOptionResponseDto> options = adminMenuOptionService.getActiveAdminMenuOptionsByAdminUser(adminUserId);
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "Search admin menu options by name", description = "Search for admin menu options by option name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search of admin menu options"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<List<AdminMenuOptionResponseDto>> searchAdminMenuOptionsByName(@RequestParam String name) {
        log.info("Searching admin menu options by name: {}", name);
        List<AdminMenuOptionResponseDto> options = adminMenuOptionService.searchAdminMenuOptionsByName(name);
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "Get admin menu options in range", description = "Retrieve admin menu options within a specified option number range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin menu options in range"),
        @ApiResponse(responseCode = "400", description = "Invalid range parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/range")
    public ResponseEntity<List<AdminMenuOptionResponseDto>> getAdminMenuOptionsInRange(
            @RequestParam Integer start,
            @RequestParam Integer end) {
        log.info("Retrieving admin menu options in range: {} to {}", start, end);
        List<AdminMenuOptionResponseDto> options = adminMenuOptionService.getAdminMenuOptionsInRange(start, end);
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "Create a new admin menu option", description = "Create a new administrative menu option")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Admin menu option created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AdminMenuOptionResponseDto> createAdminMenuOption(@RequestBody CreateAdminMenuOptionRequestDto request) {
        log.info("Creating new admin menu option: {}", request);
        AdminMenuOptionResponseDto response = adminMenuOptionService.createAdminMenuOption(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing admin menu option", description = "Update admin menu option details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin menu option updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AdminMenuOptionResponseDto> updateAdminMenuOption(
            @PathVariable Long id,
            @RequestBody UpdateAdminMenuOptionRequestDto request) {
        log.info("Updating admin menu option ID: {} with data: {}", id, request);
        AdminMenuOptionResponseDto response = adminMenuOptionService.updateAdminMenuOption(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an admin menu option", description = "Delete an admin menu option by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Admin menu option deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminMenuOption(@PathVariable Long id) {
        log.info("Deleting admin menu option ID: {}", id);
        adminMenuOptionService.deleteAdminMenuOption(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activate an admin menu option", description = "Activate a specific admin menu option by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin menu option activated successfully"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/activate")
    public ResponseEntity<AdminMenuOptionResponseDto> activateAdminMenuOption(@PathVariable Long id) {
        log.info("Activating admin menu option ID: {}", id);
        AdminMenuOptionResponseDto response = adminMenuOptionService.activateAdminMenuOption(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deactivate an admin menu option", description = "Deactivate a specific admin menu option by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin menu option deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<AdminMenuOptionResponseDto> deactivateAdminMenuOption(@PathVariable Long id) {
        log.info("Deactivating admin menu option ID: {}", id);
        AdminMenuOptionResponseDto response = adminMenuOptionService.deactivateAdminMenuOption(id);
        return ResponseEntity.ok(response);
    }
}
