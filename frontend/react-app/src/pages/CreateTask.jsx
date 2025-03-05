import fakeTeamData from "../FakeData/fakeTeamData.json"
import Header from "../Header"

import {useForm, Controller} from 'react-hook-form'
function CreateTask(){
    const { register, handleSubmit } = useForm();
    const onSubmit = data => {
        console.log(data)
    };


        

    return (
        <div>
            <Header/>
            <form onSubmit={handleSubmit(onSubmit)}>
                <label>
                    Task Name
                    <div>
                    <input type="text" name="name" id="name"{...register("name", { required: true })}/>
                    </div>
                    </label>
                <label>
                    Assign To:
                {fakeTeamData.map((teamMember)=>(
                    <div>
                        <Controller
                            name="myCheckbox"
                            control={control}
                            defaultValue={false}
                            render={({ field }) => (
                            <FormControlLabel
                                control={
                                <Checkbox
                                    {...field}
                                    checked={field.value}
                                    onChange={(e) => field.onChange(e.target.checked)}
                                />
                                }
                                label={teamMember.name}
                            />
                            )}
                        />
                    </div>
                ))}
                </label>
                <label>
                    Set Priority
                
                <div>
                    <select name="priority" id="priority" required {...register("priority", { required: true })}>
                        <option value="Low">Low</option>
                        <option value="Medium">Medium</option>
                        <option value="High">High</option>
                    </select>
                </div>
                </label>
                <label>
                    Add Discription
                
                <div>
                    <input type="text" name="input-discription" id="discription" {...register("priority", { required: false })}/>
                </div>
                </label>
                <label>
                    Add Images
                
                <div>
                    <input type="file" name="input-photos" id="photos" multiple accept="image/*"/>
                </div>
                </label>
                <input type="submit" value="Create Task"/>
                </form>
        </div>
    
    );
}

export default CreateTask