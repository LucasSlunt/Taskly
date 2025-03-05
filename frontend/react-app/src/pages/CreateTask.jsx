import fakeTeamData from "../FakeData/fakeTeamData.json"
import Header from "../Header"
function CreateTask(){
    function task(name, assignees, priority, discription, images){
        this.name = name
        this.assignees = assignees
        this.priority = priority
        this. discription = discription
        this.images = images
    }
    const handleSubmit = (event) => {
        //newTask = task(
        //    event.target.name.value,
        //    )
        console.log(event.target.name.value)          // or directly
      }
        

    return (
        <div>
            <Header/>
            <form onSubmit={handleSubmit}>
                <label>
                    Task Name
                    <div>
                    <input type="text" name="Name Of Task" id="name" required/>
                    </div>
                    </label>
                <label>
                    Assign To:
                {fakeTeamData.map((teamMember)=>(
                    <div>
                        {teamMember.name}
                        <input type="checkbox" name="checkTeamMemeber" id={teamMember.id} />
                    </div>
                ))}
                </label>
                <label>
                    Set Priority
                
                <div>
                    <select name="setPrioity" id="setPrioity" required>
                        <option value="Low">Low</option>
                        <option value="Medium">Medium</option>
                        <option value="High">High</option>
                    </select>
                </div>
                </label>
                <label>
                    Add Discription
                
                <div>
                    <input type="text" name="input-discription" id="discription" />
                </div>
                </label>
                <label>
                    Add Images
                
                <div>
                    <input type="file" name="input-photos" id="photos" multiple accept="image/*"/>
                </div>
                </label>
                <input type="submit" value="Create Task" onClick={handleSubmit()}/>
                </form>
        </div>
    
    );
}

export default CreateTask