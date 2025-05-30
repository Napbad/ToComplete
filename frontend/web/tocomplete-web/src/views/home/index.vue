<template>
  <div class="home-container">
    <!-- 英雄区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <div class="hero-text">
          <h1 class="hero-title">ToComplete</h1>
          <p class="hero-subtitle">让任务管理变得简单高效</p>
          <p class="hero-description">
            一个现代化的任务管理和计划制定平台，帮助您更好地组织工作和生活，
            提升效率，实现目标。
          </p>
          <div class="hero-actions">
            <el-button 
              type="primary" 
              size="large" 
              @click="getStarted"
              class="get-started-btn"
            >
              <el-icon><Right /></el-icon>
              开始使用
            </el-button>
            <el-button 
              size="large" 
              @click="viewDemo"
              class="demo-btn"
            >
              <el-icon><VideoPlay /></el-icon>
              查看演示
            </el-button>
          </div>
        </div>
        <div class="hero-image">
          <div class="floating-card">
            <div class="task-card">
              <div class="task-header">
                <div class="task-priority high"></div>
                <span class="task-title">完成项目演示</span>
              </div>
              <div class="task-progress">
                <div class="progress-bar">
                  <div class="progress-fill" style="width: 75%"></div>
                </div>
                <span class="progress-text">75%</span>
              </div>
            </div>
          </div>
          <div class="floating-stats">
            <div class="stat-item">
              <div class="stat-number">24</div>
              <div class="stat-label">今日完成</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">89%</div>
              <div class="stat-label">完成率</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 特性介绍 -->
    <section class="features-section">
      <div class="section-container">
        <div class="section-header">
          <h2 class="section-title">核心特性</h2>
          <p class="section-subtitle">
            强大的功能，简洁的界面，让任务管理成为一种享受
          </p>
        </div>
        <div class="features-grid">
          <div 
            v-for="feature in features" 
            :key="feature.id"
            class="feature-card"
          >
            <div class="feature-icon" :style="{ backgroundColor: feature.color }">
              <el-icon size="32">
                <component :is="feature.icon" />
              </el-icon>
            </div>
            <h3 class="feature-title">{{ feature.title }}</h3>
            <p class="feature-description">{{ feature.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 数据统计 -->
    <section class="stats-section">
      <div class="section-container">
        <div class="stats-grid">
          <div 
            v-for="stat in stats" 
            :key="stat.id"
            class="stat-card"
          >
            <div class="stat-icon">
              <el-icon size="24">
                <component :is="stat.icon" />
              </el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stat.number }}</div>
              <div class="stat-label">{{ stat.label }}</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 如何开始 -->
    <section class="how-to-section">
      <div class="section-container">
        <div class="section-header">
          <h2 class="section-title">如何开始</h2>
          <p class="section-subtitle">
            只需几个简单步骤，即可开始您的高效工作之旅
          </p>
        </div>
        <div class="steps-container">
          <div 
            v-for="(step, index) in steps" 
            :key="step.id"
            class="step-item"
          >
            <div class="step-number">{{ index + 1 }}</div>
            <div class="step-content">
              <h3 class="step-title">{{ step.title }}</h3>
              <p class="step-description">{{ step.description }}</p>
            </div>
            <div v-if="index < steps.length - 1" class="step-arrow">
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 行动号召 -->
    <section class="cta-section">
      <div class="section-container">
        <div class="cta-content">
          <h2 class="cta-title">准备好开始了吗？</h2>
          <p class="cta-description">
            立即注册，体验更高效的任务管理方式
          </p>
          <div class="cta-actions">
            <el-button 
              type="primary" 
              size="large"
              @click="startNow"
              class="cta-primary-btn"
            >
              立即开始
            </el-button>
            <el-button 
              size="large"
              @click="login"
              class="cta-secondary-btn"
            >
              已有账户？登录
            </el-button>
          </div>
        </div>
      </div>
    </section>

    <!-- 演示对话框 -->
    <el-dialog
      v-model="showDemoDialog"
      title="产品演示"
      width="800px"
      center
    >
      <div class="demo-content">
        <div class="demo-video">
          <div class="video-placeholder">
            <el-icon size="80"><VideoPlay /></el-icon>
            <p>演示视频即将上线</p>
          </div>
        </div>
        <div class="demo-features">
          <h3>演示内容包括：</h3>
          <ul>
            <li>任务创建和管理</li>
            <li>计划制定和跟踪</li>
            <li>日历视图使用</li>
            <li>数据分析和报告</li>
          </ul>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const showDemoDialog = ref(false)

