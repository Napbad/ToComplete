<template>
  <div class="dashboard">
    <!-- 页面标题和操作 -->
    <div class="dashboard-header">
      <div>
        <h1 class="page-title">仪表盘</h1>
        <p class="page-subtitle">欢迎回来，{{ userStore.userInfo?.nickname || '用户' }}！今天是 {{ todayDate }}</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="showQuickAdd = true">
          <el-icon><Plus /></el-icon>
          快速添加
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card" v-for="stat in stats" :key="stat.key">
        <div class="stat-icon" :style="{ background: stat.color }">
          <el-icon size="24">
            <component :is="stat.icon" />
          </el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
          <div class="stat-trend" :class="stat.trend > 0 ? 'positive' : 'negative'">
            <el-icon size="12">
              <ArrowUp v-if="stat.trend > 0" />
              <ArrowDown v-else />
            </el-icon>
            {{ Math.abs(stat.trend) }}%
          </div>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <el-row :gutter="24" class="dashboard-content">
      <!-- 左侧内容 -->
      <el-col :span="16">
        <!-- 今日任务 -->
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <h3>今日任务</h3>
              <el-button type="primary" link @click="$router.push('/app/memos')">
                查看全部
              </el-button>
            </div>
          </template>
          
          <div class="task-list">
            <div v-if="todayTasks.length === 0" class="empty-state">
              <el-icon size="48" class="empty-icon">
                <Document />
              </el-icon>
              <p>今天没有任务</p>
            </div>
            
            <div 
              v-for="task in todayTasks" 
              :key="task.id"
              class="task-item"
              :class="{ completed: task.status === 2 }"
            >
              <el-checkbox 
                :model-value="task.status === 2"
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
                  <span v-if="task.dueAt" class="task-time">
                    <el-icon><Clock /></el-icon>
                    {{ formatTime(task.dueAt) }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 本周计划 -->
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <h3>本周计划</h3>
              <el-button type="primary" link @click="$router.push('/app/plans')">
                查看全部
              </el-button>
            </div>
          </template>
          
          <div class="plan-timeline">
            <div v-for="day in weekPlan" :key="day.date" class="timeline-day">
              <div class="day-header">
                <div class="day-label">{{ day.label }}</div>
                <div class="day-date">{{ day.date }}</div>
              </div>
              <div class="day-events">
                <div v-if="day.events.length === 0" class="no-events">
                  无安排
                </div>
                <div 
                  v-for="event in day.events" 
                  :key="event.id"
                  class="event-item"
                >
                  <div class="event-time">{{ event.time }}</div>
                  <div class="event-title">{{ event.title }}</div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧内容 -->
      <el-col :span="8">
        <!-- 完成情况 -->
        <el-card class="content-card">
          <template #header>
            <h3>本月完成情况</h3>
          </template>
          
          <div class="progress-chart">
            <div class="progress-circle">
              <el-progress 
                type="circle" 
                :percentage="completionRate"
                :width="120"
                :stroke-width="8"
                color="#67c23a"
              >
                <span class="progress-text">{{ completionRate }}%</span>
              </el-progress>
            </div>
            <div class="progress-stats">
              <div class="progress-item">
                <span class="label">已完成</span>
                <span class="value">{{ completedTasks }}</span>
              </div>
              <div class="progress-item">
                <span class="label">总任务</span>
                <span class="value">{{ totalTasks }}</span>
              </div>
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
              class="quick-action-btn"
              @click="action.handler"
            >
              <el-icon>
                <component :is="action.icon" />
              </el-icon>
              {{ action.label }}
            </el-button>
          </div>
        </el-card>

        <!-- 最近活动 -->
        <el-card class="content-card">
          <template #header>
            <h3>最近活动</h3>
          </template>
          
          <div class="activity-list">
            <div 
              v-for="activity in recentActivities" 
              :key="activity.id"
              class="activity-item"
            >
              <div class="activity-icon">
                <el-icon>
                  <component :is="activity.icon" />
                </el-icon>
              </div>
              <div class="activity-content">
                <div class="activity-text">{{ activity.text }}</div>
                <div class="activity-time">{{ activity.time }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快速添加对话框 -->
    <el-dialog v-model="showQuickAdd" title="快速添加" width="500px">
      <el-form :model="quickAddForm" label-width="80px">
        <el-form-item label="类型">
          <el-radio-group v-model="quickAddForm.type">
            <el-radio label="memo">备忘录</el-radio>
            <el-radio label="plan">计划</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="quickAddForm.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input 
            v-model="quickAddForm.content" 
            type="textarea" 
            :rows="3"
            placeholder="请输入内容"
          />
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="quickAddForm.priority" placeholder="选择优先级">
            <el-option label="低" :value="1" />
            <el-option label="中" :value="2" />
            <el-option label="高" :value="3" />
            <el-option label="紧急" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止时间">
          <el-date-picker
            v-model="quickAddForm.dueAt"
            type="datetime"
            placeholder="选择截止时间"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showQuickAdd = false">取消</el-button>
        <el-button type="primary" @click="handleQuickAdd">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'

const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const showQuickAdd = ref(false)
const quickAddForm = ref({
  type: 'memo',
  title: '',
  content: '',
  priority: 2,
  dueAt: null
})

// 计算属性
const todayDate = computed(() => {
  return dayjs().format('YYYY年MM月DD日 dddd')
})

// 模拟统计数据
const stats = ref([
  {
    key: 'total',
    label: '总任务',
    value: 24,
    icon: 'Document',
    color: '#409eff',
    trend: 12
  },
  {
    key: 'completed',
    label: '已完成',
    value: 18,
    icon: 'Check',
    color: '#67c23a',
    trend: 8
  },
  {
    key: 'pending',
    label: '待处理',
    value: 6,
    icon: 'Clock',
    color: '#e6a23c',
    trend: -3
  },
  {
    key: 'overdue',
    label: '已逾期',
    value: 2,
    icon: 'Warning',
    color: '#f56c6c',
    trend: -1
  }
])

// 今日任务
const todayTasks = ref([
  {
    id: 1,
    title: '完成项目文档编写',
    priority: 3,
    status: 0,
    dueAt: dayjs().hour(14).minute(30).toDate()
  },
  {
    id: 2,
    title: '参加团队会议',
    priority: 2,
    status: 2,
    dueAt: dayjs().hour(10).minute(0).toDate()
  },
  {
    id: 3,
    title: '代码审查',
    priority: 2,
    status: 0,
    dueAt: dayjs().hour(16).minute(0).toDate()
  },
  {
    id: 4,
    title: '准备明天的演示',
    priority: 4,
    status: 0,
    dueAt: dayjs().hour(18).minute(0).toDate()
  }
])

// 本周计划
const weekPlan = ref([
  {
    date: dayjs().format('MM-DD'),
    label: '今天',
    events: [
      { id: 1, time: '10:00', title: '团队会议' },
      { id: 2, time: '14:30', title: '项目文档' },
      { id: 3, time: '16:00', title: '代码审查' }
    ]
  },
  {
    date: dayjs().add(1, 'day').format('MM-DD'),
    label: '明天',
    events: [
      { id: 4, time: '09:00', title: '项目演示' },
      { id: 5, time: '15:00', title: '客户沟通' }
    ]
  },
  {
    date: dayjs().add(2, 'day').format('MM-DD'),
    label: '后天',
    events: []
  }
])

// 完成率
const completedTasks = computed(() => stats.value.find(s => s.key === 'completed')?.value || 0)
const totalTasks = computed(() => stats.value.find(s => s.key === 'total')?.value || 0)
const completionRate = computed(() => {
  return totalTasks.value > 0 ? Math.round((completedTasks.value / totalTasks.value) * 100) : 0
})

// 快捷操作
const quickActions = ref([
  {
    key: 'memo',
    label: '新建备忘录',
    type: 'primary',
    icon: 'EditPen',
    handler: () => router.push('/app/memos')
  },
  {
    key: 'plan',
    label: '制定计划',
    type: 'success',
    icon: 'Calendar',
    handler: () => router.push('/app/plans')
  },
  {
    key: 'calendar',
    label: '查看日历',
    type: 'info',
    icon: 'Calendar',
    handler: () => router.push('/app/calendar')
  },
  {
    key: 'report',
    label: '生成报告',
    type: 'warning',
    icon: 'Document',
    handler: () => ElMessage.info('报告功能开发中...')
  }
])

// 最近活动
const recentActivities = ref([
  {
    id: 1,
    text: '完成了任务"团队会议"',
    time: '2小时前',
    icon: 'Check'
  },
  {
    id: 2,
    text: '创建了新的备忘录"项目计划"',
    time: '4小时前',
    icon: 'EditPen'
  },
  {
    id: 3,
    text: '更新了任务优先级',
    time: '6小时前',
    icon: 'Edit'
  },
  {
    id: 4,
    text: '设置了提醒时间',
    time: '8小时前',
    icon: 'Bell'
  }
])

// 方法
const getPriorityType = (priority) => {
  const types = ['', 'info', 'warning', 'danger', 'danger']
  return types[priority] || 'info'
}

const getPriorityText = (priority) => {
  const texts = ['', '低', '中', '高', '紧急']
  return texts[priority] || '未知'
}

const formatTime = (date) => {
  return dayjs(date).format('HH:mm')
}

const toggleTaskStatus = (task) => {
  task.status = task.status === 2 ? 0 : 2
  ElMessage.success(task.status === 2 ? '任务已完成' : '任务已重置')
}

const handleQuickAdd = () => {
  if (!quickAddForm.value.title) {
    ElMessage.warning('请输入标题')
    return
  }
  
  // 这里应该调用API保存数据
  ElMessage.success('添加成功')
  showQuickAdd.value = false
  
  // 重置表单
  quickAddForm.value = {
    type: 'memo',
    title: '',
    content: '',
    priority: 2,
    dueAt: null
  }
}

onMounted(() => {
  // 组件挂载时可以加载数据
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
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

.header-actions {
  display: flex;
  gap: 12px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  color: white;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: var(--el-text-color-regular);
  margin-bottom: 4px;
}

.stat-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 2px;
}

.stat-trend.positive {
  color: #67c23a;
}

.stat-trend.negative {
  color: #f56c6c;
}

.dashboard-content {
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

.task-list {
  max-height: 300px;
  overflow-y: auto;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: var(--el-text-color-regular);
}

.empty-icon {
  color: var(--el-color-info);
  margin-bottom: 16px;
}

.task-item {
  display: flex;
  align-items: flex-start;
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
  margin-top: 2px;
}

.task-content {
  flex: 1;
}

.task-title {
  font-size: 14px;
  color: var(--el-text-color-primary);
  margin-bottom: 4px;
  line-height: 1.4;
}

.task-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
}

.task-time {
  display: flex;
  align-items: center;
  gap: 2px;
  color: var(--el-text-color-regular);
}

.plan-timeline {
  max-height: 300px;
  overflow-y: auto;
}

.timeline-day {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.timeline-day:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.day-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.day-label {
  font-weight: 600;
  font-size: 14px;
  color: var(--el-text-color-primary);
}

.day-date {
  font-size: 12px;
  color: var(--el-text-color-regular);
}

.day-events {
  padding-left: 12px;
}

.no-events {
  font-size: 12px;
  color: var(--el-text-color-placeholder);
  font-style: italic;
}

.event-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
  font-size: 12px;
}

.event-time {
  color: var(--el-color-primary);
  font-weight: 500;
  min-width: 40px;
}

.event-title {
  color: var(--el-text-color-regular);
}

.progress-chart {
  text-align: center;
}

.progress-circle {
  margin-bottom: 20px;
}

.progress-text {
  font-size: 16px;
  font-weight: 600;
}

.progress-stats {
  display: flex;
  justify-content: space-around;
}

.progress-item {
  text-align: center;
}

.progress-item .label {
  display: block;
  font-size: 12px;
  color: var(--el-text-color-regular);
  margin-bottom: 4px;
}

.progress-item .value {
  display: block;
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.quick-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.quick-action-btn {
  height: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

.activity-list {
  max-height: 200px;
  overflow-y: auto;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 8px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 24px;
  height: 24px;
  background: var(--el-color-primary-light-8);
  color: var(--el-color-primary);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  flex-shrink: 0;
  margin-top: 2px;
}

.activity-content {
  flex: 1;
}

.activity-text {
  font-size: 13px;
  color: var(--el-text-color-primary);
  margin-bottom: 2px;
  line-height: 1.4;
}

.activity-time {
  font-size: 11px;
  color: var(--el-text-color-placeholder);
}

/* 深色主题适配 */
html.dark .stat-card,
html.dark .content-card {
  background: var(--el-bg-color);
}
</style> 