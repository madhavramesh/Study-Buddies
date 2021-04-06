import React from 'react';
import './App.css';
import Login from "./components/login/Login";
import Register from "./components/login/Register";
import HomeScreen from "./components/home/HomeScreen";
import SearchBar from "./components/home/SearchBar";
import {BrowserRouter, Switch, Route} from "react-router-dom";

function App() {
    return (
        <BrowserRouter>
            <Switch>
                <Route path='/signin' component={Login} exact/>
                <Route path='/signup' component={Register}/>
                <Route path='/home' component={HomeScreen}/>
            </Switch>
        </BrowserRouter>
    );
}

export default App;
