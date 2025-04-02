const BASE_URL = "http://localhost:8080/api/notifications";

//get all read notifications
export const getReadNotifications = async (teamMemberId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/read`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to retrieve read notifications: ${response.status} ${response.statusText}`)
            return null;
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error fetching read notifications:", error);
        throw error;
    }
};

//get all unread notifications
export const getUnreadNotifications = async (teamMemberId) => {
    try {
        const response = await fetch(`${BASE_URL}/${teamMemberId}/unread`, {
            method: 'GET'
        });

        if (!response.ok) {
            console.error(`Failed to retrieve unread notifications: ${response.status} ${response.statusText}`)
            return null;
        }

        return await response.json();
    }
    catch (error) {
        console.error("Error fetching unread notifications:", error);
        throw error;
    }
};


//mark notification as read
export const markAsRead = async (notificationId) => {
    try {
        const response = await fetch(`${BASE_URL}/${notificationId}/mark-as-read`, {
            method: 'PUT'

        });

        if (!response.ok) {
            console.error(`Failed to mark notification as read: ${response.status} ${response.statusText}`)
            return null;
        }

        return await response;
    }
    catch (error) {
        console.error("Error marking notification as read:", error);
        throw error;
    }
};


//mark notification as unread
export const markAsUnread = async (notificationId) => {
    try {
        const response = await fetch(`${BASE_URL}/${notificationId}/mark-as-unread`, {
            method: 'PUT'

        });

        if (!response.ok) {
            console.error(`Failed to mark notification as unread: ${response.status} ${response.statusText}`)
            return null;
        }

        return await response;
    }
    catch (error) {
        console.error("Error marking notification as unread:", error);
        throw error;
    }
};


//delete a notification
export const deleteNotification = async (notificationId) => {
    try {
        const response = await fetch(`${BASE_URL}/${notificationId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            console.error(`Failed to delete notification: ${response.status} ${response.statusText}`)
            return null;
        }

        return true;
    }
    catch (error) {
        console.error("Error deleting notification:", error);
        throw error;
    }
};
