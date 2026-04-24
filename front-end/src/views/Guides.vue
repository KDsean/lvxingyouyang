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
            :src="getGuideCover(guide)"
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
              :src="getGuideAuthor(guide).avatar"
              :alt="getGuideAuthor(guide).name"
              class="w-10 h-10 rounded-full"
            />
            <div class="flex-1">
              <div class="font-medium">{{ getGuideAuthor(guide).name }}</div>
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
import { searchGuides } from '@/api/modules/guide'
import type { Guide } from '@/types'
import { getGuideAuthor, getGuideImage } from '@/utils/guideDisplay'

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

const sampleGuides: Guide[] = [
  {
    id: 'sample-dali-3days',
    title: '大理洱海 3 天 2 夜慢旅行：骑行、古城和苍山日落',
    destination: '大理',
    author: { id: 'u101', name: '林小夏', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Lin' },
    content: '适合第一次去大理的轻松路线，覆盖洱海生态廊道、喜洲古镇、双廊和苍山。',
    images: ['https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/4/44/Dali_Yunnan_China_Ornamented-Door-01.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['自驾游', '古镇', '摄影'],
    viewCount: 12840,
    likeCount: 936,
    createdAt: '2026-04-20T09:30:00'
  },
  {
    id: 'sample-sanya-family',
    title: '三亚亲子海岛攻略：亚龙湾、蜈支洲岛和免税店安排',
    destination: '三亚',
    author: { id: 'u102', name: '海风旅行记', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Sea' },
    content: '把海滩、亲子酒店、海鲜市场和免税购物串成 4 天行程，节奏不赶。',
    images: ['https://upload.wikimedia.org/wikipedia/commons/9/96/Beach_of_Sanya.jpg'],
    tags: ['亲子游', '海岛', '美食'],
    viewCount: 18320,
    likeCount: 1240,
    createdAt: '2026-04-18T14:15:00'
  },
  {
    id: 'sample-beijing-history',
    title: '北京历史文化两日线：故宫、景山、胡同和长城',
    destination: '北京',
    author: { id: 'u103', name: '阿舟', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Zhou' },
    content: '用周末时间看懂北京中轴线，第二天安排八达岭长城或慕田峪长城。',
    images: ['https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F0%2F00%2FSunset_of_the_Forbidden_City_2006.JPG&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['历史文化', '摄影', '徒步'],
    viewCount: 22190,
    likeCount: 1588,
    createdAt: '2026-04-15T10:00:00'
  },
  {
    id: 'sample-chengdu-food',
    title: '成都美食 Citywalk：火锅、茶馆、熊猫基地一条线',
    destination: '成都',
    author: { id: 'u104', name: '椒盐地图', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Food' },
    content: '从春熙路到宽窄巷子，再去玉林和望平街，适合边吃边逛。',
    images: ['https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/5/54/Chengdu-pandas-d10.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['美食', '摄影', '穷游'],
    viewCount: 17650,
    likeCount: 1166,
    createdAt: '2026-04-12T19:20:00'
  },
  {
    id: 'sample-hangzhou-weekend',
    title: '杭州周末不踩雷：西湖晨游、灵隐寺和龙井村',
    destination: '杭州',
    author: { id: 'u105', name: '南风', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Nan' },
    content: '适合周五晚到杭州，周日返程的两天路线，避开西湖高峰人流。',
    images: ['https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/0/07/20090524_Hangzhou_West_Lake_7531.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['徒步', '摄影', '古镇'],
    viewCount: 15430,
    likeCount: 988,
    createdAt: '2026-04-10T08:45:00'
  },
  {
    id: 'sample-lijiang-snow',
    title: '丽江古城到玉龙雪山：高反准备、索道和拍照点',
    destination: '丽江',
    author: { id: 'u106', name: '云上阿宁', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Yun' },
    content: '整理雪山门票、蓝月谷、印象丽江和古城住宿的完整安排。',
    images: ['https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2Ff%2Ffb%2FLijiang_Yunnan_Old-town-01.jpg%2F1280px-Lijiang_Yunnan_Old-town-01.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['蜜月', '摄影', '古镇'],
    viewCount: 14380,
    likeCount: 875,
    createdAt: '2026-04-08T16:10:00'
  },
  {
    id: 'sample-xian-history',
    title: '西安 48 小时历史线：兵马俑、城墙和回民街',
    destination: '西安',
    author: { id: 'u107', name: '长安客', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Changan' },
    content: '把博物馆、古城墙、钟鼓楼和夜市串成紧凑但不累的行程。',
    images: ['https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/3/38/Xian_China_Terracotta-Army-Museum-01.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['历史文化', '美食', '穷游'],
    viewCount: 16920,
    likeCount: 1042,
    createdAt: '2026-04-06T11:00:00'
  },
  {
    id: 'sample-xiamen-island',
    title: '厦门鼓浪屿一日游：船票、路线和沙坡尾夜景',
    destination: '厦门',
    author: { id: 'u108', name: '岛屿笔记', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Island' },
    content: '上午鼓浪屿看老建筑，下午中山路和沙坡尾，晚上吃海鲜。',
    images: ['https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/b/b6/Gulangyu_Island_from_Zhongshan_Road%2C_Xiamen.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['海岛', '摄影', '美食'],
    viewCount: 11960,
    likeCount: 722,
    createdAt: '2026-04-03T13:25:00'
  },
  {
    id: 'sample-harbin-ski',
    title: '哈尔滨冬季滑雪攻略：冰雪大世界、亚布力和穿搭清单',
    destination: '哈尔滨',
    author: { id: 'u109', name: '北境旅行', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Snow' },
    content: '适合第一次冬季去东北，包含交通、保暖、滑雪和夜景拍摄建议。',
    images: ['https://upload.wikimedia.org/wikipedia/commons/5/51/Harbin_Ice_and_Snow_Festival_2013.jpg'],
    tags: ['滑雪', '摄影', '亲子游'],
    viewCount: 10180,
    likeCount: 690,
    createdAt: '2026-03-30T18:40:00'
  }
]

type GuideWithFlatAuthor = Partial<Guide> & {
  authorName?: string
  authorAvatar?: string
}

const getGuideCover = (guide: Partial<Guide>) => {
  return getGuideImage(guide)
}

const normalizeGuide = (guide: GuideWithFlatAuthor): Guide => ({
  id: String(guide.id || ''),
  title: guide.title || '未命名攻略',
  destination: guide.destination || '目的地',
  author: getGuideAuthor(guide),
  content: guide.content || '',
  images: guide.images?.length ? guide.images : [getGuideImage(guide)],
  tags: guide.tags || [],
  viewCount: guide.viewCount || 0,
  likeCount: guide.likeCount || 0,
  createdAt: guide.createdAt || new Date().toISOString()
})

const getSampleGuides = () => {
  const keyword = searchParams.keyword.trim().toLowerCase()
  const destination = searchParams.destination.trim().toLowerCase()
  const tag = selectedTag.value

  let result = sampleGuides.filter(guide => {
    const searchableText = `${guide.title} ${guide.destination} ${guide.content} ${guide.tags.join(' ')}`.toLowerCase()
    const keywordMatched = !keyword || searchableText.includes(keyword)
    const destinationMatched = !destination || guide.destination.toLowerCase().includes(destination)
    const tagMatched = !tag || guide.tags.includes(tag)
    return keywordMatched && destinationMatched && tagMatched
  })

  if (sortBy.value === 'popular') {
    result = [...result].sort((a, b) => b.viewCount - a.viewCount)
  } else if (sortBy.value === 'hot') {
    result = [...result].sort((a, b) => b.likeCount - a.likeCount)
  } else {
    result = [...result].sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
  }

  const start = (currentPage.value - 1) * pageSize.value
  return {
    list: result.slice(start, start + pageSize.value),
    total: result.length
  }
}

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
      guides.value = res.data.map(item => normalizeGuide(item as GuideWithFlatAuthor))
      total.value = res.data.length
    } else {
      const payload = res.data as unknown as { list?: GuideWithFlatAuthor[]; total?: number }
      guides.value = (payload.list ?? []).map(item => normalizeGuide(item))
      total.value = payload.total ?? guides.value.length
    }
    if (guides.value.length === 0) {
      const samples = getSampleGuides()
      guides.value = samples.list
      total.value = samples.total
    }
  } catch (error) {
    console.error('搜索错误:', error)
    const samples = getSampleGuides()
    guides.value = samples.list
    total.value = samples.total
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
