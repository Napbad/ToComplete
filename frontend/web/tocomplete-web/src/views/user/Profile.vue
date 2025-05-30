<template>
  <div class="profile-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">个人设置</h1>
        <p class="page-subtitle">管理您的个人信息和应用偏好设置</p>
      </div>
    </div>

    <!-- 设置内容 -->
    <div class="settings-layout">
      <!-- 左侧导航 -->
      <div class="settings-nav">
        <el-menu
          :default-active="activeTab"
          mode="vertical"
          @select="handleTabChange"
          class="nav-menu"
        >
          <el-menu-item index="profile">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>
          <el-menu-item index="avatar">
            <el-icon><Picture /></el-icon>
            <span>头像设置</span>
          </el-menu-item>
          <el-menu-item index="security">
            <el-icon><Lock /></el-icon>
            <span>安全设置</span>
          </el-menu-item>
          <el-menu-item index="preferences">
            <el-icon><Setting /></el-icon>
            <span>应用偏好</span>
          </el-menu-item>
          <el-menu-item index="notifications">
            <el-icon><Bell /></el-icon>
            <span>通知设置</span>
          </el-menu-item>
          <el-menu-item index="data">
            <el-icon><Document /></el-icon>
            <span>数据管理</span>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- 右侧内容区域 -->
      <div class="settings-content">
        <!-- 个人信息 -->
        <div v-if="activeTab === 'profile'" class="content-section">
          <el-card class="section-card">
            <template #header>
              <h3>个人信息</h3>
            </template>
            <el-form
              :model="profileForm"
              label-width="120px"
              :rules="profileRules"
              ref="profileFormRef"
            >
              <el-form-item label="用户名" prop="username">
                <el-input v-model="profileForm.username" placeholder="请输入用户名" disabled />
              </el-form-item>
              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="profileForm.nickname" placeholder="请输入昵称" />
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
              </el-form-item>
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
              </el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="profileForm.gender">
                  <el-radio label="male">男</el-radio>
                  <el-radio label="female">女</el-radio>
                  <el-radio label="other">其他</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="生日">
                <el-date-picker
                  v-model="profileForm.birthday"
                  type="date"
                  placeholder="选择生日"
                  style="width: 100%"
                />
              </el-form-item>
              <el-form-item label="个人简介">
                <el-input
                  v-model="profileForm.bio"
                  type="textarea"
                  :rows="4"
                  placeholder="介绍一下自己..."
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="saveProfile">保存</el-button>
                <el-button @click="resetProfile">重置</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>

        <!-- 头像设置 -->
        <div v-if="activeTab === 'avatar'" class="content-section">
          <el-card class="section-card">
            <template #header>
              <h3>头像设置</h3>
            </template>
            <div class="avatar-section">
              <div class="current-avatar">
                <el-avatar :size="120" :src="userInfo.avatar">
                  <el-icon size="60"><User /></el-icon>
                </el-avatar>
                <div class="avatar-info">
                  <h4>当前头像</h4>
                  <p>支持 JPG、PNG 格式，建议尺寸 200x200 像素</p>
                </div>
              </div>
              
              <div class="avatar-actions">
                <el-upload
                  class="avatar-uploader"
                  :action="uploadAction"
                  :show-file-list="false"
                  :before-upload="beforeAvatarUpload"
                  :on-success="handleAvatarSuccess"
                >
                  <el-button type="primary">
                    <el-icon><Upload /></el-icon>
                    上传新头像
                  </el-button>
                </el-upload>
                <el-button @click="removeAvatar">移除头像</el-button>
              </div>

              <div class="preset-avatars">
                <h4>选择预设头像</h4>
                <div class="avatar-grid">
                  <div
                    v-for="avatar in presetAvatars"
                    :key="avatar.id"
                    class="preset-avatar"
                    :class="{ active: userInfo.avatar === avatar.url }"
                    @click="selectPresetAvatar(avatar.url)"
                  >
                    <el-avatar :size="60" :src="avatar.url" />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </div>

        <!-- 安全设置 -->
        <div v-if="activeTab === 'security'" class="content-section">
          <el-card class="section-card">
            <template #header>
              <h3>密码设置</h3>
            </template>
            <el-form
              :model="passwordForm"
              label-width="120px"
              :rules="passwordRules"
              ref="passwordFormRef"
            >
              <el-form-item label="当前密码" prop="currentPassword">
                <el-input
                  v-model="passwordForm.currentPassword"
                  type="password"
                  placeholder="请输入当前密码"
                  show-password
                />
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input
                  v-model="passwordForm.newPassword"
                  type="password"
                  placeholder="请输入新密码"
                  show-password
                />
              </el-form-item>
              <el-form-item label="确认密码" prop="confirmPassword">
                <el-input
                  v-model="passwordForm.confirmPassword"
                  type="password"
                  placeholder="请再次输入新密码"
                  show-password
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="changePassword">修改密码</el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <el-card class="section-card">
            <template #header>
              <h3>两步验证</h3>
            </template>
            <div class="security-item">
              <div class="security-info">
                <h4>手机验证</h4>
                <p>通过手机短信接收验证码，提高账户安全性</p>
              </div>
              <el-switch
                v-model="securitySettings.phoneVerification"
                @change="togglePhoneVerification"
              />
            </div>
            <div class="security-item">
              <div class="security-info">
                <h4>邮箱验证</h4>
                <p>通过邮箱接收验证码，作为备用验证方式</p>
              </div>
              <el-switch
                v-model="securitySettings.emailVerification"
                @change="toggleEmailVerification"
              />
            </div>
          </el-card>

          <el-card class="section-card">
            <template #header>
              <h3>登录设备</h3>
            </template>
            <div class="device-list">
              <div
                v-for="device in loginDevices"
                :key="device.id"
                class="device-item"
              >
                <div class="device-icon">
                  <el-icon size="24">
                    <component :is="getDeviceIcon(device.type)" />
                  </el-icon>
                </div>
                <div class="device-info">
                  <div class="device-name">{{ device.name }}</div>
                  <div class="device-details">
                    {{ device.location }} · {{ device.lastActive }}
                  </div>
                </div>
                <div class="device-actions">
                  <el-tag v-if="device.isCurrent" type="success" size="small">
                    当前设备
                  </el-tag>
                  <el-button
                    v-else
                    type="danger"
                    link
                    @click="removeDevice(device)"
                  >
                    移除
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </div>

        <!-- 应用偏好 -->
        <div v-if="activeTab === 'preferences'" class="content-section">
          <el-card class="section-card">
            <template #header>
              <h3>界面设置</h3>
            </template>
            <div class="preference-item">
              <div class="preference-info">
                <h4>主题模式</h4>
                <p>选择您喜欢的界面主题</p>
              </div>
              <el-radio-group v-model="preferences.theme" @change="changeTheme">
                <el-radio label="light">浅色</el-radio>
                <el-radio label="dark">深色</el-radio>
                <el-radio label="auto">自动</el-radio>
              </el-radio-group>
            </div>
            <div class="preference-item">
              <div class="preference-info">
                <h4>语言设置</h4>
                <p>选择界面显示语言</p>
              </div>
              <el-select v-model="preferences.language" @change="changeLanguage">
                <el-option label="简体中文" value="zh-CN" />
                <el-option label="English" value="en-US" />
                <el-option label="日本語" value="ja-JP" />
              </el-select>
            </div>
            <div class="preference-item">
              <div class="preference-info">
                <h4>时间格式</h4>
                <p>选择时间显示格式</p>
              </div>
              <el-radio-group v-model="preferences.timeFormat">
                <el-radio label="12h">12小时制</el-radio>
                <el-radio label="24h">24小时制</el-radio>
              </el-radio-group>
            </div>
          </el-card>

          <el-card class="section-card">
            <template #header>
              <h3>默认设置</h3>
            </template>
            <div class="preference-item">
              <div class="preference-info">
                <h4>新任务默认优先级</h4>
                <p>创建新任务时的默认优先级</p>
              </div>
              <el-select v-model="preferences.defaultPriority">
                <el-option label="低" :value="1" />
                <el-option label="中" :value="2" />
                <el-option label="高" :value="3" />
                <el-option label="紧急" :value="4" />
              </el-select>
            </div>
            <div class="preference-item">
              <div class="preference-info">
                <h4>每页显示数量</h4>
                <p>列表页面每页显示的项目数量</p>
              </div>
              <el-select v-model="preferences.pageSize">
                <el-option label="10" :value="10" />
                <el-option label="20" :value="20" />
                <el-option label="50" :value="50" />
                <el-option label="100" :value="100" />
              </el-select>
            </div>
            <div class="preference-item">
              <div class="preference-info">
                <h4>自动保存</h4>
                <p>编辑内容时自动保存草稿</p>
              </div>
              <el-switch v-model="preferences.autoSave" />
            </div>
          </el-card>
        </div>

        <!-- 通知设置 -->
        <div v-if="activeTab === 'notifications'" class="content-section">
          <el-card class="section-card">
            <template #header>
              <h3>通知偏好</h3>
            </template>
            <div class="notification-item">
              <div class="notification-info">
                <h4>任务提醒</h4>
                <p>任务即将到期时发送提醒</p>
              </div>
              <el-switch v-model="notifications.taskReminder" />
            </div>
            <div class="notification-item">
              <div class="notification-info">
                <h4>每日总结</h4>
                <p>每天结束时发送工作总结</p>
              </div>
              <el-switch v-model="notifications.dailySummary" />
            </div>
            <div class="notification-item">
              <div class="notification-info">
                <h4>计划更新</h4>
                <p>计划状态发生变化时通知</p>
              </div>
              <el-switch v-model="notifications.planUpdate" />
            </div>
            <div class="notification-item">
              <div class="notification-info">
                <h4>系统通知</h4>
                <p>系统维护、更新等重要通知</p>
              </div>
              <el-switch v-model="notifications.systemNotice" />
            </div>
          </el-card>

          <el-card class="section-card">
            <template #header>
              <h3>通知方式</h3>
            </template>
            <div class="notification-method">
              <h4>推送通知</h4>
              <div class="method-item">
                <span>浏览器推送</span>
                <el-switch v-model="notificationMethods.browser" />
              </div>
              <div class="method-item">
                <span>邮件通知</span>
                <el-switch v-model="notificationMethods.email" />
              </div>
              <div class="method-item">
                <span>短信通知</span>
                <el-switch v-model="notificationMethods.sms" />
              </div>
            </div>
          </el-card>
        </div>

        <!-- 数据管理 -->
        <div v-if="activeTab === 'data'" class="content-section">
          <el-card class="section-card">
            <template #header>
              <h3>数据导出</h3>
            </template>
            <div class="data-export">
              <div class="export-item">
                <div class="export-info">
                  <h4>导出所有数据</h4>
                  <p>包含任务、计划、设置等所有个人数据</p>
                </div>
                <el-button type="primary" @click="exportAllData">
                  <el-icon><Download /></el-icon>
                  导出数据
                </el-button>
              </div>
              <div class="export-item">
                <div class="export-info">
                  <h4>导出任务数据</h4>
                  <p>仅导出任务和备忘录相关数据</p>
                </div>
                <el-button @click="exportTaskData">导出任务</el-button>
              </div>
              <div class="export-item">
                <div class="export-info">
                  <h4>导出计划数据</h4>
                  <p>仅导出计划相关数据</p>
                </div>
                <el-button @click="exportPlanData">导出计划</el-button>
              </div>
            </div>
          </el-card>

          <el-card class="section-card">
            <template #header>
              <h3>数据清理</h3>
            </template>
            <div class="data-cleanup">
              <div class="cleanup-item">
                <div class="cleanup-info">
                  <h4>清理已完成任务</h4>
                  <p>删除30天前已完成的任务（不可恢复）</p>
                </div>
                <el-button type="warning" @click="cleanupCompletedTasks">
                  清理数据
                </el-button>
              </div>
              <div class="cleanup-item">
                <div class="cleanup-info">
                  <h4>重置应用数据</h4>
                  <p>清除所有数据，恢复到初始状态（不可恢复）</p>
                </div>
                <el-button type="danger" @click="resetAllData">
                  重置数据
                </el-button>
              </div>
            </div>
          </el-card>

          <el-card class="section-card">
            <template #header>
              <h3>账户管理</h3>
            </template>
            <div class="account-management">
              <div class="account-item">
                <div class="account-info">
                  <h4>注销账户</h4>
                  <p>永久删除账户和所有相关数据（不可恢复）</p>
                </div>
                <el-button type="danger" @click="deleteAccount">
                  注销账户
                </el-button>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import dayjs from 'dayjs'

