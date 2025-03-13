import { createAdmin, deleteAdmin, modifyAdminName, createTeamMember, deleteTeamMember, modifyTeamMemberName, modifyTeamMemberEmail, modifyAdminEmail, assignTeamMemberToTeam, lockTask, promoteTeamMemberToAdmin, unlockTask, getTeamMembers, getAdmins, getTeams } from '../../api/adminApi';

const BASE_URL = "http://localhost:8080/api/admin";

beforeEach(() => {
    fetchMock.resetMocks();
});

describe('Admin API', () => {

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

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/update-name`, {
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

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/update-email`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                newEmail: "new_adminEmail@example.com"
            })
        });

        expect(result).toEqual(updatedAdmin);
    });

    //test: creating team member
    test('createTeamMember should return team member data on success', async () => {
        const mockTeamMember = {
            name: "Test TM",
            email: "test_tm@example.com",
            password: "tm_password"
        };

        fetch.mockResponseOnce(JSON.stringify(mockTeamMember), { status: 200 });

        const result = await createTeamMember("Test TM", "test_tm@example.com", "tm_password");
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/team-member`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                name: "Test TM",
                email: "test_tm@example.com",
                password: "tm_password"
            })
        });

        expect(result).toEqual(mockTeamMember);
    });

    //test: modify team member name
    test('modifyTeamMemberName should return new team member data on success', async () => {
        const updatedTeamMember = {
            teamMemberId: 1,
            newName: "New TMName"
        };

        fetch.mockResponseOnce(JSON.stringify(updatedTeamMember), { status: 200 });

        const result = await modifyTeamMemberName(1, "New TMName");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/team-member/1/update-name`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                newName: "New TMName"
            })
        });

        expect(result).toEqual(updatedTeamMember);
    });

    //test: modify team member email
    test('modifyTeamMemberEmail should return new team member data on success', async () => {
        const updatedTeamMember = {
            teamMemberId: 1,
            newEmail: "new@example.com"
        };

        fetch.mockResponseOnce(JSON.stringify(updatedTeamMember), { status: 200 });

        const result = await modifyTeamMemberEmail(1, "new@example.com");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/team-member/1/update-email`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                newEmail: "new@example.com"
            })
        });

        expect(result).toEqual(updatedTeamMember);
    });

    //test: deleting a team member
    test('deleteTeamMember should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 204 });

        const result = await deleteTeamMember(1);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/team-member/1`, {
            method: 'DELETE'
        });

        expect(result).toBe(true);
    });

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

    //test: promoting a team member to admin
    test('promoteTeamMemberToAdmin should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 200 });

        const result = await promoteTeamMemberToAdmin(1, 1);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/team-member/1/promote`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" }
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
    test('getAllAdmins should return list of admins on success', async () => {
        const mockAdmins = [
            { accountId: 1, userName: "Admin One", userEmail: "admin1@example.com" },
            { accountId: 2, userName: "Admin Two", userEmail: "admin2@example.com" }
        ];
        
        fetch.mockResponseOnce(JSON.stringify(mockAdmins), { status: 200 });

        const result = await getAdmins();

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/admins`, { method: 'GET' });
        expect(result).toEqual(mockAdmins);
    });

    //test: getting all team members
    test('getAllTeamMembers should return list of team members on success', async () => {
        const mockTeamMembers = [
            { accountId: 3, userName: "TeamMember One", userEmail: "tm1@example.com" },
            { accountId: 4, userName: "TeamMember Two", userEmail: "tm2@example.com" }
        ];
        
        fetch.mockResponseOnce(JSON.stringify(mockTeamMembers), { status: 200 });

        const result = await getTeamMembers();

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/team-members`, { method: 'GET' });
        expect(result).toEqual(mockTeamMembers);
    });

    //test: getting all teams
    test('getTeams should return list of team members on success', async () => {
        const mockTeams = [
            { teamId: 1, teamName: "Team Alpha", teamLeadId: 3 },
            { teamId: 2, teamName: "Team Beta", teamLeadId: 4 }
        ];
        
        fetch.mockResponseOnce(JSON.stringify(mockTeams), { status: 200 });

        const result = await getTeams();

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/all-teams`, { method: 'GET' });
        expect(result).toEqual(mockTeams);
    });
});