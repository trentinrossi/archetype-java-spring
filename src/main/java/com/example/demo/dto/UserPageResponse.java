package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paginated user response")
public class UserPageResponse {
    
    @Schema(description = "List of users", required = true)
    private List<UserResponse> users;
    
    @Schema(description = "Current page number", example = "0", required = true)
    private int currentPage;
    
    @Schema(description = "Total number of pages", example = "5", required = true)
    private int totalPages;
    
    @Schema(description = "Total number of elements", example = "50", required = true)
    private long totalElements;
    
    @Schema(description = "Number of elements per page", example = "10", required = true)
    private int pageSize;
    
    @Schema(description = "Whether this is the first page", example = "true", required = true)
    private boolean first;
    
    @Schema(description = "Whether this is the last page", example = "false", required = true)
    private boolean last;
    
    @Schema(description = "Whether there is a next page", example = "true", required = true)
    private boolean hasNext;
    
    @Schema(description = "Whether there is a previous page", example = "false", required = true)
    private boolean hasPrevious;
}