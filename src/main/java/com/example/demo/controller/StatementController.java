package com.example.demo.controller;

import com.example.demo.dto.CreateStatementRequestDto;
import com.example.demo.dto.StatementResponseDto;
import com.example.demo.enums.StatementStatus;
import com.example.demo.service.StatementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statements")
public class StatementController {

    private final StatementService statementService;

    @GetMapping
    public ResponseEntity<Page<StatementResponseDto>> getAllStatements(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all statements with pagination");
        Page<StatementResponseDto> statements = statementService.getAllStatements(pageable);
        return ResponseEntity.ok(statements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatementResponseDto> getStatementById(@PathVariable Long id) {
        log.info("Retrieving statement by ID: {}", id);
        return statementService.getStatementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<StatementResponseDto>> getStatementsByAccountId(
            @PathVariable Long accountId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving statements for account ID: {}", accountId);
        Page<StatementResponseDto> statements = statementService.getStatementsByAccountId(accountId, pageable);
        return ResponseEntity.ok(statements);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<StatementResponseDto>> getStatementsByCustomerId(
            @PathVariable Long customerId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving statements for customer ID: {}", customerId);
        Page<StatementResponseDto> statements = statementService.getStatementsByCustomerId(customerId, pageable);
        return ResponseEntity.ok(statements);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<StatementResponseDto>> getStatementsByStatus(
            @PathVariable StatementStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving statements with status: {}", status);
        Page<StatementResponseDto> statements = statementService.getStatementsByStatus(status, pageable);
        return ResponseEntity.ok(statements);
    }

    @PostMapping
    public ResponseEntity<StatementResponseDto> generateStatement(
            @Valid @RequestBody CreateStatementRequestDto request) {
        log.info("Generating statement for account ID: {}", request.getAccountId());
        StatementResponseDto response = statementService.generateStatement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatement(@PathVariable Long id) {
        log.info("Deleting statement with ID: {}", id);
        statementService.deleteStatement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/plain-text")
    public ResponseEntity<String> getPlainTextStatement(@PathVariable Long id) {
        log.info("Retrieving plain text statement for ID: {}", id);
        String plainText = statementService.getPlainTextStatement(id);
        return ResponseEntity.ok(plainText);
    }

    @GetMapping("/{id}/html")
    public ResponseEntity<String> getHtmlStatement(@PathVariable Long id) {
        log.info("Retrieving HTML statement for ID: {}", id);
        String html = statementService.getHtmlStatement(id);
        return ResponseEntity.ok(html);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<StatementResponseDto> updateStatementStatus(
            @PathVariable Long id,
            @RequestParam StatementStatus status) {
        log.info("Updating statement status for ID: {} to {}", id, status);
        StatementResponseDto response = statementService.updateStatementStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/regenerate")
    public ResponseEntity<StatementResponseDto> regenerateStatementFormats(@PathVariable Long id) {
        log.info("Regenerating statement formats for ID: {}", id);
        StatementResponseDto response = statementService.regenerateStatementFormats(id);
        return ResponseEntity.ok(response);
    }
}
