import { useCookies } from 'react-cookie'
import { Home, ListTodo, UserCircle, Settings, ListCheck, ListChecks, ShieldAlert, ShieldUser } from "lucide-react";

function Header(){
    const [cookies] = useCookies(['userInfo'])
    return(
        <div>
            <h1 className="inner-header">
                <div className="logo" >
                    <a href="/home">Taskly</a>
                </div>

                <ul className ='icon-nav'>
                    <li><a href="/home" title="Home"><Home/></a></li>
                    <li><a href="/my-tasks" title="My Tasks"><ListChecks/></a></li>
                    <li><a href="/profile" title="Profile"><UserCircle/></a></li>

                    {cookies.userInfo.role === 'admin' && (
                        <li><a href="/admin-panel" title="Admin Controls"><ShieldUser /></a></li>)}
                </ul>
            </h1>
        </div>
    );
}

export default Header