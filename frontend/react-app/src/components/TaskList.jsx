import {useTable} from 'react-table'
import React from 'react';
import "../css/TaskList.css"
import fakeData from "../fakeTaskData.json"
import SearchFilterSort from './SearchFilterSort';
import { useState } from 'react';



const mockData = {
    name: "Task",
    team: "Team 1",
    assignees: "James Liam Evan Kelly",
    status: "in progress",
    priority: "Low",
    dueDate: "2/15/2025"
}

function TaskList(){
    const [searchQuery, setSearchQuery] = useState(""); //creates a state variable "searchQuery" and function "setSearchQuery" to update it
    
    //whenever the searchQuery changes, runs the useMemo and filters through the fakeData data to only the task names that contain the searchQuery 
    const filteredData = React.useMemo(()=> {return fakeData.filter((task) =>
        task.name.toLowerCase().includes(searchQuery.toLowerCase())
    );}, [searchQuery]) ;
    const columns = React.useMemo(() => [
        {
            Header: "Task Name",
            accessor: "name",
        },
        {
            Header: "Team",
            accessor:"team",
        },
        {
            Header: "Assignee(s)",
            accessor: "assignees",
        },
        {
            Header: "Status",
            accessor: "status",
        },
        {
            Header: "Priority",
            accessor: "priority",
        },
        {
            Header: "Due Date",
            accessor: "dueDate",
        }
    ],
    [])

    const {getTableProps, getTableBodyProps, headerGroups, rows, prepareRow} = useTable({columns,data: filteredData})
    return(
        
        <div className='container'>
            {/* pass searchQuery and setSearchQuery to SearchFilterSort component */}
            <SearchFilterSort
                searchQuery={searchQuery}
                setSearchQuery={setSearchQuery}
            />
            

            <table {...getTableProps()}>
                <thead>
                    {headerGroups.map((headerGroup) => (
                        <tr {...headerGroup.getHeaderGroupProps()}>
                            {headerGroup.headers.map((column) =>(
                                <th {...column.getHeaderProps()}>
                            {column.render("Header")}
                            </th>
                            ))}
                        </tr>
                    ))}
                </thead>
                <tbody{...getTableBodyProps()}>
                        {rows.map((row)=>{
                            prepareRow(row)
                            return( 
                                <tr {...row.getRowProps()}>
                                    {row.cells.map((cell)=>(
                                        <td {...cell.getCellProps()}>{cell.render("Cell")}</td>
                                    ))}
                                </tr>
                            )
                        })}
                </tbody>
            </table>
        </div>

    );
}

export default TaskList


