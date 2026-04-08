<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <!-- 搜索区域 -->
    <div class="bg-gradient-to-r from-indigo-600 to-blue-500 rounded-2xl shadow-lg p-8 mb-8 text-white">
      <h1 class="text-3xl font-bold mb-6 flex items-center">
        <span class="iconify mr-3" data-icon="material-symbols:flight"></span>
        机票预订
      </h1>
      
      <el-tabs v-model="tripType" class="flight-tabs">
        <el-tab-pane label="单程" name="oneway"></el-tab-pane>
        <el-tab-pane label="往返" name="roundtrip"></el-tab-pane>
      </el-tabs>
      
      <div class="grid grid-cols-1 md:grid-cols-5 gap-4 mt-6">
        <el-input
          v-model="searchParams.from"
          placeholder="出发城市"
          size="large"
          clearable
        >
          <template #prefix>
            <span class="iconify" data-icon="material-symbols:flight-takeoff"></span>
          </template>
        </el-input>
        
        <el-input
          v-model="searchParams.to"
          placeholder="到达城市"
          size="large"
          clearable
        >
          <template #prefix>
            <span class="iconify" data-icon="material-symbols:flight-land"></span>
          </template>
        </el-input>
        
        <el-date-picker
          v-model="searchParams.date"
          type="date"
          placeholder="出发日期"
          size="large"
          style="width: 100%"
        />
        
        <el-date-picker
          v-if="tripType === 'roundtrip'"
          v-model="searchParams.returnDate"
          type="date"
          placeholder="返程日期"
          size="large"
          style="width: 100%"
        />
        
        <el-button type="primary" size="large" @click="handleSearch" class="bg-white text-indigo-600 hover:bg-gray-100">
          <span class="iconify mr-2" data-icon="material-symbols:search"></span>
          搜索航班
        </el-button>
      </div>
      
      <div class="mt-4 flex gap-4">
        <el-select v-model="searchParams.cabinClass" placeholder="舱位等级" size="large">
          <el-option label="经济舱" value="economy"></el-option>
          <el-option label="商务舱" value="business"></el-option>
          <el-option label="头等舱" value="first"></el-option>
        </el-select>
        
        <el-input-number
          v-model="searchParams.passengers"
          :min="1"
          :max="9"
          size="large"
          placeholder="乘客人数"
        />
      </div>
    </div>

    <!-- 热门航线 -->
    <div class="mb-8">
      <h2 class="text-2xl font-bold mb-4">热门航线</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div
          v-for="route in popularRoutes"
          :key="route.id"
          @click="selectRoute(route)"
          class="bg-white rounded-xl p-4 shadow-sm border border-gray-100 hover:shadow-md transition cursor-pointer"
        >
          <div class="flex items-center justify-between mb-2">
            <span class="font-bold text-lg">{{ route.from }}</span>
            <span class="iconify text-indigo-600" data-icon="material-symbols:arrow-forward"></span>
            <span class="font-bold text-lg">{{ route.to }}</span>
          </div>
          <div class="text-orange-500 font-bold">¥{{ route.price }} 起</div>
        </div>
      </div>
    </div>

    <!-- 航班列表 -->
    <div v-if="flights.length > 0" class="space-y-4">
      <h2 class="text-2xl font-bold mb-4">可选航班</h2>
      
      <div
        v-for="flight in flights"
        :key="flight.id"
        class="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition"
      >
        <div class="flex flex-col md:flex-row items-center justify-between gap-6">
          <div class="flex items-center gap-6 flex-1">
            <div class="text-center">
              <div class="text-3xl font-bold">{{ formatTime(flight.departure.time) }}</div>
              <div class="text-gray-500 text-sm mt-1">{{ flight.departure.city }}</div>
              <div class="text-gray-400 text-xs">{{ flight.departure.airport }}</div>
            </div>
            
            <div class="flex-1 relative">
              <div class="flex items-center justify-center">
                <div class="flex-1 border-t-2 border-dashed border-gray-300"></div>
                <span class="iconify text-2xl text-indigo-600 mx-2" data-icon="material-symbols:flight"></span>
                <div class="flex-1 border-t-2 border-dashed border-gray-300"></div>
              </div>
              <div class="text-center text-sm text-gray-500 mt-2">
                {{ flight.airline }} {{ flight.flightNumber }}
              </div>
            </div>
            
            <div class="text-center">
              <div class="text-3xl font-bold">{{ formatTime(flight.arrival.time) }}</div>
              <div class="text-gray-500 text-sm mt-1">{{ flight.arrival.city }}</div>
              <div class="text-gray-400 text-xs">{{ flight.arrival.airport }}</div>
            </div>
          </div>
          
          <div class="text-center">
            <div class="text-3xl font-bold text-orange-500">¥{{ flight.price }}</div>
            <div class="text-sm text-gray-500 mb-3">{{ getCabinClassName(flight.cabinClass) }}</div>
            <el-button type="primary" @click="bookFlight(flight)" class="w-full">
              预订
            </el-button>
            <div class="text-xs text-gray-400 mt-2">剩余 {{ flight.availableSeats }} 个座位</div>
          </div>
        </div>
      </div>
    </div>
    
    <div v-else class="text-center py-20">
      <span class="iconify text-6xl text-gray-300 mb-4" data-icon="material-symbols:flight"></span>
      <p class="text-gray-400">请输入出发地和目的地搜索航班</p>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { searchFlights } from '@/api/modules/flight'
