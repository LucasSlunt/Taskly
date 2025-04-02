import React from 'react';

const Notification = ({ notif, toggleRead, deleteNotification }) => {
    return (
        <tr>
            <td className="items">
                <input type="checkbox" checked={notif.read} onChange={() => toggleRead(notif.notificationId, notif.isRead)} />
            </td>
            <td className="items">
                {notif.taskId}
            </td>
            <td className="notifDetails">{notif.message}</td>
            <td>
                <button className="delete" onClick={() => deleteNotification(notif.notificationId, notif.isRead)}>Delete</button>
            </td>
        </tr>
    )
}

export default Notification;