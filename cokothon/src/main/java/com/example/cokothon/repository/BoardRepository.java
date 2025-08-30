package com.example.cokothon.repository;

import com.example.cokothon.entity.Board;
import com.example.cokothon.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    // 카테고리별 게시글 조회 (페이징)
    Page<Board> findByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);
    
    // 카테고리별 게시글 조회 (리스트)
    List<Board> findByCategoryOrderByCreatedAtDesc(Category category);
    
    // 전체 게시글 조회 (페이징)
    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // 제목으로 검색
    @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% ORDER BY b.createdAt DESC")
    Page<Board> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);
    
    // 내용으로 검색
    @Query("SELECT b FROM Board b WHERE b.content LIKE %:keyword% ORDER BY b.createdAt DESC")
    Page<Board> findByContentContaining(@Param("keyword") String keyword, Pageable pageable);
    
    // 제목 또는 내용으로 검색
    @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword% ORDER BY b.createdAt DESC")
    Page<Board> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
}
