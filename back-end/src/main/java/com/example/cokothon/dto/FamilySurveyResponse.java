package com.example.cokothon.dto;

import com.example.cokothon.entity.FamilySurvey;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class FamilySurveyResponse {
    
    private Long id;
    private Long userId;
    private String userName;
    
    // 1. 생년월일
    private LocalDate birthDate;
    
    // 2. 사망자와의 관계
    private String relationshipToDeceased;
    private String relationshipDescription;
    
    // 3. 심리적 지원 필요도
    private String psychologicalSupportLevel;
    
    // 4. 모임 참석 희망 여부
    private Boolean meetingParticipationDesire;
    
    // 5. 개인적 메모/요청사항
    private String personalNotes;
    private Boolean privacyAgreement;
    private Boolean surveyCompleted;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static FamilySurveyResponse from(FamilySurvey survey) {
        FamilySurveyResponse response = new FamilySurveyResponse();
        response.setId(survey.getId());
        response.setUserId(survey.getUser().getId());
        response.setUserName(survey.getUser().getName());
        
        response.setBirthDate(survey.getBirthDate());
        response.setRelationshipToDeceased(survey.getRelationshipToDeceased());
        response.setRelationshipDescription(survey.getRelationshipDescription());
        response.setPsychologicalSupportLevel(survey.getPsychologicalSupportLevel());
        response.setMeetingParticipationDesire(survey.getMeetingParticipationDesire());
        response.setPersonalNotes(survey.getPersonalNotes());
        response.setPrivacyAgreement(survey.getPrivacyAgreement());
        response.setSurveyCompleted(survey.getSurveyCompleted());
        
        response.setCreatedAt(survey.getCreatedAt());
        response.setUpdatedAt(survey.getUpdatedAt());
        
        return response;
    }
}
