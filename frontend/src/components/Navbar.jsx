import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useTheme } from '../context/ThemeContext';
import { useAuth } from '../context/AuthContext';
import { Sun, Moon, LogOut } from 'lucide-react';
import '../styles/components.css';

/**
 * Navbar Component - Navigation header with theme toggle
 */
const Navbar = () => {
  const { isDarkMode, toggleTheme } = useTheme();
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          <span>💳</span>
          QuickPe
        </Link>

        {user && (
          <ul className="navbar-menu">
            <li><Link to="/dashboard" className="navbar-link">Dashboard</Link></li>
            <li><Link to="/send-money" className="navbar-link">Send Money</Link></li>
            <li><Link to="/transactions" className="navbar-link">History</Link></li>
            <li><Link to="/profile" className="navbar-link">Profile</Link></li>
          </ul>
        )}

        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <button 
            onClick={toggleTheme}
            style={{
              background: 'none',
              border: 'none',
              cursor: 'pointer',
              fontSize: '20px',
              color: 'var(--text-primary)',
            }}
            title="Toggle Theme"
          >
            {isDarkMode ? <Sun size={24} /> : <Moon size={24} />}
          </button>

          {user && (
            <button
              onClick={handleLogout}
              className="btn btn-secondary btn-small"
              style={{ display: 'flex', alignItems: 'center', gap: '8px' }}
            >
              <LogOut size={16} /> Logout
            </button>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
