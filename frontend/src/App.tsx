import React from 'react';
import './App.scss';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Signin from './components/Signin';
import Signup from './components/Signup';
import HomePage from './components/HomePage';

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact path="/" component={HomePage} />
          <Route path="/signin" component={Signin} />
          <Route path="/signup" component={Signup} />
          {/* <Route path="/preferences" component={Preferences} />
          <Route path="/carddemo" component={() => <TimesPane slotLength={30} />} /> */}
        </Switch>
      </Router>
    </div>
  );
};

export default App;
