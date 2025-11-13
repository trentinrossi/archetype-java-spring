package com.example.demo.controller;

import com.example.demo.dto.AdminMenuOptionResponseDto;
import com.example.demo.service.AdminMenuOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Admin Menu Option Management", description = "APIs for managing admin-specific menu options")
@RequestMapping("/api/admin-menu-options")
public class AdminMenuOptionController {

    private final AdminMenuOptionService adminMenuOptionService;

    @Operation(
        summary = "Get all active admin menu options",
        description = "Retrieve all active admin menu options ordered by display order"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin menu options"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active")
    public ResponseEntity<List<AdminMenuOptionResponseDto>> getAllActiveAdminMenuOptions() {
        log.info("Retrieving all active admin menu options");
        List<AdminMenuOptionResponseDto> options = adminMenuOptionService.getAllActiveAdminMenuOptions();
        return ResponseEntity.ok(options);
    }

    @Operation(
        summary = "Get admin menu option by number",
        description = "Retrieve a specific admin menu option by its option number"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of admin menu option"),
        @ApiResponse(responseCode = "404", description = "Admin menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-number/{optionNumber}")
    public ResponseEntity<AdminMenuOptionResponseDto> getAdminMenuOptionByNumber(
            @Parameter(description = "Admin menu option number", required = true)
            @PathVariable Integer optionNumber) {
        log.info("Retrieving admin menu option by number: {}", optionNumber);
        return adminMenuOptionService.getAdminMenuOptionByNumber(optionNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
