package com.example.tasker.feature.column;

import com.example.tasker.domain.Board;
import com.example.tasker.domain.ColumnEntity;
import com.example.tasker.feature.board.BoardAuthorization;
import com.example.tasker.feature.board.BoardMembershipRepository;
import com.example.tasker.feature.board.BoardRepository;
import com.example.tasker.feature.column.dto.CreateColumnRequest;
import com.example.tasker.feature.column.dto.ReorderColumnsRequest;
import com.example.tasker.feature.column.dto.UpdateColumnRequest;
import com.example.tasker.feature.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ColumnService {
    private final BoardRepository boardRepository;
    private final ColumnEntityRepository columnEntityRepository;
    private final BoardMembershipRepository boardMembershipRepository;
    private final UserRepository userRepository;
    private final BoardAuthorization boardAuth;
    public List<ColumnEntity> list(String email,Long boardId){
        boardAuth.ensureMember(email,boardId);
        return columnEntityRepository.findByBoard_IdOrderByPositionAsc(boardId);
    }


    public ColumnEntity create(String email, Long boardId, CreateColumnRequest request){
        boardAuth.ensureOwnerOrAdmin(email, boardId);
        Board board = boardRepository.findById(boardId).orElseThrow();
        ColumnEntity column = new ColumnEntity();
        column.setBoard(board);
        column.setName(request.getName());
        Integer max = columnEntityRepository.findMaxPositionByBoardId(boardId);
        column.setPosition(request.getPosition()==null ?(max==null?1:max+1):request.getPosition());
        return columnEntityRepository.save(column);
    }

    public ColumnEntity update(String email, Long columnId, UpdateColumnRequest request){
        ColumnEntity column = columnEntityRepository.findById(columnId).orElseThrow();
        boardAuth.ensureOwnerOrAdmin(email,column.getBoard().getId());
        if(request.getName()!=null) column.setName(request.getName());
        if(request.getPosition()!=null) column.setPosition(request.getPosition());
        return column;
    }

    public void delete(String email,Long columnId){
        ColumnEntity column = columnEntityRepository.findById(columnId).orElseThrow();
        boardAuth.ensureOwnerOrAdmin(email,column.getBoard().getId());
        columnEntityRepository.delete(column);
    }

    public void  reorder(String email, Long boardId, ReorderColumnsRequest request){
        boardAuth.ensureOwnerOrAdmin(email,boardId);
        request.getItems().forEach(
                item -> {
                    ColumnEntity column = columnEntityRepository.findById(item.getId()).orElseThrow();
                    if(!column.getBoard().getId().equals(boardId)) throw new IllegalArgumentException("Column not in board");
                    column.setPosition(item.getPosition());
                }
        );
    }

}
