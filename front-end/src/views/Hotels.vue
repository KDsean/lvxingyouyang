<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <!-- 搜索区域 -->
    <div class="bg-white rounded-2xl shadow-lg p-8 mb-8">
      <h1 class="text-3xl font-bold mb-6 flex items-center">
        <span class="iconify mr-3 text-blue-600" data-icon="ic:baseline-hotel"></span>
        酒店住宿
      </h1>
      
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <el-input
          v-model="searchParams.destination"
          placeholder="目的地城市"
          size="large"
          clearable
        >
          <template #prefix>
            <span class="iconify" data-icon="material-symbols:location-on"></span>
          </template>
        </el-input>
        
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="入住日期"
          end-placeholder="退房日期"
          size="large"
          style="width: 100%"
        />
        
        <el-input-number
          v-model="searchParams.guests"
          :min="1"
          :max="10"
          size="large"
          style="width: 100%"
        />
        
        <el-button type="primary" size="large" @click="handleSearch" class="bg-blue-600">
          <span class="iconify mr-2" data-icon="material-symbols:search"></span>
          搜索酒店
        </el-button>
      </div>
      
      <!-- 筛选条件 -->
      <div class="mt-6 flex flex-wrap gap-4">
        <div>
          <span class="text-sm text-gray-600 mr-2">价格区间：</span>
          <el-slider
            v-model="priceRange"
            range
            :min="0"
            :max="5000"
            :step="100"
            style="width: 200px"
          />
          <span class="text-xs text-gray-500">¥{{ priceRange[0] }} - ¥{{ priceRange[1] }}</span>
        </div>
        
        <div>
          <span class="text-sm text-gray-600 mr-2">星级：</span>
          <el-checkbox-group v-model="selectedStars">
            <el-checkbox :label="5">五星</el-checkbox>
            <el-checkbox :label="4">四星</el-checkbox>
            <el-checkbox :label="3">三星</el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
    </div>

    <!-- 酒店列表 -->
    <div class="grid grid-cols-1 gap-6">
      <div
        v-for="hotel in hotels"
        :key="hotel.id"
        class="bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-lg transition flex flex-col md:flex-row"
      >
        <div class="md:w-80 h-64 md:h-auto relative overflow-hidden">
          <img
            :src="getHotelImage(hotel)"
            :alt="hotel.name"
            class="w-full h-full object-cover hover:scale-110 transition duration-500"
          />
          <div class="absolute top-4 left-4 bg-black/50 backdrop-blur text-white px-3 py-1 rounded-lg text-sm">
            {{ hotel.city }}
          </div>
        </div>
        
        <div class="flex-1 p-6 flex flex-col justify-between">
          <div>
            <div class="flex items-start justify-between mb-3">
              <div>
                <h3 class="text-2xl font-bold mb-2">{{ hotel.name }}</h3>
                <div class="flex items-center text-yellow-500 mb-2">
                  <span v-for="i in hotel.starLevel" :key="i" class="iconify" data-icon="material-symbols:star"></span>
                  <span class="ml-2 text-gray-500 text-sm">{{ hotel.rating }}/5 ({{ hotel.reviewCount }}+ 条评价)</span>
                </div>
              </div>
              <div class="text-right">
                <div class="text-3xl font-bold text-orange-500">¥{{ hotel.price }}</div>
                <div class="text-sm text-gray-500">/晚起</div>
              </div>
            </div>
            
            <p class="text-gray-600 mb-4">{{ hotel.description }}</p>
            
            <div class="flex flex-wrap gap-2 mb-4">
              <span
                v-for="tag in hotel.tags"
                :key="tag"
                class="px-3 py-1 bg-blue-50 text-blue-600 rounded-full text-xs"
              >
                {{ tag }}
              </span>
            </div>
            
            <div class="flex flex-wrap gap-3 text-sm text-gray-600">
              <span v-for="facility in hotel.facilities.slice(0, 5)" :key="facility" class="flex items-center">
                <span class="iconify mr-1 text-green-600" data-icon="material-symbols:check-circle"></span>
                {{ facility }}
              </span>
            </div>
          </div>
          
          <div class="flex gap-3 mt-4">
            <el-button type="primary" @click="bookHotel(hotel)" class="flex-1">立即预订</el-button>
            <el-button @click="viewDetail(hotel.id)">查看详情</el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="flex justify-center mt-8">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { searchHotels } from '@/api/modules/hotel'
