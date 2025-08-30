package com.example.cokothon.controller;

import com.example.cokothon.dto.*;
import com.example.cokothon.entity.FamilySurvey;
import com.example.cokothon.entity.User;
import com.example.cokothon.service.AuthService;
import com.example.cokothon.service.FamilySurveyService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/family-survey")
@RequiredArgsConstructor
public class FamilySurveyController {
    
    private final FamilySurveyService familySurveyService;
    private final AuthService authService;
    
    // 현재 로그인한 사용자의 설문조사 조회
    @GetMapping("/my-survey")
    public ResponseEntity<ApiResponse<FamilySurveyResponse>> getMySurvey(HttpSession session) {
        User currentUser = authService.getCurrentUser(session);
        if (currentUser == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }
        
        Optional<FamilySurvey> survey = familySurveyService.findSurveyByUser(currentUser);
        if (survey.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("설문조사가 없습니다.", null));
        }
        
        FamilySurveyResponse response = FamilySurveyResponse.from(survey.get());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    // 설문조사 생성 또는 업데이트
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<FamilySurveyResponse>> submitSurvey(
            @Valid @RequestBody FamilySurveyRequest request,
            HttpSession session) {
        
        User currentUser = authService.getCurrentUser(session);
        if (currentUser == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }
        
        try {
            FamilySurvey survey = familySurveyService.createOrUpdateSurvey(currentUser, request);
            FamilySurveyResponse response = FamilySurveyResponse.from(survey);
            
            String message = survey.getSurveyCompleted() 
                    ? "설문조사가 성공적으로 완료되었습니다." 
                    : "설문조사가 저장되었습니다.";
            
            return ResponseEntity.ok(ApiResponse.success(message, response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("설문조사 저장 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
    
    // 설문조사 완료 여부 확인
    @GetMapping("/completion-status")
    public ResponseEntity<ApiResponse<Boolean>> checkCompletionStatus(HttpSession session) {
        User currentUser = authService.getCurrentUser(session);
        if (currentUser == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }
        
        boolean completed = familySurveyService.hasUserCompletedSurvey(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(completed));
    }
    
    // 관리자용 - 전체 설문조사 목록 조회 (완료된 것만)
    @GetMapping("/admin/completed")
    public ResponseEntity<ApiResponse<List<FamilySurveyResponse>>> getCompletedSurveys(
            HttpSession session) {
        
        if (!authService.isAdmin(session)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관리자 권한이 필요합니다."));
        }
        
        List<FamilySurvey> surveys = familySurveyService.getCompletedSurveys();
        List<FamilySurveyResponse> responses = surveys.stream()
                .map(FamilySurveyResponse::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
    
    // 관리자용 - 미완료 설문조사 목록 조회
    @GetMapping("/admin/incomplete")
    public ResponseEntity<ApiResponse<List<FamilySurveyResponse>>> getIncompleteSurveys(
            HttpSession session) {
        
        if (!authService.isAdmin(session)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관리자 권한이 필요합니다."));
        }
        
        List<FamilySurvey> surveys = familySurveyService.getIncompleteSurveys();
        List<FamilySurveyResponse> responses = surveys.stream()
                .map(FamilySurveyResponse::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
    
    // 관리자용 - 모임 참석 희망자 조회
    @GetMapping("/admin/meeting-participants")
    public ResponseEntity<ApiResponse<List<FamilySurveyResponse>>> getMeetingParticipants(
            HttpSession session) {
        
        if (!authService.isAdmin(session)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관리자 권한이 필요합니다."));
        }
        
        List<FamilySurvey> surveys = familySurveyService.getMeetingParticipants();
        List<FamilySurveyResponse> responses = surveys.stream()
                .map(FamilySurveyResponse::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
    
    // 관리자용 - 심리적 지원 필요도별 조회
    @GetMapping("/admin/psychological-support/{supportLevel}")
    public ResponseEntity<ApiResponse<List<FamilySurveyResponse>>> getUsersByPsychologicalSupportLevel(
            @PathVariable String supportLevel,
            HttpSession session) {
        
        if (!authService.isAdmin(session)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관리자 권한이 필요합니다."));
        }
        
        List<FamilySurvey> surveys = familySurveyService.getUsersByPsychologicalSupportLevel(supportLevel);
        List<FamilySurveyResponse> responses = surveys.stream()
                .map(FamilySurveyResponse::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
    
    // 관리자용 - 관계별 유가족 조회
    @GetMapping("/admin/by-relationship/{relationship}")
    public ResponseEntity<ApiResponse<List<FamilySurveyResponse>>> getUsersByRelationship(
            @PathVariable String relationship,
            HttpSession session) {
        
        if (!authService.isAdmin(session)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관리자 권한이 필요합니다."));
        }
        
        List<FamilySurvey> surveys = familySurveyService.getUsersByRelationship(relationship);
        List<FamilySurveyResponse> responses = surveys.stream()
                .map(FamilySurveyResponse::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
    
    // 관리자용 - 설문조사 통계
    @GetMapping("/admin/statistics")
    public ResponseEntity<ApiResponse<SurveyStatisticsResponse>> getStatistics(
            HttpSession session) {
        
        if (!authService.isAdmin(session)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관리자 권한이 필요합니다."));
        }
        
        SurveyStatisticsResponse statistics = familySurveyService.getStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }
    
    // 관리자용 - 특정 사용자의 설문조사 조회
    @GetMapping("/admin/user/{userId}")
    public ResponseEntity<ApiResponse<FamilySurveyResponse>> getUserSurvey(
            @PathVariable Long userId,
            HttpSession session) {
        
        if (!authService.isAdmin(session)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관리자 권한이 필요합니다."));
        }
        
        Optional<FamilySurvey> survey = familySurveyService.findSurveyByUserId(userId);
        if (survey.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("해당 사용자의 설문조사가 없습니다.", null));
        }
        
        FamilySurveyResponse response = FamilySurveyResponse.from(survey.get());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    // 관리자용 - 설문조사 삭제
    @DeleteMapping("/admin/{surveyId}")
    public ResponseEntity<ApiResponse<Void>> deleteSurvey(
            @PathVariable Long surveyId,
            HttpSession session) {
        
        if (!authService.isAdmin(session)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관리자 권한이 필요합니다."));
        }
        
        try {
            familySurveyService.deleteSurvey(surveyId);
            return ResponseEntity.ok(ApiResponse.success("설문조사가 성공적으로 삭제되었습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
