const BASE_URL = "http://localhost:8080/api/auth-info";

export const login = async (teamMemberId, password) => {
    try {
        const response = await fetch(`${BASE_URL}/login`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({teamMemberId, password})
        });

        if (!response.ok) {
            console.error(`Login failed: ${response.status} ${response.statusText}`);
            return null;
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error during login: ", error);
        throw error;
    }
};

export const isAdmin = async (teamMemberId) => {
    try {
        const response = await fetch(`${BASE_URL}`/is-admin, {
            method: 'GET',
            
        })
    }
    catch (error) {
        console.error("Error checking member role: ", error);
        throw error;
    }
};