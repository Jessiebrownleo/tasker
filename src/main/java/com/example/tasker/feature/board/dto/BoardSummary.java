package com.example.tasker.feature.board.dto;

import com.example.tasker.domain.Visibility;
import lombok.Data;

@Data
public class BoardSummary {
    private Long id;
    private String name;
    private Visibility visibility;
    private Long ownerId;
    private int columnCount;
    private int memberCount;
}
