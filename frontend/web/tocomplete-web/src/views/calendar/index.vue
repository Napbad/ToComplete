<template>
  <div class="calendar-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">日历视图</h1>
        <p class="page-subtitle">查看和管理您的计划安排</p>
      </div>
      <div class="header-actions">
        <el-button-group>
          <el-button 
            :type="viewMode === 'month' ? 'primary' : 'default'"
            @click="viewMode = 'month'"
          >
            月视图
          </el-button>
          <el-button 
            :type="viewMode === 'week' ? 'primary' : 'default'"
            @click="viewMode = 'week'"
          >
            周视图
          </el-button>
          <el-button 
            :type="viewMode === 'day' ? 'primary' : 'default'"
            @click="viewMode = 'day'"
          >
            日视图
          </el-button>
        </el-button-group>
        <el-button type="primary" @click="showEventDialog = true">
          <el-icon><Plus /></el-icon>
          新建事件
        </el-button>
      </div>
    </div>

    <!-- 日历导航 -->
    <div class="calendar-nav">
      <div class="nav-left">
        <el-button @click="goToPrevious">
          <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <el-button @click="goToNext">
          <el-icon><ArrowRight /></el-icon>
        </el-button>
        <el-button @click="goToToday">今天</el-button>
      </div>
      <div class="nav-center">
        <h2 class="current-date">{{ currentDateDisplay }}</h2>
      </div>
      <div class="nav-right">
        <el-select v-model="selectedCategory" placeholder="筛选分类" clearable style="width: 120px">
          <el-option label="全部" value="" />
          <el-option 
            v-for="category in categories" 
            :key="category.value"
            :label="category.label" 
            :value="category.value"
          />
        </el-select>
      </div>
    </div>

    <!-- 月视图 -->
    <div v-if="viewMode === 'month'" class="month-view">
      <el-card class="calendar-card">
        <div class="month-header">
          <div v-for="day in weekDays" :key="day" class="weekday">
            {{ day }}
          </div>
        </div>
        <div class="month-grid">
          <div
            v-for="date in monthDates"
            :key="date.key"
            class="month-cell"
            :class="{
              'other-month': !date.isCurrentMonth,
              'today': date.isToday,
              'selected': date.date === selectedDate
            }"
            @click="selectDate(date)"
          >
            <div class="cell-header">
              <span class="date-number">{{ date.day }}</span>
            </div>
            <div class="cell-events">
              <div
                v-for="event in date.events"
                :key="event.id"
                class="event-item"
                :class="`event-${event.type}`"
                @click.stop="viewEvent(event)"
              >
                <span class="event-title">{{ event.title }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 周视图 -->
    <div v-if="viewMode === 'week'" class="week-view">
      <el-card class="calendar-card">
        <div class="week-header">
          <div class="time-column"></div>
          <div v-for="date in weekDates" :key="date.key" class="week-day-header">
            <div class="day-name">{{ date.dayName }}</div>
            <div class="day-number" :class="{ today: date.isToday }">
              {{ date.day }}
            </div>
          </div>
        </div>
        <div class="week-grid">
          <div class="time-slots">
            <div v-for="hour in 24" :key="hour" class="time-slot">
              {{ formatHour(hour - 1) }}
            </div>
          </div>
          <div v-for="date in weekDates" :key="date.key" class="day-column">
            <div v-for="hour in 24" :key="hour" class="hour-cell">
              <div
                v-for="event in getEventsForHour(date.date, hour - 1)"
                :key="event.id"
                class="week-event"
                :class="`event-${event.type}`"
                @click="viewEvent(event)"
              >
                <div class="event-time">{{ formatEventTime(event.startTime) }}</div>
                <div class="event-title">{{ event.title }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 日视图 -->
    <div v-if="viewMode === 'day'" class="day-view">
      <el-card class="calendar-card">
        <div class="day-header">
          <h3>{{ selectedDateDisplay }}</h3>
        </div>
        <div class="day-schedule">
          <div v-if="todayEvents.length === 0" class="empty-day">
            <el-empty description="今天没有安排" />
          </div>
          <div v-else class="event-list">
            <div
              v-for="event in todayEvents"
              :key="event.id"
              class="day-event"
              :class="`event-${event.type}`"
              @click="viewEvent(event)"
            >
              <div class="event-time-range">
                {{ formatEventTime(event.startTime) }} - {{ formatEventTime(event.endTime) }}
              </div>
              <div class="event-content">
                <h4 class="event-title">{{ event.title }}</h4>
                <p class="event-description" v-if="event.description">
                  {{ event.description }}
                </p>
                <div class="event-meta">
                  <el-tag size="small" :type="getCategoryType(event.category)">
                    {{ getCategoryLabel(event.category) }}
                  </el-tag>
                  <span v-if="event.location" class="event-location">
                    <el-icon><Location /></el-icon>
                    {{ event.location }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 侧边栏事件详情 -->
    <div v-if="selectedDate" class="sidebar">
      <el-card class="sidebar-card">
        <template #header>
          <h3>{{ formatSelectedDate(selectedDate) }}</h3>
        </template>
        <div class="sidebar-content">
          <div v-if="selectedDateEvents.length === 0" class="no-events">
            <p>这一天没有安排</p>
            <el-button type="primary" @click="showEventDialog = true">
              添加事件
            </el-button>
          </div>
          <div v-else class="event-summary">
            <div
              v-for="event in selectedDateEvents"
              :key="event.id"
              class="summary-event"
              @click="viewEvent(event)"
            >
              <div class="summary-time">
                {{ formatEventTime(event.startTime) }}
              </div>
              <div class="summary-content">
                <div class="summary-title">{{ event.title }}</div>
                <div class="summary-category">
                  <el-tag size="small" :type="getCategoryType(event.category)">
                    {{ getCategoryLabel(event.category) }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 新建/编辑事件对话框 -->
    <el-dialog
      v-model="showEventDialog"
      :title="editingEvent ? '编辑事件' : '新建事件'"
      width="600px"
    >
      <el-form :model="eventForm" label-width="100px" :rules="eventFormRules" ref="eventFormRef">
        <el-form-item label="事件标题" prop="title">
          <el-input v-model="eventForm.title" placeholder="请输入事件标题" />
        </el-form-item>
        <el-form-item label="事件描述">
          <el-input
            v-model="eventForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入事件描述"
          />
        </el-form-item>
        <el-form-item label="日期" prop="date">
          <el-date-picker
            v-model="eventForm.date"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker
            v-model="eventForm.startTime"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker
            v-model="eventForm.endTime"
            placeholder="选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="eventForm.category" placeholder="选择分类">
            <el-option
              v-for="category in categories"
              :key="category.value"
              :label="category.label"
              :value="category.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="地点">
          <el-input v-model="eventForm.location" placeholder="请输入地点" />
        </el-form-item>
        <el-form-item label="提醒">
          <el-select v-model="eventForm.reminder" placeholder="选择提醒时间">
            <el-option label="无提醒" value="" />
            <el-option label="15分钟前" value="15m" />
            <el-option label="30分钟前" value="30m" />
            <el-option label="1小时前" value="1h" />
            <el-option label="1天前" value="1d" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showEventDialog = false">取消</el-button>
        <el-button 
          v-if="editingEvent" 
          type="danger" 
          @click="deleteEvent"
        >
          删除
        </el-button>
        <el-button type="primary" @click="handleSaveEvent">
          {{ editingEvent ? '更新' : '创建' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- 事件详情对话框 -->
    <el-dialog v-model="showEventDetailDialog" title="事件详情" width="500px">
      <div v-if="selectedEvent" class="event-detail">
        <div class="detail-item">
          <label>标题:</label>
          <span>{{ selectedEvent.title }}</span>
        </div>
        <div class="detail-item" v-if="selectedEvent.description">
          <label>描述:</label>
          <span>{{ selectedEvent.description }}</span>
        </div>
        <div class="detail-item">
          <label>日期:</label>
          <span>{{ formatDate(selectedEvent.date) }}</span>
        </div>
        <div class="detail-item">
          <label>时间:</label>
          <span>
            {{ formatEventTime(selectedEvent.startTime) }} - 
            {{ formatEventTime(selectedEvent.endTime) }}
          </span>
        </div>
        <div class="detail-item">
          <label>分类:</label>
          <el-tag :type="getCategoryType(selectedEvent.category)">
            {{ getCategoryLabel(selectedEvent.category) }}
          </el-tag>
        </div>
        <div class="detail-item" v-if="selectedEvent.location">
          <label>地点:</label>
          <span>{{ selectedEvent.location }}</span>
        </div>
      </div>

      <template #footer>
        <el-button @click="showEventDetailDialog = false">关闭</el-button>
        <el-button type="primary" @click="editEvent(selectedEvent)">
          编辑
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'

// 响应式数据
const viewMode = ref('month')
const selectedDate = ref(dayjs().format('YYYY-MM-DD'))
const currentDate = ref(dayjs())
const selectedCategory = ref('')
const showEventDialog = ref(false)
const showEventDetailDialog = ref(false)
const editingEvent = ref(null)
const selectedEvent = ref(null)
const eventFormRef = ref()

// 表单数据
const eventForm = ref({
  title: '',
  description: '',
  date: null,
  startTime: null,
  endTime: null,
  category: 'work',
  location: '',
  reminder: ''
})

const eventFormRules = {
  title: [{ required: true, message: '请输入事件标题', trigger: 'blur' }],
  date: [{ required: true, message: '请选择日期', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

// 分类配置
const categories = ref([
  { label: '工作', value: 'work', type: 'primary' },
  { label: '学习', value: 'study', type: 'success' },
  { label: '生活', value: 'life', type: 'info' },
  { label: '运动', value: 'sport', type: 'warning' },
  { label: '娱乐', value: 'entertainment', type: 'danger' }
])

// 周日期标题
const weekDays = ['日', '一', '二', '三', '四', '五', '六']

// 模拟事件数据
const events = ref([
  {
    id: 1,
    title: '团队会议',
    description: '讨论项目进度和下周计划',
    date: dayjs().format('YYYY-MM-DD'),
    startTime: '09:00',
    endTime: '10:30',
    category: 'work',
    location: '会议室A',
    type: 'work'
  },
  {
    id: 2,
    title: 'Vue.js学习',
    description: '学习Vue 3组合式API',
    date: dayjs().format('YYYY-MM-DD'),
    startTime: '14:00',
    endTime: '16:00',
    category: 'study',
    location: '',
    type: 'study'
  },
  {
    id: 3,
    title: '健身训练',
    description: '有氧运动和力量训练',
    date: dayjs().add(1, 'day').format('YYYY-MM-DD'),
    startTime: '18:00',
    endTime: '19:30',
    category: 'sport',
    location: '健身房',
    type: 'sport'
  },
  {
    id: 4,
    title: '项目评审',
    description: '月度项目进度评审会议',
    date: dayjs().add(2, 'day').format('YYYY-MM-DD'),
    startTime: '15:00',
    endTime: '17:00',
    category: 'work',
    location: '大会议室',
    type: 'work'
  }
])

// 计算属性
const currentDateDisplay = computed(() => {
  if (viewMode.value === 'month') {
    return currentDate.value.format('YYYY年MM月')
  } else if (viewMode.value === 'week') {
    const startOfWeek = currentDate.value.startOf('week')
    const endOfWeek = currentDate.value.endOf('week')
    return `${startOfWeek.format('MM月DD日')} - ${endOfWeek.format('MM月DD日')}`
  } else {
    return currentDate.value.format('YYYY年MM月DD日')
  }
})

const selectedDateDisplay = computed(() => {
  return dayjs(selectedDate.value).format('YYYY年MM月DD日 dddd')
})

// 获取月视图日期
const monthDates = computed(() => {
  const startOfMonth = currentDate.value.startOf('month')
  const endOfMonth = currentDate.value.endOf('month')
  const startDate = startOfMonth.startOf('week')
  const endDate = endOfMonth.endOf('week')
  
  const dates = []
  let current = startDate
  
  while (current.isBefore(endDate) || current.isSame(endDate, 'day')) {
    const dateStr = current.format('YYYY-MM-DD')
    const dayEvents = getEventsForDate(dateStr)
    
    dates.push({
      key: dateStr,
      date: dateStr,
      day: current.date(),
      isCurrentMonth: current.month() === currentDate.value.month(),
      isToday: current.isSame(dayjs(), 'day'),
      events: dayEvents
    })
    
    current = current.add(1, 'day')
  }
  
  return dates
})

// 获取周视图日期
const weekDates = computed(() => {
  const startOfWeek = currentDate.value.startOf('week')
  const dates = []
  
  for (let i = 0; i < 7; i++) {
    const date = startOfWeek.add(i, 'day')
    dates.push({
      key: date.format('YYYY-MM-DD'),
      date: date.format('YYYY-MM-DD'),
      day: date.date(),
      dayName: weekDays[i],
      isToday: date.isSame(dayjs(), 'day')
    })
  }
  
  return dates
})

// 获取今日事件
const todayEvents = computed(() => {
  return getEventsForDate(selectedDate.value).sort((a, b) => {
    return a.startTime.localeCompare(b.startTime)
  })
})

// 获取选中日期的事件
const selectedDateEvents = computed(() => {
  return getEventsForDate(selectedDate.value)
})

// 方法
const getEventsForDate = (date) => {
  return events.value.filter(event => {
    if (selectedCategory.value && event.category !== selectedCategory.value) {
      return false
    }
    return event.date === date
  })
}

const getEventsForHour = (date, hour) => {
  const dayEvents = getEventsForDate(date)
  return dayEvents.filter(event => {
    const startHour = parseInt(event.startTime.split(':')[0])
    return startHour === hour
  })
}

const selectDate = (date) => {
  selectedDate.value = date.date
}

const goToPrevious = () => {
  if (viewMode.value === 'month') {
    currentDate.value = currentDate.value.subtract(1, 'month')
  } else if (viewMode.value === 'week') {
    currentDate.value = currentDate.value.subtract(1, 'week')
  } else {
    currentDate.value = currentDate.value.subtract(1, 'day')
    selectedDate.value = currentDate.value.format('YYYY-MM-DD')
  }
}

const goToNext = () => {
  if (viewMode.value === 'month') {
    currentDate.value = currentDate.value.add(1, 'month')
  } else if (viewMode.value === 'week') {
    currentDate.value = currentDate.value.add(1, 'week')
  } else {
    currentDate.value = currentDate.value.add(1, 'day')
    selectedDate.value = currentDate.value.format('YYYY-MM-DD')
  }
}

const goToToday = () => {
  currentDate.value = dayjs()
  selectedDate.value = dayjs().format('YYYY-MM-DD')
}

const formatHour = (hour) => {
  return `${hour.toString().padStart(2, '0')}:00`
}

const formatEventTime = (time) => {
  return time
}

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD')
}

const formatSelectedDate = (date) => {
  return dayjs(date).format('MM月DD日 dddd')
}

const getCategoryType = (category) => {
  const categoryConfig = categories.value.find(c => c.value === category)
  return categoryConfig?.type || 'primary'
}

const getCategoryLabel = (category) => {
  const categoryConfig = categories.value.find(c => c.value === category)
  return categoryConfig?.label || category
}

const viewEvent = (event) => {
  selectedEvent.value = event
  showEventDetailDialog.value = true
}

const editEvent = (event) => {
  editingEvent.value = event
  eventForm.value = {
    title: event.title,
    description: event.description,
    date: dayjs(event.date).toDate(),
    startTime: dayjs(`2000-01-01 ${event.startTime}`).toDate(),
    endTime: dayjs(`2000-01-01 ${event.endTime}`).toDate(),
    category: event.category,
    location: event.location || '',
    reminder: event.reminder || ''
  }
  showEventDetailDialog.value = false
  showEventDialog.value = true
}

const handleSaveEvent = async () => {
  try {
    await eventFormRef.value.validate()
    
    const eventData = {
      title: eventForm.value.title,
      description: eventForm.value.description,
      date: dayjs(eventForm.value.date).format('YYYY-MM-DD'),
      startTime: dayjs(eventForm.value.startTime).format('HH:mm'),
      endTime: dayjs(eventForm.value.endTime).format('HH:mm'),
      category: eventForm.value.category,
      location: eventForm.value.location,
      reminder: eventForm.value.reminder,
      type: eventForm.value.category
    }
    
    if (editingEvent.value) {
      // 更新事件
      Object.assign(editingEvent.value, eventData)
      ElMessage.success('事件更新成功')
    } else {
      // 创建新事件
      const newEvent = {
        id: Date.now(),
        ...eventData
      }
      events.value.push(newEvent)
      ElMessage.success('事件创建成功')
    }
    
    showEventDialog.value = false
    resetForm()
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const deleteEvent = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个事件吗？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const index = events.value.findIndex(e => e.id === editingEvent.value.id)
    if (index > -1) {
      events.value.splice(index, 1)
      ElMessage.success('删除成功')
      showEventDialog.value = false
      resetForm()
    }
  } catch {
    // 用户取消删除
  }
}

const resetForm = () => {
  editingEvent.value = null
  eventForm.value = {
    title: '',
    description: '',
    date: null,
    startTime: null,
    endTime: null,
    category: 'work',
    location: '',
    reminder: ''
  }
}

onMounted(() => {
  // 组件挂载时的初始化
})
</script>

<style scoped>
.calendar-container {
  padding: 0;
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 24px;
}

.page-header {
  grid-column: 1 / -1;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.header-left {
  flex: 1;
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

.calendar-nav {
  grid-column: 1 / -1;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 16px 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.nav-left {
  display: flex;
  gap: 8px;
}

.current-date {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.calendar-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

/* 月视图样式 */
.month-view {
  grid-column: 1;
}

.month-header {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.weekday {
  padding: 12px;
  text-align: center;
  font-weight: 600;
  color: var(--el-text-color-regular);
  background: var(--el-fill-color-light);
}

.month-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
}

.month-cell {
  min-height: 120px;
  border-right: 1px solid var(--el-border-color-lighter);
  border-bottom: 1px solid var(--el-border-color-lighter);
  cursor: pointer;
  transition: background-color 0.3s;
}

.month-cell:hover {
  background-color: var(--el-fill-color-light);
}

.month-cell.other-month {
  color: var(--el-text-color-placeholder);
  background-color: var(--el-fill-color-lighter);
}

.month-cell.today {
  background-color: var(--el-color-primary-light-9);
}

.month-cell.selected {
  background-color: var(--el-color-primary-light-8);
  border: 2px solid var(--el-color-primary);
}

.cell-header {
  padding: 8px;
  text-align: right;
}

.date-number {
  font-weight: 600;
}

.cell-events {
  padding: 0 4px;
  max-height: 80px;
  overflow: hidden;
}

.event-item {
  margin: 2px 0;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: opacity 0.3s;
}

.event-item:hover {
  opacity: 0.8;
}

.event-title {
  white-space: nowrap;
  overflow: hidden;
  text-decoration: ellipsis;
}

.event-work {
  background: var(--el-color-primary-light-8);
  color: var(--el-color-primary);
  border: 1px solid var(--el-color-primary-light-5);
}

.event-study {
  background: var(--el-color-success-light-8);
  color: var(--el-color-success);
  border: 1px solid var(--el-color-success-light-5);
}

.event-life {
  background: var(--el-color-info-light-8);
  color: var(--el-color-info);
  border: 1px solid var(--el-color-info-light-5);
}

.event-sport {
  background: var(--el-color-warning-light-8);
  color: var(--el-color-warning);
  border: 1px solid var(--el-color-warning-light-5);
}

.event-entertainment {
  background: var(--el-color-danger-light-8);
  color: var(--el-color-danger);
  border: 1px solid var(--el-color-danger-light-5);
}

/* 周视图样式 */
.week-view {
  grid-column: 1;
}

.week-header {
  display: grid;
  grid-template-columns: 80px repeat(7, 1fr);
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.time-column {
  background: var(--el-fill-color-light);
  border-right: 1px solid var(--el-border-color-lighter);
}

.week-day-header {
  padding: 12px;
  text-align: center;
  background: var(--el-fill-color-light);
  border-right: 1px solid var(--el-border-color-lighter);
}

.day-name {
  font-size: 12px;
  color: var(--el-text-color-regular);
  margin-bottom: 4px;
}

.day-number {
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.day-number.today {
  color: var(--el-color-primary);
}

.week-grid {
  display: grid;
  grid-template-columns: 80px repeat(7, 1fr);
  max-height: 600px;
  overflow-y: auto;
}

.time-slots {
  background: var(--el-fill-color-light);
  border-right: 1px solid var(--el-border-color-lighter);
}

.time-slot {
  height: 60px;
  padding: 8px;
  border-bottom: 1px solid var(--el-border-color-lighter);
  font-size: 12px;
  color: var(--el-text-color-regular);
  display: flex;
  align-items: flex-start;
}

.day-column {
  border-right: 1px solid var(--el-border-color-lighter);
}

.hour-cell {
  height: 60px;
  border-bottom: 1px solid var(--el-border-color-lighter);
  position: relative;
  padding: 2px;
}

.week-event {
  padding: 4px 6px;
  border-radius: 4px;
  font-size: 11px;
  cursor: pointer;
  margin-bottom: 2px;
}

.event-time {
  font-weight: 600;
  margin-bottom: 2px;
}

/* 日视图样式 */
.day-view {
  grid-column: 1;
}

.day-header {
  padding: 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);
  background: var(--el-fill-color-light);
}

.day-header h3 {
  margin: 0;
  font-size: 18px;
  color: var(--el-text-color-primary);
}

.day-schedule {
  padding: 20px;
  max-height: 600px;
  overflow-y: auto;
}

.empty-day {
  text-align: center;
  padding: 40px 20px;
}

.event-list {
  space-y: 16px;
}

.day-event {
  display: flex;
  gap: 16px;
  padding: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid transparent;
}

.day-event:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.event-time-range {
  font-size: 14px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  min-width: 120px;
}

.event-content {
  flex: 1;
}

.event-content .event-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: var(--el-text-color-primary);
}

.event-description {
  font-size: 14px;
  color: var(--el-text-color-regular);
  margin: 0 0 12px 0;
  line-height: 1.5;
}

.event-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.event-location {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: var(--el-text-color-regular);
}

/* 侧边栏样式 */
.sidebar {
  grid-column: 2;
}

.sidebar-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 20px;
}

.sidebar-content {
  max-height: 400px;
  overflow-y: auto;
}

.no-events {
  text-align: center;
  padding: 20px;
  color: var(--el-text-color-placeholder);
}

.summary-event {
  display: flex;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
  cursor: pointer;
  transition: background-color 0.3s;
}

.summary-event:hover {
  background-color: var(--el-fill-color-light);
}

.summary-event:last-child {
  border-bottom: none;
}

.summary-time {
  font-size: 12px;
  font-weight: 600;
  color: var(--el-color-primary);
  min-width: 60px;
}

.summary-content {
  flex: 1;
}

.summary-title {
  font-size: 14px;
  color: var(--el-text-color-primary);
  margin-bottom: 4px;
}

.summary-category {
  margin-top: 4px;
}

/* 对话框样式 */
.event-detail {
  space-y: 16px;
}

.detail-item {
  display: flex;
  gap: 12px;
  padding: 8px 0;
  align-items: flex-start;
}

.detail-item label {
  font-weight: 600;
  color: var(--el-text-color-regular);
  min-width: 60px;
}

.detail-item span {
  color: var(--el-text-color-primary);
  flex: 1;
}

/* 深色主题适配 */
html.dark .calendar-nav,
html.dark .calendar-card,
html.dark .sidebar-card {
  background: var(--el-bg-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .calendar-container {
    grid-template-columns: 1fr;
  }
  
  .sidebar {
    grid-column: 1;
    order: -1;
  }
  
  .month-cell {
    min-height: 80px;
  }
  
  .event-item {
    font-size: 10px;
  }
}
</style> 