package com.example.tasker.feature.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentUpdateRequest {
    @NotBlank(message = "Comment body is required")
    @Size(max = 2000, message = "Comment must be less than 2000 characters")
    private String body;
}
