import { assignTeamMemberToTask, unassignTeamMemberFromTask, checkIfAssignedToTask } from '../../api/isAssignedApi';

const BASE_URL = "http://localhost:8080/api/assignments";

beforeEach(() => {
    fetch.resetMocks();
});

describe('IsAssigned API', () => {
    //test: assinging a team member to a task
    test('assignTeamMemberToTask should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 200 });

        const result = await assignTeamMemberToTask(1, 1);
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/task/1`, {
            method: 'POST'
        });

        expect(result).toBe(true);
    });

    //test: unassigning a team member from a task
    test('unassignTeamMemberFromTask should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 200 });

        const result = await unassignTeamMemberFromTask(1, 1);
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/task/1`, {
            method: 'DELETE'
        });

        expect(result).toBe(true);
    });

    //test: checking if a team member is assigned to a task
    test('checkIfAssignedToTask should return task data on success', async () => {
        fetch.mockResponseOnce(JSON.stringify(true), { status: 200 });

        const result = await checkIfAssignedToTask(1, 1);
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/task/1`, {
            method: 'GET'
        });

        expect(result).toEqual(true);
    });
});