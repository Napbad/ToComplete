<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import Calendar from 'primevue/calendar'
import Card from 'primevue/card'
import Button from 'primevue/button'
import FileUpload from 'primevue/fileupload'
import Dialog from 'primevue/dialog'
import TaskDetail from '../../components/TaskDetail.vue'
import { useTaskStore } from '../../store/tasks'
import { useAuthStore } from '../../store/auth'
import type { Task } from '../../types'
import ProgressBar from 'primevue/progressbar'

// Import PrimeVue grid CSS
import 'primeflex/primeflex.css'

const taskStore = useTaskStore()
const authStore = useAuthStore()
const selectedDate = ref(new Date())
const showPhotoUpload = ref(false)
const selectedTask = ref<Task | null>(null)
const showTaskDetail = ref(false)
const isLoading = ref(true)
const error = ref<string | null>(null)
const taskDate = ref<Date>(new Date())
const tasks = computed(() => taskStore.tasks)
const dayStats = computed(() => taskStore.dayStats)
const isOffline = computed(() => taskStore.isOfflineMode)
const completeTaskPageVisible = ref(false)

const isTaskEditable = computed(() => {
  if (!selectedTask.value) return false
  const taskDate = new Date(selectedTask.value.startTime)
  const today = new Date()
  return taskDate >= today
})

const formatDate = (date: Date): string => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const formatted = `${year}-${month}-${day}`;
  // console.log('Formatted date:', formatted);
  return formatted;
}

const onDateSelect = async (value: Date) => {
  console.log('Selected date:', value)
  try {
    isLoading.value = true
    error.value = null
    taskDate.value = value
    await taskStore.fetchTasks(value)
  } catch (e) {
    error.value = e instanceof Error ? e.message : '加载任务失败'
  } finally {
    isLoading.value = false
  }
}

const uploadPhoto = async (event: any) => {
  if (!selectedTask.value) return
  const file = event.files[0]
  await taskStore.uploadTaskPhoto(selectedTask.value.id, file)
  showPhotoUpload.value = false
}

const openTaskDetail = (task: Task) => {
  selectedTask.value = task
  showTaskDetail.value = true
}

const handleTaskSave = async (updatedTask: Task) => {
  await taskStore.updateTask(updatedTask)
}

const computeProgressWidth = (date: Date) => {
  if (!date || !(date instanceof Date)) return '0%'
  const stats = getDayStats(date)
  if (!stats || stats.totalTasks === 0) return '0%'
  return `${(stats.completedTasks / stats.totalTasks) * 100}%`
}

const formatProgress = (date: Date) => {
  if (!date || !(date instanceof Date)) return '无任务'
  const stats = getDayStats(date)
  if (!stats || stats.totalTasks === 0) return '无任务'
  return `${stats.completedTasks}/${stats.totalTasks}`
}

const getDayStats = (date: Date) => {
  if (!date || !(date instanceof Date) || isNaN(date.getTime())) {
    console.log('Invalid date object:', date);
    return undefined;
  }
  
  const formattedDate = formatDate(date);
  // console.log('Looking for stats with date:', formattedDate);
  // console.log('Available dates:', dayStats.value?.map(stat => stat.date));
  //
  const stats = dayStats.value?.find(stat => stat.date === formattedDate);
  // console.log('Found stats:', stats);

  if (!stats) return undefined;
  return {
    completed: stats.completedTasks || 0,
    uncompleted: (stats.totalTasks || 0) - (stats.completedTasks || 0),
    total: stats.totalTasks || 0
  };
};

const hasDayStats = (date: Date) => {
  if (!date || !(date instanceof Date) || isNaN(date.getTime())) {
    console.log('Invalid date in hasDayStats:', date);
    return false;
  }
  const stats = getDayStats(date);
  return stats !== undefined && stats.total > 0;
};

const dayHasStats = (date: Date | null): boolean => {
  if (!date) return false;
  const dateStr = formatDate(date);
  console.log('Checking stats for date:', dateStr);
  return dayStats.value?.some(stat => stat.date === dateStr) || false;
};

const getStats = (date: Date | null) => {
  if (!date) return undefined;
  const dateStr = formatDate(date);
  const stats = dayStats.value?.find(stat => stat.date === dateStr);
  if (!stats) return undefined;
  return {
    completed: stats.completedTasks || 0,
    uncompleted: (stats.totalTasks || 0) - (stats.completedTasks || 0),
    total: stats.totalTasks || 0
  };
};

