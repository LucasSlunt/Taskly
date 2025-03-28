const BASE_URL = "http://localhost:8080/api/tasks";

//Create a task
export const createTask = async (title, description, isLocked, status, dueDate, teamId, priority) => {
    try {
        console.log("Sending this: ", JSON.stringify({title, description, isLocked, status, dueDate, teamId, priority}))
        const response = await fetch(`${BASE_URL}`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({title, description, isLocked, status, dueDate, teamId, priority})
        });

        if (!response.ok) {
            console.error(`Failed to create task: ${response.status} ${response.statusText}`);
            return null;
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error creating task: ", error);
        throw error;
    }
};

//Delete a task
export const deleteTask = async (taskId) => {
    try {
        const response = await fetch(`${BASE_URL}/${taskId}`, {
            method: 'DELETE'
        }); 

        if (!response.ok) {
            console.error(`Failed to delete task: ${response.status} ${response.statusText}`);
            return null;
        }

        return true;
    }
    catch (error) {
        console.error("Error deleting task: ", error);
        throw error;
    }
};

//Edit task
export const editTask = async (taskId, title, description, isLocked, status, dueDate, priority) => {
    try {
        const response = await fetch(`${BASE_URL}/${taskId}`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({title, description, isLocked, status, dueDate, priority})
        });

        if (!response.ok) {
            console.error(`Failed to edit task: ${response.status} ${response.statusText}`);
            return null;
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error editing task: ", error);
        throw error;
    }
};
