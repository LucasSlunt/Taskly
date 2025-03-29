import { getReadNotifications, getUnreadNotifications, markAsRead, markAsUnread, deleteNotification } from "../../api/notificationApi";

const BASE_URL = "http://localhost:8080/api/notifications";

beforeEach(() => {
    fetch.resetMocks();
});

describe('Notification API', () => {
    //every test method below can use these mock notifications
    const mockNotifications = [
        { notificationId: 1, message: "Task Updated", type: "TASK_EDITED", isRead: true, teamMemberId: 101 },
        { notificationId: 2, message: "New Task Assigned", type: "TASK_ASSIGNED", isRead: false, teamMemberId: 101 },
        { notificationId: 3, message: "To be deleted in test", type: "TASK_ASSIGNED", isRead: false, teamMemberId: 101 }
    ];

    //test: getting read notifications
    test('getReadNotifications should return notification data on success', async () => {
        fetch.mockResponseOnce(JSON.stringify([mockNotifications[0]]), { status: 200 });

        const result = await getReadNotifications(101); //get notifications for teamMember with ID 101

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/101/read`, {
            method: 'GET'
        });

        expect(result).toEqual([mockNotifications[0]]);
    });

    //test: getting unread notifications
    test('getUnreadNotifications should return notification data on success', async () => {
        fetch.mockResponseOnce(JSON.stringify([mockNotifications[1], mockNotifications[2]]), { status: 200 });

        const result = await getUnreadNotifications(101); //get notifications for teamMember with ID 101

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/101/unread`, {
            method: 'GET'
        });

        expect(result).toEqual([mockNotifications[1], mockNotifications[2]]);
    });

    //test: marking notification as read
    test('markAsRead should return notification data on success', async () => {
        fetch.mockResponseOnce(JSON.stringify({ message: "Notification marked as read." }));

        const result = await markAsRead(1);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/1/mark-as-read`, {
            method: 'PUT'
        });

        expect(result).toEqual({ message: "Notification marked as read." });
    });
    
    //test: marking notification as unread
    test('markAsUnread should return notification data on success', async () => {
        fetch.mockResponseOnce(JSON.stringify({ message: "Notification marked as unread." }));

        const result = await markAsUnread(2);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/2/mark-as-unread`, {
            method: 'PUT'
        });

        expect(result).toEqual({ message: "Notification marked as unread." });
    });
    
    //test: deleting a notification
    test('deleteNotification should return notification data on success', async () => {
        //status 204 means successful deletion basically
        fetch.mockResponseOnce(JSON.stringify("", { status: 204 }));
        
        const result = await deleteNotification(3);

        expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/3`, {
            method: 'DELETE'
        });

        expect(result).toBe(true);
    });

});