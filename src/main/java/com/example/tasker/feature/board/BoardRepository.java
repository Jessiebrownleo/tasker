package com.example.tasker.feature.board;

import com.example.tasker.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findByOwner_Id(Long ownerId);

    @Query("select distinct b from Board b where b.owner.id = :userId or exists (select 1 from BoardMembership m where m.board = b and m.user.id = :userId)")
    List<Board> findAllForUser(@Param("userId") Long userId);
}