const userStore = useUserStore()

// 响应式数据
const activeTab = ref('profile')
const profileFormRef = ref()
const passwordFormRef = ref()

// 用户信息
const userInfo = reactive({
  avatar: 'https://avatars.githubusercontent.com/u/1?v=4',
  username: 'johnsmith',
  nickname: 'John Smith',
  email: 'john@example.com',
  phone: '138****8888',
  gender: 'male',
  birthday: dayjs('1990-01-01').toDate(),
  bio: '热爱技术，喜欢探索新事物的开发者。专注于前端开发，对用户体验有着独特的见解。'
})

// 个人信息表单
const profileForm = reactive({
  username: userInfo.username,
  nickname: userInfo.nickname,
  email: userInfo.email,
  phone: userInfo.phone,
  gender: userInfo.gender,
  birthday: userInfo.birthday,
  bio: userInfo.bio
})

const profileRules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}

// 密码表单
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 安全设置
const securitySettings = reactive({
  phoneVerification: true,
  emailVerification: false
})

// 登录设备
const loginDevices = ref([
  {
    id: 1,
    name: 'Chrome on Windows',
    type: 'desktop',
    location: '北京市',
    lastActive: '刚刚',
    isCurrent: true
  },
  {
    id: 2,
    name: 'Safari on iPhone',
    type: 'mobile',
    location: '上海市',
    lastActive: '2小时前',
    isCurrent: false
  },
  {
    id: 3,
    name: 'Firefox on Mac',
    type: 'desktop',
    location: '深圳市',
    lastActive: '1天前',
    isCurrent: false
  }
])

