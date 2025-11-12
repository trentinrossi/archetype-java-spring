package com.example.demo.service;

import com.example.demo.dto.CreateCardRecordRequestDto;
import com.example.demo.dto.UpdateCardRecordRequestDto;
import com.example.demo.dto.CardRecordResponseDto;
import com.example.demo.entity.CardRecord;
import com.example.demo.repository.CardRecordRepository;
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
public class CardRecordService {

    private final CardRecordRepository cardRecordRepository;

    @Transactional
    public CardRecordResponseDto createCardRecord(CreateCardRecordRequestDto request) {
        log.info("Creating new card record with card number: {}", request.getCardNumber());

        if (cardRecordRepository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Card record with card number already exists");
        }

        CardRecord cardRecord = new CardRecord();
        cardRecord.setCardNumber(request.getCardNumber());
        cardRecord.setCardData(request.getCardData());

        CardRecord savedCardRecord = cardRecordRepository.save(cardRecord);
        log.info("Successfully created card record with card number: {}", savedCardRecord.getCardNumber());
        
        return convertToResponse(savedCardRecord);
    }

    @Transactional(readOnly = true)
    public Optional<CardRecordResponseDto> getCardRecordByCardNumber(String cardNumber) {
        log.info("Retrieving card record with card number: {}", cardNumber);
        return cardRecordRepository.findByCardNumber(cardNumber).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CardRecordResponseDto> getAllCardRecords(Pageable pageable) {
        log.info("Retrieving all card records with pagination");
        return cardRecordRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public CardRecordResponseDto updateCardRecord(String cardNumber, UpdateCardRecordRequestDto request) {
        log.info("Updating card record with card number: {}", cardNumber);
        
        CardRecord cardRecord = cardRecordRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card record not found"));

        if (request.getCardData() != null) {
            cardRecord.setCardData(request.getCardData());
        }

        CardRecord updatedCardRecord = cardRecordRepository.save(cardRecord);
        log.info("Successfully updated card record with card number: {}", updatedCardRecord.getCardNumber());
        
        return convertToResponse(updatedCardRecord);
    }

    @Transactional
    public void deleteCardRecord(String cardNumber) {
        log.info("Deleting card record with card number: {}", cardNumber);
        
        CardRecord cardRecord = cardRecordRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card record not found"));
        
        cardRecordRepository.delete(cardRecord);
        log.info("Successfully deleted card record with card number: {}", cardNumber);
    }

    private CardRecordResponseDto convertToResponse(CardRecord cardRecord) {
        CardRecordResponseDto response = new CardRecordResponseDto();
        response.setCardNumber(cardRecord.getCardNumber());
        response.setCardData(cardRecord.getCardData());
        response.setCreatedAt(cardRecord.getCreatedAt());
        response.setUpdatedAt(cardRecord.getUpdatedAt());
        return response;
    }
}
