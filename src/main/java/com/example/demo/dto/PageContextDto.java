package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PageContext DTO for maintaining pagination state for card listing
 * This is used for BR006: Page Navigation Control
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageContextDto {

    @Schema(description = "Current page number being displayed", example = "1", required = true)
    private Integer screenNumber;

    @Schema(description = "First card number on current page", example = "1234567890123456", required = true)
    private String firstCardNumber;

    @Schema(description = "Last card number on current page", example = "1234567890123999", required = true)
    private String lastCardNumber;

    @Schema(description = "Indicates if more pages exist (Y/N)", example = "Y", required = true)
    private String nextPageIndicator;

    @Schema(description = "Flag indicating if last page is shown (0=last page, 9=not last page)", example = "9", required = true)
    private Integer lastPageDisplayed;

    /**
     * BR006: Page Navigation Control
     * Check if there are more pages available
     */
    public boolean hasNextPage() {
        return "Y".equals(nextPageIndicator) && lastPageDisplayed != 0;
    }

    /**
     * BR006: Page Navigation Control
     * Check if this is the last page
     */
    public boolean isLastPage() {
        return lastPageDisplayed == 0 || "N".equals(nextPageIndicator);
    }

    /**
     * BR006: Page Navigation Control
     * Check if backward navigation is possible
     */
    public boolean canNavigateBackward() {
        return screenNumber != null && screenNumber > 1;
    }

    /**
     * BR006: Page Navigation Control
     * Check if forward navigation is possible
     */
    public boolean canNavigateForward() {
        return hasNextPage();
    }

    /**
     * Create page context for first page
     */
    public static PageContextDto createFirstPage(String firstCardNumber, String lastCardNumber, boolean hasMore) {
        PageContextDto context = new PageContextDto();
        context.setScreenNumber(1);
        context.setFirstCardNumber(firstCardNumber);
        context.setLastCardNumber(lastCardNumber);
        context.setNextPageIndicator(hasMore ? "Y" : "N");
        context.setLastPageDisplayed(hasMore ? 9 : 0);
        return context;
    }

    /**
     * Create page context for subsequent pages
     */
    public static PageContextDto createPage(Integer pageNumber, String firstCardNumber, 
                                            String lastCardNumber, boolean hasMore) {
        PageContextDto context = new PageContextDto();
        context.setScreenNumber(pageNumber);
        context.setFirstCardNumber(firstCardNumber);
        context.setLastCardNumber(lastCardNumber);
        context.setNextPageIndicator(hasMore ? "Y" : "N");
        context.setLastPageDisplayed(hasMore ? 9 : 0);
        return context;
    }
}
