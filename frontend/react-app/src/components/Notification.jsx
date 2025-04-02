import React from 'react';

const Notification = ({ notif, toggleRead, deleteNotification }) => {
    return (
        <tr>
            <td className="items">
                <input type="checkbox" checked={notif.read} onChange={() => toggleRead(notif.id)} />
            </td>
            <td className="items">
                <button className="smallTeamButton">{notif.team}</button>
            </td>
            <td className="notifDetails">{notif.message}</td>
            <td>
                <button className="delete" onClick={() => deleteNotification(notif.id)}>Delete</button>
            </td>
        </tr>
    )
}

export default Notification;