import React, { useState, useEffect } from "react";

function ScoresPage() {
    const [scores, setScores] = useState([]);

    useEffect(() => {
        fetch(`http://localhost:9090/api/score/${encodeURIComponent("Block Puzzle")}`, {
            credentials: "include" // <- THIS sends session cookie
        })
            .then((response) => {
                if (!response.ok) throw new Error("Unauthorized or error fetching scores");
                return response.json();
            })
            .then((data) => setScores(data))
            .catch((error) => console.error("Error fetching scores:", error));
    }, []);

    return (
        <div>
            <h2>Top Scores</h2>
            <ul>
                {scores.map((score, index) => (
                    <li key={index}>
                        {score.player.nickname} - {score.points} pts on{" "}
                        {new Date(score.playedOn).toLocaleDateString()}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default ScoresPage;