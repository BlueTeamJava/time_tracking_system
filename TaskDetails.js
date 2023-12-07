import { useParams, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import './TaskDetails.css';

const TaskDetails = () => {
  const { id } = useParams();
  const [task, setTask] = useState(null);
  const [error, setError] = useState(null);
  const [editedTask, setEditedTask] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchTask = async () => {
      try {
        const authToken = localStorage.getItem('token');
        const response = await axios.get(`http://localhost:8080/time_tracking_system/task?id=${id}`, {
          headers: {
            Authorization: `${authToken}`,
          },
        });
        setTask(response.data);
        setEditedTask(response.data);
      } catch (error) {
        setError('Error fetching task');
      }
    };

    fetchTask();
  }, [id]);

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleSave = async () => {
    try {
      const authToken = localStorage.getItem('token');
      await axios.put(`http://localhost:8080/time_tracking_system/task?id=${id}`, editedTask, {
        headers: {
          Authorization: `${authToken}`,
          'Content-Type': 'application/json',
        },
      });

      setTask(editedTask);
      setIsEditing(false);
      setError(null);
    } catch (error) {
      setError('Error updating task');
    }
  };

  const handleCancel = () => {
    setIsEditing(false);
    setEditedTask(task);
    setError(null);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
  
    let updatedValue = value;
  
    if (name === 'date') {
      const dateObj = new Date(value);
      updatedValue = dateObj.toISOString().split('T')[0];
    }
  
    setEditedTask({ ...editedTask, [e.target.name]: e.target.value });
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const formattedDate = date.toISOString().split('T')[0];
    return formattedDate;
  };

  const handleBack = () => {
    navigate('/home');
  };

  return (
    <div className="task-details">
      <h2>Task Details for Task ID: {id}</h2>
      {error && <div>Error: {error}</div>}
      {task && (
        <div>
          {isEditing ? (
            <div>
              <label>
                Date:
                <input type="date" name="date" defaultValue={formatDate(editedTask.date)} onChange={(e) => handleInputChange(e)} />
              </label>
              <label>
                Description:
                <input
                  type="text"
                  name="description"
                  value={editedTask.description}
                  onChange={handleInputChange}
                />
              </label>
              <label>
                Hours:
                <input type="text" name="hours" value={editedTask.hours} onChange={handleInputChange} />
              </label>
              <button onClick={handleSave}>Save</button>
              <button onClick={handleCancel}>Cancel</button>
            </div>
          ) : (
            <div>
              <p>Date: {task.date}</p>
              <p>Description: {task.description}</p>
              <p>Hours: {task.hours}</p>
              <button onClick={handleEdit}>Edit</button>
            </div>
          )}
        </div>
      )}
      <button onClick={handleBack}>Back to Home</button>
    </div>
  );
};

export default TaskDetails;
