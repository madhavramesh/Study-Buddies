import React from 'react';
import './App.css';
import Login from "./components/login/Login";
import Register from "./components/login/Register";
import {BrowserRouter, Switch, Route} from "react-router-dom";

function App() {
    return (
        <BrowserRouter>
            <div className="main-container">
                <div className='left-side'></div>
                <div className='right-side'>
                    <Switch>
                        <Route path='/' component={Login} exact />
                        <Route path='/register' component={Register} />
                    </Switch>
                </div>
            </div>
        </BrowserRouter>
    );
}

export default App;
