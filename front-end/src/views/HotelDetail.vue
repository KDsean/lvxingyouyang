<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <el-button @click="goBack" class="mb-6">
      <span class="iconify mr-2" data-icon="material-symbols:arrow-back"></span>
      返回
    </el-button>

    <div class="bg-white rounded-2xl shadow-lg overflow-hidden">
      <!-- 酒店图片 -->
      <div class="relative h-96">
        <img
          :src="hotelHeroImage"
          alt="Hotel"
          class="w-full h-full object-cover"
        />
      </div>

      <div class="p-8">
        <div class="flex items-start justify-between mb-6">
          <div class="flex-1">
            <h1 class="text-3xl font-bold mb-3">三亚天际海景度假酒店</h1>
            <div class="flex items-center text-yellow-500 mb-3">
              <span v-for="i in 5" :key="i" class="iconify text-xl" data-icon="material-symbols:star"></span>
              <span class="ml-2 text-gray-500">4.9/5 (2,400+ 条评价)</span>
            </div>
            <div class="flex items-center text-gray-600">
              <span class="iconify mr-2" data-icon="material-symbols:location-on"></span>
              海南省三亚市海棠湾
            </div>
          </div>
          <div class="text-right">
            <div class="text-4xl font-bold text-orange-500">¥1,899</div>
            <div class="text-gray-500">/晚起</div>
          </div>
        </div>

        <el-divider />

        <div class="mb-8">
          <h2 class="text-2xl font-bold mb-4">酒店介绍</h2>
          <p class="text-gray-600 leading-relaxed">
            独拥两公里私人沙滩，2026年全新装修，配备AI全屋智能控制系统，让您的海滨度假充满科技感与舒适度。
            酒店拥有270度海景房，每间客房都能欣赏到壮丽的海景。配备米其林三星餐厅、无边泳池、SPA中心等设施。
          </p>
        </div>

        <div class="mb-8">
          <h2 class="text-2xl font-bold mb-4">酒店设施</h2>
          <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div v-for="facility in facilities" :key="facility" class="flex items-center">
              <span class="iconify mr-2 text-green-600" data-icon="material-symbols:check-circle"></span>
              {{ facility }}
            </div>
          </div>
        </div>

        <el-divider />

        <div class="flex gap-4">
          <el-button type="primary" size="large" class="flex-1">
            立即预订
          </el-button>
          <el-button size="large">
            <span class="iconify mr-2" data-icon="material-symbols:favorite-outline"></span>
            收藏
          </el-button>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { getHotelImage } from '@/utils/businessImages'

const router = useRouter()
const hotelHeroImage = getHotelImage({ name: '三亚天际海景度假酒店', city: '三亚', location: '海棠湾' })

const facilities = [
  '免费WiFi', '游泳池', '健身房', '餐厅', '停车场', 'SPA',
  '会议室', '儿童乐园', '洗衣服务', '24小时前台', '行李寄存', '接送服务'
]

const goBack = () => {
  router.back()
}
</script>
