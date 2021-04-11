import React from 'react';
import './App.scss';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import NavBar from './components/Navbar';
import Footer from './components/Footer';
import Signin from './components/Signin';
import Signup from './components/Signup';
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
          <Route exact path="/footer" component={Footer} />
          <Route path="/signin" component={Signin} />
          <Route path="/signup" component={Signup} />
          <Route path="/preferences" component={Preferences} />
          <Route path="/carddemo" component={() => <TimesPane slotLength={30} />} />
        </Switch>
      </Router>
    </div>
  );
};

export default App;
