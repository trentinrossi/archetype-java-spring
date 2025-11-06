package com.example.demo.service;

import com.example.demo.dto.CardCrossReferenceResponseDto;
import com.example.demo.dto.CreateCardCrossReferenceRequestDto;
import com.example.demo.dto.UpdateCardCrossReferenceRequestDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.CardCrossReference;
import com.example.demo.entity.Customer;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CardCrossReferenceRepository;
import com.example.demo.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public CardCrossReferenceResponseDto createCardCrossReference(CreateCardCrossReferenceRequestDto request) {
        log.info("Creating new card cross reference with card number: {}", request.getCardNumber());

        validateCardNumber(request.getCardNumber());

        if (cardCrossReferenceRepository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Card cross reference with card number already exists");
        }

        Customer customer = customerRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + request.getCustomerId()));

        Account account = accountRepository.findByAccountId(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + request.getAccountId()));

        CardCrossReference cardCrossReference = new CardCrossReference();
        cardCrossReference.setCardNumber(request.getCardNumber());
        cardCrossReference.setCrossReferenceData(request.getCrossReferenceData());
        cardCrossReference.setCustomer(customer);
        cardCrossReference.setAccount(account);

        CardCrossReference savedCardCrossReference = cardCrossReferenceRepository.save(cardCrossReference);
        log.info("Successfully created card cross reference with card number: {}", savedCardCrossReference.getCardNumber());
        return convertToResponse(savedCardCrossReference);
    }

    @Transactional(readOnly = true)
    public Optional<CardCrossReferenceResponseDto> getCardCrossReferenceByCardNumber(String cardNumber) {
        log.debug("Retrieving card cross reference with card number: {}", cardNumber);
        validateCardNumber(cardNumber);
        return cardCrossReferenceRepository.findByCardNumber(cardNumber).map(this::convertToResponse);
    }

    @Transactional
    public CardCrossReferenceResponseDto updateCardCrossReference(String cardNumber, UpdateCardCrossReferenceRequestDto request) {
        log.info("Updating card cross reference with card number: {}", cardNumber);
        validateCardNumber(cardNumber);

        CardCrossReference cardCrossReference = cardCrossReferenceRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card cross reference not found with card number: " + cardNumber));

        if (request.getCrossReferenceData() != null) {
            cardCrossReference.setCrossReferenceData(request.getCrossReferenceData());
        }

        CardCrossReference updatedCardCrossReference = cardCrossReferenceRepository.save(cardCrossReference);
        log.info("Successfully updated card cross reference with card number: {}", updatedCardCrossReference.getCardNumber());
        return convertToResponse(updatedCardCrossReference);
    }

    @Transactional
    public void deleteCardCrossReference(String cardNumber) {
        log.info("Deleting card cross reference with card number: {}", cardNumber);
        validateCardNumber(cardNumber);

        CardCrossReference cardCrossReference = cardCrossReferenceRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card cross reference not found with card number: " + cardNumber));

        cardCrossReferenceRepository.delete(cardCrossReference);
        log.info("Successfully deleted card cross reference with card number: {}", cardNumber);
    }

    @Transactional(readOnly = true)
    public Page<CardCrossReferenceResponseDto> getAllCardCrossReferences(Pageable pageable) {
        log.debug("Retrieving all card cross references with pagination");
        return cardCrossReferenceRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<CardCrossReferenceResponseDto> getCardCrossReferencesByCustomerId(Long customerId) {
        log.debug("Retrieving card cross references for customer ID: {}", customerId);
        return cardCrossReferenceRepository.findByCustomer_CustomerId(customerId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CardCrossReferenceResponseDto> getCardCrossReferencesByAccountId(Long accountId) {
        log.debug("Retrieving card cross references for account ID: {}", accountId);
        return cardCrossReferenceRepository.findByAccount_AccountId(accountId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private void validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be null or empty");
        }
        if (cardNumber.length() != 16) {
            throw new IllegalArgumentException("Card number must be exactly 16 characters");
        }
    }

    private CardCrossReferenceResponseDto convertToResponse(CardCrossReference cardCrossReference) {
        CardCrossReferenceResponseDto response = new CardCrossReferenceResponseDto();
        response.setCardNumber(cardCrossReference.getCardNumber());
        response.setCrossReferenceData(cardCrossReference.getCrossReferenceData());
        response.setCustomerId(cardCrossReference.getCustomer() != null ? 
                String.valueOf(cardCrossReference.getCustomer().getCustomerId()) : null);
        response.setAccountId(cardCrossReference.getAccount() != null ? 
                String.valueOf(cardCrossReference.getAccount().getAccountId()) : null);
        response.setCreatedAt(cardCrossReference.getCreatedAt());
        response.setUpdatedAt(cardCrossReference.getUpdatedAt());
        return response;
    }
}
