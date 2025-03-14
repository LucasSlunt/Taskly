import React from 'react';
import EditTaskForm from "../components/EditTaskForm";
import { fireEvent, render, act} from "@testing-library/react";

const mockTeam = [{
    name: "James"
}]
const task = {
    name: "Task",
    assignees: "James",
    priority: "High",
    discription: "This is a really cool task with lots of stuff in it"}
test("renders Edit Task page", async () => {
    const handleSubmit = jest.fn();
    render(<EditTaskForm team ={mockTeam}
    task = {task}
    />);
    const input = document.getElementById('button');
    //check if the Create Task is rendered
    act(() => {
        fireEvent.click(input, { target: { value: 'Create Task' } })
      });
    expect(input === task)
    })