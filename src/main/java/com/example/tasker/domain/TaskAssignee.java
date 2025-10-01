package com.example.tasker.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "task_assignees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "task", "user" })
public class TaskAssignee {

    @EmbeddedId
    private TaskAssigneeId id = new TaskAssigneeId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskId")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;
}

