import fakeTeamData from "../FakeData/fakeTeamData.json"
import Header from "../components/Header"
import CreateTaskForm from "../components/CreateTaskForm"
import "../css/CreateTask.css"
import { useCookies } from 'react-cookie';
function CreateTask(){

    const [cookies] = useCookies(['userInfo'])
    const userId = cookies.userInfo.accountId

    return (
        <div className='pageContainer'>
            <Header/>
            <div className='createTaskPageBody'>

                    <CreateTaskForm
                    team={fakeTeamData}
                    userId={userId}
                    />
                    
            </div>
        </div>
    );
}

export default CreateTask