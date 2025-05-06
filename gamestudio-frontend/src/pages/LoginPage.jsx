import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../assets/css/LoginPage.css';
import { User } from 'lucide-react';

const LoginPage = () => {
    const [nickname, setNickname] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { setAuthenticated } = useAuth();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:9090/api/player/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ nickname, password }),
                credentials: 'include',
            });

            const success = await response.json();
            if (response.ok && success) {
                localStorage.setItem('nickname', nickname);
                setAuthenticated(true);
                navigate('/');
            } else {
                setError('Invalid username or password');
            }
        } catch (err) {
            setError('An error occurred. Please try again.');
        }
    };

    return (
        <div className="login-page">
            <div className="login-container">
                <h2 className="flex items-center justify-center gap-2">
                    <User className="w-6 h-6" /> Login
                </h2>
                {error && <p className="error-message">{error}</p>}
                <form onSubmit={handleLogin}>
                    <div className="form-group">
                        <label htmlFor="nickname">Username</label>
                        <input
                            type="text"
                            id="nickname"
                            value={nickname}
                            onChange={(e) => setNickname(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="submit-btn">
                        Login
                    </button>
                </form>
                <p className="link-text">
                    Don't have an account?{' '}
                    <a href="/register">Register</a>
                </p>
            </div>
        </div>
    );
};

export default LoginPage;