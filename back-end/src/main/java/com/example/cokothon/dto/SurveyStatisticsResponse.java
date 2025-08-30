package com.example.cokothon.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SurveyStatisticsResponse {
    
    private long totalSurveys;
    private long completedSurveys;
    private long incompleteSurveys;
    
    // 관계별 통계
    private Map<String, Long> relationshipStatistics;
    
    // 애도 단계별 통계
    private Map<String, Long> griefStageStatistics;
    
    // 모임 참석 희망 통계
    private long meetingParticipationDesired;
    private long meetingParticipationNotDesired;
    
    // 상담 의향 통계
    private long counselingInterested;
    private long counselingNotInterested;
    
    // 홀로 거주 통계
    private long livingAloneCount;
    private long livingWithFamilyCount;
    
    // 가족 지원 수준 통계
    private Map<String, Long> familySupportLevelStatistics;
    
    // 선호 모임 방식 통계
    private Map<String, Long> preferredMeetingTypeStatistics;
}
