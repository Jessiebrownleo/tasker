package com.example.tasker.feature.task;

import com.example.tasker.domain.TaskAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssigneeRepository  extends JpaRepository<TaskAssignee,
        Long> {

}
