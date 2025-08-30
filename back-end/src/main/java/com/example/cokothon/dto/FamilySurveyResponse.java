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
    
    // 기본 인적사항
    private LocalDate birthDate;
    private String gender;
    private String phoneNumber;
    private String address;
    
    // 사망자와의 관계
    private String relationshipToDeceased;
    private String relationshipDescription;
    
    // 사망자 정보
    private String deceasedName;
    private Integer deceasedAge;
    private LocalDate deathDate;
    private String causeOfDeath;
    
    // 현재 가족 구성
    private String currentFamilyMembers;
    private Boolean livingAlone;
    private String familySupportLevel;
    
    // 심리적 상태
    private String griefStage;
    private Boolean counselingExperience;
    private String counselingWillingness;
    
    // 모임 및 지원 관련
    private Boolean meetingParticipationDesire;
    private String preferredMeetingType;
    private String preferredMeetingTime;
    private String supportNeeds;
    
    // 추가 정보
    private String additionalNotes;
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
        response.setGender(survey.getGender());
        response.setPhoneNumber(survey.getPhoneNumber());
        response.setAddress(survey.getAddress());
        
        response.setRelationshipToDeceased(survey.getRelationshipToDeceased());
        response.setRelationshipDescription(survey.getRelationshipDescription());
        
        response.setDeceasedName(survey.getDeceasedName());
        response.setDeceasedAge(survey.getDeceasedAge());
        response.setDeathDate(survey.getDeathDate());
        response.setCauseOfDeath(survey.getCauseOfDeath());
        
        response.setCurrentFamilyMembers(survey.getCurrentFamilyMembers());
        response.setLivingAlone(survey.getLivingAlone());
        response.setFamilySupportLevel(survey.getFamilySupportLevel());
        
        response.setGriefStage(survey.getGriefStage());
        response.setCounselingExperience(survey.getCounselingExperience());
        response.setCounselingWillingness(survey.getCounselingWillingness());
        
        response.setMeetingParticipationDesire(survey.getMeetingParticipationDesire());
        response.setPreferredMeetingType(survey.getPreferredMeetingType());
        response.setPreferredMeetingTime(survey.getPreferredMeetingTime());
        response.setSupportNeeds(survey.getSupportNeeds());
        
        response.setAdditionalNotes(survey.getAdditionalNotes());
        response.setPrivacyAgreement(survey.getPrivacyAgreement());
        response.setSurveyCompleted(survey.getSurveyCompleted());
        
        response.setCreatedAt(survey.getCreatedAt());
        response.setUpdatedAt(survey.getUpdatedAt());
        
        return response;
    }
}
