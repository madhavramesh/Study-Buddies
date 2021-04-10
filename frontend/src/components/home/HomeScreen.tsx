import React from 'react';
import SearchBar from './SearchBar';
import Sidebar from './Sidebar';

function HomeScreen() {
  return (
    <div className="home-screen">
      <Sidebar />
      <SearchBar />
    </div>
  );
}

export default HomeScreen;
