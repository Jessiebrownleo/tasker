package com.example.tasker.mapper;

import com.example.tasker.domain.Board;
import com.example.tasker.domain.BoardMembership;
import com.example.tasker.domain.ColumnEntity;
import com.example.tasker.feature.board.dto.BoardDetail;
import com.example.tasker.feature.board.dto.BoardSummary;
import com.example.tasker.feature.board.dto.MemberSummary;
import com.example.tasker.feature.column.dto.ColumnSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class BoardMapper {

    // --- Top-level mappers ---

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "columnCount", expression = "java(countSafe(board.getColumns()))")
    @Mapping(target = "memberCount", expression = "java(countSafe(board.getMemberships()))")
    public abstract BoardSummary toSummary(Board board);

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "columns", expression = "java(mapAndSortColumns(board.getColumns()))")
    @Mapping(target = "members", expression = "java(mapMembers(board.getMemberships()))")
    public abstract BoardDetail toDetail(Board board);

    // --- Element mappers ---

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "position", source = "position")
    protected abstract ColumnSummary toColumnSummary(ColumnEntity column);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "role", source = "role")
    protected abstract MemberSummary toMemberSummary(BoardMembership membership);

    // --- Helpers usable in expressions ---

    /**
     * Null-safe size for any Collection type (List, Set, etc.).
     */
    protected int countSafe(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
        // If your fields are Hibernate proxies, this will not trigger extra loads unless needed.
    }

    /**
     * Maps columns to summaries and sorts by position ascending.
     * Accepts any Collection (List/Set) to match entity field types.
     */
    protected List<ColumnSummary> mapAndSortColumns(Collection<ColumnEntity> columns) {
        if (columns == null || columns.isEmpty()) return List.of();
        return columns.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(ColumnEntity::getPosition))
                .map(this::toColumnSummary)
                .collect(Collectors.toList());
    }

    /**
     * Maps memberships to member summaries.
     */
    protected List<MemberSummary> mapMembers(Collection<BoardMembership> memberships) {
        if (memberships == null || memberships.isEmpty()) return List.of();
        return memberships.stream()
                .filter(Objects::nonNull)
                .map(this::toMemberSummary)
                .collect(Collectors.toList());
    }
}