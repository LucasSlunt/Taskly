import fakeTeamData from "../FakeData/fakeTeamData.json"
import Header from "../components/Header"
import CreateTaskForm from "../components/CreateTaskForm"
import "../css/CreateTask.css"
function CreateTask(){

    return (
        <div className='pageContainer'>
            <Header/>
            <div className='pageBody'>

                    <CreateTaskForm
                    team={fakeTeamData}
                    />
                    
            </div>
        </div>
    );
}

export default CreateTask