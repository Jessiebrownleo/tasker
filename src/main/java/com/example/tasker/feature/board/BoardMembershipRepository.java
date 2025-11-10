package com.example.tasker.feature.board;

import com.example.tasker.domain.BoardMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardMembershipRepository extends JpaRepository<BoardMembership,Long> {

    boolean existsByBoard_IdAndUser_Id(Long boardId, Long userId);

    Optional<BoardMembership> findByBoard_IdAndUser_Id(Long boardId, Long userId);

    List<BoardMembership> findByBoard_Id(Long boardId);
}
