import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API_BASE_URL from './apiConfig';
import './TaskForm.css'

const TaskForm = () => {
  const [description, setDescription] = useState('');
  const [hours, setHours] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
        const authToken = localStorage.getItem('token');
        const response = await fetch(`${API_BASE_URL}/task`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `${authToken}`
        },
        body: JSON.stringify({ description, hours: parseFloat(hours) }),
      });

      if (response.status === 201) {
        navigate('/home');
      } else if (response.status === 400) {
        console.error('Invalid task data');
      } else {
        console.error('Error:', response.statusText);
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleBack = () => {
    navigate('/home');
  };

  return (
    <div className="form-container">
      <form onSubmit={handleSubmit}>
      <h2>Create Task</h2>
        <label>
          Description:
          <input
            type="text"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </label>
        <label>
          Hours Spent:
          <input
            type="number"
            value={hours}
            onChange={(e) => setHours(e.target.value)}
          />
        </label>
        <div className="button-group">
          <button type="submit">Create Task</button>
          <button onClick={handleBack}>Back to Home</button>
        </div>
      </form>
    </div>
  );
};

export default TaskForm;
