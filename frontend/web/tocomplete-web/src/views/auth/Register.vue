<template>
  <div class="register-container">
    <div class="register-wrapper">
      <div class="register-left">
        <div class="brand">
          <img src="/logo.svg" alt="ToComplete" class="brand-logo" />
          <h1 class="brand-title">ToComplete</h1>
          <p class="brand-subtitle">开启您的高效时间管理之旅</p>
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

      <div class="register-right">
        <div class="register-form-container">
          <div class="register-header">
            <h2>创建账户</h2>
            <p>注册并开始使用ToComplete</p>
          </div>

          <el-form
            ref="registerFormRef"
            :model="registerForm"
            :rules="registerRules"
            size="large"
            class="register-form"
          >
            <el-form-item prop="username">
              <el-input
                v-model="registerForm.username"
                placeholder="用户名"
                prefix-icon="User"
                clearable
                @blur="checkUsername"
              />
              <div v-if="usernameStatus.checked" class="field-status">
                <el-icon :color="usernameStatus.available ? '#67c23a' : '#f56c6c'">
                  <CircleCheck v-if="usernameStatus.available" />
                  <CircleClose v-else />
                </el-icon>
                <span :class="usernameStatus.available ? 'success' : 'error'">
                  {{ usernameStatus.message }}
                </span>
              </div>
            </el-form-item>

            <el-form-item prop="email">
              <el-input
                v-model="registerForm.email"
                placeholder="邮箱地址"
                prefix-icon="Message"
                clearable
                @blur="checkEmail"
              />
              <div v-if="emailStatus.checked" class="field-status">
                <el-icon :color="emailStatus.available ? '#67c23a' : '#f56c6c'">
                  <CircleCheck v-if="emailStatus.available" />
                  <CircleClose v-else />
                </el-icon>
                <span :class="emailStatus.available ? 'success' : 'error'">
                  {{ emailStatus.message }}
                </span>
              </div>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="registerForm.password"
                type="password"
                placeholder="密码"
                prefix-icon="Lock"
                show-password
                clearable
              />
              <div class="password-strength">
                <div class="strength-bar">
                  <div 
                    class="strength-fill" 
                    :class="passwordStrength.level"
                    :style="{ width: passwordStrength.width }"
                  ></div>
                </div>
                <span class="strength-text">{{ passwordStrength.text }}</span>
              </div>
            </el-form-item>

            <el-form-item prop="confirmPassword">
              <el-input
                v-model="registerForm.confirmPassword"
                type="password"
                placeholder="确认密码"
                prefix-icon="Lock"
                show-password
                clearable
                @keyup.enter="handleRegister"
              />
            </el-form-item>

            <el-form-item prop="nickname">
              <el-input
                v-model="registerForm.nickname"
                placeholder="昵称（可选）"
                prefix-icon="UserFilled"
                clearable
              />
            </el-form-item>

            <el-form-item>
              <el-checkbox v-model="agreeTerms" class="terms-checkbox">
                我已阅读并同意
                <el-button type="primary" link>《用户协议》</el-button>
                和
                <el-button type="primary" link>《隐私政策》</el-button>
              </el-checkbox>
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                class="register-btn"
                :loading="loading"
                :disabled="!canRegister"
                @click="handleRegister"
              >
                注册账户
              </el-button>
            </el-form-item>

            <div class="login-link">
              已有账户？
              <router-link to="/login" class="link">立即登录</router-link>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { checkUsername as checkUsernameAPI, checkEmail as checkEmailAPI } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()

// 表单数据
const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  nickname: ''
})

// 其他状态
const loading = ref(false)
const agreeTerms = ref(false)
const registerFormRef = ref()

// 用户名和邮箱可用性检查状态
const usernameStatus = reactive({
  checked: false,
  available: false,
  message: ''
})

const emailStatus = reactive({
  checked: false,
  available: false,
  message: ''
})

// 密码强度
const passwordStrength = computed(() => {
  const password = registerForm.password
  if (!password) return { level: '', width: '0%', text: '' }

  let score = 0
  let text = ''
  
  // 长度检查
  if (password.length >= 6) score += 1
  if (password.length >= 8) score += 1
  
  // 复杂度检查
  if (/[a-z]/.test(password)) score += 1
  if (/[A-Z]/.test(password)) score += 1
  if (/[0-9]/.test(password)) score += 1
  if (/[^A-Za-z0-9]/.test(password)) score += 1

  if (score <= 2) {
    return { level: 'weak', width: '33%', text: '弱' }
  } else if (score <= 4) {
    return { level: 'medium', width: '66%', text: '中' }
  } else {
    return { level: 'strong', width: '100%', text: '强' }
  }
})

