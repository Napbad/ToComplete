<template>
  <div class="plan-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button @click="$router.back()" class="back-btn">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <div class="plan-info">
          <h1 class="plan-title">{{ plan.title }}</h1>
          <div class="plan-status-row">
            <el-tag :type="getStatusType(plan.status)" size="large">
              {{ getStatusText(plan.status) }}
            </el-tag>
            <el-tag :type="getPriorityType(plan.priority)" size="large">
              {{ getPriorityText(plan.priority) }}优先级
            </el-tag>
            <span class="plan-dates">
              <el-icon><Calendar /></el-icon>
              {{ formatDateRange(plan.startDate, plan.endDate) }}
            </span>
          </div>
        </div>
      </div>
      <div class="header-actions">
        <el-button @click="showEditDialog = true">
          <el-icon><Edit /></el-icon>
          编辑
        </el-button>
        <el-button type="danger" @click="deletePlan">
          <el-icon><Delete /></el-icon>
          删除
        </el-button>
      </div>
    </div>

    <!-- 进度概览 -->
    <div class="progress-overview">
      <el-row :gutter="24">
        <el-col :span="8">
          <el-card class="progress-card">
            <div class="progress-circle-container">
              <el-progress
                type="circle"
                :percentage="plan.progress"
                :width="120"
                :stroke-width="8"
                color="#67c23a"
              >
                <span class="progress-text">{{ plan.progress }}%</span>
              </el-progress>
              <div class="progress-label">总体进度</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="16">
          <el-card class="stats-card">
            <div class="stats-grid">
              <div class="stat-item" v-for="stat in stats" :key="stat.key">
                <div class="stat-icon" :style="{ backgroundColor: stat.color }">
                  <el-icon size="20">
                    <component :is="stat.icon" />
                  </el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-value">{{ stat.value }}</div>
                  <div class="stat-label">{{ stat.label }}</div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 主要内容 -->
    <el-row :gutter="24" class="main-content">
      <!-- 左侧内容 -->
      <el-col :span="16">
        <!-- 计划描述 -->
        <el-card class="content-card">
          <template #header>
            <h3>计划描述</h3>
          </template>
          <div class="plan-description">
            <p>{{ plan.description }}</p>
          </div>
          <div class="plan-goals" v-if="plan.goals">
            <h4>目标</h4>
            <p>{{ plan.goals }}</p>
          </div>
        </el-card>

        <!-- 任务列表 -->
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <h3>任务列表</h3>
              <el-button type="primary" @click="showTaskDialog = true">
                <el-icon><Plus /></el-icon>
                添加任务
              </el-button>
            </div>
          </template>
          <div class="task-list">
            <div v-if="tasks.length === 0" class="empty-state">
              <el-empty description="暂无任务" />
            </div>
            <div v-else>
              <div
                v-for="task in tasks"
                :key="task.id"
                class="task-item"
                :class="{ completed: task.completed }"
              >
                <el-checkbox
                  v-model="task.completed"
                  @change="toggleTaskStatus(task)"
                  class="task-checkbox"
                />
                <div class="task-content">
                  <div class="task-title">{{ task.title }}</div>
                  <div class="task-meta">
                    <el-tag
                      :type="getPriorityType(task.priority)"
                      size="small"
                    >
                      {{ getPriorityText(task.priority) }}
                    </el-tag>
                    <span v-if="task.dueDate" class="task-due">
                      <el-icon><Clock /></el-icon>
                      {{ formatDate(task.dueDate) }}
                    </span>
                  </div>
                </div>
                <div class="task-actions">
                  <el-button type="primary" link @click="editTask(task)">
                    编辑
                  </el-button>
                  <el-button type="danger" link @click="deleteTask(task)">
                    删除
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 进度时间线 -->
        <el-card class="content-card">
          <template #header>
            <h3>进度时间线</h3>
          </template>
          <div class="timeline-container">
            <el-timeline>
              <el-timeline-item
                v-for="item in timeline"
                :key="item.id"
                :timestamp="item.timestamp"
                :type="item.type"
              >
                <div class="timeline-content">
                  <h4>{{ item.title }}</h4>
                  <p>{{ item.description }}</p>
                </div>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧内容 -->
      <el-col :span="8">
        <!-- 计划信息 -->
        <el-card class="content-card">
          <template #header>
            <h3>计划信息</h3>
          </template>
          <div class="plan-info-list">
            <div class="info-item">
              <label>开始日期:</label>
              <span>{{ formatDate(plan.startDate) }}</span>
            </div>
            <div class="info-item">
              <label>结束日期:</label>
              <span>{{ formatDate(plan.endDate) }}</span>
            </div>
            <div class="info-item">
              <label>计划天数:</label>
              <span>{{ planDuration }}天</span>
            </div>
            <div class="info-item">
              <label>剩余天数:</label>
              <span :class="remainingDays < 7 ? 'text-warning' : ''">
                {{ remainingDays }}天
              </span>
            </div>
            <div class="info-item">
              <label>创建时间:</label>
              <span>{{ formatDate(plan.createdAt) }}</span>
            </div>
            <div class="info-item">
              <label>更新时间:</label>
              <span>{{ formatDate(plan.updatedAt) }}</span>
            </div>
          </div>
        </el-card>

        <!-- 快捷操作 -->
        <el-card class="content-card">
          <template #header>
            <h3>快捷操作</h3>
          </template>
          <div class="quick-actions">
            <el-button 
              v-for="action in quickActions" 
              :key="action.key"
              :type="action.type"
              class="action-btn"
              @click="action.handler"
            >
              <el-icon>
                <component :is="action.icon" />
              </el-icon>
              {{ action.label }}
            </el-button>
          </div>
        </el-card>

        <!-- 相关文档 -->
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <h3>相关文档</h3>
              <el-button type="primary" link @click="showUploadDialog = true">
                <el-icon><Plus /></el-icon>
                上传
              </el-button>
            </div>
          </template>
          <div class="document-list">
            <div v-if="documents.length === 0" class="empty-state-small">
              <p>暂无相关文档</p>
            </div>
            <div v-else>
              <div
                v-for="doc in documents"
                :key="doc.id"
                class="document-item"
              >
                <el-icon class="doc-icon">
                  <Document />
                </el-icon>
                <div class="doc-content">
                  <div class="doc-name">{{ doc.name }}</div>
                  <div class="doc-size">{{ doc.size }}</div>
                </div>
                <el-button type="primary" link>
                  <el-icon><Download /></el-icon>
                </el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 编辑计划对话框 -->
    <el-dialog v-model="showEditDialog" title="编辑计划" width="600px">
      <el-form :model="editForm" label-width="100px" :rules="editFormRules" ref="editFormRef">
        <el-form-item label="计划标题" prop="title">
          <el-input v-model="editForm.title" placeholder="请输入计划标题" />
        </el-form-item>
        <el-form-item label="计划描述" prop="description">
          <el-input
            v-model="editForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入计划描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="editForm.status" placeholder="选择状态">
            <el-option label="未开始" :value="0" />
            <el-option label="进行中" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已暂停" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="editForm.priority" placeholder="选择优先级">
            <el-option label="低" :value="1" />
            <el-option label="中" :value="2" />
            <el-option label="高" :value="3" />
            <el-option label="紧急" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标" prop="goals">
          <el-input
            v-model="editForm.goals"
            type="textarea"
            :rows="2"
            placeholder="描述计划的具体目标"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpdatePlan">更新</el-button>
      </template>
    </el-dialog>

    <!-- 添加任务对话框 -->
    <el-dialog v-model="showTaskDialog" title="添加任务" width="500px">
      <el-form :model="taskForm" label-width="80px" :rules="taskFormRules" ref="taskFormRef">
        <el-form-item label="任务标题" prop="title">
          <el-input v-model="taskForm.title" placeholder="请输入任务标题" />
        </el-form-item>
        <el-form-item label="任务描述" prop="description">
          <el-input
            v-model="taskForm.description"
            type="textarea"
            :rows="2"
            placeholder="请输入任务描述"
          />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="taskForm.priority" placeholder="选择优先级">
            <el-option label="低" :value="1" />
            <el-option label="中" :value="2" />
            <el-option label="高" :value="3" />
            <el-option label="紧急" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止日期" prop="dueDate">
          <el-date-picker
            v-model="taskForm.dueDate"
            type="date"
            placeholder="选择截止日期"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showTaskDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAddTask">添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()

