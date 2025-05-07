const API_BASE_URL = 'http://localhost:9090/api/comment';

export const getComments = async (game) => {
    try {
        const response = await fetch(`${API_BASE_URL}/${game}`, { credentials: 'include' });
        if (response.ok) {
            return await response.json();
        }
        return [];
    } catch (error) {
        console.error(`Error fetching comments for game ${game}:`, error);
        return [];
    }
};

export const addComment = async (commentDTO) => {
    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(commentDTO),
            credentials: 'include'
        });
        if (!response.ok) {
            throw new Error('Failed to add comment');
        }
    } catch (error) {
        console.error('Error adding comment:', error);
        throw error;
    }
};