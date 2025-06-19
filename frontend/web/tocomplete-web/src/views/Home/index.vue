<!-- index.vue -->
<script setup lang="ts">
import { ref, onMounted } from "vue";
import { RouterLink } from "vue-router";
import Button from 'primevue/button';
import Card from 'primevue/card';
import Image from 'primevue/image';

// Reactive data for animations and interactions
const isVisible = ref(false);
const currentFeature = ref(0);
const testimonialIndex = ref(0);

const features = [
  { icon: 'pi-bell', title: '智能备忘录', color: '#667eea' },
  { icon: 'pi-calendar', title: '精准规划', color: '#f093fb' },
  { icon: 'pi-android', title: 'AI助手', color: '#4facfe' },
  { icon: 'pi-lock', title: '强制执行', color: '#fa709a' },
  { icon: 'pi-map-marker', title: '智能提醒', color: '#a8edea' },
  { icon: 'pi-users', title: '激励社区', color: '#ffecd2' }
];

const testimonials = [
  { name: '张同学', role: '考研备考', content: 'AI帮我制定的学习计划让我效率提升了300%！', avatar: '🎓' },
  { name: '李工程师', role: '项目经理', content: '强制执行模式让我专注工作，再也不被手机分心了。', avatar: '💼' },
  { name: '王健身', role: '健身爱好者', content: '位置提醒功能太棒了，到健身房就自动显示训练计划。', avatar: '🏃‍♂️' }
];

onMounted(() => {
  isVisible.value = true;
  
  // 特性轮播
  setInterval(() => {
    currentFeature.value = (currentFeature.value + 1) % features.length;
  }, 3000);
  
  // 推荐轮播
  setInterval(() => {
    testimonialIndex.value = (testimonialIndex.value + 1) % testimonials.length;
  }, 4000);
});
</script>

