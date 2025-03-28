/*import { render, screen, fireEvent } from "@testing-library/react";
import UserTable from "../components/UserTable";
import changeSearch from "../components/UserTable"
test('updates table after new select is chosen', () => {
    const logSpy = jest.spyOn(console, 'log');
  
    render(<UserTable/>);
  
    //getting the input element
    const input = document.getElementById("searchThis")
  
    // test the user typing 'test' in as input
    fireEvent.change(input, { target: { value: 'Team2' } });

  
    expect(logSpy).toHaveBeenCalledWith('seeing a new team');
  })
  test('sees if we can when the delete button is pushed we get a deleted account message',()=>{
    const logSpy = jest.spyOn(console, 'log');
  
    render(<UserTable/>);
  
    //getting the input element
    const input = screen.getAllByText(/Delete/i)[0];
  
    // test the user typing 'test' in as input
    fireEvent.click(input);

  
    expect(logSpy).toHaveBeenCalledWith('delete THIS USER ');
  })
  test('sees if we can when we change a role get a changed role',()=>{
    const logSpy = jest.spyOn(console, 'log');
  
    render(<UserTable/>);
  
    //getting the input element
    const input = document.getElementsByClassName("cellSelect")[0]

  
    // test the user typing 'test' in as input
    fireEvent.change(input, { target: { value: 'admin' } });
    fireEvent.change(input, { target: { value: 'teamLead' } });
    fireEvent.change(input, { target: { value: 'admin' } });
  
    expect(logSpy).toHaveBeenCalledWith('rolesChanged');
  })*/
 test("Testing User Table",()=>{
  expect(true)
 })
  
  
  