import React from 'react';
import './App.scss';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import NavBar from './components/Navbar';
import Signin from './components/Signin';

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact path="/" component={NavBar} />
          <Route path="/signin" component={Signin} />
        </Switch>
      </Router>
    </div>
  );
};

export default App;
