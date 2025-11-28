package com.example.tasker.feature.board;

import com.example.tasker.domain.*;
import com.example.tasker.feature.board.dto.*;
import com.example.tasker.feature.column.ColumnEntityRepository;
import com.example.tasker.feature.user.UserRepository;
import com.example.tasker.mapper.BoardMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMembershipRepository boardMembershipRepository;
    private final ColumnEntityRepository columnEntityRepository;
    private final UserRepository userRepository;
    private final BoardMapper boardMapper;
    private final BoardAuthorization boardAuth;

    // List board for user
    public List<BoardSummary> listForUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        return boardRepository.findAllForUser(user.getId())
                .stream().map(boardMapper::toSummary).collect(Collectors.toList());
    }

    // Create board
    public BoardDetail create(String email, CreateBoardRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Board board = new Board();
        board.setName(request.getName());
        board.setOwner(user);
        board.setVisibility(request.getVisibility());
        boardRepository.save(board);
        log.info("Created board '{}' for user '{}'", board.getName(), email);
        BoardMembership member = new BoardMembership();
        member.setBoard(board);
        member.setUser(user);
        member.setRole(BoardRole.OWNER);
        member.setId(new BoardMembershipId(board.getId(), member.getBoard().getId()));
        return boardMapper.toDetail(board);
    }

    // Get board
    public BoardDetail get(String email, Long boardId) {
        boardAuth.ensureMember(email, boardId);
        Board board = boardRepository.findById(boardId).orElseThrow();
        return boardMapper.toDetail(board);
    }

    // Update board
    public BoardDetail update(String email, Long boardId,
            UpdateBoardRequest request) {
        boardAuth.ensureOwnerOrAdmin(email, boardId);
        Board board = boardRepository.findById(boardId).orElseThrow();
        if (request.getName() != null) {
            board.setName(request.getName());
        }
        if (request.getVisibility() != null) {
            board.setVisibility(request.getVisibility());
        }
        return boardMapper.toDetail(board);
    }

    // Delete board
    public void delete(String email, Long boardId) {
        boardAuth.ensureOwner(email, boardId);
        boardRepository.deleteById(boardId);
        log.info("Deleted board {} by user '{}'", boardId, email);
    }

    // Member helper
    public List<MemberSummary> listMembers(String email, Long boardId) {
        boardAuth.ensureMember(email, boardId);
        return boardMembershipRepository.findByBoard_Id(boardId).stream()
                .map(m -> {
                    MemberSummary ms = new MemberSummary();
                    ms.setUserId(m.getUser().getId());
                    ms.setFullName(m.getUser().getFullName());
                    ms.setEmail(m.getUser().getEmail());
                    ms.setRole(m.getRole());
                    return ms;
                }).collect(Collectors.toList());
    }

    // Add member
    public void addMember(String email, Long boardId, Long userId,
            BoardRole role) {
        boardAuth.ensureOwnerOrAdmin(email, boardId);
        Board b = boardRepository.findById(boardId).orElseThrow();
        User user = userRepository.findByEmail(email).orElseThrow();
        BoardMembershipId id = new BoardMembershipId(b.getId(), user.getId());
        if (boardMembershipRepository.existsByBoard_IdAndUser_Id(boardId, userId)) {
            return;
        }

        BoardMembership member = new BoardMembership();
        member.setId(id);
        member.setBoard(b);
        member.setUser(user);
        member.setRole(role == null ? BoardRole.MEMBER : role);
        boardMembershipRepository.save(member);
    }

    // Update member role
    public void updateMemberRole(String email, Long boardId, Long userId,
            BoardRole role) {
        boardAuth.ensureOwnerOrAdmin(email, boardId);
        BoardMembership member = boardMembershipRepository.findByBoard_IdAndUser_Id(boardId,
                userId).orElseThrow();
        member.setRole(role);
    }

    // Remove member
    public void removeMember(String email, Long boardId, Long userId) {
        boardAuth.ensureOwnerOrAdmin(email, boardId);
        boardMembershipRepository.findByBoard_IdAndUser_Id(boardId, userId)
                .ifPresent(boardMembershipRepository::delete);
    }

}
