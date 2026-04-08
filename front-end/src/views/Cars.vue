<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <!-- 搜索区域 -->
    <div class="bg-gradient-to-r from-green-600 to-teal-500 rounded-2xl shadow-lg p-8 mb-8 text-white">
      <h1 class="text-3xl font-bold mb-6 flex items-center">
        <span class="iconify mr-3" data-icon="material-symbols:directions-car"></span>
        租车自驾
      </h1>
      
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <el-input
          v-model="searchParams.location"
          placeholder="取车城市"
          size="large"
          clearable
        >
          <template #prefix>
            <span class="iconify" data-icon="material-symbols:location-on"></span>
          </template>
        </el-input>
        
        <el-date-picker
          v-model="searchParams.pickupDate"
          type="datetime"
          placeholder="取车时间"
          size="large"
          style="width: 100%"
        />
        
        <el-date-picker
          v-model="searchParams.returnDate"
          type="datetime"
          placeholder="还车时间"
          size="large"
          style="width: 100%"
        />
        
        <el-button type="primary" size="large" @click="handleSearch" class="bg-white text-green-600 hover:bg-gray-100">
          <span class="iconify mr-2" data-icon="material-symbols:search"></span>
          搜索车辆
        </el-button>
      </div>
      
      <div class="mt-4 flex gap-4">
        <el-select v-model="carType" placeholder="车型" size="large" clearable>
          <el-option label="经济型" value="economy"></el-option>
          <el-option label="舒适型" value="comfort"></el-option>
          <el-option label="SUV" value="suv"></el-option>
          <el-option label="豪华型" value="luxury"></el-option>
        </el-select>
        
        <el-select v-model="transmission" placeholder="变速箱" size="large" clearable>
          <el-option label="自动挡" value="auto"></el-option>
          <el-option label="手动挡" value="manual"></el-option>
        </el-select>
      </div>
    </div>

    <!-- 热门取车地点 -->
    <div class="mb-8">
      <h2 class="text-2xl font-bold mb-4">热门取车地点</h2>
      <div class="flex flex-wrap gap-3">
        <el-tag
          v-for="city in popularCities"
          :key="city"
          size="large"
          class="cursor-pointer"
          @click="searchParams.location = city"
        >
          {{ city }}
        </el-tag>
      </div>
    </div>

    <!-- 车辆列表 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="car in cars"
        :key="car.id"
        class="bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-lg transition"
      >
        <div class="relative h-48 bg-gradient-to-br from-gray-100 to-gray-200">
          <img
            :src="car.image"
            :alt="car.model"
            class="w-full h-full object-contain p-4"
          />
          <div class="absolute top-4 right-4 bg-green-500 text-white px-3 py-1 rounded-full text-sm font-bold">
            {{ getCarTypeName(car.type) }}
          </div>
        </div>
        
        <div class="p-6">
          <h3 class="text-xl font-bold mb-2">{{ car.brand }} {{ car.model }}</h3>
          
          <div class="grid grid-cols-2 gap-3 mb-4 text-sm text-gray-600">
            <div class="flex items-center">
              <span class="iconify mr-2 text-green-600" data-icon="material-symbols:airline-seat-recline-normal"></span>
              {{ car.seats }} 座
            </div>
            <div class="flex items-center">
              <span class="iconify mr-2 text-green-600" data-icon="material-symbols:settings"></span>
              {{ car.transmission === 'auto' ? '自动挡' : '手动挡' }}
            </div>
          </div>
          
          <div class="flex flex-wrap gap-2 mb-4">
            <span
              v-for="feature in car.features"
              :key="feature"
              class="px-2 py-1 bg-green-50 text-green-600 rounded text-xs"
            >
              {{ feature }}
            </span>
          </div>
          
          <div class="flex items-center justify-between pt-4 border-t">
            <div>
              <div class="text-2xl font-bold text-orange-500">¥{{ car.pricePerDay }}</div>
              <div class="text-xs text-gray-500">/天</div>
            </div>
            <el-button type="primary" @click="bookCar(car)">
              立即预订
            </el-button>
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
import { searchCars } from '@/api/modules/car'
import type { CarRental } from '@/types'

