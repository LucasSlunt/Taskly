import React from 'react';


const Task = ({taskName, taskTeam, dueDate}) => {
    return (
        <tr>
            <td className="taskName">{taskName}</td>
            <td><button className="miniButton">{taskTeam}</button></td>
            <td>{dueDate}</td>
        </tr>
    )
}

export default Task;