<template>
  <nav class="sticky top-0 z-50 bg-white shadow-sm border-b border-gray-100">
    <div class="max-w-[1440px] mx-auto px-6 h-16 flex items-center justify-between">
      <router-link to="/" class="flex items-center space-x-2">
        <span class="iconify text-blue-600 text-3xl" data-icon="ri:pangea-fill"></span>
        <span class="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-indigo-600">
          旅行有样
        </span>
      </router-link>

      <div class="hidden md:flex space-x-8 font-medium">
        <router-link 
          to="/" 
          class="hover:text-blue-600 transition"
          :class="{ 'text-blue-600 border-b-2 border-blue-600 pb-1': isActive('/') }"
        >
          首页
        </router-link>
        <router-link 
          to="/destinations" 
          class="hover:text-blue-600 transition"
          :class="{ 'text-blue-600 border-b-2 border-blue-600 pb-1': isActive('/destinations') }"
        >
          探索目的地
        </router-link>
        <router-link 
          to="/hotels" 
          class="hover:text-blue-600 transition"
          :class="{ 'text-blue-600 border-b-2 border-blue-600 pb-1': isActive('/hotels') }"
        >
          特价酒店
        </router-link>
        <router-link 
          to="/history" 
          class="hover:text-blue-600 transition"
          :class="{ 'text-blue-600 border-b-2 border-blue-600 pb-1': isActive('/history') }"
        >
          历史规划
        </router-link>
      </div>

      <div class="flex items-center space-x-4">
        <template v-if="userStore.isLoggedIn">
          <el-dropdown @command="handleCommand">
            <div class="flex items-center space-x-2 cursor-pointer">
              <el-avatar :size="32" :src="userStore.user?.avatar">
                {{ userStore.user?.username?.charAt(0) }}
              </el-avatar>
              <span class="text-sm">{{ userStore.user?.username }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="history">我的行程</el-dropdown-item>
                <el-dropdown-item command="orders">我的订单</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <router-link to="/login">
            <button class="text-gray-600 hover:text-blue-600">登录</button>
          </router-link>
          <router-link to="/register">
            <button class="bg-trip-gradient text-white px-6 py-2 rounded-full font-medium hover:opacity-90 transition shadow-lg">
              立即注册
            </button>
          </router-link>
        </template>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { useRouter, useRoute } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()

const isActive = (path: string) => {
  if (path === '/') {
    return route.path === '/'
  }
  return route.path.startsWith(path)
}

const handleCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'history':
      router.push('/history')
      break
    case 'orders':
      router.push('/profile?tab=orders')
      break
    case 'logout':
      userStore.logout()
      router.push('/')
      break
  }
}
</script>
