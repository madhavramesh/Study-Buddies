import React, { useState } from 'react';
import { Button, Form, FormControl, InputGroup } from 'react-bootstrap';
import SearchIcon from '@material-ui/icons/Search';
import './SearchBarStyle.scss';

type SearchBarProps = {
  onChange: any;
};

const SearchBar: React.FC<SearchBarProps> = ({ onChange }: SearchBarProps) => {
  return (
    <>
      <div className="search-header">Search</div>
      <InputGroup className="search-bar-input-group mb-2">
        <InputGroup.Prepend>
          <Button variant="outline-light">
            <SearchIcon />
          </Button>
        </InputGroup.Prepend>
        <FormControl
          type="text"
          placeholder="Search for classes"
          className="search-bar-form-control mr-sm-2"
          onChange={onChange}
        />
      </InputGroup>
      <Form.Text className="text-muted">Search for classes by class name or number</Form.Text>
    </>
  );
};

export default SearchBar;
