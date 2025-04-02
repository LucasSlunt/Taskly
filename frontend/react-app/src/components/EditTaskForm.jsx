
import {useForm, Controller} from 'react-hook-form'
import Select from 'react-select'
import { assignMemberToTask, massAssignMemberToTask } from '../api/teamMemberApi';
import {editTask} from '../api/taskApi'
import { unassignTeamMemberFromTask } from '../api/isAssignedApi';
const customStyles = {
    control: (provided) => ({
      ...provided,
      width: '560px;',
    minWidth: '100px',
    maxWidth: '100%',
    minHeight: '30px',
    maxHeight: '100%',
      border: '1.5px solid #2d2644',
      borderRadius: '10px',
      paddingLeft: '8px',
      backgroundColor: 'white',
      margin: 'auto',
      marginTop: '0.5%',
      marginBottom: '1%',
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
function EditTaskForm({task, team}){
    console.log(formatTeamData(task.assignedMembers))
    const { register, handleSubmit, formState: {errors}, control} = useForm();

    const onSubmit = async(data) => {
        console.log(task.assignedMembers)
        try {
            console.log(task.taskId, data.name, data.description, null,null, data.dueDate, data.priority)
            const response = await editTask(task.taskId, data.name, data.description, null,null, data.dueDate, data.priority)
            console.log(response)
            if(JSON.stringify(data.teamMembers) !== JSON.stringify(task.assignedMembers)){
                let teamMembersToAdd = []
                let teamMembersToDelete = []
                data.teamMembers.map((teamMemberOfChoice)=>{
                    let addToTeam = true;
                    task.assignedMembers.map((teamMember)=>{
                        if(teamMemberOfChoice.value === teamMember.accountId){
                            addToTeam = false;
                        }
                    })
                    if(addToTeam){
                        teamMembersToAdd = [...teamMembersToAdd, teamMemberOfChoice.value]
                    }
                })
                
                task.assignedMembers.map((teamMemberToDelete)=>{
                    let deleteTeamMember = true
                    data.teamMembers.map((teamMember)=>{
                        if(teamMemberToDelete.accountId === teamMember.value){
                            deleteTeamMember = false;
                        }
                    })
                    if(deleteTeamMember){
                        teamMembersToDelete = [...teamMembersToDelete, teamMemberToDelete.accountId]}
                })
                if(teamMembersToAdd.length >0){
                    const addedTeamMembersAPIResponse = await massAssignMemberToTask(task.taskId, teamMembersToAdd);
                    console.log(addedTeamMembersAPIResponse)
                }if(teamMembersToDelete.length >0 && teamMembersToDelete[0]!== undefined){
                    teamMembersToDelete.map(async(teamMemberToDel)=>{
                        const delTeamMemberAPIResponse = await unassignTeamMemberFromTask(teamMemberToDel, task.taskId)
                    })
                }
            }

            await alert('Task Has Been Edited')
            window.location.href="/home";
        } catch (error) {
            alert(error)
        }
    };
    return(
        <form onSubmit={handleSubmit(onSubmit)} className='CreateTaskForm'>
                        <label className='majorLabel'>
                                Task Name
                                <div>
                                <input type="text" placeholder="Add task name" name="name" id="name" className='input' defaultValue={task.title}{...register("name", { 
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
                                {/*<div className='Checkboxs'>
                                    {team.map((teamMember)=>(
                                        <div className='checkbox' key = {teamMember.name}>
                                            {teamMember.userName}
                                            <input type="checkbox" name="" id="" value = {teamMember.accountId} {...register("assignees")}/>
                                        </div>
                                    ))
                                </div>}*/}
                                <Controller
                                    control={control}
                                    className='Select'
                                    name="teamMembers"
                                    rules={{required:true}}
                                    defaultValue={formatTeamData(task.assignedMembers)}
                                    render={({field}) => (
                                        <Select
                                        {...field}
                                        options={formatTeamData(team)}
                                        isMulti
                                        
                                        styles={customStyles}
                                        
                                        />)}
                                    />
                            </label>
                            <label>
                                Priority
                                <select name="" id="" defaultValue={task.priority}{...register("priority")}>
                                    <option value="LOW">Low</option>
                                    <option value="MEDIUM">Medium</option>
                                    <option value="HIGH">High</option>
                                </select>
                            </label>
                            <label className='majorLabel'>
                            Description
                            
                            <div>
                                {/* <input type="text" name="input-description" id="description" className='input' defaultValue={task.description}  {...register("description", { required: false })}/> */}
                                <textarea name="input-description" id="description" placeholder="Add description" className='input' defaultValue={task.description}  {...register("description", { required: false })}/>
                            </div>
                            </label>
                            <label className='majorLabel'>
                                Due Date
                                <div>
                                    <input type='date' name="input-dueDate" id="dueDate" className='input' defaultValue={task.dueDate}  {...register("dueDate", { required: false })}/>
                                </div>
                            </label>
                            <input type="submit" value="Edit Task" id="editTaskButton"/>
                        
                    </form>
    )
}
export default EditTaskForm