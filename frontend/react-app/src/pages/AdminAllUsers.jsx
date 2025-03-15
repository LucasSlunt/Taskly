import UserTable from '../components/UserTable'
import Header from '../components/Header'
import '../css/AdminAllUsers.css'
import {useState, useEffect} from 'react'


function AdminAllUsers(){
    return(
        <div className='page'>
            <Header/>
            <div>
                <UserTable/>
            </div>

            <a href="create-account"><button >Create New Account</button></a>
        </div>
    )
}
export default AdminAllUsers
/*
 <UserTable
                teamMember = {AllTeams[0].members}
                />
*/