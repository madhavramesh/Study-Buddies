import React from 'react';
import './App.scss';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Signin from './components/Signin';
import Signup from './components/Signup';
import AboutPage from './components/AboutPage';
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
import Preferences from './components/Preferences';
import HomePage from './pages/HomePage';
import DashboardPage from './pages/DashboardPage';
import OwnerDashboardPage from './pages/OwnerDashboardPage';


const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact path="/" component={HomePage} />
          <Route path="/preferences" component={Preferences} />
          <Route path="/carddemo" component={() => <TimesPane slotLength={60} />} />
          <Route path="/peoplePane" component={() => <PeoplePane people={testPeople} />} />
          <Route exact path="/" component={HomePage} />
          <Route path="/signin" component={Signin} />
          <Route path="/signup" component={Signup} />
          <Route path="/about" component={AboutPage} />
          <Route path="/dashboard" component={DashboardPage} />
          <Route path="/owner-dashboard" component={OwnerDashboardPage} />
          <Route path="/profile" component={Signin} />
          {/* <Route path="/preferences" component={Preferences} />
          <Route path="/carddemo" component={() => <TimesPane slotLength={30} />} /> */}
        </Switch>
      </Router>
    </div>
  );
};

export default App;
