import { render, screen } from '@testing-library/react';
import Notifications from '../pages/Notifications'

test("renders Notifications page", () => {
    render(<Notifications/>);
});