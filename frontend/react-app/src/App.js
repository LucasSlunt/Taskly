import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

// Import your pages/components
import Login from './Login';
import Home from './Home';
import ViewTask from './pages/ViewTask';
import Profile from './pages/Profile'
import TeamTasks from './pages/TeamTasks';
import MyTasks from './pages/MyTasks';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/login" element={<Login/>} />
          <Route path="/home" element={<ProtectedRoute allowedRoles={['admin', 'teamMember']} protectedContent={<Home/>} urlReirect={"/login"}></ProtectedRoute>}/>
          <Route path="/view-task" element={<ProtectedRoute allowedRoles={['admin', 'teamMember']} protectedContent={<ViewTask/>} urlReirect={"/login"}></ProtectedRoute>}/>
          <Route path="/profile" element={<ProtectedRoute allowedRoles={['admin', 'teamMember']} protectedContent={<Profile/>} urlReirect={"/login"}></ProtectedRoute>}/>
          <Route path="/team-tasks" element={<ProtectedRoute allowedRoles={['admin', 'teamMember']} protectedContent={<TeamTasks/>} urlReirect={"/login"}></ProtectedRoute>}/>
          <Route path="/my-tasks" element={<ProtectedRoute allowedRoles={['admin', 'teamMember']} protectedContent={<MyTasks/>} urlReirect={"/login"}></ProtectedRoute>}/>

          {/*Default path should be login, unless specified */}
          <Route path="/" exact element={<Login/>} />

          <Route path="*" element={<div>Page Not Found</div>} /> {/*Default route if no match*/}
        </Routes>
      </div>
    </Router>
  );
}

export default App;