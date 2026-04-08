<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <!-- 搜索区域 -->
    <div class="bg-gradient-to-r from-purple-600 to-indigo-500 rounded-2xl shadow-lg p-8 mb-8 text-white">
      <h1 class="text-3xl font-bold mb-6 flex items-center">
        <span class="iconify mr-3" data-icon="material-symbols:train"></span>
        火车票预订
      </h1>
      
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <el-autocomplete
          v-model="searchParams.from"
          :fetch-suggestions="queryStations"
          placeholder="出发站"
          size="large"
          clearable
          style="width: 100%"
        >
          <template #prefix>
            <span class="iconify" data-icon="material-symbols:location-on"></span>
          </template>
        </el-autocomplete>
        
        <el-autocomplete
          v-model="searchParams.to"
          :fetch-suggestions="queryStations"
          placeholder="到达站"
          size="large"
          clearable
          style="width: 100%"
        >
          <template #prefix>
            <span class="iconify" data-icon="material-symbols:location-on"></span>
          </template>
        </el-autocomplete>
        
        <el-date-picker
          v-model="searchParams.date"
          type="date"
          placeholder="出发日期"
          size="large"
          style="width: 100%"
        />
        
        <el-button type="primary" size="large" @click="handleSearch" class="bg-white text-purple-600 hover:bg-gray-100">
          <span class="iconify mr-2" data-icon="material-symbols:search"></span>
          查询车次
        </el-button>
      </div>
    </div>

    <!-- 车次列表 -->
    <div v-if="trains.length > 0" class="space-y-4">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold">可选车次</h2>
        <div class="flex gap-2">
          <el-tag>共 {{ trains.length }} 个车次</el-tag>
        </div>
      </div>
      
      <div
        v-for="train in trains"
        :key="train.id"
        class="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition"
      >
        <div class="flex flex-col md:flex-row items-center justify-between gap-6">
          <div class="flex items-center gap-8 flex-1">
            <div class="text-center">
              <div class="text-2xl font-bold text-purple-600 mb-2">{{ train.trainNumber }}</div>
              <div class="text-xs text-gray-400">{{ getTrainType(train.trainNumber) }}</div>
            </div>
            
            <div class="text-center">
              <div class="text-3xl font-bold">{{ formatTime(train.departure.time) }}</div>
              <div class="text-gray-500 text-sm mt-1">{{ train.departure.station }}</div>
            </div>
            
            <div class="flex-1 text-center">
              <div class="flex items-center justify-center mb-2">
                <div class="flex-1 border-t-2 border-dashed border-gray-300"></div>
                <span class="iconify text-2xl text-purple-600 mx-2" data-icon="material-symbols:train"></span>
                <div class="flex-1 border-t-2 border-dashed border-gray-300"></div>
              </div>
              <div class="text-sm text-gray-500">{{ train.duration }}</div>
            </div>
            
            <div class="text-center">
              <div class="text-3xl font-bold">{{ formatTime(train.arrival.time) }}</div>
              <div class="text-gray-500 text-sm mt-1">{{ train.arrival.station }}</div>
            </div>
          </div>
          
          <div class="flex gap-3">
            <div
              v-for="seat in train.seatTypes"
              :key="seat.type"
              class="text-center p-3 border rounded-lg hover:border-purple-600 transition cursor-pointer"
              :class="{ 'border-purple-600 bg-purple-50': selectedSeats[train.id] === seat.type }"
              @click="selectSeat(train.id, seat.type)"
            >
              <div class="text-sm text-gray-600 mb-1">{{ seat.type }}</div>
              <div class="text-xl font-bold text-orange-500">¥{{ seat.price }}</div>
              <div class="text-xs text-gray-400 mt-1">{{ seat.available > 0 ? `余${seat.available}` : '无票' }}</div>
            </div>
          </div>
          
          <el-button
            type="primary"
            @click="bookTrain(train)"
            :disabled="!selectedSeats[train.id]"
            class="w-32"
          >
            预订
          </el-button>
        </div>
      </div>
    </div>
    
    <div v-else class="text-center py-20">
      <span class="iconify text-6xl text-gray-300 mb-4" data-icon="material-symbols:train"></span>
      <p class="text-gray-400">请输入出发站和到达站查询车次</p>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { searchTrains, getStations } from '@/api/modules/train'
import type { Train } from '@/types'

const router = useRouter()

const searchParams = reactive({
  from: '',
  to: '',
  date: ''
})

// 从后端动态加载车站列表
const stations = ref<Array<{ value: string; city: string }>>([])

const loadStations = async () => {
  try {
    const res = await getStations()
    stations.value = res.data as Array<{ value: string; city: string }>
  } catch (e) {
    // 接口失败时兜底用本地数据
    stations.value = [
      { value: '北京南', city: '北京' },
      { value: '上海虹桥', city: '上海' },
      { value: '广州南', city: '广州' },
      { value: '深圳北', city: '深圳' },
      { value: '杭州东', city: '杭州' },
      { value: '南京南', city: '南京' },
      { value: '武汉', city: '武汉' },
      { value: '成都东', city: '成都' }
    ]
  }
}

const trains = ref<Train[]>([])
const selectedSeats = ref<Record<string, string>>({})

const queryStations = async (queryString: string, cb: any) => {
  try {
    const res = await getStations(queryString || undefined)
    const list = res.data as Array<{ value: string; city: string }>
    cb(list.length ? list : stations.value)
  } catch {
    const results = queryString
      ? stations.value.filter(s => s.value.includes(queryString) || s.city.includes(queryString))
      : stations.value
    cb(results)
  }
}

const handleSearch = async () => {
  try {
    console.log('开始搜索火车...')
    const params = {
      from: searchParams.from || '',
      to: searchParams.to || '',
      date: searchParams.date,
      page: 1,
      pageSize: 50
    }
    console.log('搜索参数:', params)
    const res = await searchTrains(params)
    console.log('搜索结果:', res)
    // res.data 是 PageResponse，取 .list
    trains.value = (res.data?.list ?? res.data ?? []) as Train[]
    if (searchParams.from && searchParams.to) {
      ElMessage.success(`查询到 ${trains.value.length} 个车次`)
    }
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('查询失败，请稍后重试')
  }
}

const selectSeat = (trainId: string, seatType: string) => {
  selectedSeats.value[trainId] = seatType
}

const bookTrain = (train: Train) => {
  if (!selectedSeats.value[train.id]) {
    ElMessage.warning('请选择座位类型')
    return
  }
  ElMessage.success('跳转到预订页面')
}

const formatTime = (time: string) => {
  return new Date(time).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

const getTrainType = (trainNumber: string) => {
  if (trainNumber.startsWith('G')) return '高速动车'
  if (trainNumber.startsWith('D')) return '动车'
  if (trainNumber.startsWith('C')) return '城际'
  if (trainNumber.startsWith('Z')) return '直达特快'
  if (trainNumber.startsWith('T')) return '特快'
  return '普快'
}

onMounted(() => {
  console.log('火车页面已加载，准备获取数据...')
  const today = new Date()
  searchParams.date = today
  loadStations()
  handleSearch()  // 自动加载全部列车
})
</script>
