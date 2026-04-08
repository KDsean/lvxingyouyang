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
              <el-button type="primary" @click="updateProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="我的订单" name="orders">
        <div class="space-y-4">
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
import { ref, reactive } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { Order } from '@/types'
import { onImageError } from '@/utils/image'

const route = useRoute()

const activeTab = ref((route.query.tab as string) || 'profile')

const profileForm = reactive({
  avatar: '',
  username: '旅行达人',
  email: 'user@example.com',
  phone: '13800138000'
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const orders = ref<Order[]>([
  {
    id: 'ORD20260301001',
    userId: '1',
    type: 'hotel',
    itemId: '1',
    itemName: '三亚天际海景度假酒店',
    amount: 1899,
    status: 'paid',
    createdAt: '2026-02-28T10:00:00',
    paidAt: '2026-02-28T10:05:00'
  },
  {
    id: 'ORD20260301002',
    userId: '1',
    type: 'flight',
    itemId: '1',
    itemName: '北京-上海 CA1234',
    amount: 880,
    status: 'completed',
    createdAt: '2026-02-25T14:00:00',
    paidAt: '2026-02-25T14:05:00'
  }
])

const updateProfile = () => {
  ElMessage.success('保存成功')
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
