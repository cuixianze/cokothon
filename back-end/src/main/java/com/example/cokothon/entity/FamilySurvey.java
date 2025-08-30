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
    
    // 1. 생년월일 (필수)
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    
    // 2. 사망자와의 관계 (필수)
    @Column(name = "relationship_to_deceased", nullable = false)
    private String relationshipToDeceased; // SPOUSE(배우자), CHILD(자녀), PARENT(부모), SIBLING(형제자매), OTHER(기타)
    
    @Column(name = "relationship_description")
    private String relationshipDescription; // 기타 관계 설명
    
    // 3. 심리적 지원 필요도 (필수)
    @Column(name = "psychological_support_level", nullable = false)
    private String psychologicalSupportLevel; // HIGH(높음), MEDIUM(보통), LOW(낮음), NONE(필요없음)
    
    // 4. 모임 참석 희망 여부 (필수)
    @Column(name = "meeting_participation_desire", nullable = false)
    private Boolean meetingParticipationDesire = false;
    
    // 5. 개인적 메모/요청사항 (선택)
    @Column(name = "personal_notes", columnDefinition = "TEXT")
    private String personalNotes;
    
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
