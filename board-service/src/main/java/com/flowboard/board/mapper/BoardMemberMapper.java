package com.flowboard.board.mapper;

import com.flowboard.board.dto.BoardMemberDto;
import com.flowboard.board.entity.BoardMember;

public class BoardMemberMapper {

    public static BoardMemberDto mapToDto(BoardMember entity) {
        if (entity == null) {
            return null;
        }
        BoardMemberDto dto = new BoardMemberDto();
        dto.setBoardMemberId(entity.getBoardMemberId());
        dto.setBoardId(entity.getBoardId());
        dto.setUserId(entity.getUserId());
        dto.setRole(entity.getRole());
        dto.setAddedAt(entity.getAddedAt());
        return dto;
    }

    public static BoardMember mapToEntity(BoardMemberDto dto) {
        if (dto == null) {
            return null;
        }
        BoardMember entity = new BoardMember();
        entity.setBoardMemberId(dto.getBoardMemberId());
        entity.setBoardId(dto.getBoardId());
        entity.setUserId(dto.getUserId());
        entity.setRole(dto.getRole());
        entity.setAddedAt(dto.getAddedAt());
        return entity;
    }
}
