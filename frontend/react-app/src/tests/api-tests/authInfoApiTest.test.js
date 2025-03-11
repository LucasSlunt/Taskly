import { login, isAdmin } from '../../api/authInfo';

const BASE_URL = "http://localhost:8080/api/auth-info";

beforeEach(() => {
    fetchMock.resetMocks();
});

describe('Auth API', () => {
    //Test: logging in and returning login info
    test('Login should return auth data on success', async () => {
        const mockAuthInfo = {
            accountId: 1,
            userName: "Test User",
            isAdmin: false
        };

        fetch.mockResponseOnce(JSON.stringify(mockAuthInfo), { status: 200 });

        const result = await login(1, "securepassword");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/login`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                teamMemberId: 1,
                password: "securepassword"
            })
        });

        expect(result).toEqual(mockAuthInfo);
    });

    //test: login should return null when given invalid credentials
    test('login should return null when credentials are incorrect', async () => {
        fetch.mockResponseOnce('', { status: 401 });

        const result = await login(123, "wrongpassword");

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/login`, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                teamMemberId: 123,
                password: "wrongpassword"
            })
        });

        expect(result).toBeNull();
    });

    //test: admins return true when checking if the user is an admin
    test('isAdmin should return true for admin users', async () => {
        fetch.mockResponseOnce(JSON.stringify(true), { status: 200 });

        const result = await isAdmin(123);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/123/is-admin`, {
            method: 'GET'
        });

        expect(result).toBe(true);
    });

    //test: non admins should return false when checking if the user is an admin
    test('isAdmin should return false for non-admin users', async () => {
        fetch.mockResponseOnce(JSON.stringify(false), { status: 200 });

        const result = await isAdmin(456);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/456/is-admin`, {
            method: 'GET'
        });

        expect(result).toBe(false);
    });
});