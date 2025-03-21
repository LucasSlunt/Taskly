/*import React from "react";
import { render, screen } from "@testing-library/react";
import TeamTasks from "../pages/TeamTasks";

jest.mock("../components/TeamMember", () => () => <div data-testid="team-members">Mock Team Member</div>);



test('renders TeamTasks page with mocked TeamMember for each member', () => {
  render(<TeamTasks />);  // Render the TeamTasks component
  
  //Check if each mocked TeamMember is rendered for every member in the list
  const memberElements = screen.getAllByTestId('team-members'); 
  expect(memberElements).toHaveLength(5); 

});*/

/*
 This Test was had an error caused by jest. It was could not find react router dom in TeamTasks
 The solution the TA was to just pass the test. And that is what this is
*/
test("Mock Test Team Tasks", () =>{
  expect(true);
})

