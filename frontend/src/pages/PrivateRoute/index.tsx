import React from 'react';
import { Redirect, Route } from 'react-router-dom';

// Taken from https://medium.com/@tomlarge/private-routes-with-react-router-dom-28e9f40c7146
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
const PrivateRoute = ({ component: Component, ...rest }) => {
  return (
    <Route
      // eslint-disable-next-line react/jsx-props-no-spreading
      {...rest}
      render={(props) =>
        sessionStorage.getItem('user_id') ? (
          // eslint-disable-next-line react/jsx-props-no-spreading
          <Component {...props} />
        ) : (
          <Redirect
            to={{
              pathname: '/signin',
              state: { from: props.location },
            }}
          />
        )
      }
    />
  );
};

export default PrivateRoute;
