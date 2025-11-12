package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardRecordRequestDto {

    @Size(max = 134, message = "Card data must not exceed 134 characters")
    private String cardData;
}
