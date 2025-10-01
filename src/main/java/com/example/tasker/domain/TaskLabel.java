package com.example.tasker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "task_labels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "task", "label" })
public class TaskLabel {

    @EmbeddedId
    private TaskLabelId id = new TaskLabelId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskId")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("labelId")
    private Label label;
}