// 是否可以注册
const canRegister = computed(() => {
  return registerForm.username && 
         registerForm.email && 
         registerForm.password && 
         registerForm.confirmPassword && 
         usernameStatus.available &&
         emailStatus.available &&
         agreeTerms.value
})

// 表单验证规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ]
}

// 功能特色
const features = [
  {
    icon: 'Trophy',
    title: '成就系统',
    description: '完成任务获得成就，激励持续进步'
  },
  {
    icon: 'Share',
    title: '多端同步',
    description: '数据云端同步，随时随地访问您的计划'
  },
  {
    icon: 'Lock',
    title: '隐私安全',
    description: '严格保护用户隐私，数据安全有保障'
  }
]

// 方法
const checkUsername = async () => {
  if (!registerForm.username) {
    usernameStatus.checked = false
    return
  }

  try {
    const response = await checkUsernameAPI(registerForm.username)
    usernameStatus.checked = true
    usernameStatus.available = !response.data
    usernameStatus.message = response.data ? '用户名已存在' : '用户名可用'
  } catch (error) {
    usernameStatus.checked = true
    usernameStatus.available = false
    usernameStatus.message = '检查失败，请重试'
  }
}

const checkEmail = async () => {
  if (!registerForm.email) {
    emailStatus.checked = false
    return
  }

  try {
    const response = await checkEmailAPI(registerForm.email)
    emailStatus.checked = true
    emailStatus.available = !response.data
    emailStatus.message = response.data ? '邮箱已被注册' : '邮箱可用'
  } catch (error) {
    emailStatus.checked = true
    emailStatus.available = false
    emailStatus.message = '检查失败，请重试'
  }
}

const handleRegister = async () => {
  if (!registerFormRef.value) return

  try {
    await registerFormRef.value.validate()
    
    if (!agreeTerms.value) {
      ElMessage.warning('请先同意用户协议和隐私政策')
      return
    }

    loading.value = true

    const result = await userStore.registerAction(registerForm)
    
    if (result.success) {
      ElMessage.success('注册成功，请登录')
      router.push('/login')
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    console.error('注册失败:', error)
  } finally {
    loading.value = false
  }
}

// 监听用户名和邮箱变化，重置状态
watch(() => registerForm.username, () => {
  usernameStatus.checked = false
})

watch(() => registerForm.email, () => {
  emailStatus.checked = false
})
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.register-wrapper {
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  max-width: 1000px;
  width: 100%;
  min-height: 700px;
  display: flex;
}

.register-left {
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

.register-right {
  flex: 1;
  padding: 60px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.register-form-container {
  width: 100%;
  max-width: 400px;
}

.register-header {
  text-align: center;
  margin-bottom: 40px;
}

.register-header h2 {
  font-size: 28px;
  font-weight: bold;
  color: #1f2937;
  margin: 0 0 12px 0;
}

.register-header p {
  font-size: 16px;
  color: #6b7280;
  margin: 0;
}

.register-form {
  margin-top: 40px;
}

.field-status {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 6px;
  font-size: 12px;
}

.field-status .success {
  color: var(--el-color-success);
}

.field-status .error {
  color: var(--el-color-danger);
}

.password-strength {
  margin-top: 6px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.strength-bar {
  flex: 1;
  height: 4px;
  background: #e4e7ed;
  border-radius: 2px;
  overflow: hidden;
}

.strength-fill {
  height: 100%;
  transition: width 0.3s ease;
}

.strength-fill.weak {
  background: #f56c6c;
}

.strength-fill.medium {
  background: #e6a23c;
}

.strength-fill.strong {
  background: #67c23a;
}

.strength-text {
  font-size: 12px;
  color: #909399;
  white-space: nowrap;
}

.terms-checkbox {
  line-height: 1.5;
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.login-link {
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
  .register-wrapper {
    flex-direction: column;
    max-width: 400px;
    min-height: auto;
  }

  .register-left {
    padding: 40px 30px;
  }

  .register-right {
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