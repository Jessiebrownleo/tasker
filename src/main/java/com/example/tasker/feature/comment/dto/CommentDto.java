package com.example.tasker.feature.comment.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class CommentDto {
    private Long id;
    private String body;
    private Instant createdAt;
    private AuthorDto author;

    @Data
    public static class AuthorDto {
        private Long id;
        private String fullName;
        private String email;
    }
}
