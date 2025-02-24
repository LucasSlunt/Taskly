import { render, screen } from "@testing-library/react";
import UserInfo from "../components/UserInfo";

test("renders user info with name, username, email, and profile picture", () => {
    const { getByAltText, getByText } = render(<UserInfo />);

    //Check if name, username, and email are displayed
    expect(getByText("Bobby Joe")).toBeInTheDocument();
    expect(getByText("bobbyjoe")).toBeInTheDocument();
    expect(getByText("bobbyjoe@example.com")).toBeInTheDocument();

    //Check if profile picture is rendered
    const profileImage = getByAltText("Profile Picture");
    expect(profileImage).toBeInTheDocument();
    expect(profileImage).toHaveAttribute("src", "https://i.pravatar.cc/150?img=12");
    expect(profileImage).toHaveAttribute("alt", "Profile Picture");
});