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

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

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

    @Operation(summary = "Get admin menu option by ID", description = "Retrieve an administrative menu option by its ID")
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

    @Operation(summary = "Get admin menu option by option number", description = "Retrieve an administrative menu option by its option number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin menu option"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-number/{optionNumber}")
    public ResponseEntity<AdminMenuOptionResponseDto> getAdminMenuOptionByNumber(@PathVariable Integer optionNumber) {
        log.info("Retrieving admin menu option by option number: {}", optionNumber);
        return adminMenuOptionService.getAdminMenuOptionByOptionNumber(optionNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all active admin menu options", description = "Retrieve a list of all active administrative menu options")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of active admin menu options"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active")
    public ResponseEntity<List<AdminMenuOptionResponseDto>> getActiveAdminMenuOptions() {
        log.info("Retrieving all active admin menu options");
        List<AdminMenuOptionResponseDto> activeOptions = adminMenuOptionService.getAllActiveAdminMenuOptions();
        return ResponseEntity.ok(activeOptions);
    }

    @Operation(summary = "Create a new admin menu option", description = "Create a new administrative menu option")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Admin menu option created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AdminMenuOptionResponseDto> createAdminMenuOption(
            @Valid @RequestBody CreateAdminMenuOptionRequestDto request) {
        log.info("Creating new admin menu option: {}", request);
        AdminMenuOptionResponseDto response = adminMenuOptionService.createAdminMenuOption(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing admin menu option", description = "Update administrative menu option details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin menu option updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AdminMenuOptionResponseDto> updateAdminMenuOption(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAdminMenuOptionRequestDto request) {
        log.info("Updating admin menu option with ID: {}", id);
        AdminMenuOptionResponseDto response = adminMenuOptionService.updateAdminMenuOption(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an admin menu option", description = "Delete an administrative menu option by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Admin menu option deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminMenuOption(@PathVariable Long id) {
        log.info("Deleting admin menu option with ID: {}", id);
        adminMenuOptionService.deleteAdminMenuOption(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Validate menu option for execution", description = "Validate if a menu option can be executed based on its active status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu option is valid and active"),
        @ApiResponse(responseCode = "400", description = "Invalid menu option number"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Menu option is not active (coming soon)")
    })
    @PostMapping("/{optionNumber}/validate")
    public ResponseEntity<Map<String, Object>> validateMenuOption(@PathVariable Integer optionNumber) {
        log.info("Validating menu option with option number: {}", optionNumber);
        try {
            adminMenuOptionService.validateMenuOptionForExecution(optionNumber);
            return ResponseEntity.ok(Map.of(
                "valid", true,
                "optionNumber", optionNumber,
                "message", "Menu option is valid and active"
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                "valid", false,
                "optionNumber", optionNumber,
                "message", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Get program name for option", description = "Retrieve the program name associated with a menu option")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of program name"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{optionNumber}/program")
    public ResponseEntity<Map<String, String>> getProgramNameForOption(@PathVariable Integer optionNumber) {
        log.info("Retrieving program name for option number: {}", optionNumber);
        String programName = adminMenuOptionService.getProgramNameForOption(optionNumber);
        return ResponseEntity.ok(Map.of("programName", programName, "optionNumber", optionNumber.toString()));
    }

    @Operation(summary = "Check if menu option is active", description = "Check if a menu option is currently active")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful check of menu option status"),
        @ApiResponse(responseCode = "400", description = "Invalid menu option number"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{optionNumber}/is-active")
    public ResponseEntity<Map<String, Object>> isMenuOptionActive(@PathVariable Integer optionNumber) {
        log.info("Checking if menu option {} is active", optionNumber);
        boolean isActive = adminMenuOptionService.isMenuOptionActive(optionNumber);
        return ResponseEntity.ok(Map.of(
            "optionNumber", optionNumber,
            "isActive", isActive
        ));
    }
}
