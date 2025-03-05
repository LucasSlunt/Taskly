const BASE_URL = "http://localhost:8080/api/tasks";

export const notifyMembers = async (taskId) => {
    try {
        const response = await fetch(`${BASE_URL}/${taskId}/notify`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" }
        });

        if (!response.ok) {
            console.error(`Failed to notify members: ${response.status} ${response.statusText}`);
            return false;
        }

        return true;
    }
    catch (error) {
        console.error("Error when notifying members", error);
        return false;
    }
};
