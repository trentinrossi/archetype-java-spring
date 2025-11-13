package com.example.demo.service;

import com.example.demo.dto.CreateMenuOptionRequestDto;
import com.example.demo.dto.UpdateMenuOptionRequestDto;
import com.example.demo.dto.MenuOptionResponseDto;
import com.example.demo.entity.MenuOption;
import com.example.demo.repository.MenuOptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;

    @Transactional
    public MenuOptionResponseDto createMenuOption(CreateMenuOptionRequestDto request) {
        log.info("Creating new menu option: {}", request.getOptionName());

        if (menuOptionRepository.existsByOptionNumber(request.getOptionNumber())) {
            log.error("Menu option number already exists: {}", request.getOptionNumber());
            throw new IllegalArgumentException("Menu option number already exists");
        }

        MenuOption menuOption = new MenuOption();
        menuOption.setOptionNumber(request.getOptionNumber());
        menuOption.setOptionName(request.getOptionName());
        menuOption.setProgramName(request.getProgramName());
        menuOption.setUserTypeRequired(request.getUserTypeRequired());
        menuOption.setOptionCount(request.getOptionCount());
        menuOption.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        menuOption.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : request.getOptionNumber());

        MenuOption savedOption = menuOptionRepository.save(menuOption);
        log.info("Menu option created successfully: {}", savedOption.getOptionName());
        return convertToResponse(savedOption);
    }

    @Transactional(readOnly = true)
    public Optional<MenuOptionResponseDto> getMenuOptionById(Long id) {
        log.info("Fetching menu option by id: {}", id);
        return menuOptionRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<MenuOptionResponseDto> getMenuOptionByNumber(Integer optionNumber) {
        log.info("Fetching menu option by number: {}", optionNumber);
        return menuOptionRepository.findByOptionNumber(optionNumber).map(this::convertToResponse);
    }

    @Transactional
    public MenuOptionResponseDto updateMenuOption(Long id, UpdateMenuOptionRequestDto request) {
        log.info("Updating menu option with id: {}", id);

        MenuOption menuOption = menuOptionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Menu option not found: {}", id);
                    return new IllegalArgumentException("Menu option not found");
                });

        if (request.getOptionNumber() != null) {
            menuOption.setOptionNumber(request.getOptionNumber());
        }
        if (request.getOptionName() != null) {
            menuOption.setOptionName(request.getOptionName());
        }
        if (request.getProgramName() != null) {
            menuOption.setProgramName(request.getProgramName());
        }
        if (request.getUserTypeRequired() != null) {
            menuOption.setUserTypeRequired(request.getUserTypeRequired());
        }
        if (request.getOptionCount() != null) {
            menuOption.setOptionCount(request.getOptionCount());
        }
        if (request.getIsActive() != null) {
            menuOption.setIsActive(request.getIsActive());
        }
        if (request.getDisplayOrder() != null) {
            menuOption.setDisplayOrder(request.getDisplayOrder());
        }

        MenuOption updatedOption = menuOptionRepository.save(menuOption);
        log.info("Menu option updated successfully: {}", updatedOption.getOptionName());
        return convertToResponse(updatedOption);
    }

    @Transactional
    public void deleteMenuOption(Long id) {
        log.info("Deleting menu option with id: {}", id);

        if (!menuOptionRepository.existsById(id)) {
            log.error("Menu option not found: {}", id);
            throw new IllegalArgumentException("Menu option not found");
        }

        menuOptionRepository.deleteById(id);
        log.info("Menu option deleted successfully: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<MenuOptionResponseDto> getAllMenuOptions(Pageable pageable) {
        log.info("Fetching all menu options - page: {}", pageable.getPageNumber());
        return menuOptionRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<MenuOptionResponseDto> getActiveMenuOptions() {
        log.info("Fetching all active menu options");
        return menuOptionRepository.findAllActiveOrderedByDisplay()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuOptionResponseDto> getMenuOptionsForUserType(String userType) {
        log.info("Fetching menu options for user type: {}", userType);
        
        if ("A".equalsIgnoreCase(userType)) {
            // Admin can see all active options
            return menuOptionRepository.findAllActiveOrderedByDisplay()
                    .stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } else {
            // Regular users can only see non-admin options
            return menuOptionRepository.findActiveByUserTypeOrdered("U")
                    .stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        }
    }

    @Transactional(readOnly = true)
    public Page<MenuOptionResponseDto> getMenuOptionsByUserType(String userType, Pageable pageable) {
        log.info("Fetching menu options for user type: {} - page: {}", userType, pageable.getPageNumber());
        return menuOptionRepository.findByUserTypeRequired(userType, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<MenuOptionResponseDto> getComingSoonOptions() {
        log.info("Fetching coming soon menu options");
        return menuOptionRepository.findComingSoonOptions()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuOptionResponseDto> getImplementedOptions() {
        log.info("Fetching implemented menu options");
        return menuOptionRepository.findImplementedOptions()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<MenuOptionResponseDto> searchMenuOptions(String searchTerm, Pageable pageable) {
        log.info("Searching menu options with term: {}", searchTerm);
        return menuOptionRepository.searchByOptionName(searchTerm, pageable).map(this::convertToResponse);
    }

    public void validateMenuOption(Integer optionNumber, Integer maxOptions) {
        log.info("Validating menu option: {} against max: {}", optionNumber, maxOptions);

        if (optionNumber == null || optionNumber == 0) {
            log.error("Menu option is null or zero");
            throw new IllegalArgumentException("Please enter a valid option number...");
        }

        if (optionNumber > maxOptions) {
            log.error("Menu option {} exceeds maximum {}", optionNumber, maxOptions);
            throw new IllegalArgumentException("Please enter a valid option number...");
        }

        Optional<MenuOption> menuOption = menuOptionRepository.findActiveByOptionNumber(optionNumber);
        if (menuOption.isEmpty()) {
            log.error("Menu option not found or inactive: {}", optionNumber);
            throw new IllegalArgumentException("Please enter a valid option number...");
        }

        log.info("Menu option validated successfully: {}", optionNumber);
    }

    public boolean isOptionAccessibleByUser(Integer optionNumber, String userType) {
        log.info("Checking if option {} is accessible by user type: {}", optionNumber, userType);

        Optional<MenuOption> menuOption = menuOptionRepository.findByOptionNumber(optionNumber);
        if (menuOption.isEmpty()) {
            return false;
        }

        return menuOption.get().isAccessibleByUserType(userType);
    }

    private MenuOptionResponseDto convertToResponse(MenuOption menuOption) {
        MenuOptionResponseDto response = new MenuOptionResponseDto();
        response.setId(menuOption.getId());
        response.setOptionNumber(menuOption.getOptionNumber());
        response.setOptionName(menuOption.getOptionName());
        response.setProgramName(menuOption.getProgramName());
        response.setUserTypeRequired(menuOption.getUserTypeRequired());
        response.setOptionCount(menuOption.getOptionCount());
        response.setIsActive(menuOption.getIsActive());
        response.setDisplayOrder(menuOption.getDisplayOrder());
        response.setIsAdminOnly(menuOption.isAdminOnly());
        response.setIsUserAccessible(menuOption.isUserAccessible());
        response.setIsComingSoon(menuOption.isComingSoon());
        response.setAccessLevelDisplay(menuOption.getAccessLevelDisplay());
        response.setStatusDisplay(menuOption.getStatusDisplay());
        response.setCreatedAt(menuOption.getCreatedAt());
        response.setUpdatedAt(menuOption.getUpdatedAt());
        return response;
    }
}