<template>
  <div class="home-container">
    <!-- Floating Elements -->
    <div class="floating-elements">
      <div class="floating-circle circle-1"></div>
      <div class="floating-circle circle-2"></div>
      <div class="floating-circle circle-3"></div>
      <div class="floating-square square-1"></div>
      <div class="floating-square square-2"></div>
    </div>

    <!-- Hero Section -->
    <section class="hero-section" :class="{ 'fade-in': isVisible }">
      <div class="hero-content">
        <div class="hero-text">
          <div class="hero-badge">
            <span class="badge-icon">✨</span>
            <span>智能生产力工具</span>
          </div>
          <h1 class="hero-title">
            <span class="title-part-1">To</span><span class="title-part-2">Complete</span>
            <div class="title-decoration"></div>
          </h1>
          <h2 class="hero-subtitle">让每一分钟都有意义的智能助手</h2>
          <p class="hero-description">
            结合AI技术的下一代时间管理工具，从简单备忘到精确规划，
            <br>
            <span class="highlight">让您的生活更有序，目标更清晰</span>
          </p>
          <div class="hero-actions">
            <Button 
              label="免费开始" 
              icon="pi pi-arrow-right" 
              size="large"
              class="hero-btn-primary" 
            />
            <Button 
              label="观看演示" 
              icon="pi pi-play" 
              severity="secondary" 
              outlined 
              size="large"
              class="hero-btn-secondary" 
            />
          </div>
          <div class="hero-stats">
            <div class="stat-item">
              <span class="stat-number">50K+</span>
              <span class="stat-label">活跃用户</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">98%</span>
              <span class="stat-label">满意度</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">24/7</span>
              <span class="stat-label">AI支持</span>
            </div>
          </div>
        </div>
        <div class="hero-visual">
          <div class="phone-container">
            <div class="phone-mockup">
              <div class="phone-notch"></div>
              <div class="phone-screen">
                <div class="app-preview">
                  <div class="preview-header">
                    <div class="header-left">
                      <span class="preview-time">9:41</span>
                    </div>
                    <div class="header-right">
                      <div class="signal-bars">
                        <div class="bar"></div>
                        <div class="bar"></div>
                        <div class="bar"></div>
                        <div class="bar"></div>
                      </div>
                      <div class="battery">
                        <div class="battery-level"></div>
                      </div>
                    </div>
                  </div>
                  <div class="preview-content">
                    <div class="app-header">
                      <h3>今日焦点</h3>
                      <div class="progress-ring">
                        <svg class="progress-ring-svg" width="60" height="60">
                          <circle class="progress-ring-circle-bg" cx="30" cy="30" r="25"></circle>
                          <circle class="progress-ring-circle" cx="30" cy="30" r="25"></circle>
                        </svg>
                        <span class="progress-text">75%</span>
                      </div>
                    </div>
                    <div class="task-list">
                      <div class="task-item completed">
                        <div class="task-checkbox checked">✓</div>
                        <div class="task-content">
                          <span class="task-title">完成项目设计</span>
                          <span class="task-time">09:00 - 11:30</span>
                        </div>
                      </div>
                      <div class="task-item completed">
                        <div class="task-checkbox checked">✓</div>
                        <div class="task-content">
                          <span class="task-title">团队会议讨论</span>
                          <span class="task-time">14:00 - 15:00</span>
                        </div>
                      </div>
                      <div class="task-item active">
                        <div class="task-checkbox">
                          <div class="pulse"></div>
                        </div>
                        <div class="task-content">
                          <span class="task-title">健身训练</span>
                          <span class="task-time">18:00 - 19:00</span>
                        </div>
                        <div class="task-priority high">!</div>
                      </div>
                      <div class="task-item">
                        <div class="task-checkbox"></div>
                        <div class="task-content">
                          <span class="task-title">阅读技术文档</span>
                          <span class="task-time">20:00 - 21:00</span>
                        </div>
                      </div>
                    </div>
                    <div class="ai-suggestion">
                      <div class="ai-avatar">🤖</div>
                      <div class="ai-message">
                        建议将健身时间调整到17:30，避开交通高峰期
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="phone-shadow"></div>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Showcase -->
    <section class="features-showcase">
      <div class="features-container">
        <div class="feature-display">
          <div class="feature-visual">
            <div class="feature-icon-large" :style="{ background: features[currentFeature].color }">
              <i :class="`pi ${features[currentFeature].icon}`"></i>
            </div>
            <div class="feature-rings">
              <div class="ring ring-1"></div>
              <div class="ring ring-2"></div>
              <div class="ring ring-3"></div>
            </div>
          </div>
          <div class="feature-info">
            <h3>{{ features[currentFeature].title }}</h3>
            <p>体验下一代智能生产力工具的强大功能</p>
          </div>
        </div>
        <div class="feature-indicators">
          <div 
            v-for="(feature, index) in features" 
            :key="index"
            class="indicator"
            :class="{ active: currentFeature === index }"
            @click="currentFeature = index"
          ></div>
        </div>
      </div>
    </section>

    <!-- Core Features Grid -->
    <section class="features-section">
      <div class="section-header">
        <div class="section-badge">核心功能</div>
        <h2>强大功能，简单易用</h2>
        <p>六大核心功能模块，满足您的所有生产力需求</p>
      </div>
      
      <div class="features-grid">
        <Card class="feature-card modern" v-for="(feature, index) in features" :key="index">
          <template #header>
            <div class="feature-card-header">
              <div class="feature-icon" :style="{ background: `linear-gradient(135deg, ${feature.color}, ${feature.color}80)` }">
                <i :class="`pi ${feature.icon}`"></i>
              </div>
              <div class="feature-sparkle">✨</div>
            </div>
          </template>
          <template #title>{{ feature.title }}</template>
          <template #content>
            <div class="feature-description">
              <p v-if="feature.title === '智能备忘录'">
                灵活闹钟 • 桌面组件 • 拍照验证 • 强制提醒
              </p>
              <p v-else-if="feature.title === '精准规划'">
                分钟级规划 • 日历视图 • 冲突检测 • 动态调整
              </p>
              <p v-else-if="feature.title === 'AI助手'">
                本地AI • 学习计划 • 消息过滤 • 自动更新
              </p>
              <p v-else-if="feature.title === '强制执行'">
                专注锁定 • 应用限制 • 紧急白名单 • 番茄工作法
              </p>
              <p v-else-if="feature.title === '智能提醒'">
                位置提醒 • 上下文感知 • 多模态提醒 • AI建议
              </p>
              <p v-else-if="feature.title === '激励社区'">
                打卡分享 • 成就系统 • 伙伴监督 • 云端同步
              </p>
            </div>
            <div class="feature-action">
              <Button label="了解更多" text size="small" />
            </div>
          </template>
        </Card>
      </div>
    </section>

    <!-- Testimonials -->
    <section class="testimonials-section">
      <div class="section-header">
        <div class="section-badge">用户评价</div>
        <h2>真实用户，真实体验</h2>
      </div>
      <div class="testimonials-container">
        <div class="testimonial-card" v-for="(testimonial, index) in testimonials" :key="index"
             :class="{ active: testimonialIndex === index }">
          <div class="testimonial-content">
            <div class="quote-mark">"</div>
            <p>{{ testimonial.content }}</p>
            <div class="testimonial-author">
              <div class="author-avatar">{{ testimonial.avatar }}</div>
              <div class="author-info">
                <div class="author-name">{{ testimonial.name }}</div>
                <div class="author-role">{{ testimonial.role }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section">
      <div class="cta-background">
        <div class="cta-pattern"></div>
      </div>
      <div class="cta-content">
        <div class="cta-icon">🚀</div>
        <h2>开启您的高效人生</h2>
        <p>加入数万用户的选择，体验AI驱动的智能生产力工具</p>
        <div class="cta-actions">
          <Button 
            label="立即免费使用" 
            icon="pi pi-arrow-right" 
            size="large"
            class="cta-btn-primary" 
          />
        </div>
        <div class="cta-features">
          <div class="cta-feature">
            <i class="pi pi-check-circle"></i>
            <span>永久免费基础功能</span>
          </div>
          <div class="cta-feature">
            <i class="pi pi-shield"></i>
            <span>本地数据，隐私保护</span>
          </div>
          <div class="cta-feature">
            <i class="pi pi-cloud"></i>
            <span>多设备云端同步</span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
/* Global Styles */
.home-container {
  width: 100%;
  min-height: 100vh;
  margin: 0;
  padding: 0;
  overflow-x: hidden;
  position: relative;
}

/* Floating Background Elements */
.floating-elements {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.floating-circle, .floating-square {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(45deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.1));
  animation: float 6s ease-in-out infinite;
}

.floating-square {
  border-radius: 20%;
  background: linear-gradient(45deg, rgba(255, 107, 107, 0.1), rgba(255, 165, 0, 0.1));
}

.circle-1 { width: 100px; height: 100px; top: 10%; left: 10%; animation-delay: 0s; }
.circle-2 { width: 150px; height: 150px; top: 60%; right: 10%; animation-delay: 2s; }
.circle-3 { width: 80px; height: 80px; top: 30%; right: 30%; animation-delay: 4s; }
.square-1 { width: 60px; height: 60px; top: 70%; left: 20%; animation-delay: 1s; }
.square-2 { width: 120px; height: 120px; top: 20%; right: 20%; animation-delay: 3s; }

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

/* Hero Section */
.hero-section {
  min-height: 100vh;
  background: linear-gradient(135deg, 
    rgba(102, 126, 234, 0.95) 0%, 
    rgba(118, 75, 162, 0.95) 50%,
    rgba(240, 147, 251, 0.95) 100%
  );
  position: relative;
  display: flex;
  align-items: center;
  padding: 2rem 1rem;
  overflow: hidden;
  z-index: 1;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.05'%3E%3Ccircle cx='30' cy='30' r='2'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  z-index: -1;
}

.hero-section.fade-in {
  opacity: 1;
  transform: translateY(0);
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
  position: relative;
  z-index: 2;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 50px;
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  color: white;
  margin-bottom: 1.5rem;
  animation: glow 2s ease-in-out infinite alternate;
}

@keyframes glow {
  from { box-shadow: 0 0 10px rgba(255, 255, 255, 0.3); }
  to { box-shadow: 0 0 20px rgba(255, 255, 255, 0.5); }
}

.badge-icon {
  animation: sparkle 1.5s ease-in-out infinite;
}

@keyframes sparkle {
  0%, 100% { transform: scale(1) rotate(0deg); }
  50% { transform: scale(1.2) rotate(180deg); }
}

.hero-title {
  font-size: 4rem;
  font-weight: 800;
  margin-bottom: 1rem;
  color: white;
  position: relative;
  line-height: 1.1;
}

.title-part-1 {
  background: linear-gradient(45deg, #fff, #a8edea);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.title-part-2 {
  background: linear-gradient(45deg, #ffd89b, #19547b);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.title-decoration {
  position: absolute;
  bottom: -10px;
  left: 0;
  width: 100px;
  height: 4px;
  background: linear-gradient(90deg, #ff6b6b, #ffa500);
  border-radius: 2px;
  animation: expand 2s ease-out;
}

@keyframes expand {
  from { width: 0; }
  to { width: 100px; }
}

.hero-subtitle {
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  color: rgba(255, 255, 255, 0.9);
  font-weight: 500;
}

.hero-description {
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 2rem;
  color: rgba(255, 255, 255, 0.8);
}

.highlight {
  background: linear-gradient(45deg, #ffd89b, #19547b);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-weight: 600;
}

.hero-actions {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.hero-btn-primary {
  padding: 0.5rem;
  background: linear-gradient(45deg, #ff6b6b, #ffa500) !important;
  border: none !important;
  transform: translateY(0);
  transition: all 0.3s ease;
  box-shadow: 0 8px 30px rgba(255, 107, 107, 0.3);
}

.hero-btn-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 15px 40px rgba(255, 107, 107, 0.4);
}

.hero-btn-secondary {
  padding: 0.5rem;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.1) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  transition: all 0.3s ease;
}

.hero-btn-secondary:hover {
  background: rgba(255, 255, 255, 0.2) !important;
  transform: translateY(-2px);
}

.hero-stats {
  display: flex;
  gap: 2rem;
  flex-wrap: wrap;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 1.8rem;
  font-weight: 700;
  color: white;
  margin-bottom: 0.3rem;
}

.stat-label {
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.7);
}

/* Phone Mockup */
.phone-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.phone-mockup {
  width: 320px;
  height: 650px;
  background: linear-gradient(145deg, #2c3e50, #34495e);
  border-radius: 35px;
  padding: 8px;
  position: relative;
  box-shadow: 0 30px 80px rgba(0, 0, 0, 0.3);
  animation: phoneFloat 4s ease-in-out infinite;
}

@keyframes phoneFloat {
  0%, 100% { transform: translateY(0px) rotateY(0deg); }
  50% { transform: translateY(-10px) rotateY(5deg); }
}

.phone-notch {
  position: absolute;
  top: 8px;
  left: 50%;
  transform: translateX(-50%);
  width: 120px;
  height: 25px;
  background: #2c3e50;
  border-radius: 0 0 15px 15px;
  z-index: 3;
}

.phone-screen {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 27px;
  overflow: hidden;
  position: relative;
}

.app-preview {
  padding: 30px 20px 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  font-weight: 600;
  color: #2c3e50;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.signal-bars {
  display: flex;
  gap: 2px;
  align-items: end;
}

.bar {
  width: 3px;
  background: #2c3e50;
  border-radius: 1px;
}

.bar:nth-child(1) { height: 4px; }
.bar:nth-child(2) { height: 6px; }
.bar:nth-child(3) { height: 8px; }
.bar:nth-child(4) { height: 10px; }

.battery {
  width: 24px;
  height: 12px;
  border: 2px solid #2c3e50;
  border-radius: 2px;
  position: relative;
}

.battery::after {
  content: '';
  position: absolute;
  right: -4px;
  top: 3px;
  width: 2px;
  height: 4px;
  background: #2c3e50;
  border-radius: 0 1px 1px 0;
}

.battery-level {
  width: 80%;
  height: 100%;
  background: #27ae60;
  border-radius: 1px;
}

.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.app-header h3 {
  color: #2c3e50;
  font-size: 1.3rem;
  margin: 0;
}

.progress-ring {
  position: relative;
  width: 60px;
  height: 60px;
}

.progress-ring-svg {
  transform: rotate(-90deg);
}

.progress-ring-circle-bg {
  fill: none;
  stroke: #e0e6ed;
  stroke-width: 3;
}

.progress-ring-circle {
  fill: none;
  stroke: #667eea;
  stroke-width: 3;
  stroke-linecap: round;
  stroke-dasharray: 157;
  stroke-dashoffset: 39;
  animation: progress 2s ease-in-out;
}

@keyframes progress {
  from { stroke-dashoffset: 157; }
  to { stroke-dashoffset: 39; }
}

.progress-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 0.8rem;
  font-weight: 600;
  color: #667eea;
}

.task-list {
  flex: 1;
  space-y: 12px;
}

.task-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 12px;
  transition: all 0.3s ease;
  position: relative;
}

.task-item.completed {
  opacity: 0.7;
}

.task-item.active {
  border-left: 4px solid #667eea;
  background: linear-gradient(90deg, rgba(102, 126, 234, 0.05) 0%, white 100%);
}

.task-checkbox {
  width: 20px;
  height: 20px;
  border: 2px solid #ddd;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.task-checkbox.checked {
  background: #27ae60;
  border-color: #27ae60;
  color: white;
  font-size: 0.7rem;
}

.pulse {
  width: 8px;
  height: 8px;
  background: #667eea;
  border-radius: 50%;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.5); opacity: 0.5; }
  100% { transform: scale(1); opacity: 1; }
}

.task-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.task-title {
  font-weight: 500;
  color: #2c3e50;
  font-size: 0.9rem;
}

.task-time {
  font-size: 0.75rem;
  color: #7f8c8d;
}

.task-priority {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.7rem;
  font-weight: bold;
  color: white;
}

.task-priority.high {
  background: #e74c3c;
}

.ai-suggestion {
  display: flex;
  align-items: center;
  gap: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 12px;
  border-radius: 12px;
  margin-top: 20px;
}

.ai-avatar {
  font-size: 1.2rem;
}

.ai-message {
  color: white;
  font-size: 0.8rem;
  line-height: 1.4;
}

.phone-shadow {
  position: absolute;
  bottom: -20px;
  left: 50%;
  transform: translateX(-50%);
  width: 280px;
  height: 40px;
  background: radial-gradient(ellipse, rgba(0, 0, 0, 0.3) 0%, transparent 70%);
  border-radius: 50%;
  filter: blur(15px);
}

/* Features Showcase */
.features-showcase {
  padding: 4rem 2rem;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  text-align: center;
}

.features-container {
  max-width: 800px;
  margin: 0 auto;
}

.feature-display {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
  margin-bottom: 3rem;
}

.feature-visual {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.feature-icon-large {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3rem;
  color: white;
  position: relative;
  z-index: 2;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  animation: iconFloat 3s ease-in-out infinite;
}

@keyframes iconFloat {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

.feature-rings {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.ring {
  position: absolute;
  top: 50%;
  left: 50%;
  border: 2px solid rgba(102, 126, 234, 0.2);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  animation: ripple 2s infinite;
}

.ring-1 { width: 140px; height: 140px; animation-delay: 0s; }
.ring-2 { width: 170px; height: 170px; animation-delay: 0.5s; }
.ring-3 { width: 200px; height: 200px; animation-delay: 1s; }

@keyframes ripple {
  0% { opacity: 1; transform: translate(-50%, -50%) scale(0.8); }
  100% { opacity: 0; transform: translate(-50%, -50%) scale(1.2); }
}

.feature-info h3 {
  font-size: 2rem;
  color: #2d3748;
  margin-bottom: 0.5rem;
}

.feature-info p {
  font-size: 1.1rem;
  color: #718096;
}

.feature-indicators {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
}

.indicator {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #cbd5e0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.indicator.active {
  background: #667eea;
  transform: scale(1.2);
}

/* Features Section */
.features-section {
  padding: 6rem 2rem;
  background: white;
}

.section-header {
  text-align: center;
  margin-bottom: 4rem;
}

.section-badge {
  display: inline-block;
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 600;
  margin-bottom: 1rem;
}

.section-header h2 {
  font-size: 2.8rem;
  color: #2d3748;
  margin-bottom: 1rem;
  font-weight: 700;
}

.section-header p {
  font-size: 1.2rem;
  color: #718096;
  max-width: 600px;
  margin: 0 auto;
}

.features-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 2rem;
}

.feature-card.modern {
  border: none;
  border-radius: 20px;
  overflow: hidden;
  transition: all 0.4s ease;
  position: relative;
  background: white;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.feature-card.modern:hover {
  transform: translateY(-15px);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
}

.feature-card-header {
  position: relative;
  padding: 2rem;
  text-align: center;
}

.feature-icon {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1rem;
  font-size: 2rem;
  color: white;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.feature-sparkle {
  position: absolute;
  top: 1rem;
  right: 1rem;
  font-size: 1.2rem;
  animation: sparkle 2s ease-in-out infinite;
}

.feature-description {
  color: #4a5568;
  line-height: 1.6;
  margin-bottom: 1.5rem;
  text-align: center;
}

.feature-action {
  text-align: center;
  padding: 1.6rem;
}

.feature-card.modern :deep(.p-card-title) {
  text-align: center;
  font-size: 1.4rem;
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 1rem;
}

/* Testimonials */
.testimonials-section {
  padding: 6rem 2rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  text-align: center;
}

.testimonials-container {
  max-width: 800px;
  margin: 0 auto;
  position: relative;
  height: 300px;
}

.testimonial-card {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 2rem;
  opacity: 0;
  transform: translateX(100px);
  transition: all 0.6s ease;
}

.testimonial-card.active {
  opacity: 1;
  transform: translateX(0);
}

.quote-mark {
  font-size: 4rem;
  color: rgba(255, 255, 255, 0.3);
  margin-bottom: 1rem;
}

.testimonial-content p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  line-height: 1.6;
}

.testimonial-author {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
}

.author-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

.author-name {
  font-weight: 600;
  margin-bottom: 0.3rem;
}

.author-role {
  opacity: 0.8;
  font-size: 0.9rem;
}

/* CTA Section */
.cta-section {
  padding: 6rem 2rem;
  background: #1a202c;
  color: white;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.cta-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
}

.cta-pattern {
  width: 100%;
  height: 100%;
  background: url("data:image/svg+xml,%3Csvg width='40' height='40' viewBox='0 0 40 40' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='%23ffffff' fill-opacity='0.03'%3E%3Cpath d='M20 20c0-11.046-8.954-20-20-20v20h20z'/%3E%3C/g%3E%3C/svg%3E");
}

.cta-content {
  position: relative;
  z-index: 1;
  max-width: 800px;
  margin: 0 auto;
}

.cta-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.cta-content h2 {
  font-size: 3rem;
  margin-bottom: 1rem;
  font-weight: 700;
}

.cta-content p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  opacity: 0.9;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.cta-actions {
  margin-bottom: 3rem;
}

.cta-btn-primary {
  background: linear-gradient(45deg, #ff6b6b, #ffa500) !important;
  border: none !important;
  padding: 1rem 2rem !important;
  font-size: 1.1rem !important;
  box-shadow: 0 10px 30px rgba(255, 107, 107, 0.4);
  transition: all 0.3s ease;
}

.cta-btn-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 15px 40px rgba(255, 107, 107, 0.5);
}

.cta-features {
  display: flex;
  justify-content: center;
  gap: 3rem;
  flex-wrap: wrap;
}

.cta-feature {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  opacity: 0.8;
}

.cta-feature i {
  color: #48bb78;
}

/* Responsive Design */
@media (max-width: 768px) {
  .hero-content {
    grid-template-columns: 1fr;
    gap: 2rem;
    text-align: center;
  }
  
  .hero-title {
    font-size: 2.5rem;
  }
  
  .phone-mockup {
    width: 280px;
    height: 570px;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  
  .hero-actions,
  .cta-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .cta-features {
    flex-direction: column;
    gap: 1rem;
  }
  
  .hero-stats {
    justify-content: center;
  }
  
  .section-header h2 {
    font-size: 2rem;
  }
  
  .cta-content h2 {
    font-size: 2rem;
  }
}

@media (max-width: 480px) {
  .hero-section,
  .features-section,
  .testimonials-section,
  .cta-section {
    padding: 3rem 1rem;
  }
  
  .hero-title {
    font-size: 2rem;
  }
  
  .phone-mockup {
    width: 240px;
    height: 480px;
  }
  
  .features-showcase {
    padding: 3rem 1rem;
  }
  
  .feature-icon-large {
    width: 80px;
    height: 80px;
    font-size: 2rem;
  }
}
</style>
