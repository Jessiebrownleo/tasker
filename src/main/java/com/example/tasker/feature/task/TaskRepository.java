package com.example.tasker.feature.task;

import com.example.tasker.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByColumn_IdOrderByPositionAsc(Long columnId);


    @Query("select coalesce(max(t.position), 0) from Task t where t.column.id = :columnId")
    Integer findMaxPositionByColumnId(Long columnId);


    // Simple global search by title/description
    @Query("select t from Task t where (:q is null or lower(t.title) like lower(concat('%', :q, '%')) or lower(t.description) like lower(concat('%', :q, '%')))")
    List<Task> search(String q);
}
