import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth.jsx';
import './Navbar.css';

const Navbar = () => {
  const { user, isLoggedIn, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    const result = await logout();
    if (result.success) {
      navigate('/');
    }
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        {/* 로고/홈 링크 */}
        <Link to="/" className="navbar-brand">
          cokothon
        </Link>

        {/* 네비게이션 메뉴 */}
        <div className="navbar-menu">
          <div className="navbar-nav">
            {/* 게시판 드롭다운 */}
            <div className="dropdown">
              <button className="dropdown-toggle">
                게시판
                <span className="dropdown-arrow">▼</span>
              </button>
              <div className="dropdown-menu">
                <Link to="/boards" className="dropdown-item">전체 게시글</Link>
                <Link to="/boards/category/1" className="dropdown-item">자유게시판</Link>
                <Link to="/boards/category/2" className="dropdown-item">질문게시판</Link>
                <Link to="/boards/category/3" className="dropdown-item">공지사항</Link>
                <div className="dropdown-divider"></div>
                <Link to="/boards/create" className="dropdown-item">글쓰기</Link>
              </div>
            </div>

            {/* 설문조사 링크 (로그인한 사용자만) */}
            {isLoggedIn && (
              <Link to="/survey" className="nav-link">
                설문조사
              </Link>
            )}

            {/* 관리자 메뉴 (관리자만) */}
            {isLoggedIn && user?.isAdmin && (
              <div className="dropdown">
                <button className="dropdown-toggle admin-menu">
                  관리자
                  <span className="dropdown-arrow">▼</span>
                </button>
                <div className="dropdown-menu">
                  <Link to="/admin/surveys" className="dropdown-item">설문조사 관리</Link>
                  <Link to="/admin/statistics" className="dropdown-item">통계</Link>
                  <Link to="/admin/users" className="dropdown-item">사용자 관리</Link>
                </div>
              </div>
            )}
          </div>

          {/* 사용자 메뉴 */}
          <div className="navbar-user">
            {isLoggedIn ? (
              <div className="user-menu">
                <div className="dropdown">
                  <button className="dropdown-toggle user-info">
                    <span className="user-name">{user?.name}</span>
                    {user?.isAdmin && <span className="admin-badge">관리자</span>}
                    <span className="dropdown-arrow">▼</span>
                  </button>
                  <div className="dropdown-menu dropdown-menu-right">
                    <Link to="/profile" className="dropdown-item">프로필</Link>
                    <Link to="/my-posts" className="dropdown-item">내 게시글</Link>
                    <div className="dropdown-divider"></div>
                    <button onClick={handleLogout} className="dropdown-item logout-btn">
                      로그아웃
                    </button>
                  </div>
                </div>
              </div>
            ) : (
              <div className="auth-buttons">
                <Link to="/login" className="btn btn-outline">로그인</Link>
                <Link to="/register" className="btn btn-primary">회원가입</Link>
              </div>
            )}
          </div>
        </div>

        {/* 모바일 메뉴 토글 버튼 */}
        <div className="navbar-toggle">
          <span></span>
          <span></span>
          <span></span>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
