import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../assets/css/LevelsPage.css";
import { Trophy, Check, AlertTriangle, LogOut } from "lucide-react";

const LevelsPage = () => {
    const navigate = useNavigate();
    const levels = [
        { name: "EASY", rows: 5, cols: 5, description: "Beginner-friendly, small board.", icon: <Trophy className="w-8 h-8 text-primary" /> },
        { name: "MEDIUM", rows: 7, cols: 7, description: "More challenging, larger board.", icon: <Check className="w-8 h-8 text-primary" /> },
        { name: "HARD", rows: 10, cols: 10, description: "Big board, requires strategy.", icon: <AlertTriangle className="w-8 h-8 text-primary" /> },
        { name: "EXIT", rows: -1, cols: -1, description: "Go back if you are scared)", icon: <LogOut className="w-8 h-8 text-primary" /> },
    ];

    const [selectedLevel, setSelectedLevel] = useState(null);

    const handleLevelSelect = (level) => {
        setSelectedLevel(level);
        if (level.name === "EXIT") {
            navigate("/");
        } else {
            navigate(`/level?difficulty=${level.name}`);
        }
    };

    return (
        <div className="levels-page min-h-screen flex flex-col items-center justify-center">
            <div className="container max-w-4xl mx-auto px-6 py-12">
                <h1 className="text-5xl font-bold mb-8 text-highlight glow">Choose Your Level</h1>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {levels.map((level) => (
                        <div
                            key={level.name}
                            className={`level-card bg-section-bright rounded-lg p-6 cursor-pointer transition-all duration-300 hover:shadow-lg ${
                                selectedLevel && selectedLevel.name === level.name ? "border-4 border-highlight" : ""
                            }`}
                            onClick={() => handleLevelSelect(level)}
                        >
                            <div className="flex items-center justify-between mb-4">
                                {level.icon}
                                <span className="text-2xl font-semibold text-gray">{level.name}</span>
                            </div>
                            <p className="text-gray-500 mb-2">{level.description}</p>
                            {level.rows !== -1 && <p className="text-sm text-gray-600">Size: {level.rows}x{level.cols}</p>}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default LevelsPage;