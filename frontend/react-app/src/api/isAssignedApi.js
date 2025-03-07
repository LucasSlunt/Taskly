const BASE_URL = "http://localhost:8080/api/assignments";

//Assign a team member to a task
export const assignTeamMemberToTask = async (teamMemberId, taskId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/task/${taskId}`, {
            method: 'POST'
        });

        if (!response.ok) {
            console.error(`Failed assigning team member to task: ${response.status} ${response.statusText}`);
            return false;
        }

        return true;
    }
    catch (error) {
        console.error("Error assigning team member to task: ", error);
        return false;
    }
};

//Unassign a team member to a task
export const unassignTeamMemberFromTask = async (teamMemberId, taskId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/task/${taskId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            console.error(`Failed unassigning team member to task: ${response.status} ${response.statusText}`);
            return false;
        }

        return true;
    }
    catch (error) {
        console.error("Error unassigning team member to task: ", error);
        return false;
    }
};

//Check if team member is assigned to task
export const checkIfAssignedToTask = async (teamMemberId, taskId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/task/${taskId}`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to check if team member is assigned to the task: ${response.status} ${response.statusText}`);
            return null;
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error checking if team member is assigned to the task: ", error);
        return null;
    }
};