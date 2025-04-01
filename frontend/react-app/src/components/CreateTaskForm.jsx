import { useState, useEffect, useMemo } from 'react';
import {useForm, Controller} from 'react-hook-form'
import Select from 'react-select'
import '../css/CreateTaskForm.css'
import {assignMemberToTask, getTeamsForMember, massAssignMemberToTask} from '../api/teamMemberApi'
import { createTask } from '../api/taskApi';
import { getTeamMembers } from '../api/teamApi';

function CreateTaskForm({userId}){
    
    const [userTeams, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [teamMembers, setTeamMembers] = useState([]);
    const { register, handleSubmit, formState: {errors}, control} = useForm();
    function getTeamMemberIds(teamMembers){
        return teamMembers.map((teamMember)=>(
            teamMember.value
        ))
      }
    const customStyles = {
        control: (provided) => ({
          ...provided,
          width: '100%',
        minWidth: '100px',
        maxWidth: '100%',
        minHeight: '30px',
        maxHeight: '100%',
          border: '2px solid grey',
          borderRadius: '10px',
          paddingLeft: '8px',
          backgroundColor: '#BFCDE0',
          margin: '10px 0px',
          cursor: 'pointer'
        }),
        option: (provided, state) =>({
            ...provided,
            cursor: state.isSelected ? 'default' : 'pointer',
        })
      };
    function formatTeamData(myTeamMembers){
        return myTeamMembers.map((teamMember)=>(
            {
                value: teamMember.accountId,
                label: teamMember.userName
             }
        ))
      }
    //Once team is selected get the team members
    const teamSelected = ((event)=>{
        setTeamMembers([])
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
    const data = useMemo(()=>(teamMembers),[teamMembers])
    const onSubmit =  async (data)=> {
        try {
            createTask(
                data.title,
                data.description,
                false,
                "notStarted",
                data.dueDate,
                userTeams[0].teamId,
                data.priority

            ).then((response)=>{
                if(Array.isArray(data.assignees)){
                    const assignees = getTeamMemberIds(data.assignees);
                    massAssignMemberToTask(response.taskId ,assignees).then((result)=>{
                        //debugging
                         console.log(result)
                         alert("TASK CREATED")
                     })    
                }else{
                     assignMemberToTask(response.taskId ,data.assignees.value).then((result)=>{
                        //debugging
                         console.log(result)
                         alert("TASK CREATED")
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
        return (<div>Loading...</div>)
    }
    return(
        <form onSubmit={handleSubmit(onSubmit)} className='CreateTaskForm'>
            <label className='majorLabel'>
                    Task Name
                    <div>
                    <input type="text" name="name" id="name" className='input'{...register("title", { 
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
                            <option disabled selected value=''>Choose A Team To Assign</option>
                            {userTeams.map((team)=>(
                                <option value = {team.teamId}>{team.teamName}</option>
                            ))}
                        </select>
                    </div>
                </label>
                <label className='majorLabel'>
                    Assigned To:
                    {data.length !== 0&&(<Controller
                                    control={control}
                                    className='Select'
                                    name="assignees"
                                    rules={{required:true}}
                                    defaultValue={[]}
                                    render={({field}) => (
                                        <Select
                                        {...field}
                                        options={formatTeamData(data)}
                                        isMulti
                                        
                                        styles={customStyles}
                                        
                                        />)}
                                    />)}
                        {(data.length===0)&&(
                            <select {...register('assignees',{required: true})}>
                                <option disabled selected value =''>CHOOSE TEAM TO CHOOSE MEMBERS</option>
                            </select>
                        )}
                </label>
                <label className='majorLabel'>
                    Add Description
                
                <div>
                    <input type="text" name="input-description" id="description" className='input'{...register("description", { required: true })}/>
                </div>
                </label>
                <label>
                    Priority
                    <div>
                        <select name="" id="" {...register("priority")}>
                            <option value="LOW">Low</option>
                            <option value="MEDIUM">Medium</option>
                            <option value="HIGH">High</option>
                        </select>
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