package com.example.tasker.feature.board;

import com.example.tasker.domain.BoardMembership;
import com.example.tasker.domain.BoardRole;
import com.example.tasker.domain.User;
import com.example.tasker.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BoardAuthorization {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardMembershipRepository boardMembershipRepository;

    //If the user is not owner or member of the board
    public void ensureMember(String email, Long boardId){
        User user = userRepository.findByEmail(email).orElseThrow();
        boolean allowed =
                boardRepository.findById(boardId).map(b->b.getOwner().getId().equals(user.getId())).orElse(false)
                        || boardMembershipRepository.existsByBoard_IdAndUser_Id(boardId,user.getId());

        if(!allowed){
            throw  new IllegalArgumentException("Not  a board member");
        }
    }

    //If user is neither Owner or Admin for the board
    public void ensureOwnerOrAdmin(String email,Long boardId){
        User user= userRepository.findByEmail(email).orElseThrow();

        //Owner is always allowed
        boolean isOwner =
                boardRepository.findById(boardId)
                        .map(b->b.getOwner().getId().equals(user.getId())).orElse(false);
        if (isOwner) return;

        BoardMembership member =
                boardMembershipRepository.findByBoard_IdAndUser_Id(boardId,
                        user.getId()).orElse(null);
        if (member==null || (member.getRole()!= BoardRole.ADMIN && member.getRole()!= BoardRole.OWNER)){
            throw  new IllegalArgumentException("Requires Owner or Admin role");
        }
    }

    /** Throws if user is not the owner of the board. */
    public void ensureOwner(String email, Long boardId) {
        User me = userRepository.findByEmail(email).orElseThrow();

        boolean isOwner = boardRepository.findById(boardId)
                .map(b -> b.getOwner().getId().equals(me.getId()))
                .orElse(false);

        if (!isOwner) {
            throw new IllegalArgumentException("Requires OWNER role");
        }
    }
}