// 响应式数据
const showEditDialog = ref(false)
const showTaskDialog = ref(false)
const showUploadDialog = ref(false)
const editFormRef = ref()
const taskFormRef = ref()

// 计划数据（模拟）
const plan = ref({
  id: 1,
  title: '学习Vue.js进阶课程',
  description: '深入学习Vue 3的组合式API、状态管理和高级特性，包括Pinia状态管理、Vue Router路由管理、组件通信等核心概念。通过实际项目练习，掌握现代Vue.js开发技能。',
  status: 1,
  priority: 3,
  progress: 65,
  startDate: dayjs().subtract(15, 'day').toDate(),
  endDate: dayjs().add(15, 'day').toDate(),
  goals: '掌握Vue 3核心概念，能够独立开发中型项目，理解现代前端开发最佳实践',
  createdAt: dayjs().subtract(20, 'day').toDate(),
  updatedAt: dayjs().subtract(2, 'day').toDate()
})

// 编辑表单
const editForm = ref({
  title: '',
  description: '',
  status: 1,
  priority: 2,
  goals: ''
})

const editFormRules = {
  title: [{ required: true, message: '请输入计划标题', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }]
}

// 任务表单
const taskForm = ref({
  title: '',
  description: '',
  priority: 2,
  dueDate: null
})

