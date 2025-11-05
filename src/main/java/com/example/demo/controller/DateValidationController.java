package com.example.demo.controller;

import com.example.demo.service.DateValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validation")
@RequiredArgsConstructor
public class DateValidationController {
    
    private final DateValidationService dateValidationService;
    
    /**
     * Validates a date string against a format
     * Endpoint: GET /api/validation/date
     */
    @GetMapping("/date")
    public ResponseEntity<DateValidationService.DateValidationResult> validateDate(
            @RequestParam String dateString,
            @RequestParam(defaultValue = "yyyy-MM-dd") String dateFormat) {
        DateValidationService.DateValidationResult result = dateValidationService.validateDate(dateString, dateFormat);
        return ResponseEntity.ok(result);
    }
}
