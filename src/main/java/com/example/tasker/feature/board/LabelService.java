package com.example.tasker.feature.board;

import com.example.tasker.domain.Board;
import com.example.tasker.domain.Label;
import com.example.tasker.feature.board.dto.CreateLabelRequest;
import com.example.tasker.feature.board.dto.UpdateLabelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;
    private final BoardRepository boardRepository;
    private final BoardAuthorization boardAuth;

    public List<Label> listByBoard(String email, Long boardId) {
        boardAuth.ensureMember(email, boardId);
        return labelRepository.findByBoard_Id(boardId);
    }

    @Transactional
    public Label create(String email, Long boardId, CreateLabelRequest request) {
        boardAuth.ensureOwnerOrAdmin(email, boardId);
        Board board = boardRepository.findById(boardId).orElseThrow();

        Label label = new Label();
        label.setBoard(board);
        label.setName(request.getName());
        label.setColor(request.getColor());

        return labelRepository.save(label);
    }

    @Transactional
    public Label update(String email, Long labelId, UpdateLabelRequest request) {
        Label label = labelRepository.findById(labelId).orElseThrow();
        boardAuth.ensureOwnerOrAdmin(email, label.getBoard().getId());

        if (request.getName() != null) {
            label.setName(request.getName());
        }
        if (request.getColor() != null) {
            label.setColor(request.getColor());
        }

        return labelRepository.save(label);
    }

    @Transactional
    public void delete(String email, Long labelId) {
        Label label = labelRepository.findById(labelId).orElseThrow();
        boardAuth.ensureOwnerOrAdmin(email, label.getBoard().getId());
        labelRepository.delete(label);
    }
}
