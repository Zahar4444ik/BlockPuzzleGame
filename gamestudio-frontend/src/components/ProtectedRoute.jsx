import React, { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(null);

    useEffect(() => {
        fetch("http://localhost:9090/api/player/authenticated", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
        })
            .then((res) => {
                console.log("Authenticated response status:", res.status);
                if (!res.ok) {
                    throw new Error(`HTTP error! Status: ${res.status}`);
                }
                return res.json();
            })
            .then((data) => {
                console.log("Authenticated response data:", data);
                setIsAuthenticated(data === true);
            })
            .catch((err) => {
                console.error("Error checking authentication:", err);
                setIsAuthenticated(false);
            });
    }, []);

    if (isAuthenticated === null) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="text-gray-500">Loading...</div>
            </div>
        );
    }

    console.log("Is authenticated:", isAuthenticated);
    return isAuthenticated ? children : <Navigate to="/login" />;
};

export default ProtectedRoute;