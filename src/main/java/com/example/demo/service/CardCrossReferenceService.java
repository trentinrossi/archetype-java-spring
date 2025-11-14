package com.example.demo.service;

import com.example.demo.dto.CreateCardCrossReferenceRequestDto;
import com.example.demo.dto.UpdateCardCrossReferenceRequestDto;
import com.example.demo.dto.CardCrossReferenceResponseDto;
import com.example.demo.entity.CardCrossReference;
import com.example.demo.repository.CardCrossReferenceRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
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
public class CardCrossReferenceService {

    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public CardCrossReferenceResponseDto createCardCrossReference(CreateCardCrossReferenceRequestDto request) {
        log.info("Creating new card cross reference for account ID: {}", request.getAccountId());

        validateAccountExists(request.getAccountId());
        validateCustomerExists(request.getCustomerId());

        CardCrossReference cardCrossReference = new CardCrossReference();
        cardCrossReference.setAccountId(request.getAccountId());
        cardCrossReference.setCustomerId(request.getCustomerId());

        CardCrossReference saved = cardCrossReferenceRepository.save(cardCrossReference);
        log.info("Card cross reference created successfully for account ID: {}", saved.getAccountId());
        return convertToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Optional<CardCrossReferenceResponseDto> getCardCrossReferenceById(Long accountId) {
        log.info("Retrieving card cross reference for account ID: {}", accountId);
        return cardCrossReferenceRepository.findById(accountId).map(this::convertToResponse);
    }

    @Transactional
    public CardCrossReferenceResponseDto updateCardCrossReference(Long accountId, UpdateCardCrossReferenceRequestDto request) {
        log.info("Updating card cross reference for account ID: {}", accountId);

        CardCrossReference existing = cardCrossReferenceRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException(
                    String.format("Card cross reference not found for account ID: %d", accountId)
                ));

        if (request.getCustomerId() != null) {
            validateCustomerExists(request.getCustomerId());
            existing.setCustomerId(request.getCustomerId());
        }

        CardCrossReference updated = cardCrossReferenceRepository.save(existing);
        log.info("Card cross reference updated successfully for account ID: {}", accountId);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteCardCrossReference(Long accountId) {
        log.info("Deleting card cross reference for account ID: {}", accountId);
        
        if (!cardCrossReferenceRepository.existsById(accountId)) {
            throw new IllegalArgumentException(
                String.format("Card cross reference not found for account ID: %d", accountId)
            );
        }
        
        cardCrossReferenceRepository.deleteById(accountId);
        log.info("Card cross reference deleted successfully for account ID: {}", accountId);
    }

    @Transactional(readOnly = true)
    public Page<CardCrossReferenceResponseDto> getAllCardCrossReferences(Pageable pageable) {
        log.info("Retrieving all card cross references with pagination");
        return cardCrossReferenceRepository.findAll(pageable).map(this::convertToResponse);
    }

    private void validateAccountExists(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new IllegalArgumentException(
                String.format("Account with ID %d does not exist", accountId)
            );
        }
    }

    private void validateCustomerExists(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException(
                String.format("Customer with ID %d does not exist", customerId)
            );
        }
    }

    private CardCrossReferenceResponseDto convertToResponse(CardCrossReference cardCrossReference) {
        CardCrossReferenceResponseDto response = new CardCrossReferenceResponseDto();
        response.setAccountId(cardCrossReference.getAccountId());
        response.setCustomerId(cardCrossReference.getCustomerId());
        response.setCreatedAt(cardCrossReference.getCreatedAt());
        response.setUpdatedAt(cardCrossReference.getUpdatedAt());
        return response;
    }
}
