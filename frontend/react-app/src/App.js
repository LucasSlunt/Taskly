import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

// Import your pages/components
import Login from './Login';
import Home from './Home';
import ViewTask from './ViewTask';
import Profile from './pages/Profile'
import TeamTasks from './pages/TeamTasks';

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/login" element={<Login/>} />
          <Route path="/home" element={<Home/>}/>
          <Route path="/view-task" element={<ViewTask/>}/>
          <Route path="/profile" element={<Profile/>}/>
          <Route path="/team-tasks" element={<TeamTasks/>}/>

          {/*Default path should be login, unless specified */}
          <Route path="/" exact element={<Login/>} />

          <Route path="*" element={<div>Page Not Found</div>} /> {/*Default route if no match*/}
        </Routes>
      </div>
    </Router>
  );
}

export default App;