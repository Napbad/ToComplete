<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑备忘录' : '创建备忘录'"
    width="800px"
    :before-close="handleClose"
    class="memo-dialog"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
      class="memo-form"
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="标题" prop="title">
            <el-input
              v-model="form.title"
              placeholder="请输入备忘录标题"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="内容">
            <el-input
              v-model="form.content"
              type="textarea"
              :rows="4"
              placeholder="请输入备忘录内容"
              show-word-limit
              maxlength="500"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="优先级" prop="priority">
            <el-select v-model="form.priority" placeholder="选择优先级">
              <el-option label="低" :value="1">
                <div class="priority-option">
                  <el-tag type="" size="small">低</el-tag>
                  <span>低优先级</span>
                </div>
              </el-option>
              <el-option label="中" :value="2">
                <div class="priority-option">
                  <el-tag type="info" size="small">中</el-tag>
                  <span>中优先级</span>
                </div>
              </el-option>
              <el-option label="高" :value="3">
                <div class="priority-option">
                  <el-tag type="warning" size="small">高</el-tag>
                  <span>高优先级</span>
                </div>
              </el-option>
              <el-option label="紧急" :value="4">
                <div class="priority-option">
                  <el-tag type="danger" size="small">紧急</el-tag>
                  <span>紧急处理</span>
                </div>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="分类">
            <el-select v-model="form.categoryId" placeholder="选择分类" clearable>
              <el-option
                v-for="category in categories"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="状态">
            <el-select v-model="form.status" placeholder="选择状态">
              <el-option label="待完成" :value="0">
                <div class="status-option">
                  <el-tag type="warning" size="small">待完成</el-tag>
                </div>
              </el-option>
              <el-option label="进行中" :value="1">
                <div class="status-option">
                  <el-tag type="primary" size="small">进行中</el-tag>
                </div>
              </el-option>
              <el-option label="已完成" :value="2">
                <div class="status-option">
                  <el-tag type="success" size="small">已完成</el-tag>
                </div>
              </el-option>
              <el-option label="已取消" :value="3">
                <div class="status-option">
                  <el-tag type="info" size="small">已取消</el-tag>
                </div>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="提醒时间">
            <el-date-picker
              v-model="form.remindAt"
              type="datetime"
              placeholder="选择提醒时间"
              format="YYYY-MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="截止时间">
            <el-date-picker
              v-model="form.dueAt"
              type="datetime"
              placeholder="选择截止时间"
              format="YYYY-MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="标签">
            <el-select
              v-model="form.tagIds"
              multiple
              filterable
              allow-create
              default-first-option
              placeholder="选择或创建标签"
              style="width: 100%"
            >
              <el-option
                v-for="tag in tags"
                :key="tag.id"
                :label="tag.name"
                :value="tag.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="地点提醒">
            <el-input
              v-model="form.location"
              placeholder="输入地点信息"
              clearable
            >
              <template #append>
                <el-button @click="getLocation">
                  <el-icon><Location /></el-icon>
                </el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="重复设置">
            <el-select v-model="form.repeatType" placeholder="选择重复" clearable>
              <el-option label="不重复" :value="0" />
              <el-option label="每天" :value="1" />
              <el-option label="每周" :value="2" />
              <el-option label="每月" :value="3" />
              <el-option label="每年" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="高级设置">
            <el-space wrap>
              <el-checkbox v-model="form.requirePhoto">
                需要拍照打卡
              </el-checkbox>
              <el-checkbox v-model="form.enableNotification">
                启用通知提醒
              </el-checkbox>
              <el-checkbox v-model="form.isImportant">
                标记为重要
              </el-checkbox>
              <el-checkbox v-model="form.allowDelay">
                允许延期
              </el-checkbox>
            </el-space>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 子任务 -->
      <el-row :gutter="20" v-if="form.subTasks.length > 0 || showSubTasks">
        <el-col :span="24">
          <el-form-item label="子任务">
            <div class="subtasks-container">
              <div
                v-for="(subtask, index) in form.subTasks"
                :key="index"
                class="subtask-item"
              >
                <el-checkbox v-model="subtask.completed" />
                <el-input
                  v-model="subtask.title"
                  placeholder="子任务标题"
                  size="small"
                  style="flex: 1; margin: 0 8px"
                />
                <el-button
                  type="danger"
                  link
                  size="small"
                  @click="removeSubTask(index)"
                >
                  删除
                </el-button>
              </div>
              <el-button
                type="primary"
                link
                @click="addSubTask"
                style="margin-top: 8px"
              >
                <el-icon><Plus /></el-icon>
                添加子任务
              </el-button>
            </div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="!showSubTasks && form.subTasks.length === 0">
        <el-col :span="24">
          <el-form-item>
            <el-button type="primary" link @click="showSubTasks = true">
              <el-icon><Plus /></el-icon>
              添加子任务
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSubmit">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  memo: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

