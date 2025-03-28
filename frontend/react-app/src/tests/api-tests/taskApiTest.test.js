import { createTask, deleteTask, editTask} from '../../api/taskApi';

const BASE_URL = "http://localhost:8080/api/tasks";

beforeEach(() => {
    fetch.resetMocks();
});

describe('Task API', () => {
    //test: creating a task
    test('createTask should return task data on sucess', async () => {
        const mockTask = {
            id: 1,
            title: "New Task",
            description: "Task description",
            isLocked: false,
            status: "To-Do",
            dueDate: "4-14-2025",
            teamId: 2
        };

        fetch.mockResponseOnce(JSON.stringify(mockTask), {status: 201});

        const result = await createTask("New Task", "Task description", false, "To-Do", "4-14-2025", 2, "LOW");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                title: "New Task",
                description: "Task description",
                isLocked: false,
                status: "To-Do",
                dueDate: "4-14-2025",
                teamId: 2,
                priority: "LOW"
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
            dueDate: "2025-03-01",
            priority: "HIGH"
        };

        fetch.mockResponseOnce(JSON.stringify(updatedTask), { status: 200 });

        const result = await editTask(1, "Updated Task", "Updated description", true, "In progress", "2025-03-01", "HIGH");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1`, {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                title: "Updated Task",
                description: "Updated description",
                isLocked: true,
                status: "In progress",
                dueDate: "2025-03-01",
                priority: "HIGH"
            })
        });

        expect(result).toEqual(updatedTask);
    });
});