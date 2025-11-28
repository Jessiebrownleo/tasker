package com.example.tasker.feature.board.dto;

import com.example.tasker.domain.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBoardRequest {
    @NotBlank(message = "Board name is required")
    @Size(max = 120, message = "Board name must be less than 120 characters")
    private String name;

    private Visibility visibility = Visibility.PRIVATE;
}
