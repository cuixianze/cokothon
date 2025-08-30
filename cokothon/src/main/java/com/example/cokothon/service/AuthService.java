package com.example.cokothon.service;

import com.example.cokothon.dto.LoginRequest;
import com.example.cokothon.dto.RegisterRequest;
import com.example.cokothon.entity.User;
import com.example.cokothon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    
    private final UserRepository userRepository;
    private static final String SESSION_USER_KEY = "loggedInUser";
    
    @Transactional
    public User register(RegisterRequest request) {
        // 중복 사용자명 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
        }
        
        User user = new User(
                request.getUsername(),
                request.getPassword(),
                request.getName(),
                request.getEmail(),
                false // 일반 사용자로 등록
        );
        
        return userRepository.save(user);
    }
    
    public User login(LoginRequest request, HttpSession session) {
        Optional<User> userOpt = userRepository.findByUsernameAndPassword(
                request.getUsername(), 
                request.getPassword()
        );
        
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("사용자명 또는 비밀번호가 잘못되었습니다.");
        }
        
        User user = userOpt.get();
        session.setAttribute(SESSION_USER_KEY, user);
        
        return user;
    }
    
    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_USER_KEY);
        session.invalidate();
    }
    
    public User getCurrentUser(HttpSession session) {
        Object userObj = session.getAttribute(SESSION_USER_KEY);
        if (userObj instanceof User) {
            return (User) userObj;
        }
        return null;
    }
    
    public boolean isLoggedIn(HttpSession session) {
        return getCurrentUser(session) != null;
    }
    
    public boolean isAdmin(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null && user.getIsAdmin();
    }
}
