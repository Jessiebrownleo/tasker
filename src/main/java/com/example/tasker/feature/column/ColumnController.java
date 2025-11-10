package com.example.tasker.feature.column;

import com.example.tasker.domain.ColumnEntity;
import com.example.tasker.feature.column.dto.UpdateColumnRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/columns")
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    /*Update*/
    @PatchMapping("/{columnId}")
    public ResponseEntity<ColumnEntity> update(@AuthenticationPrincipal UserDetails me, @PathVariable Long columnId, @RequestBody UpdateColumnRequest request){
        columnService.update(me.getUsername(),columnId,request);
        return ResponseEntity.noContent().build();
    }


    /*Delete*/
    @DeleteMapping("/{columnId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails me,@PathVariable Long columnId){
        columnService.delete(me.getUsername(),columnId);
        return ResponseEntity.noContent().build();
    }


}
