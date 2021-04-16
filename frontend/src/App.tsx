import React from 'react';
import './App.scss';
import { BrowserRouter as Router, Switch, Route, Redirect } from 'react-router-dom';
import Signin from './components/Signin';
import Signup from './components/Signup';
import AboutPage from './pages/AboutPage';
import Preferences from './components/Preferences';
import HomePage from './pages/HomePage';
import DashboardPage from './pages/DashboardPage';
import OwnerDashboardPage from './pages/OwnerDashboardPage';
import StudentDashboardPage from './pages/StudentDashboardPage';
import ErrorPage from './pages/ErrorPage';
import PrivateRoute from './pages/PrivateRoute';

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact path="/" component={HomePage} />
          <Route path="/signin" component={Signin} />
          <Route path="/signup" component={Signup} />
          <Route path="/about" component={AboutPage} />
          <PrivateRoute path="/dashboard" component={DashboardPage} />
          <PrivateRoute path="/class/owner/:classID" component={OwnerDashboardPage} />
          <PrivateRoute path="/class/student/:classID" component={StudentDashboardPage} />
          <PrivateRoute path="/preferences" component={Preferences} />
          <Route path="*" component={ErrorPage} />
          <Redirect to="/error" />
        </Switch>
      </Router>
    </div>
  );
};

export default App;
