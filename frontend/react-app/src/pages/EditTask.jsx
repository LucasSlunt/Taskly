import {Link, useLocation} from 'react-router-dom'
import { useEffect, useState } from 'react'
import EditTaskForm from '../components/EditTaskForm'
import Header from '../components/Header'
import '../css/EditTask.css'
import { getTeamMembers } from '../api/teamApi'

function EditTask(){
        const location = useLocation()
        const { taskToEdit } = location.state
        const [teamMembers, setTeamMembers] = useState();
        const [loading, setLoading] = useState(true);
        useEffect(()=>{
            async function getTeam(teamId){
                try {
                    const response = await getTeamMembers(taskToEdit.teamId);
                    setTeamMembers(response)
                    console.log(response)
                } catch (error) {
                    
                }finally{
                    setLoading(false);
                }    
            }
            getTeam();
            
            
        },[]);
        console.log("taskToEdit: ", taskToEdit)
        if(loading){
            return(<div>..Loading</div>)
        }
        return(
            <div className='pageContainer'>
                <Header/>
                <div className='pageBody'>
                        <EditTaskForm
                        task={taskToEdit}
                        team={teamMembers}
                        />
                </div>
            </div>
        );
    
}
export default EditTask