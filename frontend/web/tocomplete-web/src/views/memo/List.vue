<template>
  <div class="memo-list">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">备忘录</h1>
        <p class="page-subtitle">管理您的所有备忘录和待办事项</p>
      </div>
      <div class="header-actions">
        <el-button @click="showFilters = !showFilters">
          <el-icon><Filter /></el-icon>
          筛选
        </el-button>
        <el-button type="primary" @click="showCreateDialog = true">
          <el-icon><Plus /></el-icon>
          新建备忘录
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
              <el-option label="待完成" :value="0" />
              <el-option label="进行中" :value="1" />
              <el-option label="已完成" :value="2" />
              <el-option label="已取消" :value="3" />
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
            <el-select v-model="filters.categoryId" placeholder="分类" clearable>
              <el-option label="全部" value="" />
              <el-option
                v-for="category in categories"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-input
              v-model="filters.keyword"
              placeholder="搜索标题或内容"
              clearable
              @keyup.enter="loadMemos"
            >
              <template #suffix>
                <el-icon class="search-icon" @click="loadMemos">
                  <Search />
                </el-icon>
              </template>
            </el-input>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <!-- 统计信息 -->
    <div class="stats-row">
      <el-row :gutter="20">
        <el-col :span="6" v-for="stat in stats" :key="stat.key">
          <div class="stat-item">
            <div class="stat-value" :style="{ color: stat.color }">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 备忘录列表 -->
    <el-card class="memo-list-card">
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
            <el-option label="截止时间" value="dueAt" />
            <el-option label="优先级" value="priority" />
          </el-select>
          <el-button
            :icon="sortOrder === 'asc' ? 'SortUp' : 'SortDown'"
            @click="toggleSortOrder"
          />
        </div>
      </div>

      <!-- 列表视图 -->
      <div v-if="viewMode === 'list'" class="memo-list-view">
        <div v-if="loading" class="loading-state">
          <el-skeleton :rows="5" animated />
        </div>
        <div v-else-if="memos.length === 0" class="empty-state">
          <el-empty description="暂无备忘录" />
        </div>
        <div v-else class="memo-items">
          <div
            v-for="memo in memos"
            :key="memo.id"
            class="memo-item"
            :class="{ completed: memo.status === 2 }"
            @click="handleMemoClick(memo)"
          >
            <div class="memo-checkbox">
              <el-checkbox
                :model-value="memo.status === 2"
                @change="toggleMemoStatus(memo)"
                @click.stop
              />
            </div>
            <div class="memo-content">
              <div class="memo-title">{{ memo.title }}</div>
              <div class="memo-description" v-if="memo.content">
                {{ memo.content }}
              </div>
              <div class="memo-meta">
                <el-tag
                  :type="getPriorityType(memo.priority)"
                  size="small"
                >
                  {{ getPriorityText(memo.priority) }}
                </el-tag>
                <span v-if="memo.category" class="memo-category">
                  {{ memo.category.name }}
                </span>
                <span v-if="memo.dueAt" class="memo-due">
                  <el-icon><Clock /></el-icon>
                  {{ formatDate(memo.dueAt) }}
                </span>
              </div>
            </div>
            <div class="memo-actions">
              <el-button
                type="primary"
                link
                @click.stop="editMemo(memo)"
              >
                编辑
              </el-button>
              <el-button
                type="danger"
                link
                @click.stop="deleteMemo(memo)"
              >
                删除
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 网格视图 -->
      <div v-if="viewMode === 'grid'" class="memo-grid-view">
        <div v-if="loading" class="loading-state">
          <el-row :gutter="20">
            <el-col :span="8" v-for="i in 6" :key="i">
              <el-skeleton :rows="3" animated />
            </el-col>
          </el-row>
        </div>
        <div v-else-if="memos.length === 0" class="empty-state">
          <el-empty description="暂无备忘录" />
        </div>
        <div v-else class="memo-grid">
          <div
            v-for="memo in memos"
            :key="memo.id"
            class="memo-card"
            :class="{ completed: memo.status === 2 }"
            @click="handleMemoClick(memo)"
          >
            <div class="card-header">
              <el-checkbox
                :model-value="memo.status === 2"
                @change="toggleMemoStatus(memo)"
                @click.stop
              />
              <el-dropdown @command="(command) => handleCardAction(command, memo)" @click.stop>
                <el-button type="text" :icon="MoreFilled" />
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="edit">编辑</el-dropdown-item>
                    <el-dropdown-item command="delete">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <div class="card-content">
              <h3 class="card-title">{{ memo.title }}</h3>
              <p class="card-description" v-if="memo.content">
                {{ memo.content }}
              </p>
            </div>
            <div class="card-footer">
              <el-tag
                :type="getPriorityType(memo.priority)"
                size="small"
              >
                {{ getPriorityText(memo.priority) }}
              </el-tag>
              <span v-if="memo.dueAt" class="card-due">
                {{ formatDate(memo.dueAt) }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 创建/编辑备忘录对话框 -->
    <MemoDialog
      v-model="showCreateDialog"
      :memo="currentMemo"
      @success="handleDialogSuccess"
    />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import MemoDialog from '@/components/MemoDialog.vue'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const showFilters = ref(false)
const showCreateDialog = ref(false)
const viewMode = ref('list')
const sortBy = ref('createdAt')
const sortOrder = ref('desc')
const currentMemo = ref(null)

// 筛选条件
const filters = reactive({
  status: '',
  priority: '',
  categoryId: '',
  keyword: ''
})

// 分页信息
const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

// 备忘录列表
const memos = ref([])

// 分类列表
const categories = ref([
  { id: 1, name: '工作' },
  { id: 2, name: '学习' },
  { id: 3, name: '生活' }
])

// 统计信息
const stats = computed(() => [
  {
    key: 'total',
    label: '总数',
    value: memos.value.length,
    color: '#409EFF'
  },
  {
    key: 'pending',
    label: '待完成',
    value: memos.value.filter(m => m.status === 0).length,
    color: '#E6A23C'
  },
  {
    key: 'completed',
    label: '已完成',
    value: memos.value.filter(m => m.status === 2).length,
    color: '#67C23A'
  },
  {
    key: 'overdue',
    label: '已逾期',
    value: memos.value.filter(m => m.dueAt && dayjs(m.dueAt).isBefore(dayjs()) && m.status !== 2).length,
    color: '#F56C6C'
  }
])

// 方法
const getPriorityType = (priority) => {
  const types = { 1: '', 2: 'info', 3: 'warning', 4: 'danger' }
  return types[priority] || ''
}

const getPriorityText = (priority) => {
  const texts = { 1: '低', 2: '中', 3: '高', 4: '紧急' }
  return texts[priority] || ''
}

const formatDate = (date) => {
  return dayjs(date).format('MM-DD HH:mm')
}

const toggleSortOrder = () => {
  sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  loadMemos()
}

const loadMemos = async () => {
  loading.value = true
  try {
    // TODO: 调用API获取备忘录列表
    await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟API调用
    
    // 模拟数据
    memos.value = [
      {
        id: 1,
        title: '完成项目报告',
        content: '需要整理本月的项目进度和成果',
        status: 0,
        priority: 3,
        categoryId: 1,
        category: { id: 1, name: '工作' },
        dueAt: '2024-01-20 18:00:00',
        createdAt: '2024-01-15 09:00:00'
      },
      {
        id: 2,
        title: '学习Vue.js',
        content: '观看Vue.js官方教程视频',
        status: 1,
        priority: 2,
        categoryId: 2,
        category: { id: 2, name: '学习' },
        dueAt: '2024-01-25 20:00:00',
        createdAt: '2024-01-14 10:00:00'
      }
    ]
    
    pagination.total = memos.value.length
  } catch (error) {
    ElMessage.error('加载备忘录失败')
    console.error('加载备忘录失败:', error)
  } finally {
    loading.value = false
  }
}

const toggleMemoStatus = async (memo) => {
  try {
    memo.status = memo.status === 2 ? 0 : 2
    // TODO: 调用API更新备忘录状态
    ElMessage.success(memo.status === 2 ? '标记为已完成' : '标记为未完成')
  } catch (error) {
    memo.status = memo.status === 2 ? 0 : 2 // 回滚状态
    ElMessage.error('更新状态失败')
  }
}

const handleMemoClick = (memo) => {
  router.push(`/memo/${memo.id}`)
}

const editMemo = (memo) => {
  currentMemo.value = memo
  showCreateDialog.value = true
}

const deleteMemo = async (memo) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个备忘录吗？',
      '确认删除',
      {
        type: 'warning'
      }
    )
    
    // TODO: 调用API删除备忘录
    memos.value = memos.value.filter(m => m.id !== memo.id)
    ElMessage.success('删除成功')
  } catch (error) {
    console.log('取消删除')
  }
}

