package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {
    
    @Schema(description = "List of users", required = true)
    private List<UserResponseDTO> users;
    
    @Schema(description = "Current page number", example = "0", required = true)
    private int currentPage;
    
    @Schema(description = "Total number of pages", example = "5", required = true)
    private int totalPages;
    
    @Schema(description = "Total number of elements", example = "100", required = true)
    private long totalElements;
    
    @Schema(description = "Number of elements per page", example = "20", required = true)
    private int pageSize;
    
    @Schema(description = "Indicates if this is the first page", example = "true", required = true)
    private boolean isFirst;
    
    @Schema(description = "Indicates if this is the last page", example = "false", required = true)
    private boolean isLast;
    
    @Schema(description = "Indicates if there is a next page", example = "true", required = true)
    private boolean hasNext;
    
    @Schema(description = "Indicates if there is a previous page", example = "false", required = true)
    private boolean hasPrevious;
}