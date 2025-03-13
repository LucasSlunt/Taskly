const BASE_URL = "http://localhost:8080/api/admin";

//Create admin
export const createAdmin = async (name, email, password) => {
    try {
        const response = await fetch(`${BASE_URL}`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, email, password })
        });

        if (!response.ok) {
            console.error(`Failed to create admin: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    } 
    catch (error) {
        console.error("Error creating admin: ", error);
        throw error;
    }
};

//Delete admin
export const deleteAdmin = async (adminId) => {
    try {
        const response = await fetch(`${BASE_URL}/${adminId}`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            console.error(`Failed to delete admin: ${response.status} ${response.statusText}`);
        }

        return true;
    }
    catch (error) {
        console.error("Error deleting admin: ", error);
        throw error;
    }
};

//Change admin name
export const modifyAdminName = async (adminId, newName) => {
    try {
        const response = await fetch(`${BASE_URL}/${adminId}/update-name`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({newName})
        });

        if (!response.ok) {
            console.error(`Failed to modify admin name: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error modifying admin name: ", error);
        throw error;
    }
};

//Change admin email
export const modifyAdminEmail = async (adminId, newEmail) => {
    try {
        const response = await fetch(`${BASE_URL}/${adminId}/update-email`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ newEmail })
        });

        if (!response.ok) {
            console.error(`Failed to modify admin email: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error modifying admin email: ", error);
        throw error;
    }
};

//Create team member
export const createTeamMember = async (name, email, password) => {
    try {
        const response = await fetch(`${BASE_URL}/team-member`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, email, password })
        });

        if (!response.ok) {
            console.error(`Failed to create team member: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    } 
    catch (error) {
        console.error("Error creating team member: ", error);
        throw error;
    }
};

//Change team member name
export const modifyTeamMemberName = async (teamMemberId, newName) => {
    try {
        const response = await fetch(`${BASE_URL}/team-member/${teamMemberId}/update-name`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({newName})
        });

        if (!response.ok) {
            console.error(`Failed to modify team member name: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error modifying team member name: ", error);
        throw error;
    }
};

//Change team member email
export const modifyTeamMemberEmail = async (teamMemberId, newEmail) => {
    try {
        const response = await fetch(`${BASE_URL}/team-member/${teamMemberId}/update-email`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ newEmail })
        });

        if (!response.ok) {
            console.error(`Failed to modify team member email: ${response.status} ${response.statusText}`);
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error modifying team member email: ", error);
        throw error;
    }
};

//Delete team member
export const deleteTeamMember = async (teamMemberId) => {
    try {
        const response = await fetch(`${BASE_URL}/team-member/${teamMemberId}`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            console.error(`Failed to delete team member: ${response.status} ${response.statusText}`);
        }

        return true;
    }
    catch (error) {
        console.error("Error deleting team member: ", error);
        throw error;
    }
};

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

//Promote team member to admin
export const promoteTeamMemberToAdmin = async (teamMemberId) => {
    try {
        const response = await fetch(`${BASE_URL}/team-member/${teamMemberId}/promote`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" }
        });

        if (!response.ok) {
            console.error(`Failed to promote team member to admin: ${response.status} ${response.statusText}`);
        }

        return true;
    }
    catch (error) {
        console.error(`Failed to promote team member to admin: `, error);
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
        
    }
    catch (error) {
        console.error(`Failed to retrieve all admins: `, error);
        throw error;
    } 
};
