import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import ProtectedRoute from "./components/ProtectedRoute";
import Layout from "./components/Layout";
import { AuthProvider } from "./context/AuthContext";
import LevelsPage from "./pages/LevelsPage";
import EasyLevelPage from "./pages/levels/EasyLevelPage";

function App() {

    return (
        <AuthProvider>
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
                            path="/level-easy"
                            element={
                                <ProtectedRoute>
                                    <EasyLevelPage />
                                </ProtectedRoute>
                            }
                        />
                    </Route>
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
