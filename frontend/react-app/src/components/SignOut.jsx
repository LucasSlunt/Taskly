import useSignOut from 'react-auth-kit/hooks/useSignOut';
export default function SignOut(){
    const signout = useSignOut();
    return(
        <button onClick={() => signOut()}>Sign Out</button>
    )
}