package com.example.tasker.feature.board.dto;

import com.example.tasker.domain.BoardRole;
import lombok.Data;

@Data
public class UpdateMemberRequest {
    private BoardRole role;
}
