import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { surveyAPI } from '../services/api';
import { useAuth } from '../hooks/useAuth.jsx';
import './Survey.css';

const Survey = () => {
  const [formData, setFormData] = useState({
    birthDate: '',
    relationshipToDeceased: '',
    relationshipDescription: '',
    psychologicalSupportLevel: '',
    meetingParticipationDesire: false,
    personalNotes: '',
    privacyAgreement: false,
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [existingSurvey, setExistingSurvey] = useState(null);
  
  const { isLoggedIn, user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoggedIn) {
      navigate('/login');
      return;
    }
    loadExistingSurvey();
  }, [isLoggedIn, navigate]);

  const loadExistingSurvey = async () => {
    try {
      const response = await surveyAPI.getMySurvey();
      if (response.data.success && response.data.data) {
        const survey = response.data.data;
        setExistingSurvey(survey);
        
        // 기존 데이터로 폼 채우기
        setFormData({
          birthDate: survey.birthDate || '',
          relationshipToDeceased: survey.relationshipToDeceased || '',
          relationshipDescription: survey.relationshipDescription || '',
          psychologicalSupportLevel: survey.psychologicalSupportLevel || '',
          meetingParticipationDesire: survey.meetingParticipationDesire || false,
          personalNotes: survey.personalNotes || '',
          privacyAgreement: survey.privacyAgreement || false,
        });
      }
    } catch (error) {
      console.error('Failed to load existing survey:', error);
    }
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
    // 입력 시 에러/성공 메시지 제거
    if (error) setError('');
    if (success) setSuccess('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // 유효성 검사
    if (!formData.birthDate) {
      setError('생년월일을 입력해주세요.');
      return;
    }
    
    if (!formData.relationshipToDeceased) {
      setError('사망자와의 관계를 선택해주세요.');
      return;
    }
    
    if (formData.relationshipToDeceased === 'OTHER' && !formData.relationshipDescription.trim()) {
      setError('기타 관계에 대한 설명을 입력해주세요.');
      return;
    }
    
    if (!formData.psychologicalSupportLevel) {
      setError('심리적 지원 필요도를 선택해주세요.');
      return;
    }
    
    if (!formData.privacyAgreement) {
      setError('개인정보 처리 동의는 필수입니다.');
      return;
    }

    setIsLoading(true);
    setError('');
    setSuccess('');

    try {
      // 데이터 타입 확인 및 변환
      const submitData = {
        ...formData,
        meetingParticipationDesire: Boolean(formData.meetingParticipationDesire),
        privacyAgreement: Boolean(formData.privacyAgreement),
        // birthDate는 이미 문자열이므로 그대로 전송 (백엔드에서 파싱)
      };

      console.log('Submitting survey data:', submitData); // 디버깅용
      const response = await surveyAPI.submitSurvey(submitData);
      
      if (response.data.success) {
        setSuccess('설문조사가 성공적으로 저장되었습니다.');
        setExistingSurvey(response.data.data);
      } else {
        setError(response.data.message || '설문조사 저장에 실패했습니다.');
      }
    } catch (error) {
      console.error('Failed to submit survey:', error);
      if (error.response?.status === 401) {
        setError('로그인이 필요합니다.');
        navigate('/login');
      } else {
        setError(error.response?.data?.message || '설문조사 저장 중 오류가 발생했습니다.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const relationshipOptions = [
    { value: 'SPOUSE', label: '배우자' },
    { value: 'CHILD', label: '자녀' },
    { value: 'PARENT', label: '부모' },
    { value: 'SIBLING', label: '형제자매' },
    { value: 'OTHER', label: '기타' },
  ];

  const supportLevelOptions = [
    { value: 'HIGH', label: '높음 (전문적인 도움이 많이 필요)' },
    { value: 'MEDIUM', label: '보통 (어느 정도 도움이 필요)' },
    { value: 'LOW', label: '낮음 (조금의 도움이 필요)' },
    { value: 'NONE', label: '필요없음 (현재 괜찮음)' },
  ];

  return (
    <div className="survey-page">
      <div className="survey-container">
        <div className="survey-header">
          <h1>설문조사</h1>
          <p>더 나은 지원을 위해 몇 가지 질문에 답해주세요.</p>
          {existingSurvey && (
            <div className="survey-status">
              <span className={`status-badge ${existingSurvey.surveyCompleted ? 'completed' : 'draft'}`}>
                {existingSurvey.surveyCompleted ? '완료됨' : '임시저장'}
              </span>
            </div>
          )}
        </div>

        <form onSubmit={handleSubmit} className="survey-form">
          {error && (
            <div className="error-message">
              {error}
            </div>
          )}

          {success && (
            <div className="success-message">
              {success}
            </div>
          )}

          {/* 1. 생년월일 */}
          <div className="form-group">
            <label htmlFor="birthDate">
              생년월일 <span className="required">*</span>
            </label>
            <input
              type="date"
              id="birthDate"
              name="birthDate"
              value={formData.birthDate}
              onChange={handleChange}
              required
              disabled={isLoading}
              className="form-control"
            />
          </div>

          {/* 2. 사망자와의 관계 */}
          <div className="form-group">
            <label htmlFor="relationshipToDeceased">
              사망자와의 관계 <span className="required">*</span>
            </label>
            <select
              id="relationshipToDeceased"
              name="relationshipToDeceased"
              value={formData.relationshipToDeceased}
              onChange={handleChange}
              required
              disabled={isLoading}
              className="form-control"
            >
              <option value="">관계를 선택하세요</option>
              {relationshipOptions.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>

          {/* 기타 관계 설명 */}
          {formData.relationshipToDeceased === 'OTHER' && (
            <div className="form-group">
              <label htmlFor="relationshipDescription">
                관계 설명 <span className="required">*</span>
              </label>
              <input
                type="text"
                id="relationshipDescription"
                name="relationshipDescription"
                value={formData.relationshipDescription}
                onChange={handleChange}
                placeholder="구체적인 관계를 입력해주세요"
                required
                disabled={isLoading}
                className="form-control"
                maxLength={100}
              />
            </div>
          )}

          {/* 3. 심리적 지원 필요도 */}
          <div className="form-group">
            <label htmlFor="psychologicalSupportLevel">
              심리적 지원 필요도 <span className="required">*</span>
            </label>
            <div className="radio-group">
              {supportLevelOptions.map(option => (
                <label key={option.value} className="radio-label">
                  <input
                    type="radio"
                    name="psychologicalSupportLevel"
                    value={option.value}
                    checked={formData.psychologicalSupportLevel === option.value}
                    onChange={handleChange}
                    disabled={isLoading}
                  />
                  <span className="radio-text">{option.label}</span>
                </label>
              ))}
            </div>
          </div>

          {/* 4. 모임 참석 희망 여부 */}
          <div className="form-group">
            <label htmlFor="meetingParticipationDesire">
              모임 참석 희망 여부 <span className="required">*</span>
            </label>
            <div className="checkbox-group">
              <label className="checkbox-label">
                <input
                  type="checkbox"
                  name="meetingParticipationDesire"
                  checked={formData.meetingParticipationDesire}
                  onChange={handleChange}
                  disabled={isLoading}
                />
                <span className="checkbox-text">네, 관련 모임에 참석하고 싶습니다.</span>
              </label>
            </div>
          </div>

          {/* 5. 개인적 메모/요청사항 */}
          <div className="form-group">
            <label htmlFor="personalNotes">
              개인적 메모 및 요청사항 (선택사항)
            </label>
            <textarea
              id="personalNotes"
              name="personalNotes"
              value={formData.personalNotes}
              onChange={handleChange}
              placeholder="추가로 전달하고 싶은 내용이나 요청사항을 자유롭게 작성해주세요..."
              disabled={isLoading}
              className="form-control"
              rows={4}
              maxLength={500}
            />
            <div className="char-count">
              {formData.personalNotes.length}/500
            </div>
          </div>

          {/* 개인정보 처리 동의 */}
          <div className="form-group privacy-group">
            <label className="checkbox-label privacy-label">
              <input
                type="checkbox"
                name="privacyAgreement"
                checked={formData.privacyAgreement}
                onChange={handleChange}
                disabled={isLoading}
                required
              />
              <span className="checkbox-text">
                개인정보 수집 및 이용에 동의합니다. <span className="required">*</span>
              </span>
            </label>
            <div className="privacy-notice">
              수집된 정보는 서비스 개선 및 맞춤형 지원 제공 목적으로만 사용됩니다.
            </div>
          </div>

          <div className="form-actions">
            <button
              type="button"
              onClick={() => navigate('/boards')}
              className="btn btn-secondary"
              disabled={isLoading}
            >
              나중에 하기
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={isLoading}
            >
              {isLoading ? '저장 중...' : '설문조사 제출'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Survey;
