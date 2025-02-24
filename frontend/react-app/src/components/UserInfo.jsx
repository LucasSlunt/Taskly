import "../css/Profile.css"
import logo from '../logo.svg';

function UserInfo(){
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
                    <img src={user.profilePictureUrl} alt="Profile Picture" />
                </div>
                <div className="profile-info">
                    <h2 className="profile-name">{user.name}</h2>
                    <p className="profile-username">{user.username}</p>
                    <p className="profile-email">{user.email}</p>
            </div>
        </div>
    );
}

export default UserInfo