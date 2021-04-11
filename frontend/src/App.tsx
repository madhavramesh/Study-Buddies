import React from 'react';
import './App.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import NavBar from './components/Navbar';
import Preferences from './components/Preferences';
import ClassCard from './components/ClassCard';
import PeopleCard from './PeopleCard';
import TimeCard from './components/TimeCard';
import TimesPane from './components/TimesPane';

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact path="/" component={NavBar} />
          <Route path="/preferences" component={Preferences} />
          <Route path="/carddemo" component={() => <TimesPane slotLength={60} />} />
        </Switch>
      </Router>
    </div>
  );
};

export default App;
