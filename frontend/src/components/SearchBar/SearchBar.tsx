import React, { useState } from 'react';
import { Button, Form, FormControl } from 'react-bootstrap';
import './SearchBarStyle.scss';

const SearchBar = () => {
  return (
    <Form className="search-bar-form" inline>
      <FormControl type="text" placeholder="Search" className="search-bar-form-control mr-sm-2" />
    </Form>
  );
};

export default SearchBar;