const taskFormRules = {
  title: [{ required: true, message: '请输入任务标题', trigger: 'blur' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }]
}

// 统计数据
const stats = ref([
  { key: 'tasks', label: '总任务', value: 12, icon: 'List', color: '#409eff' },
  { key: 'completed', label: '已完成', value: 8, icon: 'Check', color: '#67c23a' },
  { key: 'pending', label: '待完成', value: 4, icon: 'Clock', color: '#e6a23c' },
  { key: 'overdue', label: '已逾期', value: 1, icon: 'Warning', color: '#f56c6c' }
])

// 任务列表
const tasks = ref([
  {
    id: 1,
    title: '学习组合式API基础',
    description: '理解ref、reactive、computed等核心概念',
    priority: 3,
    completed: true,
    dueDate: dayjs().subtract(10, 'day').toDate()
  },
  {
    id: 2,
    title: '掌握Pinia状态管理',
    description: '学习Pinia的基本用法和最佳实践',
    priority: 3,
    completed: true,
    dueDate: dayjs().subtract(7, 'day').toDate()
  },
  {
    id: 3,
    title: '实践Vue Router路由',
    description: '构建多页面应用，配置路由守卫',
    priority: 2,
    completed: false,
    dueDate: dayjs().add(3, 'day').toDate()
  },
  {
    id: 4,
    title: '组件通信与插槽',
    description: '深入理解父子组件通信和插槽机制',
    priority: 2,
    completed: false,
    dueDate: dayjs().add(7, 'day').toDate()
  }
])

