import {Link, useLocation} from 'react-router-dom'
import {useForm} from 'react-hook-form'
import Header from '../components/Header'
import '../css/EditTask.css'
function EditTask(){
        const location = useLocation()
        const { taskToEdit } = location.state
        const {onThisTeam} = location.state
        console.log("taskToEdit: ", taskToEdit)
        console.log("onThisTeam: ", onThisTeam)

        const { register, handleSubmit, formState: {errors}} = useForm();
        const onSubmit = data => {
            console.log(data)
            alert("Your task was updated")
            window.location.href="/home";
        };
        return(
            <div className='page'>
                <Header/>
                <div>
                    <form onSubmit={handleSubmit(onSubmit)} className='body'>
                        <label className='majorLabel'>
                                Task Name
                                <div>
                                <input type="text" name="name" id="name" defaultValue={taskToEdit.name}{...register("name", { 
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
                                    {onThisTeam.map((teamMember)=>(
                                        <div className='checkbox' key = {teamMember.name}>
                                            {teamMember.name}
                                            <input type="checkbox" name="" id="" value = {teamMember.name} {...register("assignees")}/>
                                        </div>
                                    ))}
                                </div>
                            </label>
                            <label className='majorLabel'>
                            Discription
                            
                            <div>
                                <input type="text" name="input-discription" id="discription" defaultValue={taskToEdit.discription}  {...register("discription", { required: false })}/>
                            </div>
                            </label>
                            <input type="submit" value="Create Task"/>
                        
                    </form>
                </div>
            </div>
        );
    
}
export default EditTask