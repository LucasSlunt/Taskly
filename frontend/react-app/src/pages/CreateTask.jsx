import fakeTeamData from "../FakeData/fakeTeamData.json"
import Header from "../components/Header"
import CreateTaskForm from "../components/CreateTaskForm"
import "../css/CreateTask.css"
function CreateTask(){

    return (
        <div className="page">
            <div className="pageBox">
                <Header/>
                <div className="body">
                    <CreateTaskForm
                    team={fakeTeamData}
                    />
                </div>
                    
            </div>
        </div>
    );
}

export default CreateTask