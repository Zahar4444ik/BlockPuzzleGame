const API_BASE_URL = 'http://localhost:9090/api/score';

export const getTopScores = async (game) => {
    try {
        const response = await fetch(`${API_BASE_URL}/${game}`, { credentials: 'include' });
        if (response.ok) {
            return await response.json();
        }
        return [];
    } catch (error) {
        console.error(`Error fetching top scores for game ${game}:`, error);
        return [];
    }
};

export const addAndSetScore = async (scoreDTO) => {
    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(scoreDTO),
            credentials: 'include'
        });
        if (!response.ok) {
            throw new Error('Failed to add/set score');
        }
    } catch (error) {
        console.error('Error adding/setting score:', error);
        throw error;
    }
};