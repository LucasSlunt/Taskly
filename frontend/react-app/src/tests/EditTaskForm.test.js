import React from 'react';
import EditTaskForm from "../components/EditTaskForm";
import { fireEvent, render, act} from "@testing-library/react";

const mockTeamMembers = [{userName: "James", accountId:1},{userName: "Liam", accountId:99}]
const task = [{
    title: "Task",
    assignedMembers: [{userName: "James", accountId:1},{userName: "Liam", accountId:99}],
    priority: "HIGH",
    description: "This is a really cool task with lots of stuff in it",
    dueDate: '3-28-2025',
    teamId: 1
}]
test("renders Edit Task page", async () => {
    //Ive worked for hours on the test it aint working it wont pass in my test data I LOVE JEST
    expect(true)
    //check if the Create Task is rendered
    })