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
        log.info("Creating new menu option with option number: {}", request.getOptionNumber());

        if (menuOptionRepository.existsByOptionNumber(request.getOptionNumber())) {
            throw new IllegalArgumentException("Menu option with option number already exists");
        }

        MenuOption menuOption = new MenuOption();
        menuOption.setOptionNumber(request.getOptionNumber());
        menuOption.setOptionName(request.getOptionName());
        menuOption.setProgramName(request.getProgramName());
        menuOption.setUserTypeRequired(request.getUserTypeRequired());
        menuOption.setOptionCount(request.getOptionCount());

        MenuOption savedMenuOption = menuOptionRepository.save(menuOption);
        log.info("Successfully created menu option with ID: {}", savedMenuOption.getId());
        return convertToResponse(savedMenuOption);
    }

    @Transactional(readOnly = true)
    public Optional<MenuOptionResponseDto> getMenuOptionById(Long id) {
        log.debug("Fetching menu option with ID: {}", id);
        return menuOptionRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<MenuOptionResponseDto> getMenuOptionByOptionNumber(Integer optionNumber) {
        log.debug("Fetching menu option with option number: {}", optionNumber);
        return menuOptionRepository.findByOptionNumber(optionNumber).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<MenuOptionResponseDto> getAllMenuOptions() {
        log.debug("Fetching all menu options");
        return menuOptionRepository.findAllByOrderByOptionNumberAsc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<MenuOptionResponseDto> getAllMenuOptions(Pageable pageable) {
        log.debug("Fetching menu options with pagination");
        return menuOptionRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<MenuOptionResponseDto> getAccessibleOptionsForUserType(String userType) {
        log.debug("Fetching accessible menu options for user type: {}", userType);
        return menuOptionRepository.findAccessibleOptionsForUserType(userType)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuOptionResponseDto updateMenuOption(Long id, UpdateMenuOptionRequestDto request) {
        log.info("Updating menu option with ID: {}", id);

        MenuOption menuOption = menuOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu option not found with ID: " + id));

        if (request.getOptionNumber() != null) {
            if (!request.getOptionNumber().equals(menuOption.getOptionNumber()) &&
                    menuOptionRepository.existsByOptionNumber(request.getOptionNumber())) {
                throw new IllegalArgumentException("Menu option with option number already exists");
            }
            menuOption.setOptionNumber(request.getOptionNumber());
        }

        if (request.getOptionName() != null) menuOption.setOptionName(request.getOptionName());
        if (request.getProgramName() != null) menuOption.setProgramName(request.getProgramName());
        if (request.getUserTypeRequired() != null) menuOption.setUserTypeRequired(request.getUserTypeRequired());
        if (request.getOptionCount() != null) menuOption.setOptionCount(request.getOptionCount());

        MenuOption updatedMenuOption = menuOptionRepository.save(menuOption);
        log.info("Successfully updated menu option with ID: {}", id);
        return convertToResponse(updatedMenuOption);
    }

    @Transactional
    public void deleteMenuOption(Long id) {
        log.info("Deleting menu option with ID: {}", id);

        if (!menuOptionRepository.existsById(id)) {
            throw new IllegalArgumentException("Menu option not found with ID: " + id);
        }

        menuOptionRepository.deleteById(id);
        log.info("Successfully deleted menu option with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public void validateMenuOptionAccess(Integer optionNumber, String userType) {
        log.debug("Validating menu option {} access for user type: {}", optionNumber, userType);

        MenuOption menuOption = menuOptionRepository.findByOptionNumber(optionNumber)
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu option number: " + optionNumber));

        if (!menuOption.canUserAccess(userType)) {
            throw new SecurityException("Access denied: User type " + userType + " cannot access this menu option");
        }
    }

    private MenuOptionResponseDto convertToResponse(MenuOption menuOption) {
        MenuOptionResponseDto response = new MenuOptionResponseDto();
        response.setId(menuOption.getId());
        response.setOptionNumber(menuOption.getOptionNumber());
        response.setOptionName(menuOption.getOptionName());
        response.setProgramName(menuOption.getProgramName());
        response.setUserTypeRequired(menuOption.getUserTypeRequired());
        response.setOptionCount(menuOption.getOptionCount());
        response.setIsAdminOnly(menuOption.isAdminOnly());
        response.setIsUserAccessible(menuOption.isUserAccessible());
        response.setDisplayText(menuOption.getDisplayText());
        response.setCreatedAt(menuOption.getCreatedAt());
        response.setUpdatedAt(menuOption.getUpdatedAt());
        return response;
    }
}
