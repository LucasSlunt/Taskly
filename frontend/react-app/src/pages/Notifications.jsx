import React from 'react';
import {useEffect, useState} from 'react';
import '../css/Notifications.css';
import Notification from "../components/Notification";

const Notifications = () => {
    const [notifications, setNotifications] = useState([
        {id: 1, team: "Team 2", message: 'Bob edited your task "Create wireframe"', read: false},
        { id: 2, team: "Team 1", message: 'Mary completed your task "Code things"', read: false },
        { id: 3, team: "Team 1", message: 'Adam assigned you to "Code more"', read: true }
    ]);

    //mark as read or unread
    const toggleRead = (id) => {
        setNotifications(notifications.map(notif =>
            notif.id === id ? { ...notif, read: !notif.read } : notif
        ));
    };

    //delete any notification
    const deleteNotification = (id) => {
        setNotifications(notifications.filter(notif => notif.id !== id));
    };

    return (
        <div id="notifContainer">
           <table>
                <thead>
                    <tr>
                        <td colSpan="4">Notifications</td>
                    </tr>
                </thead>
                <tbody>
                    {notifications.some(notif => !notif.read) && (
                        <>
                            <tr>
                                <td colSpan="4" className="subHeader">Unread</td>
                            </tr>
                            {notifications.filter(notif => !notif.read).map(notif => (
                                <Notification key={notif.id} notif={notif} toggleRead={toggleRead} deleteNotification={deleteNotification} />
                            ))}
                        </>
                    )}

                    {notifications.some(notif => notif.read) && (
                        <>
                            <tr>
                                <td colSpan="4" className="subHeader">Read</td>
                            </tr>
                            {notifications.filter(notif => notif.read).map(notif => (
                                <Notification key={notif.id} notif={notif} toggleRead={toggleRead} deleteNotification={deleteNotification} />
                            ))}
                        </>
                    )}
                </tbody>
            </table>
        </div>
    )
}


//old static version (will delete later)

// const Notifications = () => {
//     return(
//         <div id="notifContainer">
//             <table>
//                 <thead>
//                     <td colspan="4">Notifications</td>
//                 </thead>
//                 <tbody>
//                     <tr>
//                         <td colspan="4" class="subHeader">Unread</td>
//                     </tr>
//                     <tr>
//                         <td class="items"><input type="checkbox" name="read"/></td>
//                         <td class="items"><button class="smallTeamButton">Team 2</button></td>
//                         <td class="notifDetails">Bob edited your task "Create wireframe"</td>
//                         <td><button class='delete'>Delete</button></td>
//                     </tr>
//                     <tr>
//                         <td class="items"><input type="checkbox" name="read"/></td>
//                         <td class="items"><button class="smallTeamButton">Team 1</button></td>
//                         <td class="notifDetails">Mary completed your task "Code things"</td>
//                         <td><button class='delete'>Delete</button></td>
//                     </tr>
//                     <tr>
//                         <td colspan="4" class="subHeader">Read</td>
//                     </tr>
//                     <tr>
//                         <td class="items"><input type="checkbox" name="read" checked/></td>
//                         <td class="items"><button class="smallTeamButton">Team 1</button></td>
//                         <td class="notifDetails">Adam assigned you to "Code more"</td>
//                         <td><button class='delete'>Delete</button></td>
//                     </tr>
//                 </tbody>
//             </table>
//         </div>
//     )
// };

export default Notifications;