package com.example.demo.controller;

import com.example.demo.dto.CreateMenuOptionRequestDto;
import com.example.demo.dto.UpdateMenuOptionRequestDto;
import com.example.demo.dto.MenuOptionResponseDto;
import com.example.demo.service.MenuOptionService;
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
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Menu Option Management", description = "APIs for managing menu options and navigation")
@RequestMapping("/api/menu-options")
public class MenuOptionController {

    private final MenuOptionService menuOptionService;

    @Operation(
        summary = "Get all menu options",
        description = "Retrieve a paginated list of all menu options"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of menu options",
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
    public ResponseEntity<Page<MenuOptionResponseDto>> getAllMenuOptions(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all menu options with pagination: {}", pageable);
        Page<MenuOptionResponseDto> options = menuOptionService.getAllMenuOptions(pageable);
        return ResponseEntity.ok(options);
    }

    @Operation(
        summary = "Get menu option by ID",
        description = "Retrieve a specific menu option by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of menu option",
            content = @Content(schema = @Schema(implementation = MenuOptionResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Menu option not found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<MenuOptionResponseDto> getMenuOptionById(
            @Parameter(description = "Menu option ID", required = true)
            @PathVariable Long id) {
        log.info("Retrieving menu option by ID: {}", id);
        return menuOptionService.getMenuOptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Get menu option by number",
        description = "Retrieve a specific menu option by its option number"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of menu option",
            content = @Content(schema = @Schema(implementation = MenuOptionResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Menu option not found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/by-number/{optionNumber}")
    public ResponseEntity<MenuOptionResponseDto> getMenuOptionByNumber(
            @Parameter(description = "Menu option number", required = true)
            @PathVariable Integer optionNumber) {
        log.info("Retrieving menu option by number: {}", optionNumber);
        return menuOptionService.getMenuOptionByNumber(optionNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Create a new menu option",
        description = "Create a new menu option for the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Menu option created successfully",
            content = @Content(schema = @Schema(implementation = MenuOptionResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data - validation errors"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Menu option number already exists"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @PostMapping
    public ResponseEntity<MenuOptionResponseDto> createMenuOption(
            @Valid @RequestBody CreateMenuOptionRequestDto request) {
        log.info("Creating new menu option: {}", request.getOptionName());
        MenuOptionResponseDto response = menuOptionService.createMenuOption(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Update an existing menu option",
        description = "Update menu option details by ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Menu option updated successfully",
            content = @Content(schema = @Schema(implementation = MenuOptionResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data - validation errors"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Menu option not found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<MenuOptionResponseDto> updateMenuOption(
            @Parameter(description = "Menu option ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateMenuOptionRequestDto request) {
        log.info("Updating menu option with ID: {}", id);
        MenuOptionResponseDto response = menuOptionService.updateMenuOption(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete a menu option",
        description = "Delete a menu option by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Menu option deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Menu option not found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuOption(
            @Parameter(description = "Menu option ID", required = true)
            @PathVariable Long id) {
        log.info("Deleting menu option with ID: {}", id);
        menuOptionService.deleteMenuOption(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get active menu options",
        description = "Retrieve all active menu options ordered by display order"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of active menu options"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/active")
    public ResponseEntity<List<MenuOptionResponseDto>> getActiveMenuOptions() {
        log.info("Retrieving all active menu options");
        List<MenuOptionResponseDto> options = menuOptionService.getActiveMenuOptions();
        return ResponseEntity.ok(options);
    }

    @Operation(
        summary = "Get menu options for user type",
        description = "Retrieve menu options accessible by a specific user type (BR003 - Access Control by User Type)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of menu options"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid user type"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/for-user-type/{userType}")
    public ResponseEntity<List<MenuOptionResponseDto>> getMenuOptionsForUserType(
            @Parameter(description = "User type (A=Admin, R=Regular)", required = true)
            @PathVariable String userType) {
        log.info("Retrieving menu options for user type: {}", userType);
        List<MenuOptionResponseDto> options = menuOptionService.getMenuOptionsForUserType(userType);
        return ResponseEntity.ok(options);
    }

    @Operation(
        summary = "Get coming soon options",
        description = "Retrieve menu options that are marked as coming soon (BR005 - Coming Soon Feature Handling)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of coming soon options"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/coming-soon")
    public ResponseEntity<List<MenuOptionResponseDto>> getComingSoonOptions() {
        log.info("Retrieving coming soon menu options");
        List<MenuOptionResponseDto> options = menuOptionService.getComingSoonOptions();
        return ResponseEntity.ok(options);
    }

    @Operation(
        summary = "Get implemented options",
        description = "Retrieve menu options that are fully implemented and active"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of implemented options"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/implemented")
    public ResponseEntity<List<MenuOptionResponseDto>> getImplementedOptions() {
        log.info("Retrieving implemented menu options");
        List<MenuOptionResponseDto> options = menuOptionService.getImplementedOptions();
        return ResponseEntity.ok(options);
    }

    @Operation(
        summary = "Search menu options",
        description = "Search menu options by name"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful search results",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid search parameters"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/search")
    public ResponseEntity<Page<MenuOptionResponseDto>> searchMenuOptions(
            @Parameter(description = "Search term for option name")
            @RequestParam String searchTerm,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Searching menu options with term: {}", searchTerm);
        Page<MenuOptionResponseDto> options = menuOptionService.searchMenuOptions(searchTerm, pageable);
        return ResponseEntity.ok(options);
    }

    @Operation(
        summary = "Validate menu option",
        description = "Validate if a menu option number is valid (BR002 - Menu Option Validation)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Menu option is valid"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Please enter a valid option number..."
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/validate/{optionNumber}")
    public ResponseEntity<Void> validateMenuOption(
            @Parameter(description = "Menu option number to validate", required = true)
            @PathVariable Integer optionNumber,
            @Parameter(description = "Maximum option number allowed", required = true)
            @RequestParam Integer maxOptions) {
        log.info("Validating menu option: {} against max: {}", optionNumber, maxOptions);
        menuOptionService.validateMenuOption(optionNumber, maxOptions);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Check option accessibility",
        description = "Check if a menu option is accessible by a specific user type"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Accessibility check completed"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @GetMapping("/{optionNumber}/accessible-by/{userType}")
    public ResponseEntity<Boolean> isOptionAccessibleByUser(
            @Parameter(description = "Menu option number", required = true)
            @PathVariable Integer optionNumber,
            @Parameter(description = "User type (A=Admin, R=Regular)", required = true)
            @PathVariable String userType) {
        log.info("Checking if option {} is accessible by user type: {}", optionNumber, userType);
        boolean accessible = menuOptionService.isOptionAccessibleByUser(optionNumber, userType);
        return ResponseEntity.ok(accessible);
    }
}
