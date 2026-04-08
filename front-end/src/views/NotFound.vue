<template>
  <main class="max-w-4xl mx-auto px-4 py-20 text-center">
    <div class="mb-8">
      <span class="iconify text-9xl text-gray-300" data-icon="material-symbols:error-outline"></span>
    </div>
    <h1 class="text-6xl font-bold text-gray-800 mb-4">404</h1>
    <p class="text-2xl text-gray-600 mb-8">页面未找到</p>
    <p class="text-gray-500 mb-12">抱歉，您访问的页面不存在或已被删除</p>
    <div class="flex gap-4 justify-center">
      <el-button type="primary" size="large" @click="goHome">
        <span class="iconify mr-2" data-icon="material-symbols:home"></span>
        返回首页
      </el-button>
      <el-button size="large" @click="goBack">
        <span class="iconify mr-2" data-icon="material-symbols:arrow-back"></span>
        返回上一页
      </el-button>
    </div>
  </main>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}

const goBack = () => {
  router.back()
}
</script>
