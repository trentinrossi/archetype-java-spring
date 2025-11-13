package com.example.demo.controller;

import com.example.demo.dto.MenuOptionResponseDto;
import com.example.demo.dto.CreateMenuOptionRequestDto;
import com.example.demo.dto.UpdateMenuOptionRequestDto;
import com.example.demo.service.MenuOptionService;
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
@Tag(name = "Menu Options", description = "APIs for managing menu options in CardDemo application")
@RequestMapping("/api/menu-options")
public class MenuOptionController {

    private final MenuOptionService menuOptionService;

    @Operation(summary = "Get all menu options", description = "Retrieve a paginated list of all menu options")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of menu options"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<MenuOptionResponseDto>> getAllMenuOptions(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all menu options with pagination: {}", pageable);
        Page<MenuOptionResponseDto> menuOptions = menuOptionService.getAllMenuOptions(pageable);
        return ResponseEntity.ok(menuOptions);
    }

    @Operation(summary = "Get menu option by ID", description = "Retrieve a menu option by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of menu option"),
        @ApiResponse(responseCode = "404", description = "Menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MenuOptionResponseDto> getMenuOptionById(@PathVariable Long id) {
        log.info("Retrieving menu option by ID: {}", id);
        return menuOptionService.getMenuOptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get menu option by option number", description = "Retrieve a menu option by its option number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of menu option"),
        @ApiResponse(responseCode = "404", description = "Menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-number/{optionNumber}")
    public ResponseEntity<MenuOptionResponseDto> getMenuOptionByNumber(@PathVariable Integer optionNumber) {
        log.info("Retrieving menu option by option number: {}", optionNumber);
        return menuOptionService.getMenuOptionByOptionNumber(optionNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get accessible menu options for user type", description = "Retrieve menu options accessible to a specific user type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accessible menu options"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/accessible/{userType}")
    public ResponseEntity<List<MenuOptionResponseDto>> getAccessibleOptionsForUserType(@PathVariable String userType) {
        log.info("Retrieving accessible menu options for user type: {}", userType);
        List<MenuOptionResponseDto> options = menuOptionService.getAccessibleOptionsForUserType(userType);
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "Create a new menu option", description = "Create a new menu option")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Menu option created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<MenuOptionResponseDto> createMenuOption(
            @Valid @RequestBody CreateMenuOptionRequestDto request) {
        log.info("Creating new menu option: {}", request);
        MenuOptionResponseDto response = menuOptionService.createMenuOption(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing menu option", description = "Update menu option details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu option updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MenuOptionResponseDto> updateMenuOption(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMenuOptionRequestDto request) {
        log.info("Updating menu option with ID: {}", id);
        MenuOptionResponseDto response = menuOptionService.updateMenuOption(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a menu option", description = "Delete a menu option by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Menu option deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuOption(@PathVariable Long id) {
        log.info("Deleting menu option with ID: {}", id);
        menuOptionService.deleteMenuOption(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Validate menu option access", description = "Validate if a user type can access a menu option")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Access validation successful"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "404", description = "Menu option not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{optionNumber}/validate-access")
    public ResponseEntity<Void> validateMenuOptionAccess(
            @PathVariable Integer optionNumber,
            @RequestParam String userType) {
        log.info("Validating menu option {} access for user type: {}", optionNumber, userType);
        menuOptionService.validateMenuOptionAccess(optionNumber, userType);
        return ResponseEntity.ok().build();
    }
}
