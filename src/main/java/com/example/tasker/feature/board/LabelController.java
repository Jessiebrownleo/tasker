package com.example.tasker.feature.board;

import com.example.tasker.domain.Label;
import com.example.tasker.feature.board.dto.CreateLabelRequest;
import com.example.tasker.feature.board.dto.UpdateLabelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;

    @GetMapping("/boards/{boardId}/labels")
    public ResponseEntity<List<Label>> listByBoard(@AuthenticationPrincipal UserDetails me,
            @PathVariable Long boardId) {
        return ResponseEntity.ok(labelService.listByBoard(me.getUsername(), boardId));
    }

    @PostMapping("/boards/{boardId}/labels")
    public ResponseEntity<Label> create(@AuthenticationPrincipal UserDetails me, @PathVariable Long boardId,
            @RequestBody CreateLabelRequest request) {
        return ResponseEntity.ok(labelService.create(me.getUsername(), boardId, request));
    }

    @PatchMapping("/labels/{labelId}")
    public ResponseEntity<Label> update(@AuthenticationPrincipal UserDetails me, @PathVariable Long labelId,
            @RequestBody UpdateLabelRequest request) {
        return ResponseEntity.ok(labelService.update(me.getUsername(), labelId, request));
    }

    @DeleteMapping("/labels/{labelId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails me, @PathVariable Long labelId) {
        labelService.delete(me.getUsername(), labelId);
        return ResponseEntity.noContent().build();
    }
}
