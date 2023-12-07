import React from 'react';
import { useNavigate, BrowserRouter as Router, Route, Routes, BrowserRouter } from 'react-router-dom';
import Login from './Login';
import Home from './Home';
import TaskForm from './TaskForm';
import TaskDetails from './TaskDetails';

const NotFound = () => {
  const navigate = useNavigate();
  return (
    <div>
      <h1>Unauthorized Access</h1>
      <p>You are not authorized to access this page.</p>
      <button onClick={() => window.location.href = navigate("/")}>
        Go to Login
      </button>
    </div>
  );
};

const App = () => {
  return (
    <BrowserRouter basename= {window?.location?.pathname ??""}>
      <div>
        <Routes>
          <Route index element={<Login />} />
          <Route path="/home" element={<Home />} />
          <Route path="/create-task" element={<TaskForm />} />
          <Route path="/task/:id" element={<TaskDetails />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
};

export default App;
