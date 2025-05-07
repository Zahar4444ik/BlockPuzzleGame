import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Home } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { getCurrentNickname, getPlayerData, logout } from '../services/PlayerService'; // Adjust path as needed
import '../assets/css/Navbar.css';

const Navbar = () => {
    const { authenticated, setAuthenticated } = useAuth();
    const navigate = useNavigate();
    const [nickname, setNickname] = useState(null);
    const [stats, setStats] = useState({ score: 0, gamesPlayed: 0 });
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    useEffect(() => {
        const fetchPlayerData = async () => {
            if (authenticated) {
                const fetchedNickname = await getCurrentNickname();
                if (fetchedNickname) {
                    setNickname(fetchedNickname);
                    const playerData = await getPlayerData(fetchedNickname);
                    if (playerData) {
                        setStats({
                            score: playerData.score || 0,
                            gamesPlayed: playerData.gamesPlayed || 0
                        });
                    }
                }
            } else {
                setNickname(null);
                setStats({ score: 0, gamesPlayed: 0 });
            }
        };
        fetchPlayerData();
    }, [authenticated]);

    const handleLogout = async () => {
        const success = await logout();
        if (success) {
            setAuthenticated(false);
            navigate('/');
        }
    };

    const toggleDropdown = () => setIsDropdownOpen(!isDropdownOpen);

    return (
        <nav>
            <Link to="/" className="title">
                <Home className="w-5 h-5" /> Block Puzzle
            </Link>
            <div className="nav-buttons">
                {authenticated && nickname ? (
                    <div className="navbar-user">
                        <span className="navbar-nickname" onClick={toggleDropdown}>
                            {nickname}
                        </span>
                        {isDropdownOpen && (
                            <div className="dropdown-menu">
                                <div className="dropdown-item">Score: {stats.score}</div>
                                <div className="dropdown-item">Games Played: {stats.gamesPlayed}</div>
                                <div className="dropdown-item logout" onClick={handleLogout}>
                                    Logout
                                </div>
                            </div>
                        )}
                    </div>
                ) : (
                    <>
                        <Link to="/login" className="login-btn">
                            Login
                        </Link>
                        <Link to="/register" className="register-btn">
                            Register
                        </Link>
                    </>
                )}
            </div>
            <style jsx>{`
                .navbar-user {
                    position: relative;
                }
                .navbar-nickname {
                    background: var(--bg-section);
                    color: var(--color-primary);
                    padding: 0.5rem 1.5rem;
                    border: 2px solid var(--color-primary);
                    border-radius: 0.5rem;
                    font-weight: 600;
                    box-shadow: 0 0 8px var(--color-primary);
                    cursor: pointer;
                    transition: all 0.3s ease;
                }
                .navbar-nickname:hover {
                    background: #2d3753;
                    transform: scale(1.05);
                    box-shadow: 0 0 12px var(--color-primary);
                }
                .dropdown-menu {
                    position: absolute;
                    top: 100%;
                    right: 0;
                    background: var(--bg-section);
                    border: 2px solid var(--color-primary);
                    border-radius: 0.5rem;
                    min-width: 180px;
                    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
                    z-index: 1000;
                }
                .dropdown-item {
                    padding: 0.75rem 1rem;
                    color: var(--color-primary);
                    cursor: pointer;
                    transition: all 0.3s ease;
                }
                .dropdown-item:hover {
                    background: #2d3753;
                    transform: scale(1.05);
                    box-shadow: 0 0 12px var(--color-primary);
                }
                .dropdown-item.logout {
                    border-top: 2px solid var(--color-primary);
                    color: #FF5555;
                }
                .dropdown-item.logout:hover {
                    background: #FF5555;
                    color: #FFFFFF;
                }
            `}</style>
        </nav>
    );
};

export default Navbar;