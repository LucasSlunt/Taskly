import {useCookies} from 'react-cookie'
function Header(){
    const [cookies] = useCookies(['userInfo'])
    return(
            
                <h1 className ="inHeader">
                        <ul className ='list'>
                            <li className ="logo list"><span><a href="/home">Whatever we Call this</a></span></li>
                            <li><span><a href="/home">Home</a></span></li>
                            <li><span><a href="/my-tasks">Tasks</a></span></li>
                            <li><span><a href="/profile">Profile</a></span></li>
                            {cookies.userInfo.role === 'admin' && (<li><span><a href="/admin-panel">Admin Controls</a></span></li>)}
                        </ul>
                </h1>
    );
}
export default Header