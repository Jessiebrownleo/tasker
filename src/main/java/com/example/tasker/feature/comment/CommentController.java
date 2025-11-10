package com.example.tasker.feature.comment;

import com.example.tasker.domain.Comment;
import com.example.tasker.feature.comment.dto.CommentCreateAndRequest;
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
    public ResponseEntity<List<Comment>> get(@AuthenticationPrincipal UserDetails me, @PathVariable Long taskId){
        return ResponseEntity.ok(commentService.getByTask(me.getUsername(),
                taskId));
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<Void> create(@AuthenticationPrincipal UserDetails me, @PathVariable Long taskId, @RequestBody CommentCreateAndRequest request){
        commentService.create(me.getUsername(),taskId,request);
        return ResponseEntity.noContent().build();
    }

/*
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> edit()
*/
}