const handleCardAction = (command, memo) => {
  if (command === 'edit') {
    editMemo(memo)
  } else if (command === 'delete') {
    deleteMemo(memo)
  }
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadMemos()
}

const handlePageChange = (page) => {
  pagination.page = page
  loadMemos()
}

const handleDialogSuccess = () => {
  showCreateDialog.value = false
  currentMemo.value = null
  loadMemos()
}

// 监听筛选条件变化
watch(filters, () => {
  pagination.page = 1
  loadMemos()
}, { deep: true })

watch([sortBy, sortOrder], () => {
  loadMemos()
})

// 生命周期
onMounted(() => {
  loadMemos()
})
</script>

<style scoped>
.memo-list {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.page-title {
  font-size: 28px;
  font-weight: bold;
  margin: 0 0 8px 0;
  color: var(--el-text-color-primary);
}

.page-subtitle {
  font-size: 16px;
  color: var(--el-text-color-secondary);
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
  text-align: center;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.memo-list-card {
  border-radius: 8px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--el-border-color-light);
}

.view-toggle {
  display: flex;
  gap: 8px;
}

.sort-options {
  display: flex;
  align-items: center;
  gap: 8px;
}

.memo-items {
  space-y: 16px;
}

.memo-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 16px;
  border: 1px solid var(--el-border-color-light);
  border-radius: 8px;
  transition: all 0.3s;
  cursor: pointer;
  margin-bottom: 16px;
}

