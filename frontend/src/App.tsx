import React from 'react';
import './App.scss';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Signin from './components/Signin';
import Signup from './components/Signup';
import AboutPage from './pages/AboutPage';
import Preferences from './components/Preferences';
import HomePage from './pages/HomePage';
import DashboardPage from './pages/DashboardPage';
import OwnerDashboardPage from './pages/OwnerDashboardPage';
import PeoplePane from './components/PeoplePane';
import StudentDashboardPage from './pages/StudentDashboardPage';
import ProfilePage from './pages/ProfilePage';
import PreferencesButton from './components/PreferencesButton';

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact path="/" component={HomePage} />
          <Route path="/preferences" component={Preferences} />
          <Route path="/signin" component={Signin} />
          <Route path="/signup" component={Signup} />
          <Route path="/about" component={AboutPage} />
          <Route path="/dashboard" component={DashboardPage} />
          <Route path="/profile" component={Signin} />
          <Route path="/profile-page" component={ProfilePage} />
          <Route path="/class/owner/:classID" component={OwnerDashboardPage} />
          <Route path="/class/student/:classID" component={StudentDashboardPage} />
          <Route path="/test_people_pane" component={PeoplePane} />
          <Route path="/test_preferences_modal" component={PreferencesButton} />
          {/* <Route path="/preferences" component={Preferences} /> */}
          {/* <Route path="/carddemo" component={() => <TimesPane slotLength={60} />} /> */}
        </Switch>
      </Router>
    </div>
  );
};

export default App;
