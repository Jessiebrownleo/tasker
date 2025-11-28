package com.example.tasker.feature.task;

import com.example.tasker.domain.Board;
import com.example.tasker.domain.ColumnEntity;
import com.example.tasker.domain.Label;
import com.example.tasker.domain.Task;
import com.example.tasker.domain.TaskAssignee;
import com.example.tasker.domain.TaskAssigneeId;
import com.example.tasker.domain.TaskLabel;
import com.example.tasker.domain.TaskLabelId;
import com.example.tasker.domain.User;
import com.example.tasker.feature.board.BoardAuthorization;
import com.example.tasker.feature.board.BoardMembershipRepository;
import com.example.tasker.feature.board.BoardRepository;
import com.example.tasker.feature.board.LabelRepository;
import com.example.tasker.feature.board.dto.LabelBrief;
import com.example.tasker.feature.column.ColumnEntityRepository;
import com.example.tasker.feature.task.dto.AssigneeBrief;
import com.example.tasker.feature.task.dto.CreateTaskRequest;
import com.example.tasker.feature.task.dto.MoveTaskRequest;
import com.example.tasker.feature.task.dto.TaskDetail;
import com.example.tasker.feature.task.dto.TaskSummary;
import com.example.tasker.feature.task.dto.UpdateTaskRequest;
import com.example.tasker.feature.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final ColumnEntityRepository columnEntityRepository;
    private final BoardRepository boardRepository;
    private final BoardMembershipRepository boardMembershipRepository;
    private final UserRepository userRepository;
    private final TaskAssigneeRepository taskAssigneeRepository;
    private final TaskLabelRepository taskLabelRepository;
    private final LabelRepository labelRepository;
    private final BoardAuthorization boardAuth;

    /* Check the user is a member or not */
    private boolean isMember(Long userId, Long boardId) {
        return boardRepository.findById(boardId).map(board -> board.getOwner().getId().equals(userId)).orElse(false)
                || boardMembershipRepository.existsByBoard_IdAndUser_Id(boardId, userId);
    }

    /* Map task to summary */
    private TaskSummary toSummary(Task task) {
        TaskSummary summary = new TaskSummary();
        summary.setId(task.getId());
        summary.setTitle(task.getTitle());
        summary.setDescription(task.getDescription());
        summary.setStatus(task.getStatus());
        summary.setPosition(task.getPosition());
        summary.setColumnId(task.getColumn().getId());
        summary.setDueDate(task.getDueDate());
        summary.setLabels(task.getLabels().stream().map(taskLabel -> {
            LabelBrief labelBrief = new LabelBrief();
            labelBrief.setId(taskLabel.getLabel().getId());
            labelBrief.setName(taskLabel.getLabel().getName());
            labelBrief.setColor(taskLabel.getLabel().getColor());
            return labelBrief;
        }).collect(Collectors.toList()));
        return summary;
    }

    /* Map task to detail */
    private TaskDetail toDetail(Task task) {
        TaskDetail detail = new TaskDetail();
        detail.setId(task.getId());
        detail.setTitle(task.getTitle());
        detail.setDescription(task.getDescription());
        detail.setStatus(task.getStatus());
        detail.setPosition(task.getPosition());
        detail.setColumnId(task.getColumn().getId());
        detail.setDueDate(task.getDueDate());
        detail.setAssignees(task.getAssignees().stream().map(taskAssignee -> {
            AssigneeBrief assigneeBrief = new AssigneeBrief();
            assigneeBrief.setId(taskAssignee.getUser().getId());
            assigneeBrief.setFullName(taskAssignee.getUser().getFullName());
            return assigneeBrief;
        }).collect(Collectors.toList()));
        detail.setLabels(task.getLabels().stream().map(taskLabel -> {
            LabelBrief labelBrief = new LabelBrief();
            labelBrief.setId(taskLabel.getLabel().getId());
            labelBrief.setName(taskLabel.getLabel().getName());
            labelBrief.setColor(taskLabel.getLabel().getColor());
            return labelBrief;
        }).collect(Collectors.toList()));
        detail.setCommentCount(task.getComments().size());
        detail.setAttachmentCount(task.getAttachments().size());

        return detail;
    }

    /* List */
    public List<TaskSummary> listByColumn(String email, Long columnId) {
        ColumnEntity column = columnEntityRepository.findById(columnId).orElseThrow();
        boardAuth.ensureMember(email, column.getBoard().getId());
        return taskRepository.findByColumn_IdOrderByPositionAsc(columnId).stream()
                .map(this::toSummary).collect(Collectors.toList());
    }

    /* Create the task in column */
    public TaskDetail createInColumn(String email, Long columnId,
            CreateTaskRequest request) {
        ColumnEntity column = columnEntityRepository.findById(columnId).orElseThrow();
        boardAuth.ensureOwnerOrAdmin(email, column.getBoard().getId());
        Task task = new Task();
        task.setColumn(column);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        Integer max = taskRepository.findMaxPositionByColumnId(columnId);
        task.setPosition(request.getPosition() == null ? (max == null ? 1 : max + 1) : request.getPosition());

        task.setDueDate(request.getDueDate());
        taskRepository.save(task);
        log.info("Created task '{}' in column {} by user '{}'", task.getTitle(), columnId, email);
        // Assignee
        if (request.getAssigneeIds() != null) {
            for (Long uid : request.getAssigneeIds()) {
                User user = userRepository.findById(uid).orElseThrow();
                TaskAssignee taskAssignee = new TaskAssignee();
                taskAssignee.setId(new TaskAssigneeId(task.getId(), user.getId()));
                taskAssignee.setUser(user);
                taskAssigneeRepository.save(taskAssignee);
            }
        }

        return toDetail(task);
    }

    /* Get task */
    @Transactional
    public TaskDetail get(String email, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        boardAuth.ensureMember(email, task.getColumn().getBoard().getId());
        return toDetail(task);
    }

    /* Update Task */
    public TaskDetail update(String email, Long taskId,
            UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        boardAuth.ensureOwnerOrAdmin(email, task.getColumn().getBoard().getId());
        if (request.getTitle() != null)
            task.setTitle(request.getTitle());
        if (request.getDescription() != null)
            task.setDescription(request.getDescription());
        if (request.getPosition() != null)
            task.setPosition(request.getPosition());
        if (request.getDueDate() != null)
            task.setDueDate(request.getDueDate());
        if (request.getStatus() != null)
            task.setStatus(request.getStatus());
        if (request.getAssigneeIds() != null) {
            List<TaskAssignee> toRemove = new ArrayList<>(task.getAssignees());
            taskAssigneeRepository.deleteAll(toRemove);
            for (Long uid : request.getAssigneeIds()) {
                User user = userRepository.findById(uid).orElseThrow();
                TaskAssignee taskAssignee = new TaskAssignee();
                taskAssignee.setId(new TaskAssigneeId(task.getId(), user.getId()));
                taskAssignee.setUser(user);
                taskAssignee.setTask(task);
                taskAssigneeRepository.save(taskAssignee);
            }
        }
        return toDetail(task);
    }

    /* Remove Task */
    public void delete(String email, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        boardAuth.ensureOwnerOrAdmin(email, task.getColumn().getBoard().getId());
        taskRepository.delete(task);
    }

    /* Move Task */
    public TaskDetail move(String email, Long taskId, MoveTaskRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        Long fromBoardId = task.getColumn().getBoard().getId();
        ColumnEntity target = columnEntityRepository.findById(request.getToColumnId()).orElseThrow();

        boardAuth.ensureOwnerOrAdmin(email, fromBoardId);
        boardAuth.ensureOwnerOrAdmin(email, target.getBoard().getId());
        task.setColumn(target);
        Integer max = taskRepository.findMaxPositionByColumnId(target.getId());
        task.setPosition(request.getPosition() == null ? (max == null ? 1 : max) : request.getPosition());
        return toDetail(task);
    }

    /* Search Task */
    public List<TaskSummary> search(String email, String q) {
        List<Task> results = taskRepository.search(q == null || q.isBlank() ? null : q);
        // Keep only task s on boards where the user is member or owner
        Long memberId = userRepository.findByEmail(email).orElseThrow().getId();
        return results.stream().filter(t -> isMember(memberId,
                t.getColumn().getBoard().getId())).map(this::toSummary).collect(Collectors.toList());
    }

    /* Add Label */
    public void addLabel(String email, Long taskId, Long labelId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        boardAuth.ensureMember(email, task.getColumn().getBoard().getId());

        // Check if label belongs to the same board
        Label label = labelRepository.findById(labelId).orElseThrow(() -> new RuntimeException("Label not found"));
        if (!label.getBoard().getId().equals(task.getColumn().getBoard().getId())) {
            throw new RuntimeException("Label belongs to a different board");
        }

        TaskLabel taskLabel = new TaskLabel();
        taskLabel.setId(new TaskLabelId(taskId, labelId));
        taskLabel.setTask(task);
        taskLabel.setLabel(label);
        taskLabelRepository.save(taskLabel);
    }

    /* Remove Label */
    public void removeLabel(String email, Long taskId, Long labelId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        boardAuth.ensureMember(email, task.getColumn().getBoard().getId());

        TaskLabelId id = new TaskLabelId(taskId, labelId);
        taskLabelRepository.deleteById(id);
    }
}