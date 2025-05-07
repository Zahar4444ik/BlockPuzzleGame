const API_BASE_URL = 'http://localhost:9090/api/player';

export const getCurrentNickname = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/current-nickname`, { credentials: 'include' });
        if (response.ok) {
            const nickname = await response.text();
            return nickname || null;
        }
        return null;
    } catch (error) {
        console.error('Error fetching current nickname:', error);
        return null;
    }
};

export const getPlayerData = async (nickname) => {
    try {
        const response = await fetch(`${API_BASE_URL}/${nickname}`, { credentials: 'include' });
        if (response.ok) {
            return await response.json();
        }
        return null;
    } catch (error) {
        console.error('Error fetching player data:', error);
        return null;
    }
};

export const logout = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/logout`, {
            method: 'POST',
            credentials: 'include'
        });
        return response.ok;
    } catch (error) {
        console.error('Error logging out:', error);
        return false;
    }
};

export const updatePlayer = async (playerDTO) => {
    try {
        const response = await fetch(`${API_BASE_URL}/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(playerDTO),
            credentials: 'include'
        });

        if (!response.ok) {
            throw new Error(`Failed to update player: ${response.status}`);
        }
    } catch (error) {
        console.error("Error updating player:", error);
        throw error;
    }
};