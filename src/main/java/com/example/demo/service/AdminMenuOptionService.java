package com.example.demo.service;

import com.example.demo.dto.AdminMenuOptionResponseDto;
import com.example.demo.entity.AdminMenuOption;
import com.example.demo.repository.AdminMenuOptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMenuOptionService {

    private final AdminMenuOptionRepository adminMenuOptionRepository;

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> getAllActiveAdminMenuOptions() {
        log.info("Fetching all active admin menu options");
        return adminMenuOptionRepository.findAllActiveOrderedByDisplay()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AdminMenuOptionResponseDto> getAdminMenuOptionByNumber(Integer optionNumber) {
        log.info("Fetching admin menu option by number: {}", optionNumber);
        return adminMenuOptionRepository.findByOptionNumber(optionNumber).map(this::convertToResponse);
    }

    private AdminMenuOptionResponseDto convertToResponse(AdminMenuOption option) {
        AdminMenuOptionResponseDto response = new AdminMenuOptionResponseDto();
        response.setId(option.getId());
        response.setOptionNumber(option.getOptionNumber());
        response.setOptionName(option.getOptionName());
        response.setProgramName(option.getProgramName());
        response.setIsActive(option.getIsActive());
        response.setDisplayOrder(option.getDisplayOrder());
        response.setIsComingSoon(option.isComingSoon());
        response.setStatusDisplay(option.getStatusDisplay());
        response.setCreatedAt(option.getCreatedAt());
        response.setUpdatedAt(option.getUpdatedAt());
        return response;
    }
}
