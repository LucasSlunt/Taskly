import {assignTeamMemberToTeam, lockTask, changeRole, unlockTask, getAdmins, resetPassword } from '../../api/adminApi';

const BASE_URL = "http://localhost:8080/api/admins/actions";

beforeEach(() => {
    fetchMock.resetMocks();
});

describe('Admin Actions API', () => {
    //test: assigning a team member to a team
    test('assignTeamMemberToTeam should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 200 });

        const result = await assignTeamMemberToTeam(1, 1);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/team-member/1/assign-to-team/1`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" }
        });

        expect(result).toBe(true);
    });

    //test: changing role of team member or admin
    test('changeRole should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 200 });

        const result = await changeRole(1, "ADMIN");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/role`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({role: "ADMIN"})
        });

        expect(result).toBe(true);
    });

    //test: locking a task
    test('lockTask should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 200 });

        const result = await lockTask(1);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/tasks/1/lock`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" }
        });

        expect(result).toBe(true);
    });

    //test: unlock a task
    test('unlockTask should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 200 });

        const result = await unlockTask(1);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/tasks/1/unlock`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" }
        });

        expect(result).toBe(true);
    });

    //test: getting all admins
    test('getAdmins should return list of admins on success', async () => {
        const mockAdmins = [
            { accountId: 1, userName: "Admin One", userEmail: "admin1@example.com" },
            { accountId: 2, userName: "Admin Two", userEmail: "admin2@example.com" }
        ];

        fetch.mockResponseOnce(JSON.stringify(mockAdmins), { status: 200 });

        const result = await getAdmins();

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}`, { method: 'GET' });
        expect(result).toEqual(mockAdmins);
    });

    //test: resetting a password
    test('resetPassword should return success on valid password reset', async () => {
        const mockResponse = true;
    
        fetch.mockResponseOnce(JSON.stringify(mockResponse), { status: 200 });
    
        const result = await resetPassword(1, "DefLeppard");
    
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/reset-password`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ newPassword: "DefLeppard" })
        });
    
        expect(result).toEqual(mockResponse);
    });
});