import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import Notifications from "../pages/Notifications";
jest.mock("../components/Header", () => () => <div data-testid="mock-header" />);

jest.mock("../components/Notification", () => ({ notif, toggleRead }) => (
    <tr>
        <td>
            <input data-testid={`checkbox-${notif.id}`} type="checkbox" checked={notif.read} onChange={() => toggleRead(notif.id)} />
        </td>
        <td>{notif.team}</td>
        <td>{notif.message}</td>
    </tr>
));

describe("Notifications Component", () => {
    test("renders the Notifications header", () => {
        render(<Notifications />);
        expect(screen.getByText("Notifications")).toBeInTheDocument();
    });

    test("displays both read and unread notifications", () => {
        render(<Notifications />);
        expect(screen.getByText("Unread")).toBeInTheDocument();
        expect(screen.getByText("Read")).toBeInTheDocument();
    });

    test("toggles notification read status when checkbox is clicked", () => {
        render(<Notifications />);
        const checkbox = screen.getByTestId("checkbox-1"); // first notification
        expect(checkbox).not.toBeChecked();

        fireEvent.click(checkbox);
        expect(checkbox).toBeChecked(); // should now be marked as read
    });
});
