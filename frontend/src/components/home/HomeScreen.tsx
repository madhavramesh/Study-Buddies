import React from 'react';
import SearchBar from './SearchBar';
import Sidebar from './Sidebar';

const HomeScreen: React.FC = () => {
  return (
    <div className="home-screen">
      <Sidebar />
      <SearchBar />
    </div>
  );
};

export default HomeScreen;
