import { createTask, deleteTask, editTask, assignMemberToTask, changePassword } from '../../api/taskApi';

beforeEach(() => {
    fetch.resetMocks();
});

describe('Admin API', () => {

    //test: creating an admin
    test('createAdmin should return admin data on sucess', async () => {
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

        expect(result).toEqual(mockTask);
    });

    //test: deleting an admin
    test('deleteAdmin should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 204 });

        const result = await deleteTask(1);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1`, {
            method: 'DELETE'
        });

        expect(result).toBe(true);
    });

    //test: modifying admin name
    test('modifyAdminName should return new admin data on sucess', async () => {
        const updatedAdmin = {
            adminId: 1,
            newName: "New AdminName"
        };

        fetch.mockResponseOnce(JSON.stringify(updatedAdmin), { status: 200 });

        //not done being implemented
    });
});