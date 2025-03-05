import {useTable, useSortBy} from 'react-table'
import React, { use } from 'react';
import "../css/TaskList.css"
import fakeData from "../fakeTaskData.json"
import SearchFilterSort from './SearchFilterSort';
import { useState} from 'react';
import $ from 'jquery'



const mockData = {
    name: "Task",
    team: "Team 1",
    assignees: "James Liam Evan Kelly",
    status: "in progress",
    priority: "Low",
    dueDate: "2/15/2025"
}


function TaskList(){
    
    const [searchForThis, setSearch] = useState({value:"name"})
    const [searchQuery, setSearchQuery] = useState(""); //creates a state variable "searchQuery" and function "setSearchQuery" to update it
    //whenever the searchQuery changes, runs the useMemo and filters through the fakeData data to only the task names that contain the searchQuery 
    const filteredData = React.useMemo(()=> {return fakeData.filter((task) =>{
        switch(searchForThis.value){
            case "name":
                return task.name.toLowerCase().includes(searchQuery.toLowerCase())
            case "team":
                return task.team.toLowerCase().includes(searchQuery.toLowerCase())
            case "status":
                return task.status.toLowerCase().includes(searchQuery.toLowerCase())
            case "priority":
                return task.priority.toLowerCase().includes(searchQuery.toLowerCase())
            case "dueDate":
                return task.dueDate.toLowerCase().includes(searchQuery.toLowerCase())
        }}
    );}, [searchQuery],((searchForThis),[setSearch])) ;
    
    const changeSearch = (event) => {
        setSearch(previousState => {
          return {value: event.target.value}
        });
      }
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
    
    const { getTableBodyProps, getTableProps, rows, prepareRow, headerGroups} = useTable({columns,data: filteredData}, useSortBy)
    return(
        
        <div className='container'>
            <select name="searchThis" id="searchThis" onChange={changeSearch}>
                <option value="name" >Task Name</option>
                <option value="team">Team Name</option>
                <option value="assignees">Assignee(s)</option>
                <option value="priority">Priority</option>
                <option value="status">Status</option>
                <option value="dueDate">Due Date</option>
            </select>
            
            {/* pass searchQuery and setSearchQuery to SearchFilterSort component */}
            <SearchFilterSort
                searchQuery={searchQuery}
                setSearchQuery={setSearchQuery}
                searchForThis={searchForThis}
            />
            

            <table {...getTableProps()}>
                <thead>
                    {headerGroups.map((headerGroup) => (
                        <tr {...headerGroup.getHeaderGroupProps()}>
                            {headerGroup.headers.map((column) =>(
                                <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                            {column.render("Header")}
                            <span>
                                {column.isSorted ? (column.isSortedDesc ? '⬇️':'⬆️'): '↕️'}
	                        </span>
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


