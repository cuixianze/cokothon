package com.example.cokothon.service;

import com.example.cokothon.dto.FamilySurveyRequest;
import com.example.cokothon.dto.SurveyStatisticsResponse;
import com.example.cokothon.entity.FamilySurvey;
import com.example.cokothon.entity.User;
import com.example.cokothon.repository.FamilySurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FamilySurveyService {
    
    private final FamilySurveyRepository familySurveyRepository;
    
    // 사용자의 설문조사 조회
    public Optional<FamilySurvey> findSurveyByUser(User user) {
        return familySurveyRepository.findByUser(user);
    }
    
    public Optional<FamilySurvey> findSurveyByUserId(Long userId) {
        return familySurveyRepository.findByUserId(userId);
    }
    
    // 설문조사 생성 또는 업데이트
    @Transactional
    public FamilySurvey createOrUpdateSurvey(User user, FamilySurveyRequest request) {
        // 기존 설문조사가 있는지 확인
        Optional<FamilySurvey> existingSurvey = familySurveyRepository.findByUser(user);
        
        FamilySurvey survey;
        if (existingSurvey.isPresent()) {
            survey = existingSurvey.get();
        } else {
            survey = new FamilySurvey(user);
        }
        
        // 요청 데이터로 설문조사 정보 업데이트
        updateSurveyFromRequest(survey, request);
        
        return familySurveyRepository.save(survey);
    }
    
    // 설문조사 완료 처리
    @Transactional
    public FamilySurvey completeSurvey(Long surveyId) {
        FamilySurvey survey = familySurveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("설문조사를 찾을 수 없습니다."));
        
        survey.completeSurvey();
        return familySurveyRepository.save(survey);
    }
    
    // 사용자가 설문조사를 완료했는지 확인
    public boolean hasUserCompletedSurvey(Long userId) {
        return familySurveyRepository.hasCompletedSurvey(userId);
    }
    
    // 완료된 설문조사 목록 조회
    public List<FamilySurvey> getCompletedSurveys() {
        return familySurveyRepository.findBySurveyCompletedTrue();
    }
    
    // 미완료 설문조사 목록 조회
    public List<FamilySurvey> getIncompleteSurveys() {
        return familySurveyRepository.findBySurveyCompletedFalse();
    }
    
    // 모임 참석 희망자 조회
    public List<FamilySurvey> getMeetingParticipants() {
        return familySurveyRepository.findByMeetingParticipationDesireTrue();
    }
    
    // 상담 의향이 있는 사용자 조회
    public List<FamilySurvey> getCounselingInterestedUsers() {
        return familySurveyRepository.findUsersInterestedInCounseling();
    }
    
    // 혼자 살고 있는 유가족 조회
    public List<FamilySurvey> getUsersLivingAlone() {
        return familySurveyRepository.findByLivingAloneTrue();
    }
    
    // 관계별 유가족 조회
    public List<FamilySurvey> getUsersByRelationship(String relationship) {
        return familySurveyRepository.findByRelationshipToDeceased(relationship);
    }
    
    // 설문조사 통계 생성
    public SurveyStatisticsResponse getStatistics() {
        SurveyStatisticsResponse stats = new SurveyStatisticsResponse();
        
        // 기본 통계
        stats.setTotalSurveys(familySurveyRepository.count());
        stats.setCompletedSurveys(familySurveyRepository.countCompletedSurveys());
        stats.setIncompleteSurveys(stats.getTotalSurveys() - stats.getCompletedSurveys());
        
        // 관계별 통계
        List<Object[]> relationshipStats = familySurveyRepository.getRelationshipStatistics();
        Map<String, Long> relationshipMap = new HashMap<>();
        for (Object[] stat : relationshipStats) {
            relationshipMap.put((String) stat[0], (Long) stat[1]);
        }
        stats.setRelationshipStatistics(relationshipMap);
        
        // 애도 단계별 통계
        List<Object[]> griefStageStats = familySurveyRepository.getGriefStageStatistics();
        Map<String, Long> griefStageMap = new HashMap<>();
        for (Object[] stat : griefStageStats) {
            griefStageMap.put((String) stat[0], (Long) stat[1]);
        }
        stats.setGriefStageStatistics(griefStageMap);
        
        // 모임 참석 희망 통계
        long meetingDesired = familySurveyRepository.findByMeetingParticipationDesireTrue().size();
        stats.setMeetingParticipationDesired(meetingDesired);
        stats.setMeetingParticipationNotDesired(stats.getCompletedSurveys() - meetingDesired);
        
        // 상담 의향 통계
        long counselingInterested = familySurveyRepository.findUsersInterestedInCounseling().size();
        stats.setCounselingInterested(counselingInterested);
        stats.setCounselingNotInterested(stats.getCompletedSurveys() - counselingInterested);
        
        // 홀로 거주 통계
        long livingAlone = familySurveyRepository.findByLivingAloneTrue().size();
        stats.setLivingAloneCount(livingAlone);
        stats.setLivingWithFamilyCount(stats.getCompletedSurveys() - livingAlone);
        
        return stats;
    }
    
    // 설문조사 삭제
    @Transactional
    public void deleteSurvey(Long surveyId) {
        FamilySurvey survey = familySurveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("설문조사를 찾을 수 없습니다."));
        familySurveyRepository.delete(survey);
    }
    
    // 요청 데이터로 설문조사 엔티티 업데이트하는 private 메서드
    private void updateSurveyFromRequest(FamilySurvey survey, FamilySurveyRequest request) {
        survey.setBirthDate(request.getBirthDate());
        survey.setGender(request.getGender());
        survey.setPhoneNumber(request.getPhoneNumber());
        survey.setAddress(request.getAddress());
        
        survey.setRelationshipToDeceased(request.getRelationshipToDeceased());
        survey.setRelationshipDescription(request.getRelationshipDescription());
        
        survey.setDeceasedName(request.getDeceasedName());
        survey.setDeceasedAge(request.getDeceasedAge());
        survey.setDeathDate(request.getDeathDate());
        survey.setCauseOfDeath(request.getCauseOfDeath());
        
        survey.setCurrentFamilyMembers(request.getCurrentFamilyMembers());
        survey.setLivingAlone(request.getLivingAlone());
        survey.setFamilySupportLevel(request.getFamilySupportLevel());
        
        survey.setGriefStage(request.getGriefStage());
        survey.setCounselingExperience(request.getCounselingExperience());
        survey.setCounselingWillingness(request.getCounselingWillingness());
        
        survey.setMeetingParticipationDesire(request.getMeetingParticipationDesire());
        survey.setPreferredMeetingType(request.getPreferredMeetingType());
        survey.setPreferredMeetingTime(request.getPreferredMeetingTime());
        survey.setSupportNeeds(request.getSupportNeeds());
        
        survey.setAdditionalNotes(request.getAdditionalNotes());
        survey.setPrivacyAgreement(request.getPrivacyAgreement());
        
        // 필수 항목이 모두 채워져 있으면 완료 처리
        if (isRequiredFieldsComplete(request)) {
            survey.setSurveyCompleted(true);
        }
    }
    
    // 필수 항목 완료 여부 확인
    private boolean isRequiredFieldsComplete(FamilySurveyRequest request) {
        return request.getBirthDate() != null
                && request.getRelationshipToDeceased() != null
                && !request.getRelationshipToDeceased().trim().isEmpty()
                && request.getMeetingParticipationDesire() != null
                && request.getPrivacyAgreement() != null
                && request.getPrivacyAgreement();
    }
}