// 特性数据
const features = ref([
  {
    id: 1,
    title: '智能任务管理',
    description: '创建、编辑、分类任务，设置优先级和截止日期，让工作井井有条',
    icon: 'List',
    color: '#409eff'
  },
  {
    id: 2,
    title: '计划制定',
    description: '制定长期计划，分解目标，跟踪进度，实现有效的项目管理',
    icon: 'TrendCharts',
    color: '#67c23a'
  },
  {
    id: 3,
    title: '日历视图',
    description: '直观的日历界面，一目了然地查看所有安排和重要日期',
    icon: 'Calendar',
    color: '#e6a23c'
  },
  {
    id: 4,
    title: '数据分析',
    description: '详细的统计报告，了解工作效率，持续改进工作方式',
    icon: 'DataAnalysis',
    color: '#f56c6c'
  },
  {
    id: 5,
    title: '团队协作',
    description: '与团队成员共享任务和计划，实现高效的团队协作',
    icon: 'Users',
    color: '#909399'
  },
  {
    id: 6,
    title: '移动端支持',
    description: '随时随地访问您的任务，跨平台同步，工作不受限制',
    icon: 'Cellphone',
    color: '#606266'
  }
])

// 统计数据
const stats = ref([
  {
    id: 1,
    number: '10,000+',
    label: '活跃用户',
    icon: 'User'
  },
  {
    id: 2,
    number: '1,000,000+',
    label: '完成任务',
    icon: 'Check'
  },
  {
    id: 3,
    number: '50,000+',
    label: '创建计划',
    icon: 'Document'
  },
  {
    id: 4,
    number: '99.9%',
    label: '系统可用性',
    icon: 'CircleCheck'
  }
])

// 使用步骤
const steps = ref([
  {
    id: 1,
    title: '注册账户',
    description: '快速注册，只需要邮箱和密码'
  },
  {
    id: 2,
    title: '创建任务',
    description: '添加您的第一个任务，设置优先级'
  },
  {
    id: 3,
    title: '制定计划',
    description: '将任务组织成计划，设定目标'
  },
  {
    id: 4,
    title: '开始执行',
    description: '使用各种视图管理和跟踪进度'
  }
])

// 方法
const getStarted = () => {
  // 检查用户登录状态
  if (userStore && userStore.isLoggedIn) {
    router.push('/app')
  } else {
    router.push('/register')
  }
}

const login = () => {
  router.push('/login')
}

const startNow = () => {
  router.push('/register')
}

