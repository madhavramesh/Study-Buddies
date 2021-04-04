import './App.css';
import {AwesomeButton} from "react-awesome-button";
import "react-awesome-button/dist/styles.css";
import axios from 'axios';
import React, { useState, useEffect, useRef } from 'react';

function UserData() {

    const [userID, setUserID] = useState(null);
    const [userData, setData] = useState([]);

    const UserdataTable = () => {
        return (
            <div className={'container'}>
                <table>
                    <thead>
                    <tr>
                        <th>Time</th>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Latitude</th>
                        <th>Longitude</th>
                    </tr>
                    </thead>
                    <tbody>
                    {userData.slice(0).reverse().map((r) => (
                        <tr>
                            <td>
                                {r[0]}
                            </td>
                            <td>
                                {r[1]}
                            </td>
                            <td>
                                {r[2]}
                            </td>
                            <td>
                                {r[3]}
                            </td>
                            <td>
                                {r[4]}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        )
    }

    const requestUserData = (id) => {
        console.log("requesting user data");
        console.log(id);
        // data to send to the handler
        const toSend = {
            id: id
        };
        console.log(toSend);
        // configurations to send to handler
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        // Make a post request
        axios.post(
            "http://localhost:4567/user",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data);
                //tileCache[coordinates[0], coordinates[1]] = response.data["ways"];
                setData(old => response.data["user"]);
            })

            .catch(function (error) {
                console.log(error);
            });
    }

    function UserIDButton () {
        return <AwesomeButton type="primary" onPress={() => {
            if (!(userID == null)) {
                requestUserData(userID);
            }
        }}>Get User Data</AwesomeButton>
    }

    return (
        <div className="UserData">
            <head>
                <title>Request User Data</title>
            </head>
            <header>
                <h1>Live check-ins</h1>
            </header>
            <TextBox label={"Enter User ID"} change={setUserID}/>
            <UserIDButton/>
            <UserdataTable/>
        </div>
    );
}

function TextBox(props) {
    const onChange = (event) => {
        props.change(event.target.value);
    };

    return (
        <div className="TextBox">
            <header className="TextBox-header">
                <p>{props.label}</p>
            </header>
            <input type={'text'} id={props.label} onChange={onChange}/>
        </div>
    )
}

export default UserData;
