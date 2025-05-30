import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, register, getUserInfo } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)

  const isLoggedIn = computed(() => !!token.value)

  // 登录
  async function loginAction(loginForm) {
    try {
      const response = await login(loginForm)
      if (response.code === 200) {
        token.value = response.data
        localStorage.setItem('token', response.data)
        await fetchUserInfo()
        return { success: true }
      } else {
        return { success: false, message: response.message }
      }
    } catch (error) {
      return { success: false, message: error.message || '登录失败' }
    }
  }

  // 注册
  async function registerAction(registerForm) {
    try {
      const response = await register(registerForm)
      if (response.code === 200) {
        return { success: true, data: response.data }
      } else {
        return { success: false, message: response.message }
      }
    } catch (error) {
      return { success: false, message: error.message || '注册失败' }
    }
  }

  // 获取用户信息
  async function fetchUserInfo() {
    if (!token.value) return
    
    try {
      const response = await getUserInfo()
      if (response.code === 200) {
        userInfo.value = response.data
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
    }
  }

  // 登出
  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    loginAction,
    registerAction,
    fetchUserInfo,
    logout
  }
}) 