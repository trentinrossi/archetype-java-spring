package com.example.demo.service;

import com.example.demo.dto.CreateAccountCrossReferenceRequestDto;
import com.example.demo.dto.UpdateAccountCrossReferenceRequestDto;
import com.example.demo.dto.AccountCrossReferenceResponseDto;
import com.example.demo.entity.AccountCrossReference;
import com.example.demo.repository.AccountCrossReferenceRepository;
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
public class AccountCrossReferenceService {

    private final AccountCrossReferenceRepository repository;

    @Transactional
    public AccountCrossReferenceResponseDto create(CreateAccountCrossReferenceRequestDto request) {
        log.info("Creating new account cross reference with card number: {}", request.getCardNumber());

        if (repository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Account cross reference with card number already exists");
        }

        AccountCrossReference entity = new AccountCrossReference();
        entity.setCardNumber(request.getCardNumber());
        entity.setCrossReferenceData(request.getCrossReferenceData());

        AccountCrossReference saved = repository.save(entity);
        log.info("Successfully created account cross reference");
        
        return convertToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Optional<AccountCrossReferenceResponseDto> getByCardNumber(String cardNumber) {
        log.info("Retrieving account cross reference with card number: {}", cardNumber);
        return repository.findByCardNumber(cardNumber).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<AccountCrossReferenceResponseDto> getAll(Pageable pageable) {
        log.info("Retrieving all account cross references");
        return repository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public AccountCrossReferenceResponseDto update(String cardNumber, UpdateAccountCrossReferenceRequestDto request) {
        log.info("Updating account cross reference with card number: {}", cardNumber);
        
        AccountCrossReference entity = repository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account cross reference not found"));

        if (request.getCrossReferenceData() != null) {
            entity.setCrossReferenceData(request.getCrossReferenceData());
        }

        AccountCrossReference updated = repository.save(entity);
        log.info("Successfully updated account cross reference");
        
        return convertToResponse(updated);
    }

    @Transactional
    public void delete(String cardNumber) {
        log.info("Deleting account cross reference with card number: {}", cardNumber);
        
        AccountCrossReference entity = repository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account cross reference not found"));
        
        repository.delete(entity);
        log.info("Successfully deleted account cross reference");
    }

    private AccountCrossReferenceResponseDto convertToResponse(AccountCrossReference entity) {
        AccountCrossReferenceResponseDto response = new AccountCrossReferenceResponseDto();
        response.setCardNumber(entity.getCardNumber());
        response.setCrossReferenceData(entity.getCrossReferenceData());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}
