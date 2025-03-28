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
        const response = await fetch(`${BASE_URL}/team-member/${teamMemberId}/change-role`, {
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
        const response = await fetch(`${BASE_URL}/admins`, {
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

//Get all team ememebrs
export const getTeamMembers = async () => {
    try {
        const response = await fetch(`${BASE_URL}/team-members`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to retrieve team members: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    }
    catch (error) {
        console.error(`Failed to retrieve all team members: `, error);
        throw error;
    } 
};

//Get all teams
export const getTeams = async () => {
    try {
        const response = await fetch(`${BASE_URL}/all-teams`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to retrieve teams: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    }
    catch (error) {
        console.error(`Failed to retrieve all teams: `, error);
        throw error;
    } 
};

//Get an admin with their ID
export const getAdminById = async (adminId) => {
    try {
        const response = await fetch(`${BASE_URL}/${adminId}`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to retrieve admin: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    }
    catch (error) {
        console.error(`Failed to retrieve admin: `, error);
        throw error;
    } 
};

//Get a team member with their ID
export const getTeamMemberById = async (teamMemberId) => {
    try {
        const response = await fetch(`${BASE_URL}/team-member/${teamMemberId}`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to retrieve team member: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    }
    catch (error) {
        console.error(`Failed to retrieve team member: `, error);
        throw error;
    } 
};