package com.example.tasker.feature.task.dto;

import com.example.tasker.domain.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskSummary {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private int position;
    private Long columnId;
    private LocalDate dueDate;
}
