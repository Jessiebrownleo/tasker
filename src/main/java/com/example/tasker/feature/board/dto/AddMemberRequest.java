package com.example.tasker.feature.board.dto;

import com.example.tasker.domain.BoardRole;
import lombok.Data;

@Data
public class AddMemberRequest {
    private Long userId;
    private BoardRole role=BoardRole.MEMBER;
}
