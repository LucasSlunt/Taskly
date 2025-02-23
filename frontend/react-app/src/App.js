import logo from './logo.svg';
import './App.css';

import React from 'react';

//routing for pages?
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

//Login page
import Login from'./Login';

const App = () => {
  return (
    <Router>
      <nav>
        <a href="/login">Login</a>
      </nav>

      <Routes>
        <Route path="/Login" element={<Login/>}></Route>
      </Routes>
    </Router>
  );
}

export default App;
