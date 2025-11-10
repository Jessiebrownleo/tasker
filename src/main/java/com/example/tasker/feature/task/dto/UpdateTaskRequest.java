package com.example.tasker.feature.task.dto;

import com.example.tasker.domain.TaskStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private Integer position;
    private LocalDate dueDate;
    private TaskStatus status;
    private List<Long> assigneeIds;
}
