import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '@/types'
import { getCurrentUser, login as loginApi, logout as logoutApi } from '@/api/modules/auth'

export const useUserStore = defineStore('user', () => {
  const user = ref<User | null>(null)
  const token = ref<string | null>(localStorage.getItem('token'))
  
  const isLoggedIn = computed(() => !!token.value)
  
  // 登录
  const login = async (username: string, password: string) => {
    const res = await loginApi({ username, password })
    if (res.code === 200 && res.data) {
      token.value = res.data.token
      user.value = res.data.user
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('user', JSON.stringify(res.data.user))
      console.log('登录成功，用户信息已保存:', res.data.user)
      return true
    }
    return false
  }
  
  // 登出
  const logout = async () => {
    try {
      await logoutApi()
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      token.value = null
      user.value = null
      localStorage.removeItem('token')
    }
  }
  
  // 获取用户信息
  const fetchUserInfo = async () => {
    if (!token.value) return
    
    try {
      const res = await getCurrentUser()
      if (res.code === 200) {
        user.value = res.data
      }
    } catch (error) {
      console.error('Fetch user info error:', error)
      // 如果获取失败，清除token
      token.value = null
      localStorage.removeItem('token')
    }
  }
  
  // 更新用户信息
  const updateUser = (newUser: User) => {
    user.value = newUser
  }
  
  return {
    user,
    token,
    isLoggedIn,
    login,
    logout,
    fetchUserInfo,
    updateUser
  }
})