const router = useRouter()

const searchParams = reactive({
  location: '',
  pickupDate: '',
  returnDate: ''
})

const carType = ref('')
const transmission = ref('')

const popularCities = ['北京', '上海', '广州', '深圳', '成都', '杭州', '三亚', '西安']

const cars = ref<CarRental[]>([
  {
    id: '1',
    brand: '大众',
    model: '朗逸',
    type: 'economy',
    seats: 5,
    transmission: 'auto',
    pricePerDay: 150,
    image: 'https://modao.cc/agent/placeholder.svg',
    features: ['蓝牙', 'USB充电', '倒车影像'],
    location: '北京'
  },
  {
    id: '2',
    brand: '本田',
    model: 'CR-V',
    type: 'suv',
    seats: 5,
    transmission: 'auto',
    pricePerDay: 280,
    image: 'https://modao.cc/agent/placeholder.svg',
    features: ['全景天窗', '座椅加热', '自动泊车', '车载导航'],
    location: '上海'
  },
  {
    id: '3',
    brand: '丰田',
    model: '凯美瑞',
    type: 'comfort',
    seats: 5,
    transmission: 'auto',
    pricePerDay: 220,
    image: 'https://modao.cc/agent/placeholder.svg',
    features: ['真皮座椅', '定速巡航', '倒车雷达'],
    location: '广州'
  },
  {
    id: '4',
    brand: '奔驰',
    model: 'E级',
    type: 'luxury',
    seats: 5,
    transmission: 'auto',
    pricePerDay: 680,
    image: 'https://modao.cc/agent/placeholder.svg',
    features: ['全景天窗', '座椅按摩', '柏林之声', '自动驾驶辅助'],
    location: '深圳'
  },
  {
    id: '5',
    brand: '别克',
    model: 'GL8',
    type: 'suv',
    seats: 7,
    transmission: 'auto',
    pricePerDay: 380,
    image: 'https://modao.cc/agent/placeholder.svg',
    features: ['7座商务', '电动侧滑门', '后排娱乐'],
    location: '成都'
  },
  {
    id: '6',
    brand: '特斯拉',
    model: 'Model 3',
    type: 'luxury',
    seats: 5,
    transmission: 'auto',
    pricePerDay: 450,
    image: 'https://modao.cc/agent/placeholder.svg',
    features: ['纯电动', '自动驾驶', '超大屏幕', '超级充电'],
    location: '杭州'
  }
])

const handleSearch = async () => {
  if (!searchParams.location || !searchParams.pickupDate || !searchParams.returnDate) {
    ElMessage.warning('请填写完整的搜索信息')
    return
  }
  
  try {
    console.log('开始搜索租车...')
    const params = {
      location: searchParams.location,
      pickupDate: searchParams.pickupDate,
      returnDate: searchParams.returnDate,
      carType: carType.value,
      transmission: transmission.value
    }
    
    console.log('搜索参数:', params)
    const res = await searchCars(params)
    console.log('搜索结果:', res)
    cars.value = res.data
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

const bookCar = (car: CarRental) => {
  if (!searchParams.pickupDate || !searchParams.returnDate) {
    ElMessage.warning('请先选择取车和还车时间')
    return
  }
  ElMessage.success('跳转到预订页面')
}

const getCarTypeName = (type: string) => {
  const map: Record<string, string> = {
    economy: '经济型',
    comfort: '舒适型',
    suv: 'SUV',
    luxury: '豪华型'
  }
  return map[type] || type
}

onMounted(() => {
  console.log('租车页面已加载，准备获取数据...')
  const today = new Date()
  searchParams.pickupDate = today
})
</script>
