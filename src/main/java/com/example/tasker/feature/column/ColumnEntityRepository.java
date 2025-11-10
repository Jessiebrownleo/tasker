package com.example.tasker.feature.column;

import com.example.tasker.domain.ColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnEntityRepository extends JpaRepository<ColumnEntity,Long> {
    List<ColumnEntity> findByBoard_IdOrderByPositionAsc(Long boardId);
    @Query("select coalesce(max(c.position), 0) from ColumnEntity c where c.board.id = :boardId")
    Integer findMaxPositionByBoardId(Long boardId);
}
