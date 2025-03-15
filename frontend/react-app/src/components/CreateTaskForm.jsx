import { useState, useEffect } from 'react';
import {useForm} from 'react-hook-form'
import '../css/CreateTaskForm.css'
import {assignMemberToTask, createTask, getTeamsForMember} from '../api/teamMemberApi'
import { useCookies } from 'react-cookie';
import { getTeamMembers } from '../api/teamApi';
function CreateTaskForm(){
    const [cookies] = useCookies(['userInfo'])
    const userId = cookies.userInfo.accountId
    const [userTeams, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [teamMembers, setTeamMembers] = useState([]);
    const { register, handleSubmit, formState: {errors}} = useForm();
    //Once team is selected get the team members
    const teamSelected = ((event)=>{
        console.log(getTeamMembers(event.target.value))
        getTeamMembers(event.target.value).then((results)=>setTeamMembers(results))
    })
    useEffect(()=>{
        try {
            getTeamsForMember(userId).then((results)=>{setData(results)})
        } catch (error) {
            console.log("error while getting teams")
        }finally{
            setLoading(false)
        }
    },[]);
    const onSubmit =  async (data)=> {
        try {
            console.log(data)
            createTask(
                data.title,
                data.description,
                false,
                "notStarted",
                data.dueDate,
                userTeams[0].teamId
    
            ).then((response)=>{
                console.log(response)
                if(Array.isArray(data.assignees)){
                    data.assignees.map((assignee)=>{
                     assignMemberToTask(response.taskId ,assignee.accountId).then((result)=>{
                         console.log(result)
                     })
                    })}else{
                     assignMemberToTask(response.taskId ,data.assignees).then((result)=>{
                         console.log(result)
                     })
                    }  
            })
            //window.location.href="/home";
        } catch (error) {
            console.log(error)
            alert("FAILED IN MAKING TASK");
        }
    };
    if(loading){
        return <div>Loading...</div>
    }
    return(
        <form onSubmit={handleSubmit(onSubmit)} className='form'>
            <label className='majorLabel'>
                    Task Name
                    <div>
                    <input type="text" name="name" id="name"{...register("title", { 
                        required:{
                            value: true,
                            message: 'Please set a Task Name'
                        }
                        })}/>
                    </div>
                    </label>
                <label className='majorLabel'>
                <div>
                    {
                        errors.name && <span>{errors.name.message}</span>
                    }
                </div>
                </label>
                <label>
                    Choose Team:
                    <div>
                        <select name="" id="" onChange={teamSelected}>
                            <option disabled selected value>Choose A Team To Assign</option>
                            {userTeams.map((team)=>(
                                <option value = {team.teamId}>{team.teamName}</option>
                            ))}
                        </select>
                    </div>
                </label>
                <label className='majorLabel'>
                    
                    {teamMembers !== null&&(<p> Assign Users: <div className='Checkboxs'>
                        {teamMembers.map((teamMember)=>(
                            <div className='checkbox' key = {teamMember.id}>
                                {teamMember.userName}
                                <input type="checkbox" name="" id="" value = {teamMember.accountId} {...register("assignees")}/>
                            </div>
                        ))}
                    </div></p>)}
                </label>
                <label className='majorLabel'>
                    Add Discription
                
                <div>
                    <input type="text" name="input-description" id="description" {...register("description", { required: false })}/>
                </div>
                </label>
                <label>
                    Due Date
                    <div>
                        <input type="date" name="" id="" {...register("dueDate", { 
                            required:{
                                value: true,
                                message: 'Please set a Due Date'
                            }
                            })}/>
                    </div>
                </label>
                {/*
                Future Image upload code not implmented for now
                <label className='majorLabel'>
                    Add Images
                
                <div>
                    <input type="file" name="input-photos" id="photos" multiple accept="image/*"/>
                </div> 
                </label>*/}
                <input type="submit" value="Create Task"/>
                
        </form>
    );
}

export default CreateTaskForm