const viewDemo = () => {
  showDemoDialog.value = true
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

/* 英雄区域 */
.hero-section {
  position: relative;
  padding: 80px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  overflow: hidden;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="1" fill="white" opacity="0.1"/><circle cx="75" cy="75" r="1" fill="white" opacity="0.1"/><circle cx="25" cy="75" r="1" fill="white" opacity="0.05"/><circle cx="75" cy="25" r="1" fill="white" opacity="0.05"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
  pointer-events: none;
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
  position: relative;
  z-index: 1;
}

.hero-title {
  font-size: 4rem;
  font-weight: 700;
  margin: 0 0 20px 0;
  background: linear-gradient(45deg, #fff, #e0e7ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 1.5rem;
  font-weight: 500;
  margin: 0 0 20px 0;
  opacity: 0.9;
}

.hero-description {
  font-size: 1.1rem;
  line-height: 1.6;
  margin: 0 0 40px 0;
  opacity: 0.8;
}

.hero-actions {
  display: flex;
  gap: 20px;
}

.get-started-btn {
  padding: 12px 32px;
  font-size: 16px;
  border-radius: 50px;
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
}

.demo-btn {
  padding: 12px 32px;
  font-size: 16px;
  border-radius: 50px;
  background: transparent;
  border: 2px solid rgba(255, 255, 255, 0.3);
  color: white;
}

/* 英雄区域动画元素 */
.hero-image {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.floating-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 20px;
  animation: float 6s ease-in-out infinite;
}

.task-card {
  min-width: 280px;
}

.task-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.task-priority {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.task-priority.high {
  background: #f56c6c;
}

.task-title {
  font-size: 16px;
  font-weight: 500;
}

.task-progress {
  display: flex;
  align-items: center;
  gap: 12px;
}

.progress-bar {
  flex: 1;
  height: 6px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #67c23a, #85ce61);
  border-radius: 3px;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 14px;
  font-weight: 500;
}

.floating-stats {
  position: absolute;
  top: -30px;
  right: -30px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  padding: 16px;
  display: flex;
  gap: 20px;
  animation: float 6s ease-in-out infinite 2s;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  opacity: 0.8;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

/* 特性区域 */
.features-section {
  padding: 100px 20px;
  background: white;
}

.section-container {
  max-width: 1200px;
  margin: 0 auto;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-title {
  font-size: 2.5rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 16px 0;
}

.section-subtitle {
  font-size: 1.1rem;
  color: #64748b;
  margin: 0;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 40px;
}

.feature-card {
  text-align: center;
  padding: 40px 20px;
  border-radius: 16px;
  background: white;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.15);
}

.feature-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px auto;
  color: white;
}

.feature-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 16px 0;
}

.feature-description {
  font-size: 1rem;
  color: #64748b;
  line-height: 1.6;
  margin: 0;
}

/* 统计区域 */
.stats-section {
  padding: 80px 20px;
  background: #f8fafc;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 30px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-content {
  text-align: left;
}

.stat-number {
  font-size: 2rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 4px;
}

.stat-card .stat-label {
  font-size: 0.9rem;
  color: #64748b;
}

/* 步骤区域 */
.how-to-section {
  padding: 100px 20px;
  background: white;
}

.steps-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 40px;
  max-width: 1000px;
  margin: 0 auto;
}

.step-item {
  display: flex;
  align-items: center;
  gap: 20px;
  flex: 1;
}

.step-number {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 600;
  flex-shrink: 0;
}

.step-content {
  flex: 1;
}

.step-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 8px 0;
}

.step-description {
  font-size: 0.9rem;
  color: #64748b;
  margin: 0;
  line-height: 1.5;
}

.step-arrow {
  color: #cbd5e1;
  font-size: 24px;
  flex-shrink: 0;
}

/* CTA区域 */
.cta-section {
  padding: 100px 20px;
  background: linear-gradient(135deg, #2c3e50, #34495e);
  color: white;
  text-align: center;
}

.cta-title {
  font-size: 2.5rem;
  font-weight: 600;
  margin: 0 0 16px 0;
}

.cta-description {
  font-size: 1.1rem;
  margin: 0 0 40px 0;
  opacity: 0.9;
}

.cta-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.cta-primary-btn,
.cta-secondary-btn {
  padding: 12px 32px;
  font-size: 16px;
  border-radius: 50px;
}

.cta-primary-btn {
  background: white;
  color: #2c3e50;
  border: none;
}

.cta-secondary-btn {
  background: transparent;
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

/* 演示对话框 */
.demo-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
}

.demo-video {
  aspect-ratio: 16/9;
  background: #f5f5f5;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.video-placeholder {
  text-align: center;
  color: #999;
}

.demo-features h3 {
  margin: 0 0 20px 0;
  color: #2c3e50;
}

.demo-features ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.demo-features li {
  padding: 8px 0;
  color: #64748b;
  position: relative;
  padding-left: 20px;
}

.demo-features li::before {
  content: '✓';
  position: absolute;
  left: 0;
  color: #67c23a;
  font-weight: bold;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-content {
    grid-template-columns: 1fr;
    gap: 40px;
    text-align: center;
  }
  
  .hero-title {
    font-size: 2.5rem;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .steps-container {
    flex-direction: column;
    gap: 30px;
  }
  
  .step-item {
    flex-direction: column;
    text-align: center;
    gap: 16px;
  }
  
  .step-arrow {
    transform: rotate(90deg);
  }
  
  .cta-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .demo-content {
    grid-template-columns: 1fr;
  }
}
</style> 