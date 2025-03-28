import { createTeamMember, modifyTeamMemberEmail, modifyTeamMemberName, getTeamMemberById, deleteTeamMember} from '../../api/teamMemberAccount';

const BASE_URL = "http://localhost:8080/api/members";

beforeEach(() => {
    fetch.resetMocks();
});

describe('Team Member Account API', () => {

    //test: creating team member
    test('createTeamMember should return team member data on success', async () => {
        const mockTeamMember = {
            name: "Test TM",
            email: "test_tm@example.com",
            password: "tm_password"
        };

        fetch.mockResponseOnce(JSON.stringify(mockTeamMember), { status: 200 });

        const result = await createTeamMember("Test TM", "test_tm@example.com", "tm_password");
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}`, {
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

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/name`, {
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

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/email`, {
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

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1`, {
            method: 'DELETE'
        });

        expect(result).toBe(true);
    });

    //test: retrieving team emember info with ID
    test('getTeamMemberById should return team member data on success', async () => {
        const mockTeamMember = {
            accountId: 2,
            userName: "John Doe",
            userEmail: "john.doe@example.com"
        };

        fetch.mockResponseOnce(JSON.stringify(mockTeamMember), { status: 200 });

        const result = await getTeamMemberById(2);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/2`, { method: 'GET' });
        expect(result).toEqual(mockTeamMember);
    });
});