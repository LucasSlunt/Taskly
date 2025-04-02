const BASE_URL = "http://localhost:8080/api/memberships";

//Add a member to a team
export const addMemberToTeam = async (teamMemberId, teamId) => {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/team/${teamId}`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" }
        });

        if (!response.ok) {
            throw Error(`Failed to add member to team: ${response.status} ${response.statusText}`);
        }

        return true;
};

//Adding multiple members to a team at once
export const massAssignToTeam = async (teamId, teamMemberIds) => {
    try {
        console.log(JSON.stringify(teamMemberIds));
        const response = await fetch(`${BASE_URL}/team/${teamId}/mass-assign`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(teamMemberIds)
        });

        if (!response.ok) {
            console.error(`Failed to assign members to team: ${response.status} ${response.statusText}`);
            return null;
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error assigning members to team: ", error);
        throw error;
    }
};

//Remove a member from a team
export const removeMemberFromTeam = async (teamMemberId, teamId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/team/${teamId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            console.error(`Failed to remove member from team: ${response.status} ${response.statusText}`);
            return false;
        }

        return true;
    } 
    catch (error) {
        console.error("Error removing member from team: ", error);
        return false;
    }
};

//Check if member is assigned to team
export const checkIfAssignedToTeam = async (teamMemberId, teamId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/team/${teamId}`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to check if member is assigned to team: ${response.status} ${response.statusText}`);
            return null;
        }

        const text = await response.text();
        return text ? JSON.parse(text) : null;
    }
    catch (error) {
        console.error("Error checking if member is assigned to team: ", error);
        return null;
    }
};