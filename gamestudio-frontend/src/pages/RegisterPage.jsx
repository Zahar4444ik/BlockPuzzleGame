import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../assets/css/RegisterPage.css';
import { UserPlus } from 'lucide-react';

const RegisterPage = () => {
    const [nickname, setNickname] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        try {
            const response = await fetch('http://localhost:9090/api/player/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ nickname, password }),
            });

            if (response.ok) {
                navigate('/login');
            } else {
                const data = await response.json();
                setError(data.message || 'Registration failed');
            }
        } catch (err) {
            setError('An error occurred. Please try again.');
        }
    };

    return (
        <div className="register-page">
            <div className="register-container">
                <h2 className="flex items-center justify-center gap-2">
                    <UserPlus className="w-6 h-6" /> Register
                </h2>
                {error && <p className="error-message">{error}</p>}
                <form onSubmit={handleRegister}>
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
                    <div className="form-group">
                        <label htmlFor="confirmPassword">Confirm Password</label>
                        <input
                            type="password"
                            id="confirmPassword"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="submit-btn">
                        Register
                    </button>
                </form>
                <p className="link-text">
                    Already have an account?{' '}
                    <a href="/login">Login</a>
                </p>
            </div>
        </div>
    );
};

export default RegisterPage;