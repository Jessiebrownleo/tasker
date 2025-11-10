package com.example.tasker.feature.board.dto;

import com.example.tasker.domain.Visibility;
import lombok.Data;

@Data
public class CreateBoardRequest {
    private String name;
    private Visibility visibility = Visibility.PRIVATE;
}
