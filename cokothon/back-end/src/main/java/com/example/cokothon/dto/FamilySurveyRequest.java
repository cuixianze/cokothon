package com.example.cokothon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FamilySurveyRequest {
    
    // 기본 인적사항
    @NotNull(message = "생년월일은 필수입니다.")
    private LocalDate birthDate;
    
    private String gender; // MALE, FEMALE, OTHER
    private String phoneNumber;
    private String address;
    
    // 사망자와의 관계
    @NotBlank(message = "사망자와의 관계는 필수입니다.")
    private String relationshipToDeceased; // SPOUSE, CHILD, PARENT, SIBLING, OTHER
    
    private String relationshipDescription; // 기타 관계 설명
    
    // 사망자 정보
    private String deceasedName;
    private Integer deceasedAge;
    private LocalDate deathDate;
    private String causeOfDeath;
    
    // 현재 가족 구성
    private String currentFamilyMembers;
    private Boolean livingAlone = false;
    private String familySupportLevel; // HIGH, MEDIUM, LOW, NONE
    
    // 심리적 상태
    private String griefStage; // DENIAL, ANGER, BARGAINING, DEPRESSION, ACCEPTANCE
    private Boolean counselingExperience = false;
    private String counselingWillingness; // VERY_INTERESTED, INTERESTED, NEUTRAL, NOT_INTERESTED
    
    // 모임 및 지원 관련
    @NotNull(message = "모임 참석 희망여부는 필수입니다.")
    private Boolean meetingParticipationDesire = false;
    
    private String preferredMeetingType; // ONLINE, OFFLINE, BOTH
    private String preferredMeetingTime; // WEEKDAY_MORNING, WEEKDAY_AFTERNOON, WEEKDAY_EVENING, WEEKEND
    private String supportNeeds;
    
    // 추가 정보
    private String additionalNotes;
    
    @NotNull(message = "개인정보 처리 동의는 필수입니다.")
    private Boolean privacyAgreement = false;
}
