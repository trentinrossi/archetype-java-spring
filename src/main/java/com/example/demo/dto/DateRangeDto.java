package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Date Range - Represents a date range with validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeDto {

    @Schema(description = "Four-digit year", example = "2023", required = true)
    @NotNull(message = "Year must be entered")
    @Min(value = 1900, message = "Year must be 1900 or later")
    @Max(value = 2100, message = "Year must be 2100 or earlier")
    private Integer year;

    @Schema(description = "Two-digit month (01-12)", example = "12", required = true)
    @NotNull(message = "Month must be entered")
    @Min(value = 1, message = "Month must be between 01 and 12")
    @Max(value = 12, message = "Month must be between 01 and 12")
    private Integer month;

    @Schema(description = "Two-digit day (01-31)", example = "31", required = true)
    @NotNull(message = "Day must be entered")
    @Min(value = 1, message = "Day must be between 01 and 31")
    @Max(value = 31, message = "Day must be between 01 and 31")
    private Integer day;

    /**
     * Convert to YYYY-MM-DD format string
     */
    public String toDateString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * Validate that the date is valid
     */
    public boolean isValidDate() {
        if (year == null || month == null || day == null) {
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > 31) {
            return false;
        }
        // Additional validation for days in month
        if (month == 2) {
            boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
            return day <= (isLeapYear ? 29 : 28);
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        }
        return true;
    }
}