import type { Flight } from '@/types'

const router = useRouter()

const tripType = ref('oneway')
const searchParams = reactive({
  from: '',
  to: '',
  date: '',
  returnDate: '',
  passengers: 1,
  cabinClass: 'economy'
})

const popularRoutes = ref([
  { id: 1, from: '北京', to: '上海', price: 580 },
  { id: 2, from: '上海', to: '深圳', price: 680 },
  { id: 3, from: '广州', to: '成都', price: 520 },
  { id: 4, from: '北京', to: '成都', price: 750 },
  { id: 5, from: '上海', to: '三亚', price: 1200 },
  { id: 6, from: '北京', to: '西安', price: 450 },
  { id: 7, from: '深圳', to: '杭州', price: 620 },
  { id: 8, from: '成都', to: '昆明', price: 480 }
])

const flights = ref<Flight[]>([])

const handleSearch = async () => {
  try {
    console.log('开始搜索航班...')
    const params = {
      from: searchParams.from || '',
      to: searchParams.to || '',
      date: searchParams.date,
      returnDate: searchParams.returnDate,
      cabinClass: searchParams.cabinClass,
      passengers: searchParams.passengers,
      tripType: tripType.value,
      page: 1,
      pageSize: 50
    }
    console.log('搜索参数:', params)
    const res = await searchFlights(params)
    console.log('搜索结果:', res)
    // res.data 是 PageResponse，取 .list
    flights.value = (res.data?.list ?? res.data ?? []) as Flight[]
    if (searchParams.from && searchParams.to) {
      ElMessage.success(`查询到 ${flights.value.length} 个航班`)
    }
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

const selectRoute = (route: any) => {
  searchParams.from = route.from
  searchParams.to = route.to
  handleSearch()
}

const bookFlight = (flight: Flight) => {
  ElMessage.success('跳转到预订页面')
  // router.push({ name: 'FlightBooking', params: { id: flight.id } })
}

const formatTime = (time: string) => {
  return new Date(time).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

const getCabinClassName = (cabinClass: string) => {
  const map: Record<string, string> = {
    economy: '经济舱',
    business: '商务舱',
    first: '头等舱'
  }
  return map[cabinClass] || '经济舱'
}

onMounted(() => {
  console.log('航班页面已加载，准备获取数据...')
  const today = new Date()
  searchParams.date = today
  handleSearch()  // 自动加载全部航班
})
</script>

<style scoped>
:deep(.flight-tabs .el-tabs__item) {
  color: white;
}

:deep(.flight-tabs .el-tabs__item.is-active) {
  color: white;
  font-weight: bold;
}

:deep(.flight-tabs .el-tabs__active-bar) {
  background-color: white;
}
</style>
