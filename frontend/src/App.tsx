import React from 'react';
import './App.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import NavBar from './components/Navbar';
import Preferences from './components/Preferences';
import ClassCard from './components/ClassCard';
import PeopleCard from './components/PeopleCard';
import TimeCard from './components/TimeCard';
import TimesPane from './components/TimesPane';
import PeoplePane from './components/PeoplePane';

const testPeople = [
  { firstName: 'Madhav', lastName: 'Ramesh', id: 1 },
  { firstName: 'Richard', lastName: 'Tang', id: 2 },
  { firstName: 'Jack', lastName: 'Cheng', id: 3 },
  { firstName: 'Akash', lastName: 'Singirikonda', id: 4 },
];

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact path="/" component={NavBar} />
          <Route path="/preferences" component={Preferences} />
          <Route path="/carddemo" component={() => <TimesPane slotLength={60} />} />
          <Route path="/peoplePane" component={() => <PeoplePane people={testPeople} />} />
        </Switch>
      </Router>
    </div>
  );
};

export default App;
