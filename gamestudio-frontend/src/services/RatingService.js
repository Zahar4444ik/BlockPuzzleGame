const API_BASE_URL = 'http://localhost:9090/api/rating';

export const getRating = async (game, player) => {
    try {
        const response = await fetch(`${API_BASE_URL}/${game}/${player}`, { credentials: 'include' });
        if (response.ok) {
            return await response.json();
        }
        return 0;
    } catch (error) {
        console.error(`Error fetching rating for game ${game} and player ${player}:`, error);
        return 0;
    }
};

export const getAverageRating = async (game) => {
    try {
        const response = await fetch(`${API_BASE_URL}/${game}`, { credentials: 'include' });
        if (response.ok) {
            return await response.json();
        }
        return 0;
    } catch (error) {
        console.error(`Error fetching average rating for game ${game}:`, error);
        return 0;
    }
};

export const setAndAddRating = async (ratingDTO) => {
    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(ratingDTO),
            credentials: 'include'
        });
        if (!response.ok) {
            throw new Error('Failed to set/add rating');
        }
    } catch (error) {
        console.error('Error setting/adding rating:', error);
        throw error;
    }
};