// 时间线数据
const timeline = ref([
  {
    id: 1,
    title: '开始学习计划',
    description: '制定详细的学习计划和时间安排',
    timestamp: dayjs().subtract(15, 'day').format('YYYY-MM-DD'),
    type: 'primary'
  },
  {
    id: 2,
    title: '完成基础概念学习',
    description: '掌握了Vue 3的基本概念和语法',
    timestamp: dayjs().subtract(10, 'day').format('YYYY-MM-DD'),
    type: 'success'
  },
  {
    id: 3,
    title: '状态管理实践',
    description: '通过实际项目练习Pinia状态管理',
    timestamp: dayjs().subtract(5, 'day').format('YYYY-MM-DD'),
    type: 'success'
  },
  {
    id: 4,
    title: '中期进度检查',
    description: '已完成60%的学习内容，进度良好',
    timestamp: dayjs().subtract(2, 'day').format('YYYY-MM-DD'),
    type: 'warning'
  }
])

// 文档列表
const documents = ref([
  { id: 1, name: 'Vue3学习笔记.md', size: '2.5MB' },
  { id: 2, name: 'Pinia使用指南.pdf', size: '1.8MB' },
  { id: 3, name: '项目代码示例.zip', size: '5.2MB' }
])

// 计算属性
const planDuration = computed(() => {
  return dayjs(plan.value.endDate).diff(dayjs(plan.value.startDate), 'day')
})

const remainingDays = computed(() => {
  return Math.max(0, dayjs(plan.value.endDate).diff(dayjs(), 'day'))
})

const quickActions = ref([
  {
    key: 'updateProgress',
    label: '更新进度',
    type: 'primary',
    icon: 'TrendCharts',
    handler: () => updateProgress()
  },
  {
    key: 'addMilestone',
    label: '添加里程碑',
    type: 'success',
    icon: 'Flag',
    handler: () => ElMessage.info('添加里程碑功能开发中...')
  },
  {
    key: 'shareProgress',
    label: '分享进度',
    type: 'info',
    icon: 'Share',
    handler: () => ElMessage.info('分享功能开发中...')
  },
  {
    key: 'exportReport',
    label: '导出报告',
    type: 'warning',
    icon: 'Document',
    handler: () => ElMessage.info('导出功能开发中...')
  }
])

// 方法
const getStatusType = (status) => {
  const types = ['info', 'success', 'warning', 'danger']
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = ['未开始', '进行中', '已完成', '已暂停']
  return texts[status] || '未知'
}

const getPriorityType = (priority) => {
  const types = ['', 'info', 'warning', 'danger', 'danger']
  return types[priority] || 'info'
}

const getPriorityText = (priority) => {
  const texts = ['', '低', '中', '高', '紧急']
  return texts[priority] || '未知'
}

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD')
}

const formatDateRange = (startDate, endDate) => {
  const start = dayjs(startDate).format('MM-DD')
  const end = dayjs(endDate).format('MM-DD')
  return `${start} ~ ${end}`
}

const toggleTaskStatus = (task) => {
  task.completed = !task.completed
  ElMessage.success(task.completed ? '任务已完成' : '任务已重置')
  // 这里应该调用API更新任务状态
}

const editTask = (task) => {
  // 编辑任务逻辑
  ElMessage.info('编辑任务功能开发中...')
}

const deleteTask = async (task) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除任务"${task.title}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const index = tasks.value.findIndex(t => t.id === task.id)
    if (index > -1) {
      tasks.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  } catch {
    // 用户取消删除
  }
}

