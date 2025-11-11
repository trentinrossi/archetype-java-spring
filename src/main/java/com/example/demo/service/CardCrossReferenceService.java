package com.example.demo.service;

import com.example.demo.dto.CreateCardCrossReferenceRequestDto;
import com.example.demo.dto.CardCrossReferenceResponseDto;
import com.example.demo.entity.CardCrossReference;
import com.example.demo.repository.CardCrossReferenceRepository;
import com.example.demo.repository.AccountRepository;
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
public class CardCrossReferenceService {

    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public CardCrossReferenceResponseDto createCardCrossReference(CreateCardCrossReferenceRequestDto request) {
        log.info("Creating card cross-reference for account ID: {} and card number: {}", 
                request.getAccountId(), request.getCardNumber());

        // Validate account exists
        if (!accountRepository.existsByAccountId(request.getAccountId())) {
            throw new IllegalArgumentException("Account ID not found: " + request.getAccountId());
        }

        // Check if cross-reference already exists
        if (cardCrossReferenceRepository.existsByAccountIdAndCardNumber(
                request.getAccountId(), request.getCardNumber())) {
            throw new IllegalArgumentException("Card cross-reference already exists for this account and card");
        }

        CardCrossReference crossReference = new CardCrossReference();
        crossReference.setAccountId(request.getAccountId());
        crossReference.setCardNumber(request.getCardNumber());

        CardCrossReference saved = cardCrossReferenceRepository.save(crossReference);
        log.info("Card cross-reference created successfully for account ID: {}", saved.getAccountId());
        
        return convertToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Optional<CardCrossReferenceResponseDto> getCardCrossReference(String accountId, String cardNumber) {
        log.info("Retrieving card cross-reference for account ID: {} and card number: {}", accountId, cardNumber);
        return cardCrossReferenceRepository.findByAccountIdAndCardNumber(accountId, cardNumber)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<CardCrossReferenceResponseDto> getCardCrossReferencesByAccountId(String accountId) {
        log.info("Retrieving all card cross-references for account ID: {}", accountId);
        return cardCrossReferenceRepository.findByAccountId(accountId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CardCrossReferenceResponseDto> getCardCrossReferencesByCardNumber(String cardNumber) {
        log.info("Retrieving all card cross-references for card number: {}", cardNumber);
        return cardCrossReferenceRepository.findByCardNumber(cardNumber).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<CardCrossReferenceResponseDto> getAllCardCrossReferences(Pageable pageable) {
        log.info("Retrieving all card cross-references with pagination");
        return cardCrossReferenceRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public void deleteCardCrossReference(String accountId, String cardNumber) {
        log.info("Deleting card cross-reference for account ID: {} and card number: {}", accountId, cardNumber);

        CardCrossReference.CardCrossReferenceId id = new CardCrossReference.CardCrossReferenceId(accountId, cardNumber);
        
        if (!cardCrossReferenceRepository.existsById(id)) {
            throw new IllegalArgumentException("Card cross-reference not found");
        }

        cardCrossReferenceRepository.deleteById(id);
        log.info("Card cross-reference deleted successfully");
    }

    @Transactional
    public void deleteCardCrossReferencesByAccountId(String accountId) {
        log.info("Deleting all card cross-references for account ID: {}", accountId);
        cardCrossReferenceRepository.deleteByAccountId(accountId);
        log.info("All card cross-references deleted for account ID: {}", accountId);
    }

    @Transactional
    public void deleteCardCrossReferencesByCardNumber(String cardNumber) {
        log.info("Deleting all card cross-references for card number: {}", cardNumber);
        cardCrossReferenceRepository.deleteByCardNumber(cardNumber);
        log.info("All card cross-references deleted for card number: {}", cardNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsCardCrossReference(String accountId, String cardNumber) {
        return cardCrossReferenceRepository.existsByAccountIdAndCardNumber(accountId, cardNumber);
    }

    @Transactional(readOnly = true)
    public long countByAccountId(String accountId) {
        return cardCrossReferenceRepository.countByAccountId(accountId);
    }

    @Transactional(readOnly = true)
    public long countByCardNumber(String cardNumber) {
        return cardCrossReferenceRepository.countByCardNumber(cardNumber);
    }

    private CardCrossReferenceResponseDto convertToResponse(CardCrossReference crossReference) {
        CardCrossReferenceResponseDto response = new CardCrossReferenceResponseDto();
        response.setAccountId(crossReference.getAccountId());
        response.setCardNumber(crossReference.getCardNumber());
        response.setCreatedAt(crossReference.getCreatedAt());
        response.setUpdatedAt(crossReference.getUpdatedAt());
        return response;
    }
}
