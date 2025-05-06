import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { Home } from "lucide-react";
import { useAuth } from "../context/AuthContext";
import "../assets/css/Navbar.css";

const Navbar = () => {
    const { authenticated, setAuthenticated } = useAuth();
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            const res = await fetch("http://localhost:9090/api/player/logout", {
                method: "POST",
                credentials: "include",
            });
            if (res.ok) {
                setAuthenticated(false);
                navigate("/");
            }
        } catch {
            setAuthenticated(false);
            navigate("/");
        }
    };

    return (
        <nav>
            <Link to="/" className="title">
                <Home className="w-5 h-5" /> Block Puzzle
            </Link>
            <div className="nav-buttons">
                {authenticated ? (
                    <button
                        onClick={handleLogout}
                        className="logout-btn"
                    >
                        Logout
                    </button>
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
        </nav>
    );
};

export default Navbar;