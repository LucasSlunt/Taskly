import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

// Import your pages/components
import Login from './Login';
import Home from './Home';
import ViewTask from './pages/ViewTask';
import Profile from './pages/Profile'
import TeamTasks from './pages/TeamTasks';
import MyTasks from './pages/MyTasks';
import RequireAuth from '@auth-kit/react-router/RequireAuth'
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/login" element={<Login/>} />
          <Route path="/home" element={<RequireAuth fallbackPath={'/login'}><Home/></RequireAuth>}/>
          <Route path="/view-task" element={<RequireAuth fallbackPath={'/login'}><ViewTask/></RequireAuth>}/>
          <Route path="/profile" element={<RequireAuth fallbackPath={'/login'}><Profile/></RequireAuth>}/>
          <Route path="/team-tasks" element={<RequireAuth fallbackPath={'/login'}><TeamTasks/></RequireAuth>}/>
          <Route path="/my-tasks" element={<RequireAuth fallbackPath={'/login'}><ProtectedRoute allowedRoles={"admin"}><MyTasks/></ProtectedRoute></RequireAuth>}/>

          {/*Default path should be login, unless specified */}
          <Route path="/" exact element={<Login/>} />

          <Route path="*" element={<div>Page Not Found</div>} /> {/*Default route if no match*/}
        </Routes>
      </div>
    </Router>
  );
}

export default App;