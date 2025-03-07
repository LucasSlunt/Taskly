import { notifyMembers} from '../../api/taskApi';

const BASE_URL = "http://localhost:8080/api/tasks";

beforeEach(() => {
    fetch.resetMocks();
});

describe('Task API', () => {
    //test: notifying members
    test('notifyMembers should return true on success', async () => {
        fetch.mockResponseOnce('', { status: 200 });

        const result = await notifyMembers(1, 1);
        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/notify`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" }
        });

        expect(result).toBe(true);
    });
});