import type { Hotel } from '@/types'
import { getHotelImage } from '@/utils/businessImages'

const router = useRouter()

const searchParams = reactive({
  destination: '',
  guests: 2
})

const dateRange = ref<[Date, Date]>()
const priceRange = ref([0, 5000])
const selectedStars = ref<number[]>([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

const hotels = ref<Hotel[]>([
  {
    id: '1',
    name: '三亚天际海景度假酒店',
    location: '海棠湾',
    city: '三亚',
    address: '海南省三亚市海棠湾',
    rating: 4.9,
    reviewCount: 2400,
    price: 1899,
    images: ['https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80'],
    description: '独拥两公里私人沙滩，2026年全新装修，配备AI全屋智能控制系统，让您的海滨度假充满科技感与舒适度。',
    facilities: ['免费WiFi', '游泳池', '健身房', '餐厅', '停车场', 'SPA'],
    starLevel: 5,
    tags: ['海景房', '私人沙滩', '亲子友好', '蜜月推荐']
  },
  {
    id: '2',
    name: '外滩W·未来幻影酒店',
    location: '外滩',
    city: '上海',
    address: '上海市黄浦区外滩',
    rating: 4.8,
    reviewCount: 3150,
    price: 2450,
    images: ['https://modao.cc/agent-py/media/generated_images/2026-03-01/5194616f91ac4327b106ec84dbe1e4a1.jpg'],
    description: '270度环幕江景房，直击外滩经典，配备米其林三星云端餐厅，是商务精英与潮流达人的不二之选。',
    facilities: ['免费WiFi', '行政酒廊', '米其林餐厅', '会议室', '代客泊车'],
    starLevel: 5,
    tags: ['江景房', '商务出行', '米其林餐厅', '设计酒店']
  },
  {
    id: '3',
    name: '丽江古城精品客栈',
    location: '古城区',
    city: '丽江',
    address: '云南省丽江市古城区',
    rating: 4.7,
    reviewCount: 1800,
    price: 680,
    images: ['https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=1200&q=80'],
    description: '坐落于丽江古城核心区域，纳西族传统建筑风格，每间房都能看到玉龙雪山，体验原汁原味的古城生活。',
    facilities: ['免费WiFi', '茶室', '庭院', '早餐', '行李寄存'],
    starLevel: 4,
    tags: ['古城民宿', '雪山景观', '文艺清新', '摄影推荐']
  }
])

const handleSearch = async () => {
  try {
    console.log('开始搜索酒店...')
    const params = {
      destination: searchParams.destination,
      checkIn: dateRange.value?.[0]?.toISOString().split('T')[0],
      checkOut: dateRange.value?.[1]?.toISOString().split('T')[0],
      guests: searchParams.guests,
      minPrice: priceRange.value[0],
      maxPrice: priceRange.value[1],
      starLevel: selectedStars.value,
      page: currentPage.value,
      pageSize: pageSize.value
    }
    
    console.log('搜索参数:', params)
    const res = await searchHotels(params)
    console.log('搜索结果:', res)
    hotels.value = res.data.list
    total.value = res.data.total
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

const bookHotel = (hotel: Hotel) => {
  if (!dateRange.value) {
    ElMessage.warning('请先选择入住和退房日期')
    return
  }
  router.push({
    name: 'HotelDetail',
    params: { id: hotel.id },
    query: {
      checkIn: dateRange.value[0].toISOString().split('T')[0],
      checkOut: dateRange.value[1].toISOString().split('T')[0],
      guests: searchParams.guests
    }
  })
}

const viewDetail = (id: string) => {
  router.push({ name: 'HotelDetail', params: { id } })
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  handleSearch()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  handleSearch()
}

onMounted(() => {
  // 初始化日期为今天和明天
  const today = new Date()
  const tomorrow = new Date(today)
  tomorrow.setDate(tomorrow.getDate() + 1)
  dateRange.value = [today, tomorrow]
  
  // 页面加载时自动获取热门酒店
  console.log('页面已加载，准备获取热门酒店...')
  handleSearch()
})
</script>
