package com.example.cokothon.repository;

import com.example.cokothon.entity.FamilySurvey;
import com.example.cokothon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilySurveyRepository extends JpaRepository<FamilySurvey, Long> {
    
    // 사용자별 설문조사 조회
    Optional<FamilySurvey> findByUser(User user);
    Optional<FamilySurvey> findByUserId(Long userId);
    
    // 완료된 설문조사만 조회
    List<FamilySurvey> findBySurveyCompletedTrue();
    
    // 미완료 설문조사 조회
    List<FamilySurvey> findBySurveyCompletedFalse();
    
    // 사망자와의 관계별 조회
    List<FamilySurvey> findByRelationshipToDeceased(String relationship);
    
    // 모임 참석 희망자 조회
    List<FamilySurvey> findByMeetingParticipationDesireTrue();
    
    // 심리적 지원 필요도별 조회
    List<FamilySurvey> findByPsychologicalSupportLevel(String supportLevel);
    
    // 통계용 쿼리들
    @Query("SELECT COUNT(fs) FROM FamilySurvey fs WHERE fs.surveyCompleted = true")
    long countCompletedSurveys();
    
    @Query("SELECT fs.relationshipToDeceased, COUNT(fs) FROM FamilySurvey fs WHERE fs.surveyCompleted = true GROUP BY fs.relationshipToDeceased")
    List<Object[]> getRelationshipStatistics();
    
    @Query("SELECT fs.psychologicalSupportLevel, COUNT(fs) FROM FamilySurvey fs WHERE fs.surveyCompleted = true AND fs.psychologicalSupportLevel IS NOT NULL GROUP BY fs.psychologicalSupportLevel")
    List<Object[]> getPsychologicalSupportStatistics();
    
    // 사용자가 설문조사를 완료했는지 확인
    @Query("SELECT CASE WHEN COUNT(fs) > 0 THEN true ELSE false END FROM FamilySurvey fs WHERE fs.user.id = :userId AND fs.surveyCompleted = true")
    boolean hasCompletedSurvey(@Param("userId") Long userId);
}
