import CreateTask from "./pages/CreateTask";
import { render, screen } from "@testing-library/react";
test("renders Task page", () => {
    render(<CreateTask/>);
  
    //check if the Create Task is rendered
    expect(screen.getByText("Task Name")).toBeInTheDocument()

  });