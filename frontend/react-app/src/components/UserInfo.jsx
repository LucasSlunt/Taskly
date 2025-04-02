import "../css/Profile.css"
import logo from '../logo.svg';
import {  User, UserCircle } from "lucide-react";

//user information used in the profile page
function UserInfo({userInfo}){

    //mock data
    const user  = {
        name: "Bobby Joe",
        username: "bobbyjoe",
        email: "bobbyjoe@example.com",
        profilePictureUrl: "https://i.pravatar.cc/150?img=12" 
    }
    
    return (
        <div className="user-info">
            <div className="profile-picture">
                    <UserCircle size={100}/>
                </div>
                <div className="profile-info">
                    <h1 className="profile-name">{userInfo.accountId}</h1>
                    <p className="profile-username">{userInfo.userName}</p>
                    <h3 className="profile-email">{userInfo.userEmail}</h3>
            </div>
        </div>
    );
}

export default UserInfo