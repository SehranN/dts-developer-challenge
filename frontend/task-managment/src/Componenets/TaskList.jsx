"use client"

import { useEffect, useState } from "react"
import api from "../api"
import { Clock, Check, Edit, Trash, Plus } from "react-feather"

const TaskList = ({ onCreateTask, onEditTask }) => {
  const [tasks, setTasks] = useState([])

  useEffect(() => {
    fetchTasks()
  }, [])

  const fetchTasks = () => {
    api
      .get("/jds")
      .then((res) => {
        setTasks(res.data)
        console.log(res.data)
      })
      .catch((err) => console.error(err))
  }

  const formatToDisplayDate = (dateString) => {
    if (!dateString) return "";
    const date = new Date(dateString);
    return date.toISOString().split("T")[0];
  }

  const markAsComplete = (id) => {
    const task = tasks.find((t) => t.id === id)
    if (task) {
      api
        .put(`/jds/${id}`, { ...task, completed: true })
        .then(() => {
          fetchTasks()
        })
        .catch((err) => console.error(err))
    }
  }

  const deleteTask = (id) => {
    if (window.confirm("Are you sure you want to delete this task?")) {
      api
        .delete(`/jds/${id}`)
        .then(() => {
          fetchTasks()
        })
        .catch((err) => console.error(err))
    }
  }

  return (
    <div className="max-w-md mx-auto">
      <div className="phone-container bg-white rounded-3xl shadow-xl overflow-hidden border-8 border-gray-800 p-1">
        <div className="phone-notch bg-gray-800 h-5 w-1/3 mx-auto rounded-b-lg mb-2"></div>
        <div className="phone-screen bg-gray-100 rounded-2xl p-4 h-[600px] overflow-y-auto">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-semibold">My Tasks</h2>
            <button
              onClick={onCreateTask}
              className="bg-blue-500 text-white p-2 rounded-full hover:bg-blue-600 transition-colors"
            >
              <Plus size={20} />
            </button>
          </div>

          {tasks.length === 0 ? (
            <div className="text-center py-10 text-gray-500">
              <p>No tasks yet. Create your first task!</p>
            </div>
          ) : (
            <ul className="space-y-3">
              {tasks.map((task) => (
                <li key={task.id} className="bg-white p-4 rounded-lg shadow-sm border border-gray-200">
                  <div className="flex items-start justify-between">
                    <div className="flex items-start space-x-3">
                      {task.completed ? (
                        <div className="bg-green-100 p-2 rounded-full">
                          <Check size={18} className="text-green-600" />
                        </div>
                      ) : (
                        <div className="bg-amber-100 p-2 rounded-full">
                          <Clock size={18} className="text-amber-600" />
                        </div>
                      )}
                      <div>
                        <h3 className={`font-medium ${task.completed ? "line-through text-gray-500" : ""}`}>
                          {task.title}
                        </h3>
                        <p className="text-xs text-gray-500 mt-1">Due: {formatToDisplayDate(task.dueDate) || "No due date"}</p>
                      </div>
                    </div>
                    <div className="flex space-x-2">
                      <button onClick={() => onEditTask(task.id)} className="text-blue-500 hover:text-blue-700">
                        <Edit size={16} />
                      </button>
                      <button onClick={() => deleteTask(task.id)} className="text-red-500 hover:text-red-700">
                        <Trash size={16} />
                      </button>
                    </div>
                  </div>

                  {!task.completed && (
                    <button
                      onClick={() => markAsComplete(task.id)}
                      className="mt-3 text-xs bg-green-50 text-green-600 py-1 px-2 rounded hover:bg-green-100 transition-colors"
                    >
                      Mark as complete
                    </button>
                  )}
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  )
}

export default TaskList
