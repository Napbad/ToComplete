// Common types for the application
export interface User {
  id: string;
  username: string;
  email: string;
}

// Add more type definitions as needed
export type Theme = 'light' | 'dark';

export interface ApiResponse<T> {
  data: T;
  message: string;
  success: boolean;
}

export interface Task {
  id: string;
  title: string;
  description: string;
  startTime: string;
  endTime: string;
  completed: boolean;
  photoRequired: boolean;
  photoUrl?: string;
}

export interface DayStats {
  totalTasks: number;
  completedTasks: number;
  date: string;
} 