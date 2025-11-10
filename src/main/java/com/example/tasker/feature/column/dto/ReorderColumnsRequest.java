package com.example.tasker.feature.column.dto;

import com.example.tasker.feature.board.dto.ReorderItem;
import lombok.Data;

import java.util.List;

@Data
public class ReorderColumnsRequest {
    List<ReorderItem> items;
}
