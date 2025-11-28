package com.example.tasker.feature.task;

import com.example.tasker.domain.TaskLabel;
import com.example.tasker.domain.TaskLabelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskLabelRepository extends JpaRepository<TaskLabel, TaskLabelId> {
}
