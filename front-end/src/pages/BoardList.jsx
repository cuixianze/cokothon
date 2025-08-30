import { useState, useEffect } from 'react';
import { Link, useParams, useSearchParams } from 'react-router-dom';
import { boardAPI, categoryAPI } from '../services/api';
import { useAuth } from '../hooks/useAuth.jsx';
import './BoardList.css';

const BoardList = () => {
  const { categoryId } = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const { isLoggedIn } = useAuth();

  const [boards, setBoards] = useState([]);
  const [categories, setCategories] = useState([]);
  const [currentCategory, setCurrentCategory] = useState(null);
  const [loading, setLoading] = useState(true);
  const [pagination, setPagination] = useState({
    currentPage: 0,
    totalPages: 0,
    totalElements: 0,
    size: 10,
  });

  const page = parseInt(searchParams.get('page')) || 0;
  const size = parseInt(searchParams.get('size')) || 10;

  useEffect(() => {
    loadCategories();
  }, []);

  useEffect(() => {
    loadBoards();
  }, [categoryId, page, size]);

  const loadCategories = async () => {
    try {
      const response = await categoryAPI.getAllCategories();
      if (response.data.success) {
        setCategories(response.data.data);
      }
    } catch (error) {
      console.error('Failed to load categories:', error);
    }
  };

  const loadBoards = async () => {
    setLoading(true);
    try {
      let response;
      if (categoryId) {
        response = await boardAPI.getBoardsByCategory(categoryId, page, size);
        const category = categories.find(cat => cat.id === parseInt(categoryId));
        setCurrentCategory(category);
      } else {
        response = await boardAPI.getAllBoards(page, size);
        setCurrentCategory(null);
      }

      if (response.data.success) {
        setBoards(response.data.data.content);
        setPagination({
          currentPage: response.data.data.number,
          totalPages: response.data.data.totalPages,
          totalElements: response.data.data.totalElements,
          size: response.data.data.size,
        });
      }
    } catch (error) {
      console.error('Failed to load boards:', error);
    } finally {
      setLoading(false);
    }
  };

  const handlePageChange = (newPage) => {
    setSearchParams({ page: newPage, size });
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  if (loading) {
    return (
      <div className="board-page">
        <div className="loading">게시글을 불러오는 중...</div>
      </div>
    );
  }

  return (
    <div className="board-page">
      <div className="board-container">
        {/* 헤더 */}
        <div className="board-header">
          <div className="board-title">
            <h1>{currentCategory ? currentCategory.name : '전체 게시글'}</h1>
            {currentCategory && (
              <p className="category-description">{currentCategory.description}</p>
            )}
          </div>
          
          {isLoggedIn && (
            <Link to="/boards/create" className="btn btn-primary">
              글쓰기
            </Link>
          )}
        </div>

        {/* 카테고리 필터 */}
        <div className="category-filter">
          <Link 
            to="/boards" 
            className={`category-chip ${!categoryId ? 'active' : ''}`}
          >
            전체
          </Link>
          {categories.map(category => (
            <Link
              key={category.id}
              to={`/boards/category/${category.id}`}
              className={`category-chip ${
                categoryId === category.id.toString() ? 'active' : ''
              }`}
            >
              {category.name}
              <span className="post-count">({category.boardCount || 0})</span>
            </Link>
          ))}
        </div>

        {/* 게시글 목록 */}
        <div className="board-list">
          {boards.length === 0 ? (
            <div className="empty-state">
              <p>게시글이 없습니다.</p>
              {isLoggedIn && (
                <Link to="/boards/create" className="btn btn-outline">
                  첫 번째 글을 작성해보세요
                </Link>
              )}
            </div>
          ) : (
            <div className="board-table">
              <div className="board-table-header">
                <div className="col-no">번호</div>
                <div className="col-category">카테고리</div>
                <div className="col-title">제목</div>
                <div className="col-author">작성자</div>
                <div className="col-date">작성일</div>
                <div className="col-views">조회</div>
              </div>
              
              {boards.map((board, index) => (
                <div key={board.id} className="board-table-row">
                  <div className="col-no">
                    {pagination.totalElements - (pagination.currentPage * pagination.size) - index}
                  </div>
                  <div className="col-category">
                    <span className={`category-badge ${board.isAdminPost ? 'admin' : ''}`}>
                      {board.categoryName}
                    </span>
                  </div>
                  <div className="col-title">
                    <Link to={`/boards/${board.id}`} className="board-title-link">
                      {board.title}
                      {board.isAdminPost && (
                        <span className="admin-badge">관리자</span>
                      )}
                    </Link>
                  </div>
                  <div className="col-author">{board.author}</div>
                  <div className="col-date">{formatDate(board.createdAt)}</div>
                  <div className="col-views">{board.viewCount}</div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* 페이지네이션 */}
        {pagination.totalPages > 1 && (
          <div className="pagination">
            <button
              onClick={() => handlePageChange(0)}
              disabled={pagination.currentPage === 0}
              className="page-btn"
            >
              ««
            </button>
            <button
              onClick={() => handlePageChange(pagination.currentPage - 1)}
              disabled={pagination.currentPage === 0}
              className="page-btn"
            >
              ‹
            </button>
            
            {Array.from({ length: pagination.totalPages }, (_, i) => (
              <button
                key={i}
                onClick={() => handlePageChange(i)}
                className={`page-btn ${pagination.currentPage === i ? 'active' : ''}`}
              >
                {i + 1}
              </button>
            ))}
            
            <button
              onClick={() => handlePageChange(pagination.currentPage + 1)}
              disabled={pagination.currentPage === pagination.totalPages - 1}
              className="page-btn"
            >
              ›
            </button>
            <button
              onClick={() => handlePageChange(pagination.totalPages - 1)}
              disabled={pagination.currentPage === pagination.totalPages - 1}
              className="page-btn"
            >
              »»
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default BoardList;
