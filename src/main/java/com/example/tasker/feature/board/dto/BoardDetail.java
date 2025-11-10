package com.example.tasker.feature.board.dto;

import com.example.tasker.domain.Visibility;
import com.example.tasker.feature.column.dto.ColumnSummary;
import lombok.Data;

import java.util.List;

@Data
public class BoardDetail {
    private Long id;
    private String name;
    private Visibility visibility;
    private Long ownerId;
    private List<ColumnSummary> columns;
    private List<MemberSummary> members;
}
