import React from 'react';
import '../css/Notifications.css';

const Notifications = () => {
    return(
        <div id="notifContainer">
            <table>
                <thead>
                    <td colspan="3">Notifications</td>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="3" class="subHeader">Unread</td>
                    </tr>
                    <tr>
                        <td class="items"><input type="checkbox" name="read"/></td>
                        <td class="items"><button>Team 2</button></td>
                        <td class="notifDetails">Bob edited your task "Create wireframe"</td>
                    </tr>
                    <tr>
                        <td class="items"><input type="checkbox" name="read"/></td>
                        <td class="items"><button>Team 1</button></td>
                        <td class="notifDetails">Mary completed your task "Code things"</td>
                    </tr>
                    <tr>
                        <td colspan="3" class="subHeader">Read</td>
                    </tr>
                    <tr>
                        <td class="items"><input type="checkbox" name="read" checked/></td>
                        <td class="items"><button>Team 1</button></td>
                        <td class="notifDetails">Adam assigned you to "Code more"</td>
                    </tr>
                </tbody>
            </table>
        </div>
    )
};

export default Notifications;