package com.example.cokothon.controller;

import com.example.cokothon.dto.*;
import com.example.cokothon.entity.User;
import com.example.cokothon.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        
        try {
            User user = authService.register(request);
            UserResponse response = UserResponse.from(user);
            return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다.", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session) {
        
        try {
            User user = authService.login(request, session);
            UserResponse response = UserResponse.from(user);
            return ResponseEntity.ok(ApiResponse.success("로그인이 완료되었습니다.", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok(ApiResponse.success("로그아웃이 완료되었습니다.", null));
    }
    
    // 현재 로그인한 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(HttpSession session) {
        User user = authService.getCurrentUser(session);
        
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }
        
        UserResponse response = UserResponse.from(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    // 로그인 상태 확인
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Boolean>> getLoginStatus(HttpSession session) {
        boolean isLoggedIn = authService.isLoggedIn(session);
        return ResponseEntity.ok(ApiResponse.success(isLoggedIn));
    }
}
