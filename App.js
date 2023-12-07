import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './Login';
import Home from './Home';
import TaskForm from './TaskForm';
import TaskDetails from './TaskDetails';

const App = () => {
  return (
    <Router>
      <div>
        <h1>Time sheet management</h1>
        <Routes>
          <Route path="/" element={<Login/>} />
          <Route path="/home" element={<Home/>} />
          <Route path="/create-task" element={<TaskForm/>} />
          <Route path="/task/:id" element={<TaskDetails/>} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
