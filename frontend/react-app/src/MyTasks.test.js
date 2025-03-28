import React from "react";
import { render, screen } from "@testing-library/react";
import MyTasks from "./pages/MyTasks"

//jest mock tests for the page
jest.mock("./pages/MyTasks", () => () => <div data-testid="task-info">Mock Taskinfo</div>);
//renders
test("renders My Task page", () => {
  render(<MyTasks/>);

  //check if the View task is rendered
  expect(screen.getByTestId("task-info")).toBeInTheDocument();
});