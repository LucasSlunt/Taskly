import { addMemberToTeam, removeMemberFromTeam, checkIfAssignedToTeam } from '../../api/isMemberOfApi';

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