import { assignMemberToTask, changePassword, getTeamsForMember, getAssignedTasks, massAssignMemberToTask } from '../../api/teamMemberApi';

const BASE_URL = "http://localhost:8080/api/members/actions";

beforeEach(() => {
    fetch.resetMocks();
});

describe('Team Member API', () => {
    //test: assigning a memebr to a task
    test('assignMemberToTask should return task assignment data on sucess', async () => {
        const mockResponse = {
            taskId: 2,
            teamMemberId: 7,
            assigned: true
        };

        fetch.mockResponseOnce(JSON.stringify(mockResponse), { status: 200 });

        const result = await assignMemberToTask(2, 7);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/2/assign/7`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" }
        });

        expect(result).toEqual(mockResponse);
    });

    //test: assigning multiple members to a task
    test('massAssignMemberToTask should return task assignment data on success', async () => {
        const mockResponse = [
            { isAssignedId: 1, taskId: 2, teamMemberId: 1, teamId: 42 },
            { isAssignedId: 2, taskId: 2, teamMemberId: 3, teamId: 42 },
            { isAssignedId: 3, taskId: 2, teamMemberId: 4, teamId: 42 }
        ];

        const teamMemberIds = [1, 3, 4];

        fetch.mockResponseOnce(JSON.stringify(mockResponse), { status: 200 });

        const result = await massAssignMemberToTask(2, teamMemberIds);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/2/mass-assign`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(teamMemberIds)
        });

        expect(result).toEqual(mockResponse);
    });

    //test: changing a password
    test('chanegPassword should return success on valid password change', async () => {
        const mockResponse = true;

        fetch.mockResponseOnce(JSON.stringify(mockResponse), { status: 200 });

        const result = await changePassword(5, "oldPassword", "newPassword");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/5/change-password`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({oldPassword: "oldPassword", newPassword: "newPassword"})
        });

        expect(result).toEqual(mockResponse);
    });

    

    //test: getting all teams for a team member
    test('getTeamsForMember should return json body on success', async () => {
        const mockResponse = { teams: ["Team 1", "Team 2"] };
        const teamMemberId = 1;

        fetch.mockResponseOnce(JSON.stringify(mockResponse), { status: 200 });

        const result = await getTeamsForMember(teamMemberId);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/${teamMemberId}/teams`, {
            method: 'GET'
        });

        expect(result).toEqual(mockResponse);
    });

    //test: getting all assigned tasks to a team member
    test('getAssignedTasks should return json body on success', async () => {
        const mockResponse = {tasks: ["Task 1", "Task 2"]};
        const teamMemberId = 1;

        fetch.mockResponseOnce(JSON.stringify(mockResponse), { status: 200 });

        const result = await getAssignedTasks(teamMemberId);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/${teamMemberId}/tasks`, {
            method: 'GET'
        });

        expect(result).toEqual(mockResponse);
    });
});