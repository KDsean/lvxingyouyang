<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <!-- 头部搜索 -->
    <div class="bg-gradient-to-r from-blue-600 to-purple-600 rounded-2xl shadow-lg p-8 mb-8 text-white">
      <h1 class="text-3xl font-bold mb-6">探索目的地</h1>
      <el-input
        v-model="searchKeyword"
        placeholder="搜索目的地..."
        size="large"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <span class="iconify" data-icon="material-symbols:search"></span>
        </template>
        <template #append>
          <el-button @click="handleSearch">搜索</el-button>
        </template>
      </el-input>
    </div>

    <!-- 热门目的地 -->
    <div class="mb-12">
      <h2 class="text-2xl font-bold mb-6">热门目的地</h2>
      <div class="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
        <div
          v-for="destination in popularDestinations"
          :key="destination.id"
          class="group relative h-80 rounded-2xl overflow-hidden shadow-lg cursor-pointer"
          @click="viewDetail(destination.id)"
        >
          <img
            :src="destination.image"
            :alt="destination.name"
            class="w-full h-full object-cover group-hover:scale-110 transition duration-700"
          />
          <div class="absolute inset-0 bg-gradient-to-t from-black/80 via-black/30 to-transparent flex flex-col justify-end p-6 text-white">
            <h3 class="text-2xl font-bold mb-2">{{ destination.name }}</h3>
            <p class="text-sm opacity-90 mb-3">{{ destination.country }}</p>
            <div class="flex flex-wrap gap-2">
              <span
                v-for="tag in destination.tags.slice(0, 3)"
                :key="tag"
                class="px-2 py-1 bg-white/20 backdrop-blur rounded text-xs"
              >
                {{ tag }}
              </span>
            </div>
          </div>
          <div class="absolute top-4 right-4 bg-orange-500 text-white px-3 py-1 rounded-full text-sm font-bold">
            🔥 热门
          </div>
        </div>
      </div>
    </div>

    <!-- 按地区浏览 -->
    <div class="mb-12">
      <h2 class="text-2xl font-bold mb-6">按地区浏览</h2>
      <el-tabs v-model="activeRegion" @tab-change="handleRegionChange">
        <el-tab-pane label="国内" name="domestic"></el-tab-pane>
        <el-tab-pane label="亚洲" name="asia"></el-tab-pane>
        <el-tab-pane label="欧洲" name="europe"></el-tab-pane>
        <el-tab-pane label="美洲" name="america"></el-tab-pane>
        <el-tab-pane label="其他" name="other"></el-tab-pane>
      </el-tabs>
      
      <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4 mt-6">
        <div
          v-for="dest in regionDestinations"
          :key="dest.id"
          class="bg-white rounded-xl p-4 shadow-sm border border-gray-100 hover:shadow-md transition cursor-pointer text-center"
          @click="viewDetail(dest.id)"
        >
          <div class="text-4xl mb-2">{{ dest.emoji }}</div>
          <div class="font-bold">{{ dest.name }}</div>
          <div class="text-xs text-gray-500 mt-1">{{ dest.country }}</div>
        </div>
      </div>
    </div>

    <!-- 最佳旅行季节 -->
    <div class="mb-12">
      <h2 class="text-2xl font-bold mb-6">当季推荐</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div
          v-for="destination in seasonalDestinations"
          :key="destination.id"
          class="bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-md transition flex cursor-pointer"
          @click="viewDetail(destination.id)"
        >
          <div class="w-48 h-48 flex-shrink-0 overflow-hidden">
            <img
              :src="destination.image"
              :alt="destination.name"
              class="w-full h-full object-cover hover:scale-110 transition duration-500"
            />
          </div>
          <div class="flex-1 p-6">
            <h3 class="text-xl font-bold mb-2">{{ destination.name }}</h3>
            <p class="text-gray-600 text-sm mb-4">{{ destination.description }}</p>
            <div class="flex items-center text-sm text-gray-500 mb-3">
              <span class="iconify mr-1 text-green-600" data-icon="material-symbols:wb-sunny"></span>
              最佳季节：{{ destination.bestSeason.join('、') }}
            </div>
            <div class="flex flex-wrap gap-2">
              <span
                v-for="tag in destination.tags"
                :key="tag"
                class="px-2 py-1 bg-blue-50 text-blue-600 rounded text-xs"
              >
                {{ tag }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { searchDestinations } from '@/api/modules/destination'
import type { Destination } from '@/types'

const router = useRouter()

const searchKeyword = ref('')
const activeRegion = ref('domestic')

const destinations = ref<Destination[]>([])
const popularDestinations = ref<Destination[]>([])
const regionDestinations = ref<Destination[]>([])
const seasonalDestinations = ref<Destination[]>([])

const handleSearch = async () => {
  try {
    console.log('开始搜索目的地...')
    const params = {
      keyword: searchKeyword.value,
      region: activeRegion.value
    }
    
    console.log('搜索参数:', params)
    const res = await searchDestinations(params)
    console.log('搜索结果:', res)
    destinations.value = res.data
    popularDestinations.value = res.data.slice(0, 4)
    regionDestinations.value = res.data.slice(0, 6)
    seasonalDestinations.value = res.data.slice(0, 4)
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

const handleRegionChange = () => {
  handleSearch()
}

const viewDetail = (id: string) => {
  router.push({ name: 'DestinationDetail', params: { id } })
}

onMounted(() => {
  console.log('目的地页面已加载，准备获取数据...')
  handleSearch()
})
</script>
