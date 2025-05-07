import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import ProtectedRoute from "./components/ProtectedRoute";
import Layout from "./components/Layout";
import { AuthProvider } from "./context/AuthContext";
import LevelsPage from "./pages/LevelsPage";
import LevelPage from "./pages/LevelPage";
import {PlayerStatsProvider} from "./context/PlayerStatsContext";

function App() {

    return (
        <AuthProvider>
            <PlayerStatsProvider>
                <Router>
                    <Routes>
                        <Route element={<Layout />}>
                            <Route path="/" element={<HomePage />} />
                            <Route path="/login" element={<LoginPage />} />
                            <Route path="/register" element={<RegisterPage />} />
                            <Route
                                path="/levels"
                                element={
                                    <ProtectedRoute>
                                        <LevelsPage />
                                    </ProtectedRoute>
                                }
                            />
                            <Route
                                path="/level"
                                element={
                                    <ProtectedRoute>
                                        <LevelPage />
                                    </ProtectedRoute>
                                }
                            />
                        </Route>
                    </Routes>
                </Router>
            </PlayerStatsProvider>
        </AuthProvider>
    );
}

export default App;
