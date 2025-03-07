
import {useForm, Controller} from 'react-hook-form'
import '../css/CreateTaskForm.css'
//team buttons (don't have onclick functionality yet)
function CreateTaskForm({team}){
    const { register, handleSubmit, formState: {errors}} = useForm();
    const onSubmit = data => {
        console.log(data)
    };
    return(
        <form onSubmit={handleSubmit(onSubmit)} className='form'>
            <label className='majorLabel'>
                    Task Name
                    <div>
                    <input type="text" name="name" id="name"{...register("name", { 
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
                    Set Priority
                
                <div>
                    <select name="priority" id="priority" {...register("priority", { required: true })}>
                        <option value="Low">Low</option>
                        <option value="Medium">Medium</option>
                        <option value="High">High</option>
                    </select>
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
                    <input type="text" name="input-discription" id="discription" {...register("discription", { required: false })}/>
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