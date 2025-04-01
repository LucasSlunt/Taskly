const BASE_URL = "http://localhost:8080/api/members";

//Create team member
export const createTeamMember = async (name, email, password) => {
    try {
        const response = await fetch(`${BASE_URL}`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, email, password })
        });

        if (!response.ok) {
            console.error(`Failed to create team member: ${response.status} ${response.statusText}`);
            throw Error ("FAILED TO CREATE Team Member")
        }

        return await response.json();
    } 
    catch (error) {
        console.error("Error creating team member: ", error);
        throw error;
    }
};


//Delete team member
export const deleteTeamMember = async (teamMemberId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}`, {
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


//Change team member name
export const modifyTeamMemberName = async (teamMemberId, newName) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/name`, {
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
        const response = await fetch(`${BASE_URL}/${teamMemberId}/email`, {
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

//Get a team member with their ID
export const getTeamMemberById = async (teamMemberId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}`, {
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

//Get all team ememebrs
export const getTeamMembers = async () => {
    try {
        const response = await fetch(`${BASE_URL}`, {
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
