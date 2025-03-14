
import {useForm} from 'react-hook-form'
import '../css/CreateTaskForm.css'
import {createTask} from '../api/teamMemberApi'
function CreateTaskForm({team}){
    let teamId = 9384;
    const { register, handleSubmit, formState: {errors}} = useForm();
    const onSubmit =  async (data)=> {
        console.log(data)
        try {
            const responseCreateTask = await createTask(
                data.title,
                data.description,
                false,
                "notStarted",
                data.dueDate,
                teamId
    
            );
            //console.log(responseCreateTask);
        } catch (error) {
            console.log(error)
        }
    };
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
                        {team.map((teamMember)=>(
                            <div className='checkbox' key = {teamMember.name}>
                                {teamMember.name}
                                <input type="checkbox" name="" id="" value = {teamMember.name} {...register("assignees")}/>
                            </div>
                        ))}
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