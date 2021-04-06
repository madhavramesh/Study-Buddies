import React from 'react';
import { Link } from 'react-router-dom';
import MenuIcon from '@material-ui/icons/Menu';
import BookIcon from '@material-ui/icons/Book';
import SearchIcon from '@material-ui/icons/Search';
import './HomeStyle.css';

function Sidebar() {
  return (
    <div className="sidebar-container">
      <div className="sidebar-header">
        {/* <img src="https://seeklogo.com/images/S/snorlax-logo-4B47B6B547-seeklogo.com.png"></img> */}
        Chunky Study Buddies
      </div>
      <div className="sidebar-content">
        <div className="classes-page">
          <Link to="/signin">
            <BookIcon className="material-icon" color="disabled" />
            <span className="icon-text">Classes</span>
          </Link>
        </div>
        <br />
        <div className="search-page">
          <Link to="/signup">
            <SearchIcon className="material-icon" color="disabled" />
            <span className="icon-text">Search</span>
          </Link>
        </div>
      </div>
    </div>
  );
}

export default Sidebar;
