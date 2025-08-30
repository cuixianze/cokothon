import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Axios 인스턴스 생성
const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true, // 세션 쿠키를 포함하여 요청
  headers: {
    'Content-Type': 'application/json',
  },
});

// 응답 인터셉터 - 에러 처리
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // 인증 에러 시 로그인 페이지로 리다이렉트할 수 있음
      console.log('Authentication required');
    }
    return Promise.reject(error);
  }
);

// 인증 관련 API
export const authAPI = {
  // 로그인
  login: (credentials) => api.post('/auth/login', credentials),
  
  // 회원가입
  register: (userData) => api.post('/auth/register', userData),
  
  // 로그아웃
  logout: () => api.post('/auth/logout'),
  
  // 현재 사용자 정보
  getCurrentUser: () => api.get('/auth/me'),
  
  // 로그인 상태 확인
  getLoginStatus: () => api.get('/auth/status'),
};

// 게시판 관련 API
export const boardAPI = {
  // 모든 게시글 조회 (페이징)
  getAllBoards: (page = 0, size = 10, sort = 'createdAt,desc') => 
    api.get('/boards', { params: { page, size, sort } }),
  
  // 카테고리별 게시글 조회
  getBoardsByCategory: (categoryId, page = 0, size = 10) =>
    api.get(`/boards/category/${categoryId}`, { params: { page, size } }),
  
  // 게시글 상세 조회
  getBoardById: (id) => api.get(`/boards/${id}`),
  
  // 게시글 생성
  createBoard: (boardData) => api.post('/boards', boardData),
  
  // 게시글 수정
  updateBoard: (id, boardData) => api.put(`/boards/${id}`, boardData),
  
  // 게시글 삭제
  deleteBoard: (id) => api.delete(`/boards/${id}`),
  
  // 게시글 검색
  searchBoards: (keyword, page = 0, size = 10) =>
    api.get('/boards/search', { params: { keyword, page, size } }),
};

// 카테고리 관련 API
export const categoryAPI = {
  // 모든 카테고리 조회
  getAllCategories: () => api.get('/categories'),
  
  // 카테고리 상세 조회
  getCategoryById: (id) => api.get(`/categories/${id}`),
};

// 설문조사 관련 API
export const surveyAPI = {
  // 내 설문조사 조회
  getMySurvey: () => api.get('/family-survey/my-survey'),
  
  // 설문조사 제출
  submitSurvey: (surveyData) => api.post('/family-survey/submit', surveyData),
  
  // 설문조사 완료 여부 확인
  getCompletionStatus: () => api.get('/family-survey/completion-status'),
  
  // 관리자 - 완료된 설문조사 목록
  getCompletedSurveys: () => api.get('/family-survey/admin/completed'),
  
  // 관리자 - 통계
  getStatistics: () => api.get('/family-survey/admin/statistics'),
};

export default api;
