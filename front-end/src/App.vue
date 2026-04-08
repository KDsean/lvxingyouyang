<template>
  <router-view />
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 应用启动时，如果 localStorage 中有 token，自动拉取用户信息恢复登录状态
onMounted(async () => {
  if (userStore.token) {
    await userStore.fetchUserInfo()
  }
})
</script>

<style>
#app {
  min-height: 100vh;
}
</style>
