import { addMemberToTeam, removeMemberFromTeam, checkIfAssignedToTeam, massAssignToTeam } from '../../api/isMemberOfApi';

const BASE_URL = "http://localhost:8080/api/memberships";

beforeEach(() => {
    fetch.resetMocks();
});

describe('IsMemberOf API', () => {
    //test: adding a member to a team
    test('addMemberToTeam should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 200 });

        const result = await addMemberToTeam(1, 1);
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/team/1`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" }
        });

        expect(result).toBe(true);
    });

    //test: adding multiple members to a team
    test('massAssignToTeam should return team assignment data on sucess', async () => {
        const mockResponse = [
            { "isMemberOfId": 101, "teamMemberId": 1, "teamId": 42 },
            { "isMemberOfId": 102, "teamMemberId": 3, "teamId": 42 },
            { "isMemberOfId": 103, "teamMemberId": 4, "teamId": 42 }
        ];

        const teamMemberIds = [1, 3, 4];

        fetch.mockResponseOnce(JSON.stringify(mockResponse), { status: 200 });

        const result = await massAssignToTeam(2, teamMemberIds);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/team/2/mass-assign`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(teamMemberIds)
        });

        expect(result).toEqual(mockResponse);
    });

    //test: removing a member from a team
    test('removeMemberFromTeam should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 200 });

        const result = await removeMemberFromTeam(1, 1);
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/team/1`, {
            method: 'DELETE'
        });

        expect(result).toBe(true);
    });

    //test: checking if a member is assigned to a specific team
    test('checkIfAssignedToTeam should return null on success', async () => {
        fetch.mockResponseOnce('', { status: 200 });

        const result = await checkIfAssignedToTeam(1, 1);
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/team/1`, {
            method: 'GET'
        });

        expect(result).toBe(null);
    });
});
