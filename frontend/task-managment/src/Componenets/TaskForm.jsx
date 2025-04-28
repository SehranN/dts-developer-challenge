"use client"

import { useState, useEffect } from "react"
import api from "../api"
import { ArrowLeft } from "react-feather"

const TaskForm = ({ id, onBack }) => {
  const [task, setTask] = useState({
    title: "",
    completed: false,
    dueDate: "",
  })

  const formatToDisplayDate = (dateString) => {
    if (!dateString) return "";
    const date = new Date(dateString);
    return date.toISOString().split("T")[0]; 
  }

  const sanitizeInput = (str) => str.replace(/</g, "&lt;").replace(/>/g, "&gt;");

  useEffect(() => {
    if (id) {
      api
        .get(`/jds/${id}`)
        .then((res) => {
          const taskData = res.data
          
          setTask({
            ...taskData,
            dueDate: formatToDisplayDate(taskData.dueDate),
          });
        })
        .catch((err) => console.error(err))
    }
  }, [id])

  const handleSubmit = (e) => {
    e.preventDefault()

    const method = id ? "put" : "post"
    const url = id ? `/jds/${id}` : "/jds"

    const taskToSubmit = id
      ? {
          ...task,
          completed: task.completed,
        }
      : task
        
    taskToSubmit.title = sanitizeInput(taskToSubmit.title.trim());

    api[method](url, taskToSubmit)
      .then(() => {
        onBack()
      })
      .catch((err) => console.error(err))
  }

  return (
    <div className="max-w-md mx-auto">
      <div className="phone-container bg-white rounded-3xl shadow-xl overflow-hidden border-8 border-gray-800 p-1">
        <div className="phone-notch bg-gray-800 h-5 w-1/3 mx-auto rounded-b-lg mb-2"></div>
        <div className="phone-screen bg-gray-100 rounded-2xl p-4 h-[600px] overflow-y-auto">
          <button onClick={onBack} className="flex items-center text-blue-500 mb-4">
            <ArrowLeft size={16} className="mr-1" /> Back to tasks
          </button>

          <h2 className="text-xl font-semibold mb-6">{id ? "Edit Task" : "Create Task"}</h2>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <label className="block text-sm font-medium text-gray-700">Task Title</label>
              <input
                type="text"
                value={task.title}
                maxLength={100}
                minLength={1}
                onChange={(e) => setTask({ ...task, title: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
                placeholder="Enter task title"
              />
            </div>

            <div className="space-y-2">
              <label className="block text-sm font-medium text-gray-700" for="due-date">Due Date</label>
              <input
                id="due-date"
                type="date"
                min={new Date().toISOString().split("T")[0]}
                value={task.dueDate}
                onChange={(e) => setTask({ ...task, dueDate: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            {id && (
              <div className="bg-gray-50 p-3 rounded-md">
                <p className="text-sm text-gray-600">
                  Status:{" "}
                  <span className={task.completed ? "text-green-600" : "text-amber-600"}>
                    {task.completed ? "Completed" : "Pending"} 
                  </span>
                </p>
                <p className="text-xs text-gray-500 mt-1">
                  Note: Task status can only be changed by marking it as complete from the task list.
                </p>
              </div>
            )}

            <button
              type="submit"
              className="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition-colors"
            >
              {id ? "Update Task" : "Create Task"}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}

export default TaskForm
