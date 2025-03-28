const BASE_URL = "http://localhost:8080/api/admins";

//Create admin
export const createAdmin = async (name, email, password) => {
    try {
        console.log(JSON.stringify({ name, email, password }))
        const response = await fetch(`${BASE_URL}`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, email, password })
        });

        if (!response.ok) {
            console.error(`Failed to create admin: ${response.status} ${response.statusText}`);
            throw Error ("FAILED TO CREATE ADMIN")
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
    console.log(`${BASE_URL}/${adminId}`)
    try {
        const response = await fetch(`${BASE_URL}/${adminId}`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            console.error(`Failed to delete admin: ${response.status} ${response.statusText}`);
            throw Error(response.body)
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
        const response = await fetch(`${BASE_URL}/${adminId}/name`, {
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
        const response = await fetch(`${BASE_URL}/${adminId}/email`, {
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