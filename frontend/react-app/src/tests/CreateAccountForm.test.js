import { fireEvent, render, screen} from "@testing-library/react"
import CreateAccountForm from "../components/CreateAccountForm"
test('Testing Form To see if onSumbit runs',()=>{
    render(<CreateAccountForm/>)
    let consoleLog = jest.spyOn(console, 'log');
    const inputUserName = screen.getByPlaceholderText('Username');
    const inputEmail = screen.getByPlaceholderText('Email');
    const inputPassword = screen.getByPlaceholderText('Password');
    const sumbit = document.getElementById('createAccountSumbitButton');
    fireEvent.change(inputUserName, { target: { value: 'test' } });
    fireEvent.change(inputEmail, { target: { value: 'test@gmail.com' } });
    fireEvent.change(inputPassword, { target: { value: 'test' } });
    fireEvent.click(sumbit)

    expect(consoleLog == JSON.stringify({username:'test',email:'test@gmail.com', password:'test'}))
});