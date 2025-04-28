import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import TaskForm from './TaskForm'
import api from '../api'
import { vi } from 'vitest'

vi.mock('../api', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
  },
}))

describe('TaskForm Integration', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('fills and submits the form to create a new task', async () => {
    const onBackMock = vi.fn()

    api.post.mockResolvedValueOnce({})

    render(<TaskForm id={null} onBack={onBackMock} />)

    const titleInput = screen.getByPlaceholderText(/Enter task title/i)
    const dueDateInput = screen.getByLabelText(/Due Date/i)
    const submitButton = screen.getByRole('button', { name: /Create Task/i })

    await userEvent.clear(titleInput)
    await userEvent.type(titleInput, 'New Task')
    await userEvent.type(dueDateInput, '2025-05-01')

    await userEvent.click(submitButton)

    expect(api.post).toHaveBeenCalledWith('/jds', {
        title: 'New Task',
        completed: false,
        dueDate: '2025-05-01',
    })

    await waitFor(() => expect(onBackMock).toHaveBeenCalled())
  })

  it('fills and submits the form to update an existing task', async () => {
    const onBackMock = vi.fn()

    api.get.mockResolvedValueOnce({
        data: {
            id: 1,
            title: 'Existing Task',
            completed: false,
            dueDate: '2025-04-20',
    },
    })

    api.put.mockResolvedValueOnce({})

    render(<TaskForm id={1} onBack={onBackMock} />)

    const titleInput = await screen.findByPlaceholderText(/Enter task title/i)
    const dueDateInput = screen.getByLabelText(/Due Date/i)
    const submitButton = screen.getByRole('button', { name: /Update Task/i })

    await userEvent.clear(titleInput)
    await userEvent.type(titleInput, 'Updated Task')
    fireEvent.change(dueDateInput, { target: { value: '2025-05-01' } })

    await userEvent.click(submitButton)

    expect(api.put).toHaveBeenCalledWith('/jds/1', {
        id: 1,
        title: 'Updated Task',
        completed: false,
        dueDate: '2025-05-01',
    })

    await waitFor(() => expect(onBackMock).toHaveBeenCalled())
  })
})
