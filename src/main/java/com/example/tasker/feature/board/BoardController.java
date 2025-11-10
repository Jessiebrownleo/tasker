package com.example.tasker.feature.board;

import com.example.tasker.domain.ColumnEntity;
import com.example.tasker.feature.board.dto.*;
import com.example.tasker.feature.column.ColumnService;
import com.example.tasker.feature.column.dto.ColumnSummary;
import com.example.tasker.feature.column.dto.CreateColumnRequest;
import com.example.tasker.feature.column.dto.ReorderColumnsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final ColumnService columnService;

    @GetMapping
    public ResponseEntity<List<BoardSummary>> myBoards (@AuthenticationPrincipal UserDetails me){
        return ResponseEntity.ok(boardService.listForUser(me.getUsername()));
    }

    @PostMapping
    public ResponseEntity<BoardDetail> create (@AuthenticationPrincipal UserDetails me, @RequestBody CreateBoardRequest request){
        return ResponseEntity.ok(boardService.create(me.getUsername(),request));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetail> get(@AuthenticationPrincipal UserDetails me ,@PathVariable Long boardId){
        return  ResponseEntity.ok(boardService.get(me.getUsername(),boardId));
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardDetail> update(@AuthenticationPrincipal UserDetails me, @PathVariable Long boardId, @RequestBody UpdateBoardRequest request){
        return ResponseEntity.ok(boardService.update(me.getUsername(),boardId
                ,request));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails me,@PathVariable Long boardId){
        boardService.delete(me.getUsername(),boardId);
        return  ResponseEntity.noContent().build();
    }


    /*Member endpoint*/
    @GetMapping("/{boardId}/members")
    public ResponseEntity<List<MemberSummary>> members(@AuthenticationPrincipal UserDetails me , @PathVariable Long boardId){
        return  ResponseEntity.ok(boardService.listMembers(me.getUsername(),boardId));
    }

    @PostMapping("/{boardId}/members/{userId}")
    public ResponseEntity<Void> addMember(@AuthenticationPrincipal UserDetails me,@PathVariable Long boardId, @RequestBody AddMemberRequest request){
        boardService.addMember(me.getUsername(),boardId,request.getUserId(),request.getRole());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{boardId}/members")
    public ResponseEntity<Void> updateMember(@AuthenticationPrincipal UserDetails me,@PathVariable Long boardId,@PathVariable Long userId,@RequestBody UpdateMemberRequest request){
        boardService.updateMemberRole(me.getUsername(),boardId,userId,request.getRole());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{boardId}/members/{userId}")
    public ResponseEntity<Void> removeMember(@AuthenticationPrincipal UserDetails me,@PathVariable Long boardId, @PathVariable Long userId){
        boardService.removeMember(me.getUsername(),boardId,userId);
        return ResponseEntity.noContent().build();
    }


    /*Some endpoint that relate to columns*/
    @GetMapping("/{boardId}/columns")
    public ResponseEntity<List<ColumnEntity>> columns(@AuthenticationPrincipal UserDetails me, @PathVariable Long boardId){
        return ResponseEntity.ok(columnService.list(me.getUsername(),boardId));
    }

    @PostMapping("/{boardId}/columns")
    public ResponseEntity<ColumnEntity> create(@AuthenticationPrincipal UserDetails me, @PathVariable Long boardId, @RequestBody CreateColumnRequest request){
        return ResponseEntity.ok(columnService.create(me.getUsername(),boardId,request));
    }

   /*ReorderColumn*/
    @PostMapping("/{boardId}/columns/reorder")
    public ResponseEntity<Void> reorderColumn(@AuthenticationPrincipal UserDetails me, @PathVariable Long boardId, @RequestBody ReorderColumnsRequest request){
        columnService.reorder(me.getUsername(),boardId,request);
        return ResponseEntity.noContent().build();
    }
}
