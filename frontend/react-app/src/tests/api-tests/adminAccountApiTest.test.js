import { createAdmin, deleteAdmin, modifyAdminName, modifyAdminEmail, getAdminById } from '../../api/adminAccountApi';

const BASE_URL = "http://localhost:8080/api/admins";

beforeEach(() => {
    fetchMock.resetMocks();
});


describe('Admin Account API', () => {

    //test: creating an admin
    test('createAdmin should return admin data on success', async () => {
        const mockAdmin = {
            name: "Test Admin",
            email: "test_admin@example.com",
            password: "admin_password"
        };

        fetch.mockResponseOnce(JSON.stringify(mockAdmin), { status: 200 });

        const result = await createAdmin("Test Admin", "test_admin@example.com", "admin_password");
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                name: "Test Admin",
                email: "test_admin@example.com",
                password: "admin_password"
            })
        });

        expect(result).toEqual(mockAdmin);
    });

    //test: deleting an admin
    test('deleteAdmin should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 204 });

        const result = await deleteAdmin(1);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1`, {
            method: 'DELETE'
        });

        expect(result).toBe(true);
    });

    //test: modifying admin name
    test('modifyAdminName should return new admin data on success', async () => {
        const updatedAdmin = {
            adminId: 1,
            newName: "New AdminName"
        };

        fetch.mockResponseOnce(JSON.stringify(updatedAdmin), { status: 200 });

        const result = await modifyAdminName(1, "New AdminName");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/name`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                newName: "New AdminName"
            })
        });

        expect(result).toEqual(updatedAdmin);
    });

    //test: modifying admin email
    test('modifyAdminEmail should return new admin data on success', async () => {
        const updatedAdmin = {
            adminId: 1,
            newEmail: "new_adminEmail@example.com"
        };

        fetch.mockResponseOnce(JSON.stringify(updatedAdmin), { status: 200 });

        const result = await modifyAdminEmail(1, "new_adminEmail@example.com");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/email`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                newEmail: "new_adminEmail@example.com"
            })
        });

        expect(result).toEqual(updatedAdmin);
    });

    //test: retrieving admin info with their ID
    test('getAdminById should return admin data on success', async () => {
        const mockAdmin = {
            accountId: 1,
            userName: "Admin User",
            userEmail: "admin@example.com"
        };

        fetch.mockResponseOnce(JSON.stringify(mockAdmin), { status: 200 });

        const result = await getAdminById(1);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1`, { method: 'GET' });
        expect(result).toEqual(mockAdmin);
    });
});