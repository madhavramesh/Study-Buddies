import React from 'react';
import './HomeStyle.css';
import EmojiObjectsIcon from '@material-ui/icons/EmojiObjects';

function SearchBar() {
  return (
    <div className="search-container">
      <div className="search-bar">
        <label htmlFor="search">
          Search
          <br />
          <input type="text" name="search" id="search" placeholder="Search for classes..." />
        </label>
      </div>
      <div className="search-hints">
        <p>
          <EmojiObjectsIcon />
          Search for classes by class name or number
        </p>
      </div>
    </div>
  );
}

export default SearchBar;
