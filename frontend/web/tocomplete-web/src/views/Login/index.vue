<!-- index.vue -->
<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../store/auth'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const errorMessage = ref('')

const handleLogin = async () => {
  try {
    // TODO: Replace with actual API call
    const userData = {
      id: '1',
      username: username.value,
      email: `${username.value}@example.com`
    }
    
    authStore.login(userData)
    router.push({ name: 'dashboard' })
  } catch (error) {
    errorMessage.value = '登录失败，请检查用户名和密码'
  }
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="hero-title">
        <span class="title-part-1">To</span><span class="title-part-2">Complete</span>
      </h1>
      <h2 class="login-title">登录</h2>
      <form class="login-form" @submit.prevent="handleLogin"> 
        <div class="form-group">
          <input 
            v-model="username"
            type="text" 
            class="form-input" 
            placeholder="请输入用户名"
            required
          >
        </div>

        <br>
        <div class="form-group">
          <input 
            v-model="password"
            type="password" 
            class="form-input-password" 
            placeholder="请输入密码"
            required
          >
        </div>
        
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        
        <br>
        <div class="form-group">
          <button type="submit" class="login-button">登录</button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  position: relative;
  overflow: hidden;
  animation: waterFlow 10s ease-in-out infinite;
  background-size: 400% 400%;
  background-image: linear-gradient(
      135deg,
      rgba(102, 126, 234, 0.9),
      rgba(118, 75, 162, 0.8),
      rgba(240, 147, 251, 0.9)
  );
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

@keyframes waterFlow {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

.login-box { 
  padding: 4vh;
  border-radius: 1vh;
  background: rgba(255, 255, 255, 0.9);
  border-color: #f7adff;
  border-width: 0.2vh;
  border-style: solid;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.hero-title {
  font-size: 4rem;
  font-weight: 800;
  margin-bottom: 1rem;
  text-align: center;
  line-height: 1.1;
}

.title-part-1 {
  background: linear-gradient(45deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.title-part-2 {
  background: linear-gradient(45deg, #764ba2, #f093fb);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.login-title {
  font-size: 2.5rem;
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  width: 40vh;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-input,
.form-input-password {
  border-radius: 0.5rem;
  height: 3rem;
  width: 100%;
  border: 2px solid #e1e1e1;
  padding: 0 1rem;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.form-input:focus,
.form-input-password:focus {
  border-color: #764ba2;
  outline: none;
}

.error-message {
  color: #ff4444;
  text-align: center;
  font-size: 0.9rem;
}

.login-button { 
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  border: none;
  border-radius: 0.5rem;
  height: 3rem;
  width: 100%;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.login-button:hover {
  transform: translateY(-2px);
}

.login-button:active {
  transform: translateY(0);
}
</style>
