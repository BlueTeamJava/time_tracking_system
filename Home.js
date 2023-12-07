import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import API_BASE_URL from './apiConfig';
import './Home.css';

const Home = () => {
  const [tasks, setTasks] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const authToken = localStorage.getItem('token');
        const response = await axios.get(`${API_BASE_URL}/list`, {
          headers: {
            Authorization: `${authToken}`,
          },
        });
        setTasks(response.data);
        setLoading(false);
      } catch (error) {
        setError('Error fetching tasks');
        setLoading(false);
      }
    };

    fetchTasks();
  }, []);

  const handleDelete = async (taskId) => {
    try {
      const authToken = localStorage.getItem('token');
      await axios.delete(`${API_BASE_URL}/list?id=${taskId}`, {
        headers: {
          Authorization: `${authToken}`,
        },
      });

      const updatedTasks = tasks.filter((task) => task.id !== taskId);
      setTasks(updatedTasks);
    } catch (error) {
      setError('Error deleting task');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');

   navigate('/');
  };


  return (
    <div className="container">
    <h2>Task List</h2>
    {loading && <div>Loading...</div>}
    {error && <div>Error: {error}</div>}
    {tasks.length === 0 && !loading && <div>List is empty</div>}
    <table className="task-table">
      <thead>
        <tr>
          <th>Date</th>
          <th>Description</th>
          <th>Hours</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        {tasks.map((task) => (
          <tr key={task.id}>
            <td>{task.date}</td>
            <td>
              <Link to={`/task/${task.id}`}>{task.description}</Link>
            </td>
            <td>{task.hours}</td>
            <td>
              <button onClick={() => handleDelete(task.id)}>Delete</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
    <Link to="/create-task">
      <button>Create Task</button>
    </Link>
    <button onClick={handleLogout}>Logout</button>
  </div>
  );
};

export default Home;
