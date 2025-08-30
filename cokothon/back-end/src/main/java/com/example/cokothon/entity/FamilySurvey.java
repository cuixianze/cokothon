package com.example.cokothon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "family_surveys")
@Getter
@Setter
@NoArgsConstructor
public class FamilySurvey {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    
    // 기본 인적사항
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "gender")
    private String gender; // MALE, FEMALE, OTHER
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "address")
    private String address;
    
    // 사망자와의 관계
    @Column(name = "relationship_to_deceased", nullable = false)
    private String relationshipToDeceased; // SPOUSE(배우자), CHILD(자녀), PARENT(부모), SIBLING(형제자매), OTHER(기타)
    
    @Column(name = "relationship_description")
    private String relationshipDescription; // 기타 관계 설명
    
    // 사망자 정보
    @Column(name = "deceased_name")
    private String deceasedName;
    
    @Column(name = "deceased_age")
    private Integer deceasedAge;
    
    @Column(name = "death_date")
    private LocalDate deathDate;
    
    @Column(name = "cause_of_death")
    private String causeOfDeath;
    
    // 현재 가족 구성
    @Column(name = "current_family_members")
    private String currentFamilyMembers; // JSON 또는 텍스트로 저장
    
    @Column(name = "living_alone")
    private Boolean livingAlone = false;
    
    @Column(name = "family_support_level")
    private String familySupportLevel; // HIGH(높음), MEDIUM(보통), LOW(낮음), NONE(없음)
    
    // 심리적 상태
    @Column(name = "grief_stage")
    private String griefStage; // DENIAL(부정), ANGER(분노), BARGAINING(협상), DEPRESSION(우울), ACCEPTANCE(수용)
    
    @Column(name = "counseling_experience")
    private Boolean counselingExperience = false;
    
    @Column(name = "counseling_willingness")
    private String counselingWillingness; // VERY_INTERESTED(매우 관심), INTERESTED(관심), NEUTRAL(보통), NOT_INTERESTED(관심없음)
    
    // 모임 및 지원 관련
    @Column(name = "meeting_participation_desire")
    private Boolean meetingParticipationDesire = false;
    
    @Column(name = "preferred_meeting_type")
    private String preferredMeetingType; // ONLINE(온라인), OFFLINE(오프라인), BOTH(둘다)
    
    @Column(name = "preferred_meeting_time")
    private String preferredMeetingTime; // WEEKDAY_MORNING(평일오전), WEEKDAY_AFTERNOON(평일오후), WEEKDAY_EVENING(평일저녁), WEEKEND(주말)
    
    @Column(name = "support_needs", columnDefinition = "TEXT")
    private String supportNeeds; // 필요한 지원사항
    
    // 추가 정보
    @Column(name = "additional_notes", columnDefinition = "TEXT")
    private String additionalNotes;
    
    @Column(name = "privacy_agreement")
    private Boolean privacyAgreement = false;
    
    @Column(name = "survey_completed")
    private Boolean surveyCompleted = false;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public FamilySurvey(User user) {
        this.user = user;
    }
    
    // 편의 메서드
    public void completeSurvey() {
        this.surveyCompleted = true;
    }
    
    public boolean isAdult() {
        if (birthDate == null) return true;
        return birthDate.isBefore(LocalDate.now().minusYears(18));
    }
}
