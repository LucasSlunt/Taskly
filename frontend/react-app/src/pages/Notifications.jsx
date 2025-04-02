import React, { useState } from 'react';
import '../css/Notifications.css';
import Notification from "../components/Notification";

const Notifications = () => {
    const [notifications, setNotifications] = useState([
        { id: 1, team: "Team 2", message: 'Bob edited your task "Create wireframe"', read: false },
        { id: 2, team: "Team 1", message: 'Mary completed your task "Code things"', read: false },
        { id: 3, team: "Team 1", message: 'Adam assigned you to "Code more"', read: true }
    ]);

    const toggleRead = (id) => {
        setNotifications(notifications.map(notif =>
            notif.id === id ? { ...notif, read: !notif.read } : notif
        ));
    };

    const deleteNotification = (id) => {
        setNotifications(notifications.filter(notif => notif.id !== id));
    };

    // Store filtered notifications to avoid redundant filtering
    const unreadNotifications = notifications.filter(notif => !notif.read);
    const readNotifications = notifications.filter(notif => notif.read);

    // reusable section component
    const NotificationSection = ({ title, items }) => (
        items.length > 0 && (
            <>
                <tr>
                    <td colSpan="4" className="subHeader">{title}</td>
                </tr>
                {items.map(notif => (
                    <Notification 
                        key={notif.id} 
                        notif={notif} 
                        toggleRead={toggleRead} 
                        deleteNotification={deleteNotification} 
                    />
                ))}
            </>
        )
    );

    return (
        <div id="notifContainer">
            <table>
                <thead>
                    <tr>
                        <td colSpan="4">Notifications</td>
                    </tr>
                </thead>
                <tbody>
                    <NotificationSection title="Unread" items={unreadNotifications} />
                    <NotificationSection title="Read" items={readNotifications} />
                </tbody>
            </table>
        </div>
    );
};

export default Notifications;


//before refactoring 


// import React from 'react';
// import {useEffect, useState} from 'react';
// import '../css/Notifications.css';
// import Notification from "../components/Notification";

// const Notifications = () => {
//     const [notifications, setNotifications] = useState([
//         {id: 1, team: "Team 2", message: 'Bob edited your task "Create wireframe"', read: false},
//         { id: 2, team: "Team 1", message: 'Mary completed your task "Code things"', read: false },
//         { id: 3, team: "Team 1", message: 'Adam assigned you to "Code more"', read: true }
//     ]);

//     //mark as read or unread
//     const toggleRead = (id) => {
//         setNotifications(notifications.map(notif =>
//             notif.id === id ? { ...notif, read: !notif.read } : notif
//         ));
//     };

//     //delete any notification
//     const deleteNotification = (id) => {
//         setNotifications(notifications.filter(notif => notif.id !== id));
//     };

//     return (
//         <div id="notifContainer">
//            <table>
//                 <thead>
//                     <tr>
//                         <td colSpan="4">Notifications</td>
//                     </tr>
//                 </thead>
//                 <tbody>
//                     {notifications.some(notif => !notif.read) && (
//                         <>
//                             <tr>
//                                 <td colSpan="4" className="subHeader">Unread</td>
//                             </tr>
//                             {notifications.filter(notif => !notif.read).map(notif => (
//                                 <Notification key={notif.id} notif={notif} toggleRead={toggleRead} deleteNotification={deleteNotification} />
//                             ))}
//                         </>
//                     )}

//                     {notifications.some(notif => notif.read) && (
//                         <>
//                             <tr>
//                                 <td colSpan="4" className="subHeader">Read</td>
//                             </tr>
//                             {notifications.filter(notif => notif.read).map(notif => (
//                                 <Notification key={notif.id} notif={notif} toggleRead={toggleRead} deleteNotification={deleteNotification} />
//                             ))}
//                         </>
//                     )}
//                 </tbody>
//             </table>
//         </div>
//     )
// }

// export default Notifications;