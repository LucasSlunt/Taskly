import { Navigate } from 'react-router-dom';
import {useCookies} from 'react-cookie'
export default function ProtectedRoute({
    allowedRoles,
    urlReirect,
    protectedContent,
}){

    const [cookies] = useCookies(['userInfo'])
    console.log(cookies)
    if(Object.keys(cookies).length === 0 || cookies.userInfo === undefined){
       return <Navigate to={urlReirect} replace />;
    }
    if(cookies && !allowedRoles.includes(cookies.userInfo.role)){
       return <Navigate to={'/login'} replace />;
    }
    
    return protectedContent;
    
}