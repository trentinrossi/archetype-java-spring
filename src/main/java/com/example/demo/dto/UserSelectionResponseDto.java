package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSelectionResponseDto {

    @Schema(description = "Unique identifier for the selection")
    private Long id;

    @Schema(description = "Selection indicator: U for update, D for delete", example = "U")
    private String selectionFlag;

    @Schema(description = "User ID of the selected user", example = "USER001")
    private String selectedUserId;

    @Schema(description = "Indicates if this is an update selection", example = "true")
    private Boolean isUpdateSelection;

    @Schema(description = "Indicates if this is a delete selection", example = "false")
    private Boolean isDeleteSelection;

    @Schema(description = "Type of selection (UPDATE or DELETE)", example = "UPDATE")
    private String selectionType;

    @Schema(description = "Timestamp when the selection was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the selection was last updated")
    private LocalDateTime updatedAt;
}
