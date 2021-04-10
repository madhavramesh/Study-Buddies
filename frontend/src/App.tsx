import React from 'react';
import './App.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import NavBar from './components/Navbar';
import Preferences from './components/Preferences';

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact path="/" component={NavBar} />
          <Route path="/preferences" component={Preferences} />
        </Switch>
      </Router>
    </div>
  );
};

export default App;
