<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <!-- 头部 -->
    <div class="bg-gradient-to-r from-teal-600 to-cyan-500 rounded-2xl shadow-lg p-8 mb-8 text-white">
      <h1 class="text-3xl font-bold mb-6 flex items-center">
        <span class="iconify mr-3" data-icon="material-symbols:restaurant"></span>
        美食林
      </h1>
      
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <el-input
          v-model="searchParams.city"
          placeholder="城市"
          size="large"
          clearable
        >
          <template #prefix>
            <span class="iconify" data-icon="material-symbols:location-on"></span>
          </template>
        </el-input>
        
        <el-select v-model="searchParams.cuisine" placeholder="菜系" size="large" clearable>
          <el-option label="川菜" value="sichuan"></el-option>
          <el-option label="粤菜" value="cantonese"></el-option>
          <el-option label="湘菜" value="hunan"></el-option>
          <el-option label="日料" value="japanese"></el-option>
          <el-option label="西餐" value="western"></el-option>
          <el-option label="火锅" value="hotpot"></el-option>
        </el-select>
        
        <el-select v-model="searchParams.priceLevel" placeholder="人均消费" size="large" clearable>
          <el-option label="¥50以下" :value="1"></el-option>
          <el-option label="¥50-100" :value="2"></el-option>
          <el-option label="¥100-200" :value="3"></el-option>
          <el-option label="¥200以上" :value="4"></el-option>
        </el-select>
        
        <el-button type="primary" size="large" @click="handleSearch" class="bg-white text-teal-600 hover:bg-gray-100">
          <span class="iconify mr-2" data-icon="material-symbols:search"></span>
          搜索餐厅
        </el-button>
      </div>
    </div>

    <!-- 推荐餐厅 -->
    <div class="mb-8">
      <h2 class="text-2xl font-bold mb-4">米其林推荐</h2>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div
          v-for="restaurant in michelinRestaurants"
          :key="restaurant.id"
          class="group bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-xl transition cursor-pointer"
          @click="viewDetail(restaurant.id)"
        >
          <div class="relative h-56 overflow-hidden">
            <img
              :src="getRestaurantImage(restaurant)"
              :alt="restaurant.name"
              class="w-full h-full object-cover group-hover:scale-110 transition duration-500"
            />
            <div class="absolute top-4 left-4 bg-yellow-500 text-white px-3 py-1 rounded-full text-sm font-bold flex items-center">
              <span class="iconify mr-1" data-icon="material-symbols:star"></span>
              米其林
            </div>
          </div>
          
          <div class="p-6">
            <h3 class="text-xl font-bold mb-2">{{ restaurant.name }}</h3>
            <div class="flex items-center text-sm text-gray-500 mb-3">
              <span class="iconify mr-1" data-icon="material-symbols:location-on"></span>
              {{ restaurant.city }}
            </div>
            
            <div class="flex items-center justify-between mb-3">
              <div class="flex items-center text-yellow-500">
                <span class="iconify mr-1" data-icon="material-symbols:star"></span>
                <span class="font-bold">{{ restaurant.rating }}</span>
              </div>
              <div class="text-orange-500 font-bold">
                {{ getPriceLevelText(restaurant.priceLevel) }}
              </div>
            </div>
            
            <div class="text-sm text-gray-600 mb-3">{{ restaurant.cuisine }}</div>
            
            <div class="flex flex-wrap gap-2">
              <span
                v-for="specialty in restaurant.specialties.slice(0, 3)"
                :key="specialty"
                class="px-2 py-1 bg-teal-50 text-teal-600 rounded text-xs"
              >
                {{ specialty }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 全部餐厅 -->
    <div>
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold">全部餐厅</h2>
        <el-select v-model="sortBy" placeholder="排序" size="large" style="width: 150px">
          <el-option label="综合排序" value="default"></el-option>
          <el-option label="评分最高" value="rating"></el-option>
          <el-option label="人气最高" value="popular"></el-option>
        </el-select>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div
          v-for="restaurant in restaurants"
          :key="restaurant.id"
          class="bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-md transition flex"
        >
          <div class="w-48 h-48 relative overflow-hidden flex-shrink-0">
            <img
              :src="getRestaurantImage(restaurant)"
              :alt="restaurant.name"
              class="w-full h-full object-cover hover:scale-110 transition duration-500"
            />
          </div>
          
          <div class="flex-1 p-6 flex flex-col justify-between">
            <div>
              <h3 class="text-xl font-bold mb-2">{{ restaurant.name }}</h3>
              <div class="flex items-center text-sm text-gray-500 mb-2">
                <span class="iconify mr-1" data-icon="material-symbols:location-on"></span>
                {{ restaurant.address }}
              </div>
              <div class="flex items-center text-yellow-500 mb-3">
                <span class="iconify mr-1" data-icon="material-symbols:star"></span>
                <span class="font-bold">{{ restaurant.rating }}</span>
                <span class="text-gray-400 text-sm ml-2">{{ restaurant.cuisine }}</span>
              </div>
              <div class="text-sm text-gray-600 mb-2">营业时间：{{ restaurant.openHours }}</div>
            </div>
            
            <div class="flex items-center justify-between mt-4">
              <div class="text-orange-500 font-bold text-lg">
                {{ getPriceLevelText(restaurant.priceLevel) }}
              </div>
              <el-button type="primary" @click="bookRestaurant(restaurant)">
                预订座位
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { Restaurant } from '@/types'
import { searchRestaurants } from '@/api/modules/restaurant'
import { onMounted } from 'vue'
import { getRestaurantImage } from '@/utils/businessImages'

const router = useRouter()

const searchParams = reactive({
  city: '',
  cuisine: '',
  priceLevel: null as number | null
})

const sortBy = ref('default')

const michelinRestaurants = ref<Restaurant[]>([])

const restaurants = ref<Restaurant[]>([])

const handleSearch = async () => {
  try {
    console.log('开始搜索餐厅...')
    const params = {
      city: searchParams.city,
      cuisine: searchParams.cuisine,
      priceLevel: searchParams.priceLevel,
      sortBy: sortBy.value
    }
    
    console.log('搜索参数:', params)
    const res = await searchRestaurants(params)
    console.log('搜索结果:', res)
    // searchRestaurants 返回 PageResponse，数据在 res.data.list
    const list = res.data?.list ?? res.data ?? []
    restaurants.value = list
    michelinRestaurants.value = list.slice(0, 3)
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

const bookRestaurant = (restaurant: Restaurant) => {
  router.push({
    name: 'RestaurantDetail',
    params: { id: restaurant.id }
  })
}

const viewDetail = (id: string) => {
  router.push({ name: 'RestaurantDetail', params: { id } })
}

onMounted(() => {
  console.log('餐厅页面已加载，准备获取数据...')
  handleSearch()
})

const getPriceLevelText = (level: number) => {
  const texts = ['', '¥50以下', '¥50-100', '¥100-200', '¥200以上']
  return texts[level] || ''
}
</script>
