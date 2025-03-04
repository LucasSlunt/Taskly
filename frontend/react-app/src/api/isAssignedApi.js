const BASE_URL = "http://localhost:8080/api/assignments";

//Assign a team member to a task
export const assignTeamMemberToTask = async (teamMemberId, taskId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/task/${taskId}`, {
            method: 'POST'
        });

        if (!response.ok) {
            console.error(`Failed to assign team member to task: ${response.status} ${response.statusText}`);
            return false;
        }

        return true;
    }
    catch (error) {
        console.error("Error to assign team member to task: ", error);
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
            console.error(`Failed to unassign team member to task: ${response.status} ${response.statusText}`);
            return false;
        }

        return true;
    }
    catch (error) {
        console.error("Error to unassign team member to task: ", error);
        return false;
    }
};

//Check if team member is assigned to task
export const checkIfAssigned = async (teamMemberId, taskId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/task/${taskId}`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to check if team member is assigned to the task: ${response.status} ${response.statusText}`);
            return false;
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error when checking if team member is assigned to the task: ", error);
        return false;
    }
};