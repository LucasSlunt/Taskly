import CreateTaskForm from "./components/CreateTaskForm";
import { render, screen } from "@testing-library/react";
import fakeData from "./FakeData/fakeTeamData.json"
test("renders Task page", () => {
    render(<CreateTaskForm
        team={fakeData}
    />);
  
    //check if the Create Task is rendered with all team members as options is rendered
    fakeData.map((teamMember)=>(
        expect(screen.getByText(teamMember.name)).toBeInTheDocument()
    ))
  });