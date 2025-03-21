import React from 'react';
import {useEffect, useState} from 'react';
import '../css/Notifications.css';

const Notifications = () => {
    return(
        <div id="notifContainer">
            <table>
                <thead>
                    <td colspan="4">Notifications</td>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="4" class="subHeader">Unread</td>
                    </tr>
                    <tr>
                        <td class="items"><input type="checkbox" name="read"/></td>
                        <td class="items"><button class="smallTeamButton">Team 2</button></td>
                        <td class="notifDetails">Bob edited your task "Create wireframe"</td>
                        <td><button class='delete'>Delete</button></td>
                    </tr>
                    <tr>
                        <td class="items"><input type="checkbox" name="read"/></td>
                        <td class="items"><button class="smallTeamButton">Team 1</button></td>
                        <td class="notifDetails">Mary completed your task "Code things"</td>
                        <td><button class='delete'>Delete</button></td>
                    </tr>
                    <tr>
                        <td colspan="4" class="subHeader">Read</td>
                    </tr>
                    <tr>
                        <td class="items"><input type="checkbox" name="read" checked/></td>
                        <td class="items"><button class="smallTeamButton">Team 1</button></td>
                        <td class="notifDetails">Adam assigned you to "Code more"</td>
                        <td><button class='delete'>Delete</button></td>
                    </tr>
                </tbody>
            </table>
        </div>
    )
};

export default Notifications;