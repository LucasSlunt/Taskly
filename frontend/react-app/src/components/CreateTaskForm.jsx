import { useState, useEffect } from 'react';
import {useForm} from 'react-hook-form'
import '../css/CreateTaskForm.css'
import {assignMemberToTask, createTask, getTeamsForMember} from '../api/teamMemberApi'
import { useCookies } from 'react-cookie';
function CreateTaskForm(){
    const [cookies] = useCookies(['userInfo'])
    const [team, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const { register, handleSubmit, formState: {errors}} = useForm();
    useEffect(()=>{
        try {
            console.log(cookies.userInfo.accountId)
            setData(()=>getTeamsForMember(cookies.userInfo.accountId))
        } catch (error) {
            console.log("error while getting teams")
        }finally{
            setLoading(false)
        }
    })
    const onSubmit =  async (data)=> {
        try {
            /*const responseCreateTask = await createTask(
                data.title,
                data.description,
                false,
                "notStarted",
                data.dueDate,
                teamId
    
            );*/
            /*console.log(data.assignees)
            data.assignees.map((assignee)=>{
                assignMemberToTask(responseCreateTask.taskId, )  
            })*/
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
                <label className='majorLabel'>
                    Assign To:
                    <div className='Checkboxs'>
                        {/*team.map((teamMember)=>(
                            <div className='checkbox' key = {teamMember.name}>
                                {teamMember.name}
                                <input type="checkbox" name="" id="" value = {teamMember.name} {...register("assignees")}/>
                            </div>
                        ))*/}
                    </div>
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