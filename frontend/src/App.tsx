import React from 'react';
import './App.scss';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Signin from './components/Signin';
import Signup from './components/Signup';
import AboutPage from './components/AboutPage';
import ClassCard from './components/ClassCard';
import PeopleCard from './PeopleCard';
import TimeCard from './components/TimeCard';
import TimesPane from './components/TimesPane';
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
          <Route path="/signin" component={Signin} />
          <Route path="/signup" component={Signup} />
          <Route path="/about" component={AboutPage} />
          <Route path="/dashboard" component={DashboardPage} />
          <Route path="/owner-dashboard" component={OwnerDashboardPage} />
          <Route path="/profile" component={Signin} />
          <Route path="/class/:classID" component={OwnerDashboardPage} />
          {/* <Route path="/preferences" component={Preferences} />
          <Route path="/carddemo" component={() => <TimesPane slotLength={30} />} /> */}
        </Switch>
      </Router>
    </div>
  );
};

export default App;
