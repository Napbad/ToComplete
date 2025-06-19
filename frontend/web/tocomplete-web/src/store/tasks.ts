import { defineStore } from 'pinia'
import type { Task } from '../types'
import { useAuthStore } from './auth'

interface DayStats {
  date: string
  completedTasks: number
  totalTasks: number
}

export interface TaskStore {
  tasks: Task[]
  dayStats: DayStats[]
  isOfflineMode: boolean
}

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:3000/api'

// 模拟数据生成函数
const generateMockData = (date: Date): Task[] => {
  const tasks: Task[] = []
  const numTasks = Math.floor(Math.random() * 3) + 2 // 2-4 tasks per day
  
  for (let i = 0; i < numTasks; i++) {
    const startHour = 9 + i * 2 // Tasks start from 9 AM, 2 hours apart
    const endHour = startHour + 1
    
    tasks.push({
      id: `mock-${date.toISOString()}-${i}`,
      title: `示例任务 ${i + 1}`,
      description: `这是 ${date.toLocaleDateString()} 的示例任务 ${i + 1}`,
      startTime: new Date(date.getFullYear(), date.getMonth(), date.getDate(), startHour, 0).toISOString(),
      endTime: new Date(date.getFullYear(), date.getMonth(), date.getDate(), endHour, 0).toISOString(),
      completed: Math.random() > 0.5,
      photoRequired: Math.random() > 0.7
    })
  }
  
  return tasks
}

const generateMockMonthStats = (year: number, month: number): DayStats[] => {
  const stats: DayStats[] = []
  const daysInMonth = new Date(year, month + 1, 0).getDate()
  
  console.log(`Generating mock stats for ${year}-${month + 1}, days: ${daysInMonth}`);
  
  for (let day = 1; day <= daysInMonth; day++) {
    const totalTasks = Math.floor(Math.random() * 3) + 2 // 2-4 tasks
    const completedTasks = Math.floor(Math.random() * (totalTasks + 1)) // 0 to totalTasks
    const date = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
    
    stats.push({
      date,
      totalTasks,
      completedTasks
    })
  }
  
  console.log('Generated mock stats:', stats);
  return stats
}

export const useTaskStore = defineStore('tasks', {
  state: (): TaskStore => ({
    tasks: [],
    dayStats: [],
    isOfflineMode: false
  }),

  actions: {
    async fetchTasks(date: Date) {
      const authStore = useAuthStore()
      
      try {
        if (!authStore.token || this.isOfflineMode) {
          console.log('Using mock data for tasks')
          this.tasks = generateMockData(date)
          return
        }

        const formattedDate = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
        const response = await fetch(`${API_URL}/tasks/daily/${formattedDate}`, {
          headers: {
            'Authorization': `Bearer ${authStore.token}`
          }
        })
        
        if (!response.ok) {
          throw new Error('Failed to fetch tasks')
        }
        
        this.tasks = await response.json()
      } catch (error) {
        console.error('Error fetching tasks:', error)
        this.isOfflineMode = true
        this.tasks = generateMockData(date)
      }
    },

    async fetchMonthStats(year: number, month: number) {
      const authStore = useAuthStore()
      
      try {
        if (!authStore.token || this.isOfflineMode) {
          console.log(`Fetching mock stats for ${year}-${month + 1}`);
          this.dayStats = generateMockMonthStats(year, month)
          return
        }

        const response = await fetch(`${API_URL}/tasks/stats/${year}/${month + 1}`, {
          headers: {
            'Authorization': `Bearer ${authStore.token}`
          }
        })
        
        if (!response.ok) {
          throw new Error('Failed to fetch month stats')
        }
        
        this.dayStats = await response.json()
      } catch (error) {
        console.error('Error fetching month stats:', error)
        this.isOfflineMode = true
        this.dayStats = generateMockMonthStats(year, month)
      }
    },

    async updateTask(task: Task) {
      const authStore = useAuthStore()
      
      try {
        if (!authStore.token || this.isOfflineMode) {
          const index = this.tasks.findIndex(t => t.id === task.id)
          if (index !== -1) {
            this.tasks[index] = task
          }
          return
        }

        const response = await fetch(`${API_URL}/tasks/${task.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${authStore.token}`
          },
          body: JSON.stringify(task)
        })
        
        if (!response.ok) {
          throw new Error('Failed to update task')
        }
        
        const updatedTask = await response.json()
        const index = this.tasks.findIndex(t => t.id === updatedTask.id)
        if (index !== -1) {
          this.tasks[index] = updatedTask
        }
      } catch (error) {
        console.error('Error updating task:', error)
        this.isOfflineMode = true
        const index = this.tasks.findIndex(t => t.id === task.id)
        if (index !== -1) {
          this.tasks[index] = task
        }
      }
    },

    async uploadTaskPhoto(taskId: string, file: File) {
      const authStore = useAuthStore()
      
      if (!authStore.token || this.isOfflineMode) {
        console.log('Photo upload not available in offline mode')
        return
      }

      try {
        const formData = new FormData()
        formData.append('photo', file)
        
        const response = await fetch(`${API_URL}/tasks/${taskId}/photo`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${authStore.token}`
          },
          body: formData
        })
        
        if (!response.ok) {
          throw new Error('Failed to upload photo')
        }
        
        const updatedTask = await response.json()
        const index = this.tasks.findIndex(t => t.id === taskId)
        if (index !== -1) {
          this.tasks[index] = updatedTask
        }
      } catch (error) {
        console.error('Error uploading photo:', error)
        this.isOfflineMode = true
      }
    }
  }
}) 