.memo-item:hover {
  border-color: var(--el-color-primary);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.memo-item.completed {
  opacity: 0.6;
}

.memo-item.completed .memo-title {
  text-decoration: line-through;
}

.memo-content {
  flex: 1;
}

.memo-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 8px;
}

.memo-description {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin-bottom: 12px;
  line-height: 1.5;
}

.memo-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 12px;
}

.memo-category {
  color: var(--el-text-color-secondary);
}

.memo-due {
  display: flex;
  align-items: center;
  gap: 4px;
  color: var(--el-text-color-secondary);
}

.memo-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.memo-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.memo-card {
  border: 1px solid var(--el-border-color-light);
  border-radius: 8px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.memo-card:hover {
  border-color: var(--el-color-primary);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.memo-card.completed {
  opacity: 0.6;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.card-content {
  margin-bottom: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: var(--el-text-color-primary);
}

.memo-card.completed .card-title {
  text-decoration: line-through;
}

.card-description {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  line-height: 1.5;
  margin: 0;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-due {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.loading-state {
  padding: 20px;
}

.empty-state {
  padding: 40px 20px;
  text-align: center;
}

.pagination-container {
  margin-top: 24px;
  text-align: center;
}

.search-icon {
  cursor: pointer;
  color: var(--el-color-primary);
}

.search-icon:hover {
  color: var(--el-color-primary-dark-2);
}
</style> 