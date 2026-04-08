import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: DefaultLayout,
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'hotels',
        name: 'Hotels',
        component: () => import('@/views/Hotels.vue'),
        meta: { title: '酒店住宿' }
      },
      {
        path: 'hotels/:id',
        name: 'HotelDetail',
        component: () => import('@/views/HotelDetail.vue'),
        meta: { title: '酒店详情' }
      },
      {
        path: 'flights',
        name: 'Flights',
        component: () => import('@/views/Flights.vue'),
        meta: { title: '机票预订' }
      },
      {
        path: 'trains',
        name: 'Trains',
        component: () => import('@/views/Trains.vue'),
        meta: { title: '火车票' }
      },
      {
        path: 'cars',
        name: 'Cars',
        component: () => import('@/views/Cars.vue'),
        meta: { title: '租车自驾' }
      },
      {
        path: 'attractions',
        name: 'Attractions',
        component: () => import('@/views/Attractions.vue'),
        meta: { title: '景点门票' }
      },
      {
        path: 'attractions/:id',
        name: 'AttractionDetail',
        component: () => import('@/views/AttractionDetail.vue'),
        meta: { title: '景点详情' }
      },
      {
        path: 'guides',
        name: 'Guides',
        component: () => import('@/views/Guides.vue'),
        meta: { title: '当地攻略' }
      },
      {
        path: 'guides/:id',
        name: 'GuideDetail',
        component: () => import('@/views/GuideDetail.vue'),
        meta: { title: '攻略详情' }
      },
      {
        path: 'restaurants',
        name: 'Restaurants',
        component: () => import('@/views/Restaurants.vue'),
        meta: { title: '美食林' }
      },
      {
        path: 'restaurants/:id',
        name: 'RestaurantDetail',
        component: () => import('@/views/RestaurantDetail.vue'),
        meta: { title: '餐厅详情' }
      },
      {
        path: 'shopping',
        name: 'Shopping',
        component: () => import('@/views/Shopping.vue'),
        meta: { title: '全球购' }
      },
      {
        path: 'destinations',
        name: 'Destinations',
        component: () => import('@/views/Destinations.vue'),
        meta: { title: '探索目的地' }
      },
      {
        path: 'planning',
        name: 'Planning',
        component: () => import('@/views/Planning.vue'),
        meta: { title: 'AI行程规划' }
      },
      {
        path: 'history',
        name: 'History',
        component: () => import('@/views/History.vue'),
        meta: { title: '历史规划', requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人中心', requiresAuth: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面未找到' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = `${to.meta.title || ''} - 旅行有样`
  
  // 检查是否需要登录
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (!token) {
      next({ name: 'Login', query: { redirect: to.fullPath } })
      return
    }
  }
  
  next()
})

export default router
