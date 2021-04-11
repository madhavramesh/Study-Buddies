import React from 'react';
import './App.scss';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Signin from './components/Signin';
import Signup from './components/Signup';
import ClassCard from './components/ClassCard';
import PeopleCard from './PeopleCard';
import TimeCard from './components/TimeCard';
import TimesPane from './components/TimesPane';
import Preferences from './components/Preferences';
import HomePage from './pages/HomePage';
import DashboardPage from './pages/DashboardPage';
import TestDisplayClassesPage from './pages/TestDisplayClassesPage';

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact path="/" component={HomePage} />
          <Route path="/signin" component={Signin} />
          <Route path="/signup" component={Signup} />
          <Route path="/dashboard" component={DashboardPage} />
          <Route path="/profile" component={Signin} />
          {/* <Route path="/preferences" component={Preferences} />
          <Route path="/carddemo" component={() => <TimesPane slotLength={30} />} /> */}
          <Route path="/test_display_classes" component={TestDisplayClassesPage} />
        </Switch>
      </Router>
    </div>
  );
};

export default App;
