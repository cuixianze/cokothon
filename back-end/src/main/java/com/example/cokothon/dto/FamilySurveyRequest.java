package com.example.cokothon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FamilySurveyRequest {
    
    // 1. 생년월일 (필수)
    @NotNull(message = "생년월일은 필수입니다.")
    private LocalDate birthDate;
    
    // 2. 사망자와의 관계 (필수)
    @NotBlank(message = "사망자와의 관계는 필수입니다.")
    private String relationshipToDeceased; // SPOUSE, CHILD, PARENT, SIBLING, OTHER
    
    private String relationshipDescription; // 기타 관계 설명
    
    // 3. 심리적 지원 필요도 (필수)
    @NotBlank(message = "심리적 지원 필요도는 필수입니다.")
    private String psychologicalSupportLevel; // HIGH, MEDIUM, LOW, NONE
    
    // 4. 모임 참석 희망 여부 (필수)
    @NotNull(message = "모임 참석 희망여부는 필수입니다.")
    private Boolean meetingParticipationDesire = false;
    
    // 5. 개인적 메모/요청사항 (선택)
    private String personalNotes;
    
    @NotNull(message = "개인정보 처리 동의는 필수입니다.")
    private Boolean privacyAgreement = false;
}
