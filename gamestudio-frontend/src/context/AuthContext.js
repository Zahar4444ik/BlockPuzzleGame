// src/context/AuthContext.js
import { createContext, useContext, useEffect, useState } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [authenticated, setAuthenticated] = useState(false);

    const checkAuth = async () => {
        try {
            const res = await fetch("http://localhost:9090/api/player/authenticated", {
                credentials: "include",
            });
            const isAuth = await res.json();
            setAuthenticated(isAuth);
        } catch {
            setAuthenticated(false);
        }
    };

    useEffect(() => {
        checkAuth();
    }, []);

    return (
        <AuthContext.Provider value={{ authenticated, setAuthenticated, checkAuth }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
