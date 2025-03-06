import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

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
        <Switch>
          {/* Define routes to pages/components */}
          <Route path="/" exact component={HomePage} />
          <Route path="/about" component={AboutPage} />
          <Route path="/contact" component={ContactPage} />
          {/* Add a default route or 404 page */}
          <Route component={() => <div>Page Not Found</div>} />
        </Switch>
      </div>
    </Router>
  );
}

export default App;