const handleUpdatePlan = async () => {
  try {
    await editFormRef.value.validate()
    // 更新计划数据
    Object.assign(plan.value, editForm.value)
    plan.value.updatedAt = new Date()
    ElMessage.success('更新成功')
    showEditDialog.value = false
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const handleAddTask = async () => {
  try {
    await taskFormRef.value.validate()
    // 添加新任务
    const newTask = {
      id: Date.now(),
      ...taskForm.value,
      completed: false
    }
    tasks.value.push(newTask)
    ElMessage.success('添加成功')
    showTaskDialog.value = false
    
    // 重置表单
    taskForm.value = {
      title: '',
      description: '',
      priority: 2,
      dueDate: null
    }
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const deletePlan = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个计划吗？此操作不可撤销。',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    ElMessage.success('删除成功')
    router.push('/app/plans')
  } catch {
    // 用户取消删除
  }
}

const updateProgress = () => {
  ElMessage.info('更新进度功能开发中...')
}

onMounted(() => {
  // 初始化编辑表单
  editForm.value = {
    title: plan.value.title,
    description: plan.value.description,
    status: plan.value.status,
    priority: plan.value.priority,
    goals: plan.value.goals
  }
})
</script>

<style scoped>
.plan-detail {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.back-btn {
  margin-top: 4px;
}

.plan-info {
  flex: 1;
}

.plan-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0 0 12px 0;
}

.plan-status-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.plan-dates {
  display: flex;
  align-items: center;
  gap: 4px;
  color: var(--el-text-color-regular);
  font-size: 14px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.progress-overview {
  margin-bottom: 24px;
}

.progress-card {
  text-align: center;
  border-radius: 8px;
}

.progress-circle-container {
  padding: 20px;
}

.progress-text {
  font-size: 18px;
  font-weight: 600;
}

.progress-label {
  margin-top: 16px;
  font-size: 16px;
  color: var(--el-text-color-primary);
}

.stats-card {
  border-radius: 8px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  padding: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: var(--el-text-color-regular);
}

.main-content {
  margin-top: 0;
}

.content-card {
  margin-bottom: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.plan-description {
  line-height: 1.6;
  color: var(--el-text-color-regular);
}

.plan-goals {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid var(--el-border-color-lighter);
}

.plan-goals h4 {
  margin: 0 0 12px 0;
  font-size: 14px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.task-list {
  max-height: 400px;
  overflow-y: auto;
}

.empty-state {
  padding: 40px 20px;
  text-align: center;
}

.task-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
  transition: opacity 0.3s;
}

.task-item:last-child {
  border-bottom: none;
}

.task-item.completed {
  opacity: 0.6;
}

.task-item.completed .task-title {
  text-decoration: line-through;
}

.task-checkbox {
  margin-right: 12px;
}

.task-content {
  flex: 1;
  margin-right: 12px;
}

.task-title {
  font-size: 14px;
  color: var(--el-text-color-primary);
  margin-bottom: 4px;
}

.task-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
}

.task-due {
  display: flex;
  align-items: center;
  gap: 2px;
  color: var(--el-text-color-regular);
}

.task-actions {
  display: flex;
  gap: 8px;
}

.timeline-container {
  max-height: 400px;
  overflow-y: auto;
}

.timeline-content h4 {
  margin: 0 0 8px 0;
  font-size: 14px;
  font-weight: 600;
}

.timeline-content p {
  margin: 0;
  font-size: 13px;
  color: var(--el-text-color-regular);
}

.plan-info-list {
  space-y: 12px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.info-item:last-child {
  border-bottom: none;
}

.info-item label {
  font-size: 14px;
  color: var(--el-text-color-regular);
  font-weight: 500;
}

.info-item span {
  font-size: 14px;
  color: var(--el-text-color-primary);
}

.text-warning {
  color: var(--el-color-warning) !important;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.action-btn {
  width: 100%;
  justify-content: flex-start;
}

.document-list {
  max-height: 200px;
  overflow-y: auto;
}

.empty-state-small {
  padding: 20px;
  text-align: center;
  color: var(--el-text-color-placeholder);
  font-size: 14px;
}

.document-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.document-item:last-child {
  border-bottom: none;
}

.doc-icon {
  color: var(--el-color-primary);
  font-size: 20px;
}

.doc-content {
  flex: 1;
}

.doc-name {
  font-size: 14px;
  color: var(--el-text-color-primary);
  margin-bottom: 2px;
}

.doc-size {
  font-size: 12px;
  color: var(--el-text-color-placeholder);
}

/* 深色主题适配 */
html.dark .page-header,
html.dark .content-card {
  background: var(--el-bg-color);
}
</style> 