import { createTask, deleteTask, editTask, assignMemberToTask, changePassword } from '../../api/teamMemberApi';

const BASE_URL = "http://localhost:8080/api/tasks";

beforeEach(() => {
    fetch.resetMocks();
});

describe('Team Member API', () => {
    //test: creating a task
    test('createTask should return task data on sucess', async () => {
        const mockTask = {
            id: 1,
            title: "New Task",
            description: "Task description",
            isLocked: false,
            status: "To-Do",
            teamId: 2
        };

        fetch.mockResponseOnce(JSON.stringify(mockTask), {status: 201});

        const result = await createTask("New Task", "Task description", false, "To-Do", 2);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                title: "New Task",
                description: "Task description",
                isLocked: false,
                status: "To-Do",
                teamId: 2
            })
        });

        expect(result).toEqual(mockTask);
    });

    //test: deleting a task
    test('deleteTask should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 204 });

        const result = await deleteTask(1);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1`, {
            method: 'DELETE'
        });

        expect(result).toBe(true);
    });

    //test: editing a task
    test('editTask should return updated task data on success', async () => {
        const updatedTask = {
            id: 1,
            title: "Updated task",
            description: "Updated description",
            isLocked: true,
            status: "In progress",
            dueDate: "2025-03-01"
        };

        fetch.mockResponseOnce(JSON.stringify(updatedTask), { status: 200 });

        const result = await editTask(1, "Updated Task", "Updated description", true, "In progress", "2025-03-01");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                title: "Updated Task",
                description: "Updated description",
                isLocked: true,
                status: "In progress",
                dueDate: "2025-03-01"
            })
        });

        expect(result).toEqual(updatedTask);
    });

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

    //test: changing a password
    test('chanegPassword should return success on valid password change', async () => {
        const mockResponse = { sucess: true };

        fetch.mockResponseOnce(JSON.stringify(mockResponse), { status: 200 });

        const result = await changePassword(5, "oldPassword", "newPassword");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/team-members/5/change-password`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({oldPassword: "oldPassword", newPassword: "newPassword"})
        });

        expect(result).toEqual(mockResponse);
    });
});