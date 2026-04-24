<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <!-- 搜索区域 -->
    <div class="bg-gradient-to-r from-orange-600 to-amber-500 rounded-2xl shadow-lg p-8 mb-8 text-white">
      <h1 class="text-3xl font-bold mb-6 flex items-center">
        <span class="iconify mr-3" data-icon="material-symbols:local-activity"></span>
        景点门票
      </h1>
      
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <el-input
          v-model="searchParams.city"
          placeholder="目的地城市"
          size="large"
          clearable
        >
          <template #prefix>
            <span class="iconify" data-icon="material-symbols:location-on"></span>
          </template>
        </el-input>
        
        <el-select v-model="searchParams.type" placeholder="景点类型" size="large" clearable>
          <el-option label="自然风光" value="nature"></el-option>
          <el-option label="历史文化" value="culture"></el-option>
          <el-option label="主题乐园" value="theme-park"></el-option>
          <el-option label="博物馆" value="museum"></el-option>
          <el-option label="动物园" value="zoo"></el-option>
        </el-select>
        
        <el-button type="primary" size="large" @click="handleSearch" class="bg-white text-orange-600 hover:bg-gray-100">
          <span class="iconify mr-2" data-icon="material-symbols:search"></span>
          搜索景点
        </el-button>
      </div>
    </div>

    <!-- 热门景点 -->
    <div class="mb-8">
      <h2 class="text-2xl font-bold mb-4">热门景点推荐</h2>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div
          v-for="attraction in hotAttractions"
          :key="attraction.id"
          class="group bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-xl transition cursor-pointer"
          @click="viewDetail(attraction.id)"
        >
          <div class="relative h-56 overflow-hidden">
            <img
              :src="getAttractionImage(attraction)"
              :alt="attraction.name"
              class="w-full h-full object-cover group-hover:scale-110 transition duration-500"
            />
            <div class="absolute top-4 left-4 bg-orange-500 text-white px-3 py-1 rounded-full text-sm font-bold">
              热门
            </div>
            <div class="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/70 to-transparent p-4">
              <h3 class="text-white text-xl font-bold">{{ attraction.name }}</h3>
              <div class="flex items-center text-white text-sm mt-1">
                <span class="iconify mr-1" data-icon="material-symbols:location-on"></span>
                {{ attraction.city }}
              </div>
            </div>
          </div>
          
          <div class="p-4">
            <div class="flex items-center justify-between mb-3">
              <div class="flex items-center text-yellow-500">
                <span class="iconify mr-1" data-icon="material-symbols:star"></span>
                <span class="font-bold">{{ attraction.rating }}</span>
                <span class="text-gray-400 text-sm ml-1">/5</span>
              </div>
              <div class="text-orange-500 font-bold text-xl">¥{{ attraction.price }}</div>
            </div>
            
            <p class="text-gray-600 text-sm mb-3 line-clamp-2">{{ attraction.description }}</p>
            
            <div class="flex flex-wrap gap-2">
              <span
                v-for="tag in attraction.tags.slice(0, 3)"
                :key="tag"
                class="px-2 py-1 bg-orange-50 text-orange-600 rounded text-xs"
              >
                {{ tag }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 景点列表 -->
    <div class="mb-8">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold">全部景点</h2>
        <el-select v-model="sortBy" placeholder="排序方式" size="large" style="width: 150px">
          <el-option label="综合排序" value="default"></el-option>
          <el-option label="价格从低到高" value="price-asc"></el-option>
          <el-option label="价格从高到低" value="price-desc"></el-option>
          <el-option label="评分最高" value="rating"></el-option>
        </el-select>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div
          v-for="attraction in attractions"
          :key="attraction.id"
          class="bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-md transition flex"
        >
          <div class="w-48 h-48 relative overflow-hidden flex-shrink-0">
            <img
              :src="getAttractionImage(attraction)"
              :alt="attraction.name"
              class="w-full h-full object-cover hover:scale-110 transition duration-500"
            />
          </div>
          
          <div class="flex-1 p-6 flex flex-col justify-between">
            <div>
              <h3 class="text-xl font-bold mb-2">{{ attraction.name }}</h3>
              <div class="flex items-center text-sm text-gray-500 mb-2">
                <span class="iconify mr-1" data-icon="material-symbols:location-on"></span>
                {{ attraction.city }}
              </div>
              <div class="flex items-center text-yellow-500 mb-3">
                <span class="iconify mr-1" data-icon="material-symbols:star"></span>
                <span class="font-bold">{{ attraction.rating }}</span>
              </div>
              <p class="text-gray-600 text-sm line-clamp-2">{{ attraction.description }}</p>
            </div>
            
            <div class="flex items-center justify-between mt-4">
              <div class="text-orange-500 font-bold text-2xl">¥{{ attraction.price }}</div>
              <el-button type="primary" @click="buyTicket(attraction)">
                购买门票
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { Attraction } from '@/types'
import { searchAttractions } from '@/api/modules/attraction'
import { getAttractionImage } from '@/utils/businessImages'

const router = useRouter()

const searchParams = reactive({
  city: '',
  type: ''
})

const sortBy = ref('default')

const hotAttractions = ref<Attraction[]>([])

const attractions = ref<Attraction[]>([])

const handleSearch = async () => {
  try {
    console.log('开始搜索景点...')
    const params = {
      city: searchParams.city,
      type: searchParams.type,
      sortBy: sortBy.value
    }
    
    console.log('搜索参数:', params)
    const res = await searchAttractions(params)
    console.log('搜索结果:', res)
    // searchAttractions 返回 PageResponse，数据在 res.data.list
    const list = res.data?.list ?? res.data ?? []
    attractions.value = list
    hotAttractions.value = list.slice(0, 3)
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

const buyTicket = (attraction: Attraction) => {
  router.push({
    name: 'AttractionDetail',
    params: { id: attraction.id }
  })
}

const viewDetail = (id: string) => {
  router.push({ name: 'AttractionDetail', params: { id } })
}

onMounted(() => {
  console.log('景点页面已加载，准备获取数据...')
  handleSearch()
})
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
