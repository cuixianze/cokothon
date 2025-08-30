package com.example.cokothon.config;

import com.example.cokothon.entity.Board;
import com.example.cokothon.entity.Category;
import com.example.cokothon.entity.FamilySurvey;
import com.example.cokothon.entity.User;
import com.example.cokothon.repository.BoardRepository;
import com.example.cokothon.repository.CategoryRepository;
import com.example.cokothon.repository.FamilySurveyRepository;
import com.example.cokothon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FamilySurveyRepository familySurveyRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            initializeData();
        }
    }
    
    private void initializeData() {
        log.info("초기 데이터를 생성합니다...");
        
        // 사용자 생성
        User admin = userRepository.save(
                new User("admin", "admin123", "관리자", "admin@example.com", true)
        );
        
        User user1 = userRepository.save(
                new User("kimdev", "password123", "김개발", "kimdev@example.com", false)
        );
        
        User user2 = userRepository.save(
                new User("leecoding", "password123", "이코딩", "leecoding@example.com", false)
        );
        
        User user3 = userRepository.save(
                new User("parkstudy", "password123", "박스터디", "parkstudy@example.com", false)
        );
        
        // 카테고리 생성
        Category freeBoard = categoryRepository.save(
                new Category("자유게시판", "자유롭게 이야기를 나누는 공간입니다.")
        );
        
        Category questionBoard = categoryRepository.save(
                new Category("질문게시판", "궁금한 것들을 질문하고 답변받는 공간입니다.")
        );
        
        Category noticeBoard = categoryRepository.save(
                new Category("공지사항", "중요한 공지사항을 전달하는 공간입니다.")
        );
        
        // 샘플 게시글 생성
        // 공지사항 (관리자가 작성)
        boardRepository.save(new Board(
                "게시판 이용 안내",
                "안녕하세요. 게시판을 이용해주셔서 감사합니다.\n\n" +
                "게시판 이용 시 다음 사항을 준수해주세요:\n" +
                "1. 서로를 존중하는 댓글 문화\n" +
                "2. 스팸성 글 금지\n" +
                "3. 개인정보 보호\n\n" +
                "감사합니다.",
                admin,
                noticeBoard
        ));
        
        boardRepository.save(new Board(
                "시스템 점검 안내",
                "다음과 같이 시스템 점검이 예정되어 있습니다.\n\n" +
                "일시: 2024년 1월 15일 오전 2시 ~ 4시\n" +
                "내용: 서버 성능 개선 및 보안 업데이트\n\n" +
                "점검 시간 동안 서비스 이용이 제한될 수 있습니다.\n" +
                "양해 부탁드립니다.",
                admin,
                noticeBoard
        ));
        
        // 자유게시판
        boardRepository.save(new Board(
                "안녕하세요! 새로 가입했습니다.",
                "안녕하세요 여러분! 오늘 새로 가입한 신입 개발자입니다.\n\n" +
                "Spring Boot를 공부하고 있는데, 이 게시판에서 많은 것을 배우고 싶습니다.\n" +
                "앞으로 잘 부탁드려요! 😊\n\n" +
                "좋은 정보나 조언 있으시면 언제든지 알려주세요!",
                user1,
                freeBoard
        ));
        
        boardRepository.save(new Board(
                "오늘의 개발 일기",
                "오늘은 REST API 개발을 했습니다.\n\n" +
                "처음에는 복잡해 보였지만, 차근차근 따라하니 생각보다 재미있네요!\n" +
                "특히 Spring Boot의 자동 설정 기능이 정말 편리한 것 같습니다.\n\n" +
                "내일은 JPA 연관관계 매핑을 공부해볼 예정입니다.\n" +
                "화이팅! 💪",
                user2,
                freeBoard
        ));
        
        boardRepository.save(new Board(
                "주말 스터디 모집합니다!",
                "안녕하세요! 주말에 함께 공부할 분들을 모집합니다.\n\n" +
                "📚 주제: Spring Boot & JPA 심화 학습\n" +
                "📅 시간: 매주 토요일 오후 2시 ~ 6시\n" +
                "📍 장소: 온라인 (Zoom)\n" +
                "👥 인원: 4~6명\n\n" +
                "관심 있으신 분들은 댓글로 연락 부탁드려요!\n" +
                "함께 성장해요! 🚀",
                user3,
                freeBoard
        ));
        
        // 질문게시판
        boardRepository.save(new Board(
                "JPA N+1 문제 해결 방법이 궁금합니다",
                "안녕하세요! JPA를 공부하고 있는 초보 개발자입니다.\n\n" +
                "N+1 문제에 대해서 들었는데, 정확히 어떤 상황에서 발생하는지,\n" +
                "그리고 어떻게 해결해야 하는지 궁금합니다.\n\n" +
                "특히 @OneToMany 관계에서 자주 발생한다고 하는데,\n" +
                "실제 코드 예시와 함께 설명해주시면 정말 감사하겠습니다!\n\n" +
                "미리 감사드립니다. 🙏",
                user1,
                questionBoard
        ));
        
        boardRepository.save(new Board(
                "Spring Security 설정 관련 질문",
                "Spring Security를 처음 적용해보고 있습니다.\n\n" +
                "JWT 토큰 기반 인증을 구현하려고 하는데,\n" +
                "SecurityConfig 설정에서 자꾸 오류가 발생합니다.\n\n" +
                "```java\n" +
                "@Configuration\n" +
                "@EnableWebSecurity\n" +
                "public class SecurityConfig {\n" +
                "    // 설정 코드...\n" +
                "}\n" +
                "```\n\n" +
                "혹시 최신 Spring Boot 3.x 버전에서 추천하는\n" +
                "설정 방법이 있을까요?",
                user2,
                questionBoard
        ));
        
        boardRepository.save(new Board(
                "H2 데이터베이스 콘솔 접속 문제",
                "H2 데이터베이스를 사용하고 있는데 콘솔에 접속이 안됩니다.\n\n" +
                "application.properties에 다음과 같이 설정했습니다:\n" +
                "```\n" +
                "spring.h2.console.enabled=true\n" +
                "spring.h2.console.path=/h2-console\n" +
                "```\n\n" +
                "브라우저에서 localhost:8080/h2-console로 접속하면\n" +
                "404 오류가 발생합니다.\n\n" +
                "혹시 추가로 설정해야 할 부분이 있을까요?",
                user3,
                questionBoard
        ));
        
        // 샘플 설문조사 생성
        createSampleSurveys(user1, user2, user3);
        
        log.info("초기 데이터 생성이 완료되었습니다.");
        log.info("사용자 4명 (관리자 1명 포함), 카테고리 3개, 게시글 8개, 설문조사 3개가 생성되었습니다.");
    }
    
    private void createSampleSurveys(User user1, User user2, User user3) {
        // 김개발 사용자의 설문조사 (완료)
        FamilySurvey survey1 = new FamilySurvey(user1);
        survey1.setBirthDate(java.time.LocalDate.of(1985, 3, 15));
        survey1.setGender("MALE");
        survey1.setPhoneNumber("010-1234-5678");
        survey1.setAddress("서울시 강남구");
        survey1.setRelationshipToDeceased("CHILD");
        survey1.setDeceasedName("김아버지");
        survey1.setDeceasedAge(68);
        survey1.setDeathDate(java.time.LocalDate.of(2024, 1, 10));
        survey1.setCauseOfDeath("암");
        survey1.setCurrentFamilyMembers("어머니, 배우자, 자녀 2명");
        survey1.setLivingAlone(false);
        survey1.setFamilySupportLevel("HIGH");
        survey1.setGriefStage("ACCEPTANCE");
        survey1.setCounselingExperience(true);
        survey1.setCounselingWillingness("INTERESTED");
        survey1.setMeetingParticipationDesire(true);
        survey1.setPreferredMeetingType("BOTH");
        survey1.setPreferredMeetingTime("WEEKEND");
        survey1.setSupportNeeds("같은 상황의 사람들과 경험 공유");
        survey1.setPrivacyAgreement(true);
        survey1.setSurveyCompleted(true);
        familySurveyRepository.save(survey1);
        
        // 이코딩 사용자의 설문조사 (완료)
        FamilySurvey survey2 = new FamilySurvey(user2);
        survey2.setBirthDate(java.time.LocalDate.of(1990, 7, 22));
        survey2.setGender("FEMALE");
        survey2.setPhoneNumber("010-2345-6789");
        survey2.setAddress("서울시 서초구");
        survey2.setRelationshipToDeceased("SPOUSE");
        survey2.setDeceasedName("박남편");
        survey2.setDeceasedAge(32);
        survey2.setDeathDate(java.time.LocalDate.of(2024, 3, 5));
        survey2.setCauseOfDeath("교통사고");
        survey2.setCurrentFamilyMembers("자녀 1명");
        survey2.setLivingAlone(false);
        survey2.setFamilySupportLevel("MEDIUM");
        survey2.setGriefStage("DEPRESSION");
        survey2.setCounselingExperience(false);
        survey2.setCounselingWillingness("VERY_INTERESTED");
        survey2.setMeetingParticipationDesire(true);
        survey2.setPreferredMeetingType("ONLINE");
        survey2.setPreferredMeetingTime("WEEKDAY_EVENING");
        survey2.setSupportNeeds("심리 상담 및 육아 지원");
        survey2.setAdditionalNotes("어린 자녀가 있어 온라인 참여를 선호합니다.");
        survey2.setPrivacyAgreement(true);
        survey2.setSurveyCompleted(true);
        familySurveyRepository.save(survey2);
        
        // 박스터디 사용자의 설문조사 (미완료)
        FamilySurvey survey3 = new FamilySurvey(user3);
        survey3.setBirthDate(java.time.LocalDate.of(1988, 11, 8));
        survey3.setGender("MALE");
        survey3.setRelationshipToDeceased("SIBLING");
        survey3.setDeceasedName("박여동생");
        survey3.setDeceasedAge(25);
        survey3.setDeathDate(java.time.LocalDate.of(2024, 2, 14));
        survey3.setLivingAlone(true);
        survey3.setFamilySupportLevel("LOW");
        survey3.setGriefStage("ANGER");
        survey3.setCounselingExperience(false);
        survey3.setMeetingParticipationDesire(false);
        survey3.setPrivacyAgreement(true);
        survey3.setSurveyCompleted(false); // 미완료 상태
        familySurveyRepository.save(survey3);
    }
}
