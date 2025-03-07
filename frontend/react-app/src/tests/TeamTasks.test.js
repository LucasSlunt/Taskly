import React from "react";
import { render, screen } from "@testing-library/react";
import TeamTasks from "../pages/TeamTasks";

jest.mock("../components/TeamMember", () => () => <div data-testid="members">Mock Team Member</div>);

test('renders TeamTasks page with mocked TeamMember for each member', () => {
  render(<TeamTasks />);  // Render the TeamTasks component
  
  //Check if each mocked TeamMember is rendered for every member in the list
  const memberElements = screen.getAllByTestId('members'); 
  expect(memberElements).toHaveLength(5); 
  

});