const handleTaskCompleting = async (task: Task) =>{
  showPhotoUpload.value = true;
};

onMounted(async () => {
  try {
    isLoading.value = true
    error.value = null
    console.log('Dashboard mounted, auth state:', authStore.$state)
    
    const today = new Date()
    await taskStore.fetchMonthStats(today.getFullYear(), today.getMonth())
    await taskStore.fetchTasks(today)

    // 打印加载的数据以便调试
    console.log('Loaded month stats:', taskStore.dayStats)
    console.log('Loaded tasks:', taskStore.tasks)

    if (taskStore.isOfflineMode) {
      error.value = '无法连接到服务器，已切换至离线模式'
    }
  } catch (e) {
    console.error('Dashboard initialization error:', e)
    error.value = e instanceof Error ? e.message : '加载数据失败'
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <div class="dashboard-container">
    <div class="grid height: 100%">
      <div class="col-12 md:col-8 height: 100%">
        <Card>
          <template #title>
            <span style="display: flex; font-size: 3rem; justify-content: center; align-items: center;">任务日历</span>
            <br>
            <span v-if="taskStore.isOfflineMode" class="offline-badge">离线模式</span>
          </template>
          <template #content>
            <div v-if="isLoading" class="loading-overlay">
              <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
            </div>
            <div v-else-if="error" class="error-message">
              {{ error }}
            </div>
            <div v-else class="calendar-wrapper">
              <Calendar 
                v-model="selectedDate"
                :inline="true"
                @date-select="onDateSelect"
                selectionMode="single"
                :showTime="false"
                class="w-full"
                :showOtherMonths="true"
                :selectOtherMonths="true"
                :firstDayOfWeek="1"
                @month-change="(e) => {
                  console.log('Month changed:', e);
                  const date = new Date(e);
                  taskStore.fetchMonthStats(date.getFullYear(), date.getMonth());
                }"
              >
                <template #date="slotProps">
                  <div class="custom-date-cell">
                    <div class="date-number">
                      {{ slotProps.date.day }}
                    </div>
                    <div v-if="hasDayStats(new Date(slotProps.date.year, slotProps.date.month, slotProps.date.day))" 
                         class="task-stats-container">
                      <div class="task-progress-bar">
                        <div 
                          class="progress-fill"
                          :style="{
                            width: `${(getDayStats(new Date(slotProps.date.year, slotProps.date.month, slotProps.date.day))?.completed / 
                                    getDayStats(new Date(slotProps.date.year, slotProps.date.month, slotProps.date.day))?.total) * 100}%`
                          }"
                        ></div>
                      </div>
                      <div class="task-stats">
                        <span class="completed">{{ getDayStats(new Date(slotProps.date.year, slotProps.date.month, slotProps.date.day))?.completed }}</span>
                        <span class="separator">/</span>
                        <span class="total">{{ getDayStats(new Date(slotProps.date.year, slotProps.date.month, slotProps.date.day))?.total }}</span>
                      </div>
                    </div>
                  </div>
                </template>
              </Calendar>
            </div>
          </template>
        </Card>
      </div>
      <div class="col-12 md:col-4">
        <Card>
          <template #title >
            <span style="display: flex; font-size: 2.5rem; justify-content: center; align-items: center;">任务日历</span>
            <span
                style="display: flex;
                font-size: 1.5rem;
                justify-content: center;
                align-items: center;">
              {{ taskDate.getFullYear() }} 年
              {{ taskDate.getMonth() + 1 }} 月
              {{ taskDate.getDate() }} 日
            </span>
            <br>
          </template>
          <br/>

          <template #content>
            <div v-if="taskStore.tasks.length === 0" class="no-tasks">
              暂无任务
            </div>
            <div v-else class="task-list">
              <div v-for="task in taskStore.tasks" :key="task.id" class="task-item">
                <div class="task-header">
                  <span class="task-title">{{ task.title }}</span>
                  <span :class="['task-status', task.completed ? 'completed' : 'pending']">
                    {{ task.completed ? '已完成' : '待完成' }}
                  </span>
                </div>
                <span class="task-time">
                  {{ new Date(task.startTime).toLocaleTimeString() }} - {{ new Date(task.endTime).toLocaleTimeString() }}
                </span>

                  <Button v-if="taskDate.getDay() == (new Date()).getDay()
                   && taskDate.getMonth() == (new Date()).getMonth()
                  && taskDate.getFullYear() == (new Date()).getFullYear()
                   && !task.completed" @click="handleTaskCompleting(task)" label="去完成" severity="success" variant="text" class="complete-task"/>
              </div>
            </div>
          </template>
        </Card>
      </div>
    </div>

    <!-- Photo upload dialog -->
    <Dialog
        :visible="showPhotoUpload"
        @update:visible="(value) => showPhotoUpload = value"
        modal
        header="上传照片完成任务"
        class="photo-upload-dialog"
        :style="{ width: '60vw', maxWidth: '800px', justifyItems: 'center' }"
    >
      <div class="upload-container">
        <!-- Image Preview Area -->
        <div class="image-preview" v-if="!uploadedImage">
          <i class="pi pi-image" style="font-size: 4rem; color: #999;"></i>
          <p>请上传任务完成的照片</p>
        </div>

        <!-- Uploaded Image Preview -->
        <div class="image-preview" v-else>
          <img :src="uploadedImage" alt="预览图" style="max-width: 100%; border-radius: 8px;" />
        </div>

        <!-- File Upload -->
        <FileUpload
            mode="advanced"
            name="demo[]"
            accept="image/*"
            :maxFileSize="1000000"
            @upload="handleUpload"
            @select="onSelectImage"
            :custom-upload="true"
            @uploader="customUploader"
            choose-label="选择照片"
            upload-label="确认上传"
            cancel-label="取消"
            class="w-full"
        />

        <small class="p-error" v-if="uploadError">{{ uploadError }}</small>
      </div>
    </Dialog>

    <!-- Task detail dialog -->
    <TaskDetail
      :visible="showTaskDetail"
      @update:visible="(value) => showTaskDetail = value"
      :task="selectedTask"
      :isEditable="isTaskEditable"
      @save="handleTaskSave"
    />
  </div>
</template>

<style>
/* Override dark mode styles */
:root {
  color: #213547;
  background-color: #ffffff;
}

body {
  color: #213547;
  background-color: #ffffff;
}

@media (prefers-color-scheme: dark) {
  :root {
    color: #213547;
    background-color: #ffffff;
  }
  body {
    color: #213547;
    background-color: #ffffff;
  }
}
</style>

<style scoped>
.dashboard-container {
  padding: 1rem;
  height: calc(100vh - 4rem);
}

.calendar-wrapper {
  position: relative;
  width: 100%;
  min-height: 400px;
  /* height: 80%; */
  background-color: #ffffff;
  border-radius: 6px;
  overflow: hidden;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.8);
  z-index: 1;
}

