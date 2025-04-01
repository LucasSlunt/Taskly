import { useCookies } from "react-cookie";
export default function SignOut(){
    const [cookie, removeCookie] = useCookies('userInfo');
    function clearCookies(){
        removeCookie("userInfo")
        window.location.href="/home";
    }
    return(
        
        <button className="importButton" onClick={clearCookies}>Sign Out</button>
    )
}