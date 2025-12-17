package com.example.tasker.feature.comment;

import com.example.tasker.feature.comment.dto.CommentCreateAndRequest;
import com.example.tasker.feature.comment.dto.CommentUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{taskId}")
    public ResponseEntity<List<com.example.tasker.feature.comment.dto.CommentDto>> get(
            @AuthenticationPrincipal UserDetails me, @PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getByTask(me.getUsername(),
                taskId));
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<Void> create(@AuthenticationPrincipal UserDetails me, @PathVariable Long taskId,
            @RequestBody CommentCreateAndRequest request) {
        commentService.create(me.getUsername(), taskId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> edit(@AuthenticationPrincipal UserDetails me, @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request) {
        commentService.edit(me.getUsername(), commentId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails me, @PathVariable Long commentId) {
        commentService.delete(me.getUsername(), commentId);
        return ResponseEntity.noContent().build();
    }
}
