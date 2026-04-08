<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <!-- 头部 -->
    <div class="bg-gradient-to-r from-red-600 to-pink-500 rounded-2xl shadow-lg p-8 mb-8 text-white">
      <h1 class="text-3xl font-bold mb-6 flex items-center">
        <span class="iconify mr-3" data-icon="material-symbols:map"></span>
        当地攻略
      </h1>
      
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <el-input
          v-model="searchParams.keyword"
          placeholder="搜索攻略关键词"
          size="large"
          clearable
        >
          <template #prefix>
            <span class="iconify" data-icon="material-symbols:search"></span>
          </template>
        </el-input>
        
        <el-input
          v-model="searchParams.destination"
          placeholder="目的地"
          size="large"
          clearable
        >
          <template #prefix>
            <span class="iconify" data-icon="material-symbols:location-on"></span>
          </template>
        </el-input>
        
        <el-button type="primary" size="large" @click="handleSearch" class="bg-white text-red-600 hover:bg-gray-100">
          <span class="iconify mr-2" data-icon="material-symbols:search"></span>
          搜索攻略
        </el-button>
      </div>
    </div>

    <!-- 标签筛选 -->
    <div class="mb-8">
      <h3 class="text-lg font-bold mb-4">热门标签</h3>
      <div class="flex flex-wrap gap-3">
        <el-tag
          v-for="tag in popularTags"
          :key="tag"
          :type="selectedTag === tag ? 'danger' : 'info'"
          size="large"
          class="cursor-pointer"
          @click="selectTag(tag)"
        >
          {{ tag }}
        </el-tag>
      </div>
    </div>

    <!-- 排序 -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold">精选攻略</h2>
      <el-select v-model="sortBy" placeholder="排序方式" size="large" style="width: 150px">
        <el-option label="最新发布" value="latest"></el-option>
        <el-option label="最热门" value="popular"></el-option>
        <el-option label="最多点赞" value="hot"></el-option>
      </el-select>
    </div>

    <!-- 攻略列表 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="guide in guides"
        :key="guide.id"
        class="bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-lg transition cursor-pointer"
        @click="viewDetail(guide.id)"
      >
        <div class="relative h-56 overflow-hidden">
          <img
            :src="guide.images[0]"
            :alt="guide.title"
            class="w-full h-full object-cover hover:scale-110 transition duration-500"
          />
          <div class="absolute top-4 left-4 bg-red-500 text-white px-3 py-1 rounded-full text-sm font-bold">
            {{ guide.destination }}
          </div>
        </div>
        
        <div class="p-6">
          <h3 class="text-xl font-bold mb-3 line-clamp-2">{{ guide.title }}</h3>
          
          <div class="flex items-center gap-3 mb-4">
            <img
              :src="guide.author.avatar"
              :alt="guide.author.name"
              class="w-10 h-10 rounded-full"
            />
            <div class="flex-1">
              <div class="font-medium">{{ guide.author.name }}</div>
              <div class="text-xs text-gray-400">{{ formatDate(guide.createdAt) }}</div>
            </div>
          </div>
          
          <div class="flex flex-wrap gap-2 mb-4">
            <span
              v-for="tag in guide.tags.slice(0, 3)"
              :key="tag"
              class="px-2 py-1 bg-red-50 text-red-600 rounded text-xs"
            >
              {{ tag }}
            </span>
          </div>
          
          <div class="flex items-center justify-between text-sm text-gray-500 pt-4 border-t">
            <div class="flex items-center gap-4">
              <span class="flex items-center">
                <span class="iconify mr-1" data-icon="material-symbols:visibility"></span>
                {{ guide.viewCount }}
              </span>
              <span class="flex items-center">
                <span class="iconify mr-1" data-icon="material-symbols:thumb-up"></span>
                {{ guide.likeCount }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="flex justify-center mt-8">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { searchGuides } from '@/api/modules/guide'
import type { Guide } from '@/types'

const router = useRouter()

const searchParams = reactive({
  keyword: '',
  destination: ''
})

const sortBy = ref('latest')
const selectedTag = ref('')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(36)

const popularTags = ['美食', '摄影', '亲子游', '自驾游', '穷游', '蜜月', '古镇', '海岛', '徒步', '滑雪']

const guides = ref<Guide[]>([])

const handleSearch = async () => {
  try {
    console.log('开始搜索攻略...')
    const params = {
      keyword: searchParams.keyword,
      destination: searchParams.destination,
      tag: selectedTag.value,
      sortBy: sortBy.value,
      page: currentPage.value,
      pageSize: pageSize.value
    }
    console.log('搜索参数:', params)
    const res = await searchGuides(params)
    console.log('搜索结果:', res)
    // res.data 可能是 List 或 PageResponse，兼容两种格式
    if (Array.isArray(res.data)) {
      guides.value = res.data as Guide[]
      total.value = res.data.length
    } else {
      guides.value = (res.data?.list ?? []) as Guide[]
      total.value = res.data?.total ?? guides.value.length
    }
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

const selectTag = (tag: string) => {
  selectedTag.value = selectedTag.value === tag ? '' : tag
  handleSearch()
}

const viewDetail = (id: string) => {
  router.push({ name: 'GuideDetail', params: { id } })
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  handleSearch()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const formatDate = (date: string) => {
  const d = new Date(date)
  const now = new Date()
  const diff = now.getTime() - d.getTime()
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  
  if (days === 0) return '今天'
  if (days === 1) return '昨天'
  if (days < 7) return `${days}天前`
  return d.toLocaleDateString('zh-CN')
}

onMounted(() => {
  console.log('攻略页面已加载，准备获取数据...')
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
