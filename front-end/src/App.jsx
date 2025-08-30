import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './hooks/useAuth.jsx';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import BoardList from './pages/BoardList';
import BoardWrite from './pages/BoardWrite';
import Survey from './pages/Survey';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="app">
          <Navbar />
          <main className="main-content">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/boards" element={<BoardList />} />
              <Route path="/boards/category/:categoryId" element={<BoardList />} />
              <Route path="/boards/create" element={<BoardWrite />} />
              <Route path="/survey" element={<Survey />} />
              {/* TODO: 추가 라우트들 */}
              {/* <Route path="/boards/:id" element={<BoardDetail />} /> */}
              {/* <Route path="/admin/*" element={<AdminRoutes />} /> */}
            </Routes>
          </main>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;