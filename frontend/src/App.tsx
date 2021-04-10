import React from 'react';
import './App.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import NavBar from './components/Navbar';
import Preferences from './components/Preferences';
import ClassCard from './components/ClassCard';
import PeopleCard from './PeopleCard';

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact path="/" component={NavBar} />
          <Route path="/preferences" component={Preferences} />
          <Route
            path="/carddemo"
            component={() => <PeopleCard firstName="Akash" lastName="Singirikonda" id="asingir1" />}
          />
        </Switch>
      </Router>
    </div>
  );
};

export default App;
