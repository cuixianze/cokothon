package com.example.cokothon.controller;

import com.example.cokothon.dto.ApiResponse;
import com.example.cokothon.dto.BoardCreateRequest;
import com.example.cokothon.dto.BoardResponse;
import com.example.cokothon.entity.Board;
import com.example.cokothon.entity.User;
import com.example.cokothon.service.AuthService;
import com.example.cokothon.service.BoardService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    
    private final BoardService boardService;
    private final AuthService authService;
    
    // 전체 게시글 목록 조회 (페이징)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> getAllBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards;
        
        if (search != null && !search.trim().isEmpty()) {
            boards = boardService.searchBoards(search, pageable);
        } else {
            boards = boardService.findAllBoards(pageable);
        }
        
        Page<BoardResponse> response = boards.map(BoardResponse::from);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    // 카테고리별 게시글 목록 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> getBoardsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Board> boards = boardService.findBoardsByCategory(categoryId, pageable);
            Page<BoardResponse> response = boards.map(BoardResponse::from);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoardById(@PathVariable Long id) {
        try {
            Board board = boardService.findByIdWithViewCount(id);
            BoardResponse response = BoardResponse.from(board);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 게시글 생성
    @PostMapping
    public ResponseEntity<ApiResponse<BoardResponse>> createBoard(
            @Valid @RequestBody BoardCreateRequest request,
            HttpSession session) {
        
        try {
            User currentUser = authService.getCurrentUser(session);
            Board board;
            
            if (currentUser != null) {
                // 로그인된 사용자라면 User 정보를 사용
                board = boardService.createBoard(request, currentUser);
            } else {
                // 로그인되지 않았다면 기존 방식 사용 (작성자명 직접 입력)
                if (request.getAuthor() == null || request.getAuthor().trim().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.error("로그인하지 않은 경우 작성자명을 입력해야 합니다."));
                }
                board = boardService.createBoard(request);
            }
            
            BoardResponse response = BoardResponse.from(board);
            return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 작성되었습니다.", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardResponse>> updateBoard(
            @PathVariable Long id,
            @Valid @RequestBody BoardCreateRequest request) {
        
        try {
            Board board = boardService.updateBoard(id, request);
            BoardResponse response = BoardResponse.from(board);
            return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 수정되었습니다.", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@PathVariable Long id) {
        try {
            boardService.deleteBoard(id);
            return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 삭제되었습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> searchBoards(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("검색 키워드가 필요합니다."));
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardService.searchBoards(keyword, pageable);
        Page<BoardResponse> response = boards.map(BoardResponse::from);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}