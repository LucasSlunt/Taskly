import TaskList from "../components/TaskList";
import React, { useEffect, useMemo, useState } from 'react';
import '../css/Notifications.css';
import Notification from "../components/Notification";
import { getReadNotifications, getUnreadNotifications, markAsRead, markAsUnread, deleteNotification as deleteThisNotification} from '../api/notificationApi';
import { useCookies } from 'react-cookie';
import Header from '../components/Header'

const Notifications = () => {
    const [cookies] = useCookies(['userInfo'])

    const [readNotifications, setReadNotifications] = useState();
    const [unreadNotifications, setUnreadNotifications] = useState();
    const [loading, setLoading] = useState(true);

    useEffect(()=>{
        async function myNotifications() {
            try {
                const readNotificationsResponse = await getReadNotifications(cookies.userInfo.accountId);
                const unreadNotificationsResponse = await getUnreadNotifications(cookies.userInfo.accountId);
                setReadNotifications(readNotificationsResponse);
                setUnreadNotifications(unreadNotificationsResponse);
            } catch (error) {
                await alert(error)
            }finally{
                setLoading(false);
            }
        }
        myNotifications()

    },[])

    const memoizedUnReadNotifications = useMemo(()=>unreadNotifications,[unreadNotifications])
    const memoizedReadNotifications = useMemo(()=>readNotifications,[readNotifications])

    
    const toggleRead = async(id, isRead) => {
        try {
            if(isRead){
                const response = await markAsUnread(id);
                setReadNotifications(
                    await readNotifications.filter((notif)=>{
                        if(notif.notificationId === id){
                            setUnreadNotifications((prev)=>([...prev, notif]))
                            return false;
                        }else{
                            return true;
                        }
                    })
                )
            }else{
                const response = await markAsRead(id);
                setUnreadNotifications(
                    unreadNotifications.filter((notif)=>{
                        if(notif.notificationId === id){
                            setReadNotifications((prev)=>([...prev, notif]))
                            return false;
                        }else{
                            return true;
                        }
                    })
                )
            }
        } catch (error) {
            await alert(error);
        }
    };

    const deleteNotification = async(id, isRead) => {
        if(isRead){
            setReadNotifications(readNotifications.filter(notif => notif.notificationId !== id));}
        else{
            setUnreadNotifications(unreadNotifications.filter(notif => notif.notificationId !== id));}
       try {
        const response = await deleteThisNotification(id)
       } catch (error) {
        alert(error);
       }
    };

    // Store filtered notifications to avoid redundant filtering
    //const unreadNotifications = notifications.filter(notif => !notif.read);
    //const readNotifications = notifications.filter(notif => notif.read);

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
    if(loading){
        return(<div>...Loading</div>)
    }
    return (
        <div className='pageContainer'>
            <Header/>
            <div className='pageBody'>
            <div id="notifContainer">
                <div className="column-box">
                    <h2>My Notifications</h2>
                        <div className="section-divider"></div>
                        <div className ="notifBox">
                        <table>
                            <thead>
                                <tr>
                                    <td colSpan="4">Notifications</td>
                                </tr>
                            </thead>
                            <tbody>
                                <NotificationSection title="Unread" items={memoizedUnReadNotifications} />
                                <NotificationSection title="Read" items={memoizedReadNotifications} />
                            </tbody>
                        </table>
                        </div>
                </div>   
            </div>
            </div>
        </div>
    );
};

export default Notifications;