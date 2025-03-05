import fakeTeamData from "../FakeData/fakeTeamData.json"
import Header from "../Header"
function CreateTask(){
    function createJSONFromForm(){
        let name = document.getElementById("name").value
            
    }
        

    return (
        <div>
            <Header/>
            <form action="">
                <label>
                    Task Name
                </label>
                <div>
                <input type="text" name="Name Of Task" id="name" />
                </div>
                <div>
                    Assign To:
                </div>
                {fakeTeamData.map((teamMember)=>(
                    <div>
                        {teamMember.name}
                        <input type="checkbox" name="checkTeamMemeber" id={teamMember.id} />
                    </div>
                ))}
                <div>
                    Set Priority
                </div>
                <div>
                    <select name="setPrioity" id="setPrioity">
                        <option value="Low">Low</option>
                        <option value="Medium">Medium</option>
                        <option value="High">High</option>
                    </select>
                </div>
                <div>
                    Add Discription
                </div>
                <div>
                    <input type="text" name="input-discription" id="discription" />
                </div>
                <div>
                    Add Images
                </div>
                <div>
                    <input type="file" name="input-photos" id="photos" multiple accept="image/*"/>
                </div>
                <div>
                    <input type="button" value="Create Task" onClick={createJSONFromForm()}/>
                </div>
                </form>
        </div>
    
    );
}

export default CreateTask