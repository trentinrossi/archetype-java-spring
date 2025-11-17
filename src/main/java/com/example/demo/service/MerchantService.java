package com.example.demo.service;

import com.example.demo.dto.CreateMerchantRequestDto;
import com.example.demo.dto.MerchantResponseDto;
import com.example.demo.dto.UpdateMerchantRequestDto;
import com.example.demo.entity.Merchant;
import com.example.demo.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantService {

    private final MerchantRepository merchantRepository;

    /**
     * BR008: Merchant ID Validation - Merchant ID must be provided and numeric
     * BR009: Mandatory Field Validation - All merchant fields must be populated
     */
    @Transactional
    public MerchantResponseDto createMerchant(CreateMerchantRequestDto request) {
        log.info("Creating new merchant with ID: {}", request.getMerchantId());
        
        if (merchantRepository.existsByMerchantId(request.getMerchantId())) {
            throw new IllegalArgumentException("Merchant ID already exists");
        }
        
        validateMerchantRequest(request);
        
        Merchant merchant = new Merchant();
        merchant.setMerchantId(request.getMerchantId());
        merchant.setMerchantName(request.getMerchantName());
        merchant.setMerchantCity(request.getMerchantCity());
        merchant.setMerchantZip(request.getMerchantZip());
        
        Merchant savedMerchant = merchantRepository.save(merchant);
        log.info("Merchant created successfully with ID: {}", savedMerchant.getMerchantId());
        
        return convertToResponse(savedMerchant);
    }

    @Transactional(readOnly = true)
    public Optional<MerchantResponseDto> getMerchantById(String merchantId) {
        log.info("Retrieving merchant with ID: {}", merchantId);
        
        validateMerchantId(merchantId);
        
        return merchantRepository.findByMerchantId(merchantId)
                .map(this::convertToResponse);
    }

    @Transactional
    public MerchantResponseDto updateMerchant(String merchantId, UpdateMerchantRequestDto request) {
        log.info("Updating merchant with ID: {}", merchantId);
        
        Merchant merchant = merchantRepository.findByMerchantId(merchantId)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));
        
        if (request.getMerchantName() != null) {
            merchant.setMerchantName(request.getMerchantName());
        }
        if (request.getMerchantCity() != null) {
            merchant.setMerchantCity(request.getMerchantCity());
        }
        if (request.getMerchantZip() != null) {
            merchant.setMerchantZip(request.getMerchantZip());
        }
        
        Merchant updatedMerchant = merchantRepository.save(merchant);
        return convertToResponse(updatedMerchant);
    }

    @Transactional
    public void deleteMerchant(String merchantId) {
        log.info("Deleting merchant with ID: {}", merchantId);
        
        if (!merchantRepository.existsByMerchantId(merchantId)) {
            throw new IllegalArgumentException("Merchant not found");
        }
        
        merchantRepository.deleteById(merchantId);
    }

    @Transactional(readOnly = true)
    public Page<MerchantResponseDto> getAllMerchants(Pageable pageable) {
        return merchantRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<MerchantResponseDto> searchMerchantsByName(String searchTerm, Pageable pageable) {
        log.info("Searching merchants by name: {}", searchTerm);
        return merchantRepository.searchByMerchantName(searchTerm, pageable)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<MerchantResponseDto> getMerchantsByCity(String city, Pageable pageable) {
        log.info("Retrieving merchants by city: {}", city);
        return merchantRepository.findByMerchantCity(city, pageable)
                .map(this::convertToResponse);
    }

    /**
     * BR008: Merchant ID Validation
     */
    public void validateMerchantExists(String merchantId) {
        log.info("BR008: Validating merchant ID: {}", merchantId);
        
        validateMerchantId(merchantId);
        
        if (!merchantRepository.existsByMerchantId(merchantId)) {
            throw new IllegalArgumentException("Merchant ID not found");
        }
    }

    private void validateMerchantId(String merchantId) {
        if (merchantId == null || merchantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant ID must be entered");
        }
        if (!merchantId.matches("\\d+")) {
            throw new IllegalArgumentException("Merchant ID must be numeric");
        }
        if (merchantId.length() > 9) {
            throw new IllegalArgumentException("Merchant ID must not exceed 9 digits");
        }
    }

    private void validateMerchantRequest(CreateMerchantRequestDto request) {
        if (request.getMerchantName() == null || request.getMerchantName().trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant Name must be entered");
        }
        if (request.getMerchantCity() == null || request.getMerchantCity().trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant City must be entered");
        }
        if (request.getMerchantZip() == null || request.getMerchantZip().trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant Zip must be entered");
        }
    }

    private MerchantResponseDto convertToResponse(Merchant merchant) {
        MerchantResponseDto response = new MerchantResponseDto();
        response.setMerchantId(merchant.getMerchantId());
        response.setMerchantName(merchant.getMerchantName());
        response.setMerchantCity(merchant.getMerchantCity());
        response.setMerchantZip(merchant.getMerchantZip());
        response.setTransactionCount(merchant.getTransactions() != null ? merchant.getTransactions().size() : 0);
        response.setCreatedAt(merchant.getCreatedAt());
        response.setUpdatedAt(merchant.getUpdatedAt());
        
        return response;
    }
}
