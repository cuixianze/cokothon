import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { boardAPI, categoryAPI } from '../services/api';
import { useAuth } from '../hooks/useAuth.jsx';
import './BoardWrite.css';

const BoardWrite = () => {
  const [formData, setFormData] = useState({
    title: '',
    content: '',
    categoryId: '',
  });
  const [categories, setCategories] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  
  const { isLoggedIn, user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoggedIn) {
      navigate('/login');
      return;
    }
    loadCategories();
  }, [isLoggedIn, navigate]);

  const loadCategories = async () => {
    try {
      const response = await categoryAPI.getAllCategories();
      if (response.data.success) {
        setCategories(response.data.data);
      }
    } catch (error) {
      console.error('Failed to load categories:', error);
      setError('카테고리 목록을 불러오는데 실패했습니다.');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // 입력 시 에러 메시지 제거
    if (error) setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.title.trim()) {
      setError('제목을 입력해주세요.');
      return;
    }
    
    if (!formData.content.trim()) {
      setError('내용을 입력해주세요.');
      return;
    }
    
    if (!formData.categoryId) {
      setError('카테고리를 선택해주세요.');
      return;
    }

    setIsLoading(true);
    setError('');

    try {
      const boardData = {
        title: formData.title.trim(),
        content: formData.content.trim(),
        categoryId: parseInt(formData.categoryId)
      };

      const response = await boardAPI.createBoard(boardData);
      
      if (response.data.success) {
        // 성공 시 게시글 상세 페이지로 이동
        navigate(`/boards/${response.data.data.id}`);
      } else {
        setError(response.data.message || '게시글 작성에 실패했습니다.');
      }
    } catch (error) {
      console.error('Failed to create board:', error);
      if (error.response?.status === 401) {
        setError('로그인이 필요합니다.');
        navigate('/login');
      } else {
        setError(error.response?.data?.message || '게시글 작성 중 오류가 발생했습니다.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = () => {
    if (formData.title || formData.content) {
      if (window.confirm('작성 중인 내용이 있습니다. 정말 나가시겠습니까?')) {
        navigate('/boards');
      }
    } else {
      navigate('/boards');
    }
  };

  return (
    <div className="board-write-page">
      <div className="board-write-container">
        <div className="board-write-header">
          <h1>글쓰기</h1>
          <p>함께 소중한 이야기를 나누어 보세요.</p>
        </div>

        <form onSubmit={handleSubmit} className="board-write-form">
          {error && (
            <div className="error-message">
              {error}
            </div>
          )}

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="categoryId">카테고리 <span className="required">*</span></label>
              <select
                id="categoryId"
                name="categoryId"
                value={formData.categoryId}
                onChange={handleChange}
                required
                disabled={isLoading}
                className="form-control"
              >
                <option value="">카테고리를 선택하세요</option>
                {categories.map(category => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="title">제목 <span className="required">*</span></label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleChange}
              placeholder="제목을 입력하세요"
              required
              disabled={isLoading}
              className="form-control"
              maxLength={100}
            />
            <div className="char-count">
              {formData.title.length}/100
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="content">내용 <span className="required">*</span></label>
            <textarea
              id="content"
              name="content"
              value={formData.content}
              onChange={handleChange}
              placeholder="내용을 입력하세요..."
              required
              disabled={isLoading}
              className="form-control content-textarea"
              rows={15}
              maxLength={2000}
            />
            <div className="char-count">
              {formData.content.length}/2000
            </div>
          </div>

          <div className="form-actions">
            <button
              type="button"
              onClick={handleCancel}
              className="btn btn-secondary"
              disabled={isLoading}
            >
              취소
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={isLoading}
            >
              {isLoading ? '작성 중...' : '게시글 작성'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default BoardWrite;
