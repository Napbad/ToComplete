<template>
  <div class="plan-list">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">计划管理</h1>
        <p class="page-subtitle">制定和管理您的长期目标和计划</p>
      </div>
      <div class="header-actions">
        <el-button @click="showFilters = !showFilters">
          <el-icon><Filter /></el-icon>
          筛选
        </el-button>
        <el-button type="primary" @click="showCreateDialog = true">
          <el-icon><Plus /></el-icon>
          新建计划
        </el-button>
      </div>
    </div>

    <!-- 筛选条件 -->
    <div v-show="showFilters" class="filters-container">
      <el-card class="filters-card">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-select v-model="filters.status" placeholder="状态" clearable>
              <el-option label="全部" value="" />
              <el-option label="未开始" :value="0" />
              <el-option label="进行中" :value="1" />
              <el-option label="已完成" :value="2" />
              <el-option label="已暂停" :value="3" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filters.priority" placeholder="优先级" clearable>
              <el-option label="全部" value="" />
              <el-option label="低" :value="1" />
              <el-option label="中" :value="2" />
              <el-option label="高" :value="3" />
              <el-option label="紧急" :value="4" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-date-picker
              v-model="filters.dateRange"
              type="daterange"
              placeholder="选择日期范围"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            />
          </el-col>
          <el-col :span="6">
            <el-input
              v-model="filters.keyword"
              placeholder="搜索计划标题"
              clearable
              @keyup.enter="loadPlans"
            >
              <template #suffix>
                <el-icon class="search-icon" @click="loadPlans">
                  <Search />
                </el-icon>
              </template>
            </el-input>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-row">
      <el-row :gutter="20">
        <el-col :span="6" v-for="stat in stats" :key="stat.key">
          <div class="stat-item" :style="{ borderColor: stat.color }">
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
        </el-col>
      </el-row>
    </div>

    <!-- 计划列表 -->
    <el-card class="plan-list-card">
      <div class="list-header">
        <div class="view-toggle">
          <el-radio-group v-model="viewMode">
            <el-radio-button label="list">
              <el-icon><List /></el-icon>
              列表
            </el-radio-button>
            <el-radio-button label="grid">
              <el-icon><Grid /></el-icon>
              网格
            </el-radio-button>
          </el-radio-group>
        </div>
        <div class="sort-options">
          <el-select v-model="sortBy" placeholder="排序" style="width: 140px">
            <el-option label="创建时间" value="createdAt" />
            <el-option label="更新时间" value="updatedAt" />
            <el-option label="开始时间" value="startDate" />
            <el-option label="结束时间" value="endDate" />
            <el-option label="优先级" value="priority" />
          </el-select>
          <el-button
            :icon="sortOrder === 'asc' ? 'SortUp' : 'SortDown'"
            @click="toggleSortOrder"
          />
        </div>
      </div>

      <!-- 列表视图 -->
      <div v-if="viewMode === 'list'" class="plan-list-view">
        <div v-if="loading" class="loading-state">
          <el-skeleton :rows="5" animated />
        </div>
        <div v-else-if="plans.length === 0" class="empty-state">
          <el-empty description="暂无计划" />
        </div>
        <div v-else class="plan-items">
          <div
            v-for="plan in plans"
            :key="plan.id"
            class="plan-item"
            @click="handlePlanClick(plan)"
          >
            <div class="plan-status">
              <el-tag :type="getStatusType(plan.status)" size="large">
                {{ getStatusText(plan.status) }}
              </el-tag>
            </div>
            <div class="plan-content">
              <div class="plan-title">{{ plan.title }}</div>
              <div class="plan-description" v-if="plan.description">
                {{ plan.description }}
              </div>
              <div class="plan-meta">
                <el-tag :type="getPriorityType(plan.priority)" size="small">
                  {{ getPriorityText(plan.priority) }}
                </el-tag>
                <span class="plan-dates">
                  <el-icon><Calendar /></el-icon>
                  {{ formatDateRange(plan.startDate, plan.endDate) }}
                </span>
                <div class="plan-progress">
                  <span class="progress-label">进度:</span>
                  <el-progress
                    :percentage="plan.progress"
                    :stroke-width="6"
                    :show-text="false"
                    style="width: 100px; margin: 0 8px;"
                  />
                  <span class="progress-text">{{ plan.progress }}%</span>
                </div>
              </div>
            </div>
            <div class="plan-actions">
              <el-button
                type="primary"
                link
                @click.stop="editPlan(plan)"
              >
                编辑
              </el-button>
              <el-button
                type="success"
                link
                @click.stop="viewPlanDetail(plan)"
              >
                详情
              </el-button>
              <el-button
                type="danger"
                link
                @click.stop="deletePlan(plan)"
              >
                删除
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 网格视图 -->
      <div v-if="viewMode === 'grid'" class="plan-grid-view">
        <div v-if="loading" class="loading-state">
          <el-row :gutter="20">
            <el-col :span="8" v-for="i in 6" :key="i">
              <el-skeleton :rows="4" animated />
            </el-col>
          </el-row>
        </div>
        <div v-else-if="plans.length === 0" class="empty-state">
          <el-empty description="暂无计划" />
        </div>
        <div v-else class="plan-grid">
          <div
            v-for="plan in plans"
            :key="plan.id"
            class="plan-card"
            @click="handlePlanClick(plan)"
          >
            <div class="card-header">
              <el-tag :type="getStatusType(plan.status)" size="small">
                {{ getStatusText(plan.status) }}
              </el-tag>
              <el-dropdown @command="(command) => handleCardAction(command, plan)">
                <el-button text>
                  <el-icon><MoreFilled /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="edit">编辑</el-dropdown-item>
                    <el-dropdown-item command="detail">详情</el-dropdown-item>
                    <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <div class="card-content">
              <h4 class="card-title">{{ plan.title }}</h4>
              <p class="card-description">{{ plan.description }}</p>
              <div class="card-progress">
                <div class="progress-header">
                  <span>进度</span>
                  <span>{{ plan.progress }}%</span>
                </div>
                <el-progress :percentage="plan.progress" :stroke-width="8" />
              </div>
              <div class="card-meta">
                <el-tag :type="getPriorityType(plan.priority)" size="small">
                  {{ getPriorityText(plan.priority) }}
                </el-tag>
                <span class="card-dates">
                  {{ formatDateRange(plan.startDate, plan.endDate) }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 创建/编辑计划对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingPlan ? '编辑计划' : '新建计划'"
      width="600px"
    >
      <el-form :model="planForm" label-width="100px" :rules="planFormRules" ref="planFormRef">
        <el-form-item label="计划标题" prop="title">
          <el-input v-model="planForm.title" placeholder="请输入计划标题" />
        </el-form-item>
        <el-form-item label="计划描述" prop="description">
          <el-input
            v-model="planForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入计划描述"
          />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="planForm.priority" placeholder="选择优先级">
            <el-option label="低" :value="1" />
            <el-option label="中" :value="2" />
            <el-option label="高" :value="3" />
            <el-option label="紧急" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker
            v-model="planForm.startDate"
            type="date"
            placeholder="选择开始日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker
            v-model="planForm.endDate"
            type="date"
            placeholder="选择结束日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="目标" prop="goals">
          <el-input
            v-model="planForm.goals"
            type="textarea"
            :rows="2"
            placeholder="描述计划的具体目标"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSavePlan">
          {{ editingPlan ? '更新' : '创建' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const showFilters = ref(false)
const showCreateDialog = ref(false)
const editingPlan = ref(null)
const viewMode = ref('list')
const sortBy = ref('createdAt')
const sortOrder = ref('desc')
const planFormRef = ref()

// 筛选条件
const filters = ref({
  status: '',
  priority: '',
  dateRange: null,
  keyword: ''
})

// 分页
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

// 计划表单
const planForm = ref({
  title: '',
  description: '',
  priority: 2,
  startDate: null,
  endDate: null,
  goals: ''
})

const planFormRules = {
  title: [
    { required: true, message: '请输入计划标题', trigger: 'blur' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ],
  startDate: [
    { required: true, message: '请选择开始日期', trigger: 'change' }
  ],
  endDate: [
    { required: true, message: '请选择结束日期', trigger: 'change' }
  ]
}

// 统计数据
const stats = ref([
  {
    key: 'total',
    label: '总计划',
    value: 12,
    icon: 'Document',
    color: '#409eff'
  },
  {
    key: 'active',
    label: '进行中',
    value: 5,
    icon: 'VideoPlay',
    color: '#67c23a'
  },
  {
    key: 'completed',
    label: '已完成',
    value: 6,
    icon: 'CircleCheck',
    color: '#e6a23c'
  },
  {
    key: 'paused',
    label: '已暂停',
    value: 1,
    icon: 'VideoPause',
    color: '#f56c6c'
  }
])

// 计划列表
const plans = ref([
  {
    id: 1,
    title: '学习Vue.js进阶课程',
    description: '深入学习Vue 3的组合式API、状态管理和高级特性',
    status: 1,
    priority: 3,
    progress: 65,
    startDate: dayjs().subtract(15, 'day').toDate(),
    endDate: dayjs().add(15, 'day').toDate(),
    goals: '掌握Vue 3核心概念，能够独立开发中型项目'
  },
  {
    id: 2,
    title: '健身训练计划',
    description: '制定为期3个月的健身训练计划，提升身体素质',
    status: 1,
    priority: 2,
    progress: 40,
    startDate: dayjs().subtract(30, 'day').toDate(),
    endDate: dayjs().add(60, 'day').toDate(),
    goals: '减重10公斤，增强肌肉力量和耐力'
  },
  {
    id: 3,
    title: '英语口语提升',
    description: '通过每日练习和实际对话提升英语口语水平',
    status: 2,
    priority: 2,
    progress: 100,
    startDate: dayjs().subtract(90, 'day').toDate(),
    endDate: dayjs().subtract(10, 'day').toDate(),
    goals: '能够流利进行日常英语对话'
  },
  {
    id: 4,
    title: '读书计划',
    description: '每月阅读2本技术书籍和1本文学作品',
    status: 1,
    priority: 1,
    progress: 25,
    startDate: dayjs().subtract(5, 'day').toDate(),
    endDate: dayjs().add(85, 'day').toDate(),
    goals: '拓展知识面，提升个人素养'
  },
  {
    id: 5,
    title: '项目重构',
    description: '对现有项目进行代码重构和性能优化',
    status: 3,
    priority: 4,
    progress: 30,
    startDate: dayjs().subtract(20, 'day').toDate(),
    endDate: dayjs().add(30, 'day').toDate(),
    goals: '提升代码质量和系统性能'
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

const formatDateRange = (startDate, endDate) => {
  const start = dayjs(startDate).format('MM-DD')
  const end = dayjs(endDate).format('MM-DD')
  return `${start} ~ ${end}`
}

const toggleSortOrder = () => {
  sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  loadPlans()
}

const handlePlanClick = (plan) => {
  router.push(`/plan/${plan.id}`)
}

const editPlan = (plan) => {
  editingPlan.value = plan
  planForm.value = {
    title: plan.title,
    description: plan.description,
    priority: plan.priority,
    startDate: plan.startDate,
    endDate: plan.endDate,
    goals: plan.goals
  }
  showCreateDialog.value = true
}

const viewPlanDetail = (plan) => {
  router.push(`/plan/${plan.id}`)
}

const deletePlan = async (plan) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除计划"${plan.title}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    ElMessage.success('删除成功')
    loadPlans()
  } catch {
    // 用户取消删除
  }
}

const handleCardAction = (command, plan) => {
  switch (command) {
    case 'edit':
      editPlan(plan)
      break
    case 'detail':
      viewPlanDetail(plan)
      break
    case 'delete':
      deletePlan(plan)
      break
  }
}

const handleSavePlan = async () => {
  try {
    await planFormRef.value.validate()
    // 这里应该调用API保存数据
    ElMessage.success(editingPlan.value ? '更新成功' : '创建成功')
    showCreateDialog.value = false
    editingPlan.value = null
    planForm.value = {
      title: '',
      description: '',
      priority: 2,
      startDate: null,
      endDate: null,
      goals: ''
    }
    loadPlans()
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const loadPlans = () => {
  loading.value = true
  // 模拟API调用
  setTimeout(() => {
    loading.value = false
  }, 500)
}

const handleSizeChange = (size) => {
  pagination.value.size = size
  loadPlans()
}

const handleCurrentChange = (page) => {
  pagination.value.current = page
  loadPlans()
}

onMounted(() => {
  loadPlans()
})
</script>

<style scoped>
.plan-list {
  padding: 0;
}

.page-header {
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

.filters-container {
  margin-bottom: 24px;
}

.filters-card {
  border-radius: 8px;
}

.stats-row {
  margin-bottom: 24px;
}

.stat-item {
  background: white;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border-left: 4px solid;
  transition: transform 0.3s;
}

.stat-item:hover {
  transform: translateY(-2px);
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
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
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: var(--el-text-color-regular);
}

.plan-list-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.sort-options {
  display: flex;
  align-items: center;
  gap: 8px;
}

.loading-state,
.empty-state {
  padding: 40px 20px;
  text-align: center;
}

.plan-item {
  display: flex;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
  cursor: pointer;
  transition: background-color 0.3s;
}

.plan-item:hover {
  background-color: var(--el-fill-color-light);
}

.plan-item:last-child {
  border-bottom: none;
}

.plan-status {
  margin-right: 16px;
  flex-shrink: 0;
}

.plan-content {
  flex: 1;
  margin-right: 16px;
}

.plan-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 8px;
}

.plan-description {
  font-size: 14px;
  color: var(--el-text-color-regular);
  margin-bottom: 12px;
  line-height: 1.5;
}

.plan-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 12px;
}

.plan-dates {
  display: flex;
  align-items: center;
  gap: 4px;
  color: var(--el-text-color-regular);
}

.plan-progress {
  display: flex;
  align-items: center;
}

.progress-label {
  color: var(--el-text-color-regular);
  margin-right: 8px;
}

.progress-text {
  color: var(--el-text-color-primary);
  font-weight: 500;
}

.plan-actions {
  display: flex;
  gap: 8px;
}

.plan-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.plan-card {
  background: white;
  border-radius: 8px;
  border: 1px solid var(--el-border-color-lighter);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
}

.plan-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 16px 0 16px;
}

.card-content {
  padding: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0 0 8px 0;
}

.card-description {
  font-size: 14px;
  color: var(--el-text-color-regular);
  margin: 0 0 16px 0;
  line-height: 1.5;
}

.card-progress {
  margin-bottom: 16px;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
}

.card-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
}

.card-dates {
  color: var(--el-text-color-regular);
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

/* 深色主题适配 */
html.dark .stat-item,
html.dark .plan-card {
  background: var(--el-bg-color);
}

html.dark .plan-item:hover {
  background-color: var(--el-fill-color-dark);
}
</style> 