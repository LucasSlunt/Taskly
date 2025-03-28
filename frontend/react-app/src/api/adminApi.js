const BASE_URL = "http://localhost:8080/api/admins/actions";

//Assign team member to a team
export const assignTeamMemberToTeam = async (teamMemberId, teamId) => {
    try {
        const response = await fetch(`${BASE_URL}/team-member/${teamMemberId}/assign-to-team/${teamId}`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" }
        });

        if (!response.ok) {
            console.error(`Failed to assign team member to team: ${response.status} ${response.statusText}`);
        }

        return true;
    }
    catch (error) {
        console.error(`Failed to assign team member to team: `, error);
        throw error;
    }
};

//Changing the role of a team member or admin
export const changeRole = async (teamMemberId, newRole) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/change-role`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ role: newRole })
        });

        if (!response.ok) {
            console.error(`Failed to change the role: ${response.status} ${response.statusText}`);
            throw Error("FAILED TO CHANGE ROLE");
        }

        return true;
    }
    catch (error) {
        console.error(`Failed to change the role: `, error);
        throw error;
    }
};

//Lock a task
export const lockTask = async (taskId) => {
    try {
        const response = await fetch(`${BASE_URL}/tasks/${taskId}/lock`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" }
        });

        if (!response.ok) {
            console.error(`Failed to lock task: ${response.status} ${response.statusText}`);
        }

        return true;
    }
    catch (error) {
        console.error(`Failed to lock task: `, error);
        throw error;
    }
};

//Unlock a task
export const unlockTask = async (taskId) => {
    try {
        const response = await fetch(`${BASE_URL}/tasks/${taskId}/unlock`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" }
        });

        if (!response.ok) {
            console.error(`Failed to unlock task: ${response.status} ${response.statusText}`);
        }

        return true;
    }
    catch (error) {
        console.error(`Failed to unlock task: `, error);
        throw error;
    }
};

//Get all admins
export const getAdmins = async () => {
    try {
        const response = await fetch(`${BASE_URL}`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to retrieve admins: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    }
    catch (error) {
        console.error(`Failed to retrieve all admins: `, error);
        throw error;
    } 
};

//reset password
export const resetPassword = async (teamMemberId, newPassword) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/reset-password`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ newPassword })
        });

        if (!response.ok) {
            console.error(`Failed to reset password: ${response.status} ${response.statusText}`)
            return null;
        }

        return await true;
    }
    catch (error) {
        console.error("Error resetting password: ", error);
        throw error;
    }
};