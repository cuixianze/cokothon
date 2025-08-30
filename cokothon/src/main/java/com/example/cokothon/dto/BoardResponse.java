package com.example.cokothon.dto;

import com.example.cokothon.entity.Board;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Long userId;
    private String categoryName;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer viewCount;
    private Boolean isAdminPost;
    
    public static BoardResponse from(Board board) {
        BoardResponse response = new BoardResponse();
        response.setId(board.getId());
        response.setTitle(board.getTitle());
        response.setContent(board.getContent());
        response.setAuthor(board.getAuthor());
        response.setUserId(board.getUser() != null ? board.getUser().getId() : null);
        response.setCategoryName(board.getCategory().getName());
        response.setCategoryId(board.getCategory().getId());
        response.setCreatedAt(board.getCreatedAt());
        response.setUpdatedAt(board.getUpdatedAt());
        response.setViewCount(board.getViewCount());
        response.setIsAdminPost(board.getIsAdminPost());
        return response;
    }
}
