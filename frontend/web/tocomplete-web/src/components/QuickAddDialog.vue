<template>
  <el-dialog
    v-model="visible"
    title="快速添加"
    width="600px"
    :before-close="handleClose"
    class="quick-add-dialog"
  >
    <el-tabs v-model="activeTab" class="quick-tabs">
      <el-tab-pane label="备忘录" name="memo">
        <el-form
          ref="memoFormRef"
          :model="memoForm"
          :rules="memoRules"
          label-width="80px"
          class="quick-form"
        >
          <el-form-item label="标题" prop="title">
            <el-input
              v-model="memoForm.title"
              placeholder="请输入备忘录标题"
              clearable
            />
          </el-form-item>

          <el-form-item label="内容">
            <el-input
              v-model="memoForm.content"
              type="textarea"
              :rows="3"
              placeholder="请输入备忘录内容"
            />
          </el-form-item>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="优先级" prop="priority">
                <el-select v-model="memoForm.priority" placeholder="选择优先级">
                  <el-option label="低" :value="1" />
                  <el-option label="中" :value="2" />
                  <el-option label="高" :value="3" />
                  <el-option label="紧急" :value="4" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="分类">
                <el-select v-model="memoForm.categoryId" placeholder="选择分类" clearable>
                  <el-option
                    v-for="category in categories"
                    :key="category.id"
                    :label="category.name"
                    :value="category.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="提醒时间">
                <el-date-picker
                  v-model="memoForm.remindAt"
                  type="datetime"
                  placeholder="选择提醒时间"
                  format="YYYY-MM-DD HH:mm"
                  value-format="YYYY-MM-DD HH:mm:ss"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="截止时间">
                <el-date-picker
                  v-model="memoForm.dueAt"
                  type="datetime"
                  placeholder="选择截止时间"
                  format="YYYY-MM-DD HH:mm"
                  value-format="YYYY-MM-DD HH:mm:ss"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item>
            <el-checkbox v-model="memoForm.requirePhoto">需要拍照打卡</el-checkbox>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane label="计划" name="plan">
        <el-form
          ref="planFormRef"
          :model="planForm"
          :rules="planRules"
          label-width="80px"
          class="quick-form"
        >
          <el-form-item label="标题" prop="title">
            <el-input
              v-model="planForm.title"
              placeholder="请输入计划标题"
              clearable
            />
          </el-form-item>

          <el-form-item label="描述">
            <el-input
              v-model="planForm.description"
              type="textarea"
              :rows="3"
              placeholder="请输入计划描述"
            />
          </el-form-item>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="计划类型" prop="planType">
                <el-select v-model="planForm.planType" placeholder="选择计划类型">
                  <el-option label="日计划" :value="1" />
                  <el-option label="周计划" :value="2" />
                  <el-option label="月计划" :value="3" />
                  <el-option label="季度计划" :value="4" />
                  <el-option label="年计划" :value="5" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="颜色">
                <el-color-picker v-model="planForm.color" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="开始时间" prop="startTime">
                <el-date-picker
                  v-model="planForm.startTime"
                  type="datetime"
                  placeholder="选择开始时间"
                  format="YYYY-MM-DD HH:mm"
                  value-format="YYYY-MM-DD HH:mm:ss"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束时间" prop="endTime">
                <el-date-picker
                  v-model="planForm.endTime"
                  type="datetime"
                  placeholder="选择结束时间"
                  format="YYYY-MM-DD HH:mm"
                  value-format="YYYY-MM-DD HH:mm:ss"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-tab-pane>
    </el-tabs>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSubmit">
          创建
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

// 响应式数据
const visible = ref(false)
const activeTab = ref('memo')
const loading = ref(false)

// 表单引用
const memoFormRef = ref()
const planFormRef = ref()

// 备忘录表单
const memoForm = reactive({
  title: '',
  content: '',
  priority: 2,
  categoryId: null,
  remindAt: null,
  dueAt: null,
  requirePhoto: false
})

// 计划表单
const planForm = reactive({
  title: '',
  description: '',
  planType: 1,
  color: '#409EFF',
  startTime: null,
  endTime: null
})

// 模拟分类数据
const categories = ref([
  { id: 1, name: '工作' },
  { id: 2, name: '学习' },
  { id: 3, name: '生活' }
])

// 表单验证规则
const memoRules = {
  title: [
    { required: true, message: '请输入备忘录标题', trigger: 'blur' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ]
}

const planRules = {
  title: [
    { required: true, message: '请输入计划标题', trigger: 'blur' }
  ],
  planType: [
    { required: true, message: '请选择计划类型', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ]
}

// 监听 modelValue 变化
watch(() => props.modelValue, (val) => {
  visible.value = val
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

// 方法
const handleClose = () => {
  visible.value = false
  resetForms()
}

const resetForms = () => {
  // 重置备忘录表单
  Object.assign(memoForm, {
    title: '',
    content: '',
    priority: 2,
    categoryId: null,
    remindAt: null,
    dueAt: null,
    requirePhoto: false
  })

  // 重置计划表单
  Object.assign(planForm, {
    title: '',
    description: '',
    planType: 1,
    color: '#409EFF',
    startTime: null,
    endTime: null
  })

  // 清除表单验证
  memoFormRef.value?.clearValidate()
  planFormRef.value?.clearValidate()
}

const handleSubmit = async () => {
  try {
    if (activeTab.value === 'memo') {
      await memoFormRef.value.validate()
      await createMemo()
    } else {
      await planFormRef.value.validate()
      await createPlan()
    }
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const createMemo = async () => {
  loading.value = true
  try {
    // TODO: 调用API创建备忘录
    await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟API调用
    
    ElMessage.success('备忘录创建成功')
    emit('success')
    handleClose()
  } catch (error) {
    ElMessage.error('备忘录创建失败')
    console.error('创建备忘录失败:', error)
  } finally {
    loading.value = false
  }
}

const createPlan = async () => {
  loading.value = true
  try {
    // TODO: 调用API创建计划
    await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟API调用
    
    ElMessage.success('计划创建成功')
    emit('success')
    handleClose()
  } catch (error) {
    ElMessage.error('计划创建失败')
    console.error('创建计划失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.quick-add-dialog {
  border-radius: 12px;
}

.quick-tabs {
  margin-top: 20px;
}

.quick-form {
  margin-top: 20px;
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

:deep(.el-tabs__header) {
  margin: 0 0 20px 0;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-select),
:deep(.el-date-picker) {
  width: 100%;
}
</style> 