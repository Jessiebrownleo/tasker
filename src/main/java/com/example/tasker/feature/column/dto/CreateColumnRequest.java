package com.example.tasker.feature.column.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateColumnRequest {
    @NotBlank(message = "Column name is required")
    @Size(max = 120, message = "Column name must be less than 120 characters")
    private String name;
    private Integer position;
}
