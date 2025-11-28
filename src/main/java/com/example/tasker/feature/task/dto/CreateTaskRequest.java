package com.example.tasker.feature.task.dto;

import com.example.tasker.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateTaskRequest {
    @NotBlank(message = "Task title is required")
    @Size(max = 200, message = "Task title must be less than 200 characters")
    private String title;

    @Size(max = 5000, message = "Task description must be less than 5000 characters")
    private String description;
    private Integer position;
    private LocalDate dueDate;
    private TaskStatus status = TaskStatus.OPEN;
    private List<Long> assigneeIds;
}
