package com.example.tasker.feature.task;

import com.example.tasker.domain.Task;
import com.example.tasker.feature.task.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
   private final TaskService taskService;

   //List by column
    @GetMapping("/{columnId}")
    public ResponseEntity<List<TaskSummary>> listByColumn(@AuthenticationPrincipal UserDetails me, @PathVariable Long columnId){
        return ResponseEntity.ok(taskService.listByColumn(me.getUsername(),columnId));
    }


    //Create task in column
    @PostMapping("/{columnId}")
    public ResponseEntity<TaskDetail> createInColumn(@AuthenticationPrincipal UserDetails me, @PathVariable Long columnId, @RequestBody CreateTaskRequest request){
       return ResponseEntity.ok(taskService.createInColumn(me.getUsername(),columnId,request));
    }

    //Get task
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetail> getById(@AuthenticationPrincipal UserDetails me,@PathVariable Long taskId){
        return ResponseEntity.ok(taskService.get(me.getUsername(),taskId));
    }

    //Update task
    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskDetail> update (@AuthenticationPrincipal UserDetails me, @PathVariable Long taskId, @RequestBody UpdateTaskRequest request){
        return ResponseEntity.ok(taskService.update(me.getUsername(),taskId,request));
    }


    //Remove task
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails me ,@PathVariable Long taskId){
        taskService.delete(me.getUsername(),taskId);
        return ResponseEntity.noContent().build();
    }

    //Move task
    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskDetail> move(@AuthenticationPrincipal UserDetails me, @PathVariable Long taskId, @RequestBody MoveTaskRequest request){
        return ResponseEntity.ok(taskService.move(me.getUsername(),taskId,
                request));
    }

    //Search
}