// 应用偏好
const preferences = reactive({
  theme: 'light',
  language: 'zh-CN',
  timeFormat: '24h',
  defaultPriority: 2,
  pageSize: 20,
  autoSave: true
})

// 通知设置
const notifications = reactive({
  taskReminder: true,
  dailySummary: true,
  planUpdate: false,
  systemNotice: true
})

const notificationMethods = reactive({
  browser: true,
  email: false,
  sms: false
})

// 预设头像
const presetAvatars = ref([
  { id: 1, url: 'https://avatars.githubusercontent.com/u/1?v=4' },
  { id: 2, url: 'https://avatars.githubusercontent.com/u/2?v=4' },
  { id: 3, url: 'https://avatars.githubusercontent.com/u/3?v=4' },
  { id: 4, url: 'https://avatars.githubusercontent.com/u/4?v=4' },
  { id: 5, url: 'https://avatars.githubusercontent.com/u/5?v=4' },
  { id: 6, url: 'https://avatars.githubusercontent.com/u/6?v=4' }
])

// 上传配置
const uploadAction = '/api/upload/avatar'

// 方法
const handleTabChange = (key) => {
  activeTab.value = key
}

const saveProfile = async () => {
  try {
    await profileFormRef.value.validate()
    // 更新用户信息
    Object.assign(userInfo, profileForm)
    ElMessage.success('个人信息保存成功')
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const resetProfile = () => {
  Object.assign(profileForm, {
    username: userInfo.username,
    nickname: userInfo.nickname,
    email: userInfo.email,
    phone: userInfo.phone,
    gender: userInfo.gender,
    birthday: userInfo.birthday,
    bio: userInfo.bio
  })
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB！')
    return false
  }
  return true
}

const handleAvatarSuccess = (response) => {
  userInfo.avatar = response.url
  ElMessage.success('头像上传成功')
}

const selectPresetAvatar = (url) => {
  userInfo.avatar = url
  ElMessage.success('头像更新成功')
}

const removeAvatar = () => {
  userInfo.avatar = ''
  ElMessage.success('头像已移除')
}

const changePassword = async () => {
  try {
    await passwordFormRef.value.validate()
    // 这里应该调用API修改密码
    ElMessage.success('密码修改成功')
    // 重置表单
    Object.assign(passwordForm, {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const togglePhoneVerification = (value) => {
  ElMessage.info(value ? '手机验证已启用' : '手机验证已关闭')
}

const toggleEmailVerification = (value) => {
  ElMessage.info(value ? '邮箱验证已启用' : '邮箱验证已关闭')
}

const getDeviceIcon = (type) => {
  return type === 'mobile' ? 'Cellphone' : 'Monitor'
}

const removeDevice = async (device) => {
  try {
    await ElMessageBox.confirm(
      `确定要移除设备"${device.name}"吗？`,
      '移除设备',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const index = loginDevices.value.findIndex(d => d.id === device.id)
    if (index > -1) {
      loginDevices.value.splice(index, 1)
      ElMessage.success('设备已移除')
    }
  } catch {
    // 用户取消
  }
}

const changeTheme = (theme) => {
  ElMessage.info(`主题已切换为${theme === 'light' ? '浅色' : theme === 'dark' ? '深色' : '自动'}模式`)
}

const changeLanguage = (language) => {
  const langMap = {
    'zh-CN': '简体中文',
    'en-US': 'English',
    'ja-JP': '日本語'
  }
  ElMessage.info(`语言已切换为${langMap[language]}`)
}

const exportAllData = () => {
  ElMessage.info('正在导出所有数据...')
}

const exportTaskData = () => {
  ElMessage.info('正在导出任务数据...')
}

const exportPlanData = () => {
  ElMessage.info('正在导出计划数据...')
}

const cleanupCompletedTasks = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清理30天前已完成的任务吗？此操作不可恢复。',
      '清理数据',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    ElMessage.success('已完成任务清理成功')
  } catch {
    // 用户取消
  }
}

const resetAllData = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要重置所有数据吗？此操作将清除所有数据，不可恢复。',
      '重置数据',
      {
        confirmButtonText: '确定重置',
        cancelButtonText: '取消',
        type: 'error'
      }
    )
    ElMessage.success('数据重置成功')
  } catch {
    // 用户取消
  }
}

const deleteAccount = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要注销账户吗？此操作将永久删除账户和所有相关数据，不可恢复。',
      '注销账户',
      {
        confirmButtonText: '确定注销',
        cancelButtonText: '取消',
        type: 'error'
      }
    )
    ElMessage.success('账户注销成功')
  } catch {
    // 用户取消
  }
}

