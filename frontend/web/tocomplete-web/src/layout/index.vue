<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="collapsed ? '64px' : '240px'" class="sidebar">
      <div class="logo">
        <img v-if="!collapsed" src="/logo.svg" alt="ToComplete" class="logo-img" />
        <span v-if="!collapsed" class="logo-text">ToComplete</span>
        <el-icon v-else class="logo-icon" size="32">
          <Document />
        </el-icon>
      </div>
      
      <el-menu
        :default-active="$route.path"
        :collapse="collapsed"
        :unique-opened="false"
        router
        class="sidebar-menu"
      >
        <template v-for="route in menuRoutes" :key="route.path">
          <el-menu-item :index="route.path" v-if="!route.meta?.hideInMenu">
            <el-icon>
              <component :is="route.meta?.icon" />
            </el-icon>
            <template #title>{{ route.meta?.title }}</template>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <!-- 主要内容区域 -->
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <div class="header-left">
          <el-button 
            text 
            @click="toggleCollapse"
            class="collapse-btn"
          >
            <el-icon size="20">
              <Expand v-if="collapsed" />
              <Fold v-else />
            </el-icon>
          </el-button>
          
          <el-breadcrumb separator="/">
            <el-breadcrumb-item 
              v-for="item in breadcrumbs" 
              :key="item.path"
              :to="item.path"
            >
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <!-- 主题切换 -->
          <el-button text @click="toggleTheme" class="theme-btn">
            <el-icon size="18">
              <Sunny v-if="isDark" />
              <Moon v-else />
            </el-icon>
          </el-button>

          <!-- 通知 -->
          <el-badge :value="unreadCount" :hidden="unreadCount === 0">
            <el-button text class="notification-btn">
              <el-icon size="18">
                <Bell />
              </el-icon>
            </el-button>
          </el-badge>

          <!-- 用户信息 -->
          <el-dropdown @command="handleUserCommand">
            <div class="user-info">
              <el-avatar :src="userStore.userInfo?.avatarUrl" class="user-avatar">
                {{ userStore.userInfo?.nickname?.charAt(0) || 'U' }}
              </el-avatar>
              <span class="user-name">{{ userStore.userInfo?.nickname || '用户' }}</span>
              <el-icon class="dropdown-icon">
                <ArrowDown />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人设置
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主要内容 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useDark, useToggle } from '@vueuse/core'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 侧边栏折叠状态
const collapsed = ref(false)
const toggleCollapse = () => {
  collapsed.value = !collapsed.value
}

// 主题切换
const isDark = useDark()
const toggleTheme = useToggle(isDark)

// 未读通知数量
const unreadCount = ref(0)

// 菜单路由
const menuRoutes = computed(() => {
  // 获取Layout路由的子路由
  const layoutRoute = router.getRoutes().find(route => route.name === 'Layout')
  if (layoutRoute && layoutRoute.children) {
    return layoutRoute.children.filter(route => route.meta?.title && !route.meta?.hideInMenu)
  }
  return []
})

// 面包屑导航
const breadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta?.title)
  return matched.map(item => ({
    path: item.path,
    title: item.meta.title
  }))
})

// 用户下拉菜单处理
const handleUserCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/app/profile')
      break
    case 'logout':
      userStore.logout()
      router.push('/')
      break
  }
}

onMounted(() => {
  // 获取用户信息
  if (userStore.isLoggedIn && !userStore.userInfo) {
    userStore.fetchUserInfo()
  }
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background: var(--el-bg-color);
  border-right: 1px solid var(--el-border-color);
  transition: width 0.28s;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid var(--el-border-color);
  padding: 0 20px;
}

.logo-img {
  height: 32px;
  margin-right: 12px;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  color: var(--el-color-primary);
}

.logo-icon {
  color: var(--el-color-primary);
}

.sidebar-menu {
  border: none;
  height: calc(100vh - 60px);
}

.header {
  background: var(--el-bg-color);
  border-bottom: 1px solid var(--el-border-color);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-btn {
  margin-right: 20px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background: var(--el-color-primary-light-9);
}

.user-avatar {
  margin-right: 8px;
}

.user-name {
  margin-right: 4px;
  font-size: 14px;
}

.dropdown-icon {
  font-size: 12px;
  transition: transform 0.3s;
}

.main-content {
  background: var(--el-bg-color-page);
  padding: 20px;
}

/* 深色主题样式调整 */
html.dark .sidebar {
  background: var(--el-bg-color);
}

html.dark .header {
  background: var(--el-bg-color);
}

html.dark .main-content {
  background: var(--el-bg-color-page);
}
</style> 