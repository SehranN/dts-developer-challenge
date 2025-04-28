"use client"

import { useState } from "react"
import TaskList from "./Componenets/TaskList"
import TaskForm from "./Componenets/TaskForm"
import "./App.css"

function App() {
  const [page, setPage] = useState("list")
  const [currentTaskId, setCurrentTaskId] = useState(null)

  // Navigation functions
  const navigateToList = () => {
    setPage("list")
    setCurrentTaskId(null)
  }

  const navigateToCreate = () => {
    setPage("create")
    setCurrentTaskId(null)
  }

  const navigateToEdit = (id) => {
    setPage("edit")
    setCurrentTaskId(id)
  }

  return (
    <div className="App">
      <h1 className="text-3xl font-bold mb-6 mt-4">Task Management App</h1>

      {page === "list" && <TaskList onCreateTask={navigateToCreate} onEditTask={navigateToEdit} />}

      {(page === "create" || page === "edit") && <TaskForm id={currentTaskId} onBack={navigateToList} />}
    </div>
  )
}

export default App
