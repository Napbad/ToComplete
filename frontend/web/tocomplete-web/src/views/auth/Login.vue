<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-left">
        <div class="brand">
          <img src="/logo.svg" alt="ToComplete" class="brand-logo" />
          <h1 class="brand-title">ToComplete</h1>
          <p class="brand-subtitle">智能备忘录和计划管理助手</p>
        </div>
        <div class="features">
          <div class="feature-item" v-for="feature in features" :key="feature.title">
            <el-icon class="feature-icon">
              <component :is="feature.icon" />
            </el-icon>
            <div>
              <h3>{{ feature.title }}</h3>
              <p>{{ feature.description }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="login-right">
        <div class="login-form-container">
          <div class="login-header">
            <h2>欢迎回来</h2>
            <p>登录您的账户开始高效管理</p>
          </div>

          <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            size="large"
            class="login-form"
          >
            <el-form-item prop="usernameOrEmail">
              <el-input
                v-model="loginForm.usernameOrEmail"
                placeholder="用户名或邮箱"
                prefix-icon="User"
                clearable
              />
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="密码"
                prefix-icon="Lock"
                show-password
                clearable
                @keyup.enter="handleLogin"
              />
            </el-form-item>

            <el-form-item>
              <div class="login-options">
                <el-checkbox v-model="rememberMe">记住我</el-checkbox>
                <el-button type="primary" link>忘记密码？</el-button>
              </div>
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                class="login-btn"
                :loading="loading"
                @click="handleLogin"
              >
                登录
              </el-button>
            </el-form-item>

            <div class="register-link">
              还没有账户？
              <router-link to="/register" class="link">立即注册</router-link>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 表单数据
const loginForm = reactive({
  usernameOrEmail: '',
  password: ''
})

// 记住我
const rememberMe = ref(false)

// 加载状态
const loading = ref(false)

// 表单引用
const loginFormRef = ref()

// 表单验证规则
const loginRules = {
  usernameOrEmail: [
    { required: true, message: '请输入用户名或邮箱', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 功能特色
const features = [
  {
    icon: 'Document',
    title: '智能备忘录',
    description: '支持分类、标签、优先级管理，多形式提醒'
  },
  {
    icon: 'Calendar',
    title: '长期计划',
    description: '精确到小时的计划安排，支持层级结构'
  },
  {
    icon: 'Star',
    title: 'AI 助手',
    description: '根据您的进度和目标生成个性化计划'
  }
]

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    await loginFormRef.value.validate()
    loading.value = true

    const result = await userStore.loginAction(loginForm)
    
    if (result.success) {
      ElMessage.success('登录成功')
      router.push('/app')
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-wrapper {
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  max-width: 1000px;
  width: 100%;
  min-height: 600px;
  display: flex;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.brand {
  text-align: center;
  margin-bottom: 60px;
}

.brand-logo {
  width: 80px;
  height: 80px;
  margin-bottom: 20px;
}

.brand-title {
  font-size: 32px;
  font-weight: bold;
  margin: 0 0 12px 0;
}

.brand-subtitle {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.features {
  space-y: 32px;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 32px;
}

.feature-icon {
  font-size: 24px;
  margin-right: 16px;
  margin-top: 4px;
  opacity: 0.9;
}

.feature-item h3 {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 8px 0;
}

.feature-item p {
  font-size: 14px;
  opacity: 0.8;
  margin: 0;
  line-height: 1.5;
}

.login-right {
  flex: 1;
  padding: 60px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-form-container {
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.login-header h2 {
  font-size: 28px;
  font-weight: bold;
  color: #1f2937;
  margin: 0 0 12px 0;
}

.login-header p {
  font-size: 16px;
  color: #6b7280;
  margin: 0;
}

.login-form {
  margin-top: 40px;
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.register-link {
  text-align: center;
  font-size: 14px;
  color: #6b7280;
  margin-top: 24px;
}

.link {
  color: var(--el-color-primary);
  text-decoration: none;
  font-weight: 500;
}

.link:hover {
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-wrapper {
    flex-direction: column;
    max-width: 400px;
    min-height: auto;
  }

  .login-left {
    padding: 40px 30px;
  }

  .login-right {
    padding: 40px 30px;
  }

  .brand-title {
    font-size: 24px;
  }

  .features {
    display: none;
  }
}
</style> 