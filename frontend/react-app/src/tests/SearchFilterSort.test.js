import { render, screen, fireEvent } from "@testing-library/react";
import SearchFilterSort from "../components/SearchFilterSort";

test('updates searchQuery state when typing in the input', () => {
    const setSearchQuery = jest.fn(); //mock the setSearchQuery function
  
    render(<SearchFilterSort searchQuery="" setSearchQuery={setSearchQuery} />);
  
    //getting the input element
    const input = screen.getByPlaceholderText('Search by task name..');
  
    // test the user typing 'test' in as input
    fireEvent.change(input, { target: { value: 'test' } });
  
    //check setSearchQuery function was called with 'test'
    expect(setSearchQuery).toHaveBeenCalledWith('test');
  });
  