.error-message {
  color: #dc3545;
  text-align: center;
  padding: 1rem;
}

.offline-badge {
  font-size: 0.8rem;
  background-color: #ffc107;
  color: #000;
  padding: 0.2rem 0.5rem;
  border-radius: 0.25rem;
  margin-left: 0.5rem;
}

.custom-date-cell {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  min-height: 70px;
  height: 12vh;
  width: 100%;
  padding: 4px;
  position: relative;
}

.date-number {
  font-size: 2.6rem;
  font-weight: 500;
  margin-bottom: 4px;
  color: #000;
}

.task-stats-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  padding: 0 4px;
}

.task-progress-bar {
  width: 100%;
  height: 4px;
  background-color: #e9ecef;
  border-radius: 2px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background-color: #4CAF50;
  transition: width 0.3s ease;
}

.task-stats {
  display: flex;
  align-items: center;
  gap: 2px;
  font-size: 1rem;
  font-weight: 500;
}

.task-stats .completed {
  color: #4CAF50;
}

.task-stats .separator {
  color: #6c757d;
  margin: 0 1px;
}

.task-stats .total {
  color: #2196F3;
}

.empty-cell {
  background-color: #f8f9fa;
}

.task-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1rem;

}

.task-item {
  border: 1px solid #dee2e6;
  border-radius: 0.5rem;
  padding: 0.75rem;
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.task-title {
  font-weight: 500;
  padding: 0.2rem 0.5rem;

}

.task-status {
  font-size: 0.8rem;
  padding: 0.2rem 0.5rem;
  border-radius: 0.25rem;
}

.task-status.completed {
  background-color: #198754;
  color: white;
}

.task-status.pending {
  background-color: #ffc107;
  color: black;
}

.task-time {
  font-size: 0.9rem;
  color: #6c757d;
  padding: 0.2rem 0.5rem;
}

.complete-task {
  height: 2vh;
  background-color: #03b7d1;
}

.complete-task:hover {
  background-color: #054f72;
}
.photo-upload-dialog .upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  padding: 2rem;
  background-color: #f5f9fc;
  border-radius: 16px;
}

