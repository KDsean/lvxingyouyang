<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="个人信息" name="profile">
        <div class="max-w-2xl">
          <el-form :model="profileForm" label-width="100px">
            <el-form-item label="头像">
              <el-upload
                class="avatar-uploader"
                action="#"
                :show-file-list="false"
                :auto-upload="false"
              >
                <img v-if="profileForm.avatar" :src="profileForm.avatar" class="avatar" @error="onImageError" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>

            <el-form-item label="用户名">
              <el-input v-model="profileForm.username"></el-input>
            </el-form-item>

            <el-form-item label="邮箱">
              <el-input v-model="profileForm.email"></el-input>
            </el-form-item>

            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone"></el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :loading="profileSaving" @click="updateProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>

      <el-tab-pane label="我的订单" name="orders">
        <div class="space-y-4">
          <el-empty v-if="!orders.length" description="暂无订单" />
          <div
            v-for="order in orders"
            :key="order.id"
            class="bg-white rounded-xl p-6 shadow-sm border border-gray-100"
          >
            <div class="flex items-center justify-between mb-4">
              <div>
                <div class="font-bold text-lg">{{ order.itemName }}</div>
                <div class="text-sm text-gray-500">订单号：{{ order.id }}</div>
              </div>
              <el-tag :type="getOrderStatusType(order.status)">
                {{ getOrderStatusText(order.status) }}
              </el-tag>
            </div>

            <div class="flex items-center justify-between">
              <div class="text-orange-500 font-bold text-xl">¥{{ order.amount }}</div>
              <div class="text-sm text-gray-500">{{ formatDate(order.createdAt) }}</div>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="修改密码" name="password">
        <div class="max-w-2xl">
          <el-form :model="passwordForm" label-width="100px">
            <el-form-item label="原密码">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password></el-input>
            </el-form-item>

            <el-form-item label="新密码">
              <el-input v-model="passwordForm.newPassword" type="password" show-password></el-input>
            </el-form-item>

            <el-form-item label="确认密码">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password></el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="changePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>
    </el-tabs>
  </main>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { Order, User } from '@/types'
import { updateProfile as updateProfileApi } from '@/api/modules/auth'
import { getMyOrders } from '@/api/modules/user'
import { useUserStore } from '@/stores/user'
import { onImageError } from '@/utils/image'

const route = useRoute()
const userStore = useUserStore()

const activeTab = ref((route.query.tab as string) || 'profile')
const profileSaving = ref(false)

const profileForm = reactive({
  avatar: '',
  username: '',
  email: '',
  phone: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const orders = ref<Order[]>([])

const fillProfileForm = (user?: User | null) => {
  profileForm.avatar = user?.avatar || ''
  profileForm.username = user?.username || ''
  profileForm.email = user?.email || ''
  profileForm.phone = user?.phone || ''
}

const loadProfile = async () => {
  await userStore.fetchUserInfo()
  fillProfileForm(userStore.user)
}

const loadOrders = async () => {
  try {
    const res = await getMyOrders({ page: 1, pageSize: 20 })
    if (res.code === 200 && res.data) {
      orders.value = res.data.list
    }
  } catch (error) {
    console.error('Load orders error:', error)
  }
}

const updateProfile = async () => {
  if (!profileForm.username.trim() || !profileForm.email.trim()) {
    ElMessage.warning('用户名和邮箱不能为空')
    return
  }

  profileSaving.value = true
  try {
    const res = await updateProfileApi({
      avatar: profileForm.avatar,
      username: profileForm.username.trim(),
      email: profileForm.email.trim(),
      phone: profileForm.phone.trim()
    })

    if (res.code === 200 && res.data) {
      userStore.updateUser(res.data)
      fillProfileForm(res.data)
      ElMessage.success('保存成功')
    }
  } finally {
    profileSaving.value = false
  }
}

const changePassword = () => {
  if (!passwordForm.oldPassword || !passwordForm.newPassword || !passwordForm.confirmPassword) {
    ElMessage.warning('请填写完整信息')
    return
  }

  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }

  ElMessage.success('密码修改成功')
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

const getOrderStatusType = (status: string) => {
  const map: Record<string, any> = {
    pending: 'warning',
    paid: 'success',
    cancelled: 'info',
    completed: ''
  }
  return map[status] || ''
}

const getOrderStatusText = (status: string) => {
  const map: Record<string, string> = {
    pending: '待支付',
    paid: '已支付',
    cancelled: '已取消',
    completed: '已完成'
  }
  return map[status] || status
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleString('zh-CN')
}

onMounted(async () => {
  fillProfileForm(userStore.user)
  await Promise.all([loadProfile(), loadOrders()])
})
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  display: block;
  border-radius: 50%;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
