<template>
  <div class="min-h-screen bg-gradient-to-br from-purple-50 to-pink-100 flex items-center justify-center p-4">
    <div class="max-w-md w-full">
      <!-- Logo -->
      <div class="text-center mb-8">
        <div class="flex items-center justify-center space-x-2 mb-4">
          <span class="iconify text-blue-600 text-5xl" data-icon="ri:pangea-fill"></span>
          <span class="text-4xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-indigo-600">
            旅行有样
          </span>
        </div>
        <p class="text-gray-600">开启你的旅行之旅</p>
      </div>

      <!-- 注册表单 -->
      <div class="bg-white rounded-2xl shadow-xl p-8">
        <h2 class="text-2xl font-bold mb-6 text-center">注册</h2>
        
        <el-form :model="registerForm" :rules="rules" ref="formRef">
          <el-form-item prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="用户名"
              size="large"
            >
              <template #prefix>
                <span class="iconify" data-icon="material-symbols:person"></span>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item prop="email">
            <el-input
              v-model="registerForm.email"
              placeholder="邮箱"
              size="large"
            >
              <template #prefix>
                <span class="iconify" data-icon="material-symbols:mail"></span>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item prop="phone">
            <el-input
              v-model="registerForm.phone"
              placeholder="手机号（可选）"
              size="large"
            >
              <template #prefix>
                <span class="iconify" data-icon="material-symbols:phone"></span>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="密码"
              size="large"
              show-password
            >
              <template #prefix>
                <span class="iconify" data-icon="material-symbols:lock"></span>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="确认密码"
              size="large"
              show-password
              @keyup.enter="handleRegister"
            >
              <template #prefix>
                <span class="iconify" data-icon="material-symbols:lock"></span>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item prop="agree">
            <el-checkbox v-model="registerForm.agree">
              我已阅读并同意
              <a href="#" class="text-blue-600 hover:underline">《用户协议》</a>
              和
              <a href="#" class="text-blue-600 hover:underline">《隐私政策》</a>
            </el-checkbox>
          </el-form-item>
          
          <el-button
            type="primary"
            size="large"
            class="w-full bg-trip-gradient"
            :loading="loading"
            @click="handleRegister"
          >
            注册
          </el-button>
        </el-form>

        <div class="mt-6 text-center text-sm text-gray-600">
          已有账号？
          <router-link to="/login" class="text-blue-600 hover:underline font-medium">
            立即登录
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { register as registerApi } from '@/api/modules/auth'

const router = useRouter()

const formRef = ref<FormInstance>()
const loading = ref(false)

const registerForm = reactive({
  username: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: '',
  agree: false
})

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (registerForm.confirmPassword !== '') {
      formRef.value?.validateField('confirmPassword')
    }
    callback()
  }
}

const validatePass2 = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, validator: validatePass, trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' }
  ],
  agree: [
    {
      validator: (rule: any, value: any, callback: any) => {
        if (!value) {
          callback(new Error('请阅读并同意用户协议和隐私政策'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}

const handleRegister = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      
      try {
        console.log('开始注册...')
        console.log('注册参数:', {
          username: registerForm.username,
          email: registerForm.email,
          password: registerForm.password,
          phone: registerForm.phone
        })
        
        // 调用注册 API
        const res = await registerApi({
          username: registerForm.username,
          email: registerForm.email,
          password: registerForm.password,
          phone: registerForm.phone
        })
        
        console.log('注册响应:', res)
        
        if (res.code === 200) {
          ElMessage.success('注册成功，即将跳转到登录页')
          
          setTimeout(() => {
            router.push('/login')
          }, 1500)
        } else {
          ElMessage.error(res.message || '注册失败')
        }
      } catch (error) {
        console.error('注册错误:', error)
        ElMessage.error('注册失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>
