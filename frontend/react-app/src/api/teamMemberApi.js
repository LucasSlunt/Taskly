const BASE_URL = "http://localhost:8080/api/members/actions";

//Assign member to task
export const assignMemberToTask = async (taskId, teamMemberId) => {
    try {
        const response = await fetch(`${BASE_URL}/${taskId}/assign/${teamMemberId}`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" }
        });

        if (!response.ok) {
            console.error(`Failed to assign member to task: ${response.status} ${response.statusText}`);
            return null;
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error assigning member to task: ", error);
        throw error;
    }
};

//Assign many members to a task
export const massAssignMemberToTask = async(taskId, teamMemberIds) => {
    try {
        console.log(JSON.stringify(teamMemberIds))
        const response = await fetch(`${BASE_URL}/${taskId}/mass-assign`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(teamMemberIds)
        });

        if (!response.ok) {
            console.error(`Failed to assign members to task: ${response.status} ${response.statusText}`);
            return null;
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error assigning member to task: ", error);
        throw error;
    }
};

//Change password
export const changePassword = async (teamMemberId, oldPassword, newPassword) => {
    try {
        console.log(JSON.stringify({oldPassword, newPassword}))
        const response = await fetch(`${BASE_URL}/${teamMemberId}/change-password`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({oldPassword, newPassword})
        });

        if (!response.ok) {
            console.error(`Failed to change password: ${response.status} ${response.statusText}`);
            return null;
        }

        return await true;
    }
    catch (error) {
        console.error("Error changing password: ", error);
        throw error;
    }
};



//getting all teams for a specific team member
export const getTeamsForMember = async (accountId) => {
    try {
        const response = await fetch(`${BASE_URL}/${accountId}/teams`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to retrieve teams: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    }
    catch (error) {
        console.error(`Failed to retrieve teams: `, error);
        throw error;
    }
};

//getting all tasks for a team member
export const getAssignedTasks = async (teamMemberId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/tasks`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to retrieve tasks: ${response.status} ${response.statusText}`);    
        }

        return await response.json();
    }
    catch (error) {
        console.error(`Failed to retrieve tasks: `, error);
        throw error;
    }
};