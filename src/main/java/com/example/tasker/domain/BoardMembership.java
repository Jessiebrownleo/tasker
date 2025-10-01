package com.example.tasker.domain;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "board_memberships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "board", "user" })
public class BoardMembership {

    @EmbeddedId
    private BoardMembershipId id = new BoardMembershipId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boardId")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private BoardRole role = BoardRole.MEMBER;
}
