<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
    <div class="max-w-md w-full">
      <!-- Logo -->
      <div class="text-center mb-8">
        <div class="flex items-center justify-center space-x-2 mb-4">
          <span class="iconify text-blue-600 text-5xl" data-icon="ri:pangea-fill"></span>
          <span class="text-4xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-indigo-600">
            旅行有样
          </span>
        </div>
        <p class="text-gray-600">探索未知的世界</p>
      </div>

      <!-- 登录表单 -->
      <div class="bg-white rounded-2xl shadow-xl p-8">
        <h2 class="text-2xl font-bold mb-6 text-center">登录</h2>
        
        <el-form :model="loginForm" :rules="rules" ref="formRef">
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="用户名/邮箱/手机号"
              size="large"
            >
              <template #prefix>
                <span class="iconify" data-icon="material-symbols:person"></span>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              size="large"
              show-password
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <span class="iconify" data-icon="material-symbols:lock"></span>
              </template>
            </el-input>
          </el-form-item>
          
          <div class="flex items-center justify-between mb-6">
            <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
            <a href="#" class="text-blue-600 text-sm hover:underline">忘记密码？</a>
          </div>
          
          <el-button
            type="primary"
            size="large"
            class="w-full bg-trip-gradient"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form>

        <div class="mt-6 text-center text-sm text-gray-600">
          还没有账号？
          <router-link to="/register" class="text-blue-600 hover:underline font-medium">
            立即注册
          </router-link>
        </div>

        <!-- 第三方登录 -->
        <div class="mt-8">
          <div class="relative">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-gray-200"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-4 bg-white text-gray-500">或使用以下方式登录</span>
            </div>
          </div>
          
          <div class="mt-6 flex justify-center space-x-4">
            <button class="w-12 h-12 rounded-full bg-green-500 text-white flex items-center justify-center hover:bg-green-600 transition">
              <span class="iconify text-2xl" data-icon="ri:wechat-fill"></span>
            </button>
            <button class="w-12 h-12 rounded-full bg-red-500 text-white flex items-center justify-center hover:bg-red-600 transition">
              <span class="iconify text-2xl" data-icon="ri:weibo-fill"></span>
            </button>
            <button class="w-12 h-12 rounded-full bg-blue-500 text-white flex items-center justify-center hover:bg-blue-600 transition">
              <span class="iconify text-2xl" data-icon="ri:qq-fill"></span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  remember: false
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      
      try {
        console.log('开始登录...')
        console.log('登录参数:', { username: loginForm.username, password: loginForm.password })
        
        // 通过 userStore.login() 调用 API，同时更新 Pinia 状态
        const success = await userStore.login(loginForm.username, loginForm.password)
        
        if (success) {
          ElMessage.success('登录成功')
          
          // 跳转到之前的页面或首页
          const redirect = route.query.redirect as string || '/'
          router.push(redirect)
        } else {
          ElMessage.error('用户名或密码错误')
        }
      } catch (error: any) {
        console.error('登录错误:', error)
        ElMessage.error(error?.response?.data?.message || error?.message || '登录失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>
