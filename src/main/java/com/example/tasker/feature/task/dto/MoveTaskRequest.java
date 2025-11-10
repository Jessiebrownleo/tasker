package com.example.tasker.feature.task.dto;

import lombok.Data;

@Data
public class MoveTaskRequest {
    private Long toColumnId;
    private Integer position;
}
