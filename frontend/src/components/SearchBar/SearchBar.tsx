import React, { useState } from 'react';
import { Button, Form, FormControl, InputGroup } from 'react-bootstrap';
import SearchIcon from '@material-ui/icons/Search';
import './SearchBarStyle.scss';

type SearchBarProps = {
  onChange: any;
  placeholderText: string;
  searchInstructions: string;
  showSearchHeader: boolean;
  showSearchDescription: boolean;
};

const SearchBar: React.FC<SearchBarProps> = ({
  onChange,
  placeholderText,
  searchInstructions,
  showSearchHeader,
  showSearchDescription,
}: SearchBarProps) => {
  return (
    <>
      {showSearchHeader && <div className="search-header">Search</div>}
      <InputGroup className="search-bar-input-group mb-2">
        <InputGroup.Prepend>
          <Button variant="outline-light">
            <SearchIcon />
          </Button>
        </InputGroup.Prepend>
        <FormControl
          type="text"
          placeholder={placeholderText}
          className="search-bar-form-control mr-sm-2"
          onChange={onChange}
        />
      </InputGroup>
      {showSearchDescription && (
        <Form.Text className="search-instructions">{searchInstructions}</Form.Text>
      )}
    </>
  );
};

export default SearchBar;