onMounted(() => {
  // 初始化设置
})
</script>

<style scoped>
.profile-container {
  padding: 0;
}

.page-header {
  margin-bottom: 24px;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0 0 8px 0;
}

.page-subtitle {
  font-size: 14px;
  color: var(--el-text-color-regular);
  margin: 0;
}

.settings-layout {
  display: grid;
  grid-template-columns: 240px 1fr;
  gap: 24px;
}

.settings-nav {
  position: sticky;
  top: 20px;
  height: fit-content;
}

.nav-menu {
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.settings-content {
  max-width: 800px;
}

.content-section {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.section-card {
  margin-bottom: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.section-card h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

/* 头像设置样式 */
.avatar-section {
  space-y: 32px;
}

.current-avatar {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 32px;
}

.avatar-info h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.avatar-info p {
  margin: 0;
  font-size: 14px;
  color: var(--el-text-color-regular);
}

.avatar-actions {
  display: flex;
  gap: 12px;
  margin-bottom: 32px;
}

.preset-avatars h4 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.avatar-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 12px;
}

.preset-avatar {
  cursor: pointer;
  border: 2px solid transparent;
  border-radius: 50%;
  transition: all 0.3s;
}

.preset-avatar:hover {
  border-color: var(--el-color-primary-light-5);
}

.preset-avatar.active {
  border-color: var(--el-color-primary);
}

/* 安全设置样式 */
.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.security-item:last-child {
  border-bottom: none;
}

.security-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.security-info p {
  margin: 0;
  font-size: 14px;
  color: var(--el-text-color-regular);
}

.device-list {
  space-y: 16px;
}

.device-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.device-item:last-child {
  border-bottom: none;
}

.device-icon {
  color: var(--el-color-primary);
}

.device-info {
  flex: 1;
}

.device-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 4px;
}

.device-details {
  font-size: 14px;
  color: var(--el-text-color-regular);
}

/* 偏好设置样式 */
.preference-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.preference-item:last-child {
  border-bottom: none;
}

.preference-info {
  flex: 1;
  margin-right: 20px;
}

.preference-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.preference-info p {
  margin: 0;
  font-size: 14px;
  color: var(--el-text-color-regular);
}

/* 通知设置样式 */
.notification-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.notification-item:last-child {
  border-bottom: none;
}

.notification-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.notification-info p {
  margin: 0;
  font-size: 14px;
  color: var(--el-text-color-regular);
}

.notification-method {
  space-y: 16px;
}

.notification-method h4 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.method-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
}

/* 数据管理样式 */
.data-export,
.data-cleanup {
  space-y: 16px;
}

.export-item,
.cleanup-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.export-item:last-child,
.cleanup-item:last-child {
  border-bottom: none;
}

.export-info,
.cleanup-info {
  flex: 1;
  margin-right: 20px;
}

.export-info h4,
.cleanup-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.export-info p,
.cleanup-info p {
  margin: 0;
  font-size: 14px;
  color: var(--el-text-color-regular);
}

.account-management {
  space-y: 16px;
}

.account-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
}

.account-info {
  flex: 1;
  margin-right: 20px;
}

.account-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--el-color-danger);
}

.account-info p {
  margin: 0;
  font-size: 14px;
  color: var(--el-text-color-regular);
}

/* 深色主题适配 */
html.dark .nav-menu,
html.dark .section-card {
  background: var(--el-bg-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .settings-layout {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .settings-nav {
    position: static;
  }
  
  .current-avatar {
    flex-direction: column;
    text-align: center;
  }
  
  .avatar-grid {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .preference-item,
  .notification-item,
  .export-item,
  .cleanup-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .preference-info,
  .notification-info,
  .export-info,
  .cleanup-info {
    margin-right: 0;
  }
}
</style> 