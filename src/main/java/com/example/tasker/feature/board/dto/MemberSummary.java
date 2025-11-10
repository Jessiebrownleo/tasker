package com.example.tasker.feature.board.dto;

import com.example.tasker.domain.BoardRole;
import lombok.Data;

@Data
public class MemberSummary {
    private Long userId;
    private String fullName;
    private String email;
    private BoardRole role;
}
