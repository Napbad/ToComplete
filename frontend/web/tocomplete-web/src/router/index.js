import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

// 路由懒加载
const Layout = () => import('@/layout/index.vue')
const Home = () => import('@/views/home/index.vue')
const Login = () => import('@/views/auth/Login.vue')
const Register = () => import('@/views/auth/Register.vue')
const Dashboard = () => import('@/views/dashboard/index.vue')
const MemoList = () => import('@/views/memo/List.vue')
const MemoDetail = () => import('@/views/memo/Detail.vue')
const PlanList = () => import('@/views/plan/List.vue')
const PlanDetail = () => import('@/views/plan/Detail.vue')
const Calendar = () => import('@/views/calendar/index.vue')
const Profile = () => import('@/views/user/Profile.vue')

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home,
      meta: { hideInMenu: true, requiresAuth: false }
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: { hideInMenu: true, requiresAuth: false }
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
      meta: { hideInMenu: true, requiresAuth: false }
    },
    {
      path: '/app',
      name: 'Layout',
      component: Layout,
      redirect: '/app/dashboard',
      meta: { requiresAuth: true },
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: Dashboard,
          meta: { 
            title: '仪表盘',
            icon: 'House',
            requiresAuth: true 
          }
        },
        {
          path: 'memos',
          name: 'MemoList',
          component: MemoList,
          meta: { 
            title: '备忘录',
            icon: 'Document',
            requiresAuth: true 
          }
        },
        {
          path: 'memo/:id',
          name: 'MemoDetail',
          component: MemoDetail,
          meta: { 
            hideInMenu: true,
            requiresAuth: true 
          }
        },
        {
          path: 'plans',
          name: 'PlanList',
          component: PlanList,
          meta: { 
            title: '计划管理',
            icon: 'Calendar',
            requiresAuth: true 
          }
        },
        {
          path: 'plan/:id',
          name: 'PlanDetail',
          component: PlanDetail,
          meta: { 
            hideInMenu: true,
            requiresAuth: true 
          }
        },
        {
          path: 'calendar',
          name: 'Calendar',
          component: Calendar,
          meta: { 
            title: '日历视图',
            icon: 'Calendar',
            requiresAuth: true 
          }
        },
        {
          path: 'profile',
          name: 'Profile',
          component: Profile,
          meta: { 
            title: '个人设置',
            icon: 'User',
            requiresAuth: true 
          }
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && userStore.isLoggedIn) {
    next('/app')
  } else {
    next()
  }
})

export default router 