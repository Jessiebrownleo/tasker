package com.example.tasker.feature.task.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskDetail extends  TaskSummary{
    private List<AssigneeBrief> assignees;
    private List<LabelBrief> labels;
    private int commentCount;
    private int attachmentCount;
}
