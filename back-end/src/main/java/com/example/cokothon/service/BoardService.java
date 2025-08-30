package com.example.cokothon.service;

import com.example.cokothon.dto.BoardCreateRequest;
import com.example.cokothon.entity.Board;
import com.example.cokothon.entity.Category;
import com.example.cokothon.entity.User;
import com.example.cokothon.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    
    private final BoardRepository boardRepository;
    private final CategoryService categoryService;
    
    // 전체 게시글 조회 (페이징)
    public Page<Board> findAllBoards(Pageable pageable) {
        return boardRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    // 카테고리별 게시글 조회 (페이징)
    public Page<Board> findBoardsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryService.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        return boardRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);
    }
    
    // 카테고리별 게시글 조회 (리스트)
    public List<Board> findBoardsByCategory(Long categoryId) {
        Category category = categoryService.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        return boardRepository.findByCategoryOrderByCreatedAtDesc(category);
    }
    
    // 게시글 상세 조회
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }
    
    // 게시글 상세 조회 (조회수 증가)
    @Transactional
    public Board findByIdWithViewCount(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        board.incrementViewCount();
        return boardRepository.save(board);
    }
    
    // 게시글 생성 (기존 방식 - 작성자명 직접 입력)
    @Transactional
    public Board createBoard(BoardCreateRequest request) {
        Category category = categoryService.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        
        Board board = new Board(request.getTitle(), request.getContent(), 
                               request.getAuthor(), category);
        return boardRepository.save(board);
    }
    
    // 게시글 생성 (로그인된 사용자)
    @Transactional
    public Board createBoard(BoardCreateRequest request, User user) {
        Category category = categoryService.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        
        Board board = new Board(request.getTitle(), request.getContent(), user, category);
        return boardRepository.save(board);
    }
    
    // 게시글 수정
    @Transactional
    public Board updateBoard(Long id, BoardCreateRequest request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        
        Category category = categoryService.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setCategory(category);
        
        return boardRepository.save(board);
    }
    
    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        boardRepository.delete(board);
    }
    
    // 검색
    public Page<Board> searchBoards(String keyword, Pageable pageable) {
        return boardRepository.findByTitleOrContentContaining(keyword, pageable);
    }
}
