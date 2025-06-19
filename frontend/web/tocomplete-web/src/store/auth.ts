import { defineStore } from 'pinia'
import { ref } from 'vue'

interface User {
  id: string
  username: string
  email: string
  token?: string
}

export const useAuthStore = defineStore('auth', () => {
  const isAuthenticated = ref(false)
  const user = ref<User | null>(null)
  const token = ref<string | null>(null)

  function login(userData: User) {
    user.value = userData
    token.value = userData.token || null
    isAuthenticated.value = true
  }

  function logout() {
    user.value = null
    token.value = null
    isAuthenticated.value = false
  }

  function checkAuth() {
    // TODO: Implement token validation logic
    return isAuthenticated.value
  }

  return {
    isAuthenticated,
    user,
    token,
    login,
    logout,
    checkAuth
  }
}) 