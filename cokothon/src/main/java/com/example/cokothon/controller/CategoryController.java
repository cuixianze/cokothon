package com.example.cokothon.controller;

import com.example.cokothon.dto.ApiResponse;
import com.example.cokothon.dto.CategoryResponse;
import com.example.cokothon.entity.Category;
import com.example.cokothon.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    // 전체 카테고리 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<Category> categories = categoryService.findAllCategories();
        List<CategoryResponse> response = categories.stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    // 카테고리 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(category -> {
                    CategoryResponse response = CategoryResponse.from(category);
                    return ResponseEntity.ok(ApiResponse.success(response));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    // 카테고리 생성
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @RequestParam String name,
            @RequestParam(required = false) String description) {
        
        try {
            Category category = categoryService.createCategory(name, description);
            CategoryResponse response = CategoryResponse.from(category);
            return ResponseEntity.ok(ApiResponse.success("카테고리가 성공적으로 생성되었습니다.", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("카테고리 생성 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
}
