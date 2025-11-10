package com.example.tasker.feature.column.dto;

import lombok.Data;

@Data
public class CreateColumnRequest {
    private String name;
    private Integer position;
}
