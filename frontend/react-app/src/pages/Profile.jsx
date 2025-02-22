import "../css/Profile.css"
import logo from '../logo.svg';

function Profile(){
    return (
    <div className="profile-page">
        <div className="profile-header">
            <div className="profile-picture">
                <img src={logo} alt="Profile Picture" />
            </div>
            <div className="profile-info">
                <h2 className="profile-name">Jane Doe</h2>
                <p className="profile-username">janedoe123</p>
                <p className="profile-email">janedoe@example.com</p>
            </div>
        </div>
    </div>
    
    );
}

export default Profile