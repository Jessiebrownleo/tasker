package com.example.tasker.feature.comment;

import com.example.tasker.domain.Comment;
import com.example.tasker.domain.Task;
import com.example.tasker.domain.User;
import com.example.tasker.feature.board.BoardAuthorization;
import com.example.tasker.feature.comment.dto.CommentCreateAndRequest;
import com.example.tasker.feature.comment.dto.CommentUpdateRequest;
import com.example.tasker.feature.task.TaskRepository;
import com.example.tasker.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final BoardAuthorization boardAuth;
    private final UserRepository userRepository;

    // Get comment on task
    public List<Comment> getByTask(String email, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        boardAuth.ensureMember(email, task.getColumn().getBoard().getId());
        return commentRepository.findByTask_IdOrderByCreatedAtAsc(taskId);
    }

    // Create comment on task
    public void create(String email, Long taskId,
            CommentCreateAndRequest request) {

        Task task = taskRepository.findById(taskId).orElseThrow();
        boardAuth.ensureMember(email, task.getColumn().getBoard().getId());
        User user = userRepository.findByEmail(email).orElseThrow();
        Comment comment = new Comment();
        comment.setBody(request.getBody());
        comment.setTask(task);
        comment.setAuthor(user);
        commentRepository.save(comment);
    }

    // Edit comment
    public void edit(String email, Long commentId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // Check comment owner
        if (!Objects.equals(email, comment.getAuthor().getEmail()))
            return;
        comment.setBody(request.getBody());
        commentRepository.save(comment);

    }

    // Delete comment
    public void delete(String email, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (!Objects.equals(email, comment.getAuthor().getEmail()))
            return;
        commentRepository.delete(comment);
    }
}