.image-preview {
  text-align: center;
  padding: 2rem;
  border: 2px dashed #a0d8b9;
  border-radius: 16px;
  width: 100%;
  min-height: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #e8f5e9;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 128, 0, 0.1);
}

.image-preview:hover {
  border-color: #4caf50;
  background-color: #f1fff3;
  box-shadow: 0 6px 16px rgba(0, 128, 0, 0.15);
}

.image-preview i.pi-image {
  color: #4caf50;
}

.image-preview p {
  margin-top: 0.5rem;
  font-size: 1rem;
  color: #333;
}

/* 文件上传区域 */
.p-fileupload .p-button-success {
  background-color: #4caf50 !important;
  border-color: #4caf50 !important;
  color: white !important;
}

.p-fileupload .p-button-success:hover {
  background-color: #388e3c !important;
  border-color: #388e3c !important;
}

.p-fileupload .p-button-danger {
  background-color: #ef5350 !important;
  border-color: #ef5350 !important;
}

.p-fileupload .p-button-danger:hover {
  background-color: #d32f2f !important;
  border-color: #d32f2f !important;
}

/* 错误信息 */
.p-error {
  color: #d32f2f;
  font-weight: bold;
}
.no-tasks {
  text-align: center;
  color: #6c757d;
  padding: 2rem 0;
}

:deep(.p-card) {
  margin-bottom: 1rem;
  background-color: #ffffff;
}

:deep(.p-card .p-card-content) {
  padding: 0;
}

:deep(.p-datepicker) {
  width: 100% !important;
  padding: 0 !important;
}

:deep(.p-datepicker table) {
  margin: 0 !important;
  width: 100% !important;
}

:deep(.p-datepicker table td) {
  padding: 0 !important;
  height: 70px !important;
}

:deep(.p-datepicker table td > span) {
  width: 100% !important;
  height: 100% !important;
  border-radius: 0 !important;
  padding: 0 !important;
  background: none !important;
}

:deep(.p-datepicker table td.p-datepicker-today > span) {
  background-color: #e3f2fd !important;
}

:deep(.p-datepicker table td.p-highlight > span) {
  background-color: #bbdefb !important;
}

:deep(.p-datepicker table td.p-highlight .date-number) {
  color: #1976d2;
  font-weight: bold;
}

:deep(.p-datepicker-header) {
  padding: 0.5rem !important;
  margin-bottom: 0.5rem !important;
  background-color: #ffffff;
  border-bottom: 1px solid #dee2e6;
}

:deep(.p-datepicker-header .p-datepicker-title) {
  margin: 0 0.5rem !important;
}

:deep(.p-datepicker-calendar-container) {
  padding: 0.5rem !important;
  background-color: #ffffff;
}

:deep(.p-datepicker-calendar) {
  margin: 0 !important;
  width: 100% !important;
}

:deep(.p-datepicker-header button) {
  background-color: transparent !important;
  color: #666 !important;
  border: none !important;
  padding: 0.5rem !important;
}

:deep(.p-datepicker-header button:hover) {
  background-color: #f8f9fa !important;
}

:deep(.p-datepicker-month),
:deep(.p-datepicker-year) {
  color: #495057 !important;
  font-weight: 600 !important;
}

:deep(.p-datepicker-weekheader) {
  background-color: #f8f9fa !important;
  border: 1px solid #dee2e6 !important;
  color: #6c757d !important;
  font-weight: 600 !important;
}

:deep(.p-datepicker-weeknumber) {
  background-color: #f8f9fa !important;
  border: 1px solid #dee2e6 !important;
  color: #6c757d !important;
  font-weight: 600 !important;
}
</style> 