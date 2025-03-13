import {useAuthUser} from 'react-auth-kit'

export default function ProtectedRoute({
    allowedRoles,
    protectedContent,
}){
    const auth = useAuthUser()

    if(auth === undefined){
        return <div>Loading...</div>
    }
    if(auth === null){
        return <div>Permisson Denied</div>
    }
    if(auth && !allowedRoles.includes(auth.role)){
        return <div>Admins Only</div>
    }
    return protectedContent;
    
}