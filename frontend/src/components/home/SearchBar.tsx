import React from "react";
import './HomeStyle.css';

function SearchBar() {
    return (
        <div className='search-container'>
            <div className='title'>
                Search
            </div>
            <div className='search-bar'>
                <label htmlFor='search'></label>
                <input type="text" name='search' id='search' placeholder='Search for classes...'/>
            </div>
            <div className='search-hints'>
                <p>
                    <i className="far fa-lightbulb"></i>
                Search for classes by class name or number
                </p>
            </div>
        </div>
    )
}

export default SearchBar;