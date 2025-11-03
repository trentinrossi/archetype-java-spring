package com.example.demo.controller;

import com.example.demo.dto.InterestCalculationDTO;
import com.example.demo.service.InterestCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/interest")
@RequiredArgsConstructor
public class InterestCalculationController {
    
    private final InterestCalculationService interestCalculationService;
    
    @PostMapping("/calculate")
    public ResponseEntity<List<InterestCalculationDTO>> calculateMonthlyInterest(
            @RequestParam String processingDate) {
        List<InterestCalculationDTO> calculations = 
            interestCalculationService.calculateMonthlyInterest(processingDate);
        return ResponseEntity.ok(calculations);
    }
}