// 响应式数据
const visible = ref(false)
const loading = ref(false)
const showSubTasks = ref(false)
const formRef = ref()

// 表单数据
const form = reactive({
  title: '',
  content: '',
  priority: 2,
  status: 0,
  categoryId: null,
  tagIds: [],
  remindAt: null,
  dueAt: null,
  location: '',
  repeatType: 0,
  requirePhoto: false,
  enableNotification: true,
  isImportant: false,
  allowDelay: true,
  subTasks: []
})

// 是否为编辑模式
const isEdit = computed(() => !!props.memo)

// 模拟数据
const categories = ref([
  { id: 1, name: '工作' },
  { id: 2, name: '学习' },
  { id: 3, name: '生活' }
])

const tags = ref([
  { id: 1, name: '重要' },
  { id: 2, name: '紧急' },
  { id: 3, name: '项目' }
])

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入备忘录标题', trigger: 'blur' },
    { min: 1, max: 100, message: '标题长度在1-100个字符', trigger: 'blur' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ]
}

// 监听 modelValue 变化
watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val) {
    nextTick(() => {
      initForm()
    })
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

// 监听 memo 变化
watch(() => props.memo, (val) => {
  if (val) {
    initForm()
  }
}, { immediate: true })

// 方法
const initForm = () => {
  if (props.memo) {
    // 编辑模式，填充数据
    Object.assign(form, {
      title: props.memo.title || '',
      content: props.memo.content || '',
      priority: props.memo.priority || 2,
      status: props.memo.status || 0,
      categoryId: props.memo.categoryId || null,
      tagIds: props.memo.tagIds || [],
      remindAt: props.memo.remindAt || null,
      dueAt: props.memo.dueAt || null,
      location: props.memo.location || '',
      repeatType: props.memo.repeatType || 0,
      requirePhoto: props.memo.requirePhoto || false,
      enableNotification: props.memo.enableNotification !== false,
      isImportant: props.memo.isImportant || false,
      allowDelay: props.memo.allowDelay !== false,
      subTasks: props.memo.subTasks ? [...props.memo.subTasks] : []
    })
    
    showSubTasks.value = form.subTasks.length > 0
  } else {
    // 新建模式，重置表单
    resetForm()
  }
}

const resetForm = () => {
  Object.assign(form, {
    title: '',
    content: '',
    priority: 2,
    status: 0,
    categoryId: null,
    tagIds: [],
    remindAt: null,
    dueAt: null,
    location: '',
    repeatType: 0,
    requirePhoto: false,
    enableNotification: true,
    isImportant: false,
    allowDelay: true,
    subTasks: []
  })
  
  showSubTasks.value = false
  formRef.value?.clearValidate()
}

const addSubTask = () => {
  form.subTasks.push({
    title: '',
    completed: false
  })
}

const removeSubTask = (index) => {
  form.subTasks.splice(index, 1)
  if (form.subTasks.length === 0) {
    showSubTasks.value = false
  }
}

const getLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords
        form.location = `${latitude.toFixed(6)}, ${longitude.toFixed(6)}`
        ElMessage.success('位置获取成功')
      },
      (error) => {
        ElMessage.error('位置获取失败: ' + error.message)
      }
    )
  } else {
    ElMessage.error('浏览器不支持地理位置定位')
  }
}

const handleClose = () => {
  visible.value = false
  resetForm()
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    // 过滤空的子任务
    const filteredSubTasks = form.subTasks.filter(task => task.title.trim())
    const submitData = {
      ...form,
      subTasks: filteredSubTasks
    }

    if (isEdit.value) {
      // TODO: 调用API更新备忘录
      await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟API调用
      ElMessage.success('备忘录更新成功')
    } else {
      // TODO: 调用API创建备忘录
      await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟API调用
      ElMessage.success('备忘录创建成功')
    }

    emit('success')
    handleClose()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(isEdit.value ? '备忘录更新失败' : '备忘录创建失败')
      console.error('操作失败:', error)
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.memo-dialog {
  border-radius: 12px;
}

.memo-form {
  margin-top: 20px;
}

.priority-option,
.status-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.subtasks-container {
  border: 1px solid var(--el-border-color-light);
  border-radius: 6px;
  padding: 12px;
  background: var(--el-fill-color-lighter);
}

.subtask-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.subtask-item:last-child {
  margin-bottom: 0;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-dialog__header) {
  padding: 20px 20px 0 20px;
}

:deep(.el-dialog__body) {
  padding: 10px 20px 20px 20px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-select),
:deep(.el-date-picker) {
  width: 100%;
}

:deep(.el-textarea__inner) {
  resize: vertical;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .memo-dialog {
    margin: 5vh auto;
    width: 95vw !important;
  }
  
  :deep(.el-dialog__body) {
    padding: 10px 15px 20px 15px;
  }
  
  .el-col {
    margin-bottom: 10px;
  }
}
</style> 