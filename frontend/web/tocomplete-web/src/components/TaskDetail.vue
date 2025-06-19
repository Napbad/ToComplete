<!-- TaskDetail.vue -->
<script setup lang="ts">
import { ref, watch } from 'vue'
import Dialog from 'primevue/dialog'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Calendar from 'primevue/calendar'
import Textarea from 'primevue/textarea'

interface Task {
  id: string
  title: string
  description: string
  startTime: string
  endTime: string
  completed: boolean
  photoRequired: boolean
  photoUrl?: string
}

const props = defineProps<{
  visible: boolean
  task: Task | null
  isEditable: boolean
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'save', task: Task): void
}>()

const editedTask = ref<Task | null>(null)

watch(() => props.task, (newTask) => {
  if (newTask) {
    editedTask.value = { ...newTask }
  }
}, { immediate: true })

const handleSave = () => {
  if (editedTask.value) {
    emit('save', editedTask.value)
    emit('update:visible', false)
  }
}

const handleClose = () => {
  emit('update:visible', false)
}
</script>

<template>
  <Dialog
    :visible="visible"
    @update:visible="(value) => emit('update:visible', value)"
    :modal="true"
    :header="isEditable ? '编辑任务' : '查看任务'"
    :style="{ width: '50vw' }"
  >
    <div v-if="editedTask" class="task-detail-form">
      <div class="form-group">
        <label>任务标题</label>
        <InputText
          v-model="editedTask.title"
          :disabled="!isEditable"
          class="w-full"
        />
      </div>

      <div class="form-group">
        <label>任务描述</label>
        <Textarea
          v-model="editedTask.description"
          :disabled="!isEditable"
          rows="3"
          class="w-full"
        />
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>开始时间</label>
          <Calendar
            v-model="editedTask.startTime"
            :disabled="!isEditable"
            showTime
            hourFormat="24"
            class="w-full"
          />
        </div>

        <div class="form-group">
          <label>结束时间</label>
          <Calendar
            v-model="editedTask.endTime"
            :disabled="!isEditable"
            showTime
            hourFormat="24"
            class="w-full"
          />
        </div>
      </div>

      <div class="form-group">
        <div class="p-field-checkbox">
          <input
            type="checkbox"
            v-model="editedTask.photoRequired"
            :disabled="!isEditable"
            id="photoRequired"
          />
          <label for="photoRequired">需要照片打卡</label>
        </div>
      </div>

      <div v-if="editedTask.photoUrl" class="form-group">
        <label>已上传照片</label>
        <img :src="editedTask.photoUrl" class="task-photo" alt="Task photo" />
      </div>
    </div>

    <template #footer>
      <Button
        v-if="isEditable"
        label="保存"
        icon="pi pi-check"
        @click="handleSave"
        autofocus
      />
      <Button
        label="关闭"
        icon="pi pi-times"
        @click="handleClose"
        class="p-button-text"
      />
    </template>
  </Dialog>
</template>

<style scoped>
.task-detail-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  padding: 1rem 0;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-row {
  display: flex;
  gap: 1rem;
}

.form-row .form-group {
  flex: 1;
}

label {
  font-weight: 600;
  color: #333;
}

.task-photo {
  max-width: 100%;
  max-height: 300px;
  border-radius: 8px;
}

.p-field-checkbox {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

:deep(.p-calendar) {
  width: 100%;
}

:deep(.p-inputtext) {
  width: 100%;
}
</style> 