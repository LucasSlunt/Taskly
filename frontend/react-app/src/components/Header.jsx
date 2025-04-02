import { useCookies } from 'react-cookie'
import { Home, UserCircle, ListChecks, ShieldUser, LogOut, Bell } from "lucide-react";

function Header(){
    const [cookies, removeCookie] = useCookies(['userInfo'])

    function clearCookies() {
        removeCookie("userInfo");
        window.location.href = "/home";
    }

    return(
        <div>
            <h1 className="inner-header">
                <div className="logo" >
                    <a href="/home">Taskly</a>
                </div>

                <ul className ='icon-nav'>
                    <li><a href="/home" title="Home"><Home/></a></li>
                    <li><a href="/my-tasks" title="My Tasks"><ListChecks /></a></li>
                    <li><a href="/notifications" title="Notifications"><Bell /></a></li>
                    <li><a href="/profile" title="Profile"><UserCircle/></a></li>

                    {cookies.userInfo.role === 'admin' && (
                        <li><a href="/admin-panel" title="Admin Controls"><ShieldUser /></a></li>)}
                    
                    <li>
                        <button className="logout-button" onClick={clearCookies} title="Sign Out">
                        <LogOut/>
                        </button>
                    </li>
                </ul>
            </h1>
        </div>
    );
}

export default Header