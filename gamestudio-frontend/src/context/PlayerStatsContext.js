import React, { createContext, useContext, useState } from 'react';

const PlayerStatsContext = createContext();

export const usePlayerStats = () => useContext(PlayerStatsContext);

export const PlayerStatsProvider = ({ children }) => {
    const [stats, setStats] = useState({ score: 0, gamesPlayed: 0 });

    return (
        <PlayerStatsContext.Provider value={{ stats, setStats }}>
            {children}
        </PlayerStatsContext.Provider>
    );
};