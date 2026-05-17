package com.flowboard.board.mapper;

import com.flowboard.board.dto.BoardDto;
import com.flowboard.board.entity.Board;

public class BoardMapper {

    public static BoardDto mapToDto(Board entity) {
        if (entity == null) {
            return null;
        }
        BoardDto dto = new BoardDto();
        dto.setBoardId(entity.getBoardId());
        dto.setWorkspaceId(entity.getWorkspaceId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setBackground(entity.getBackground());
        dto.setVisibility(entity.getVisibility());
        dto.setCreatedById(entity.getCreatedById());
        dto.setIsClosed(entity.getIsClosed());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static Board mapToEntity(BoardDto dto) {
        if (dto == null) {
            return null;
        }
        Board entity = new Board();
        entity.setBoardId(dto.getBoardId());
        entity.setWorkspaceId(dto.getWorkspaceId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setBackground(dto.getBackground());
        entity.setVisibility(dto.getVisibility());
        entity.setCreatedById(dto.getCreatedById());
        entity.setIsClosed(dto.getIsClosed());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}
