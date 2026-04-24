<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <section class="mb-8 rounded-[32px] bg-gradient-to-r from-sky-600 via-blue-600 to-indigo-700 p-8 text-white shadow-lg">
      <div class="max-w-3xl">
        <p class="mb-3 text-xs font-semibold uppercase tracking-[0.32em] text-blue-100">Destination Atlas</p>
        <h1 class="mb-4 text-4xl font-bold">探索目的地</h1>
        <p class="mb-6 text-lg leading-8 text-blue-100/90">
          先看热力，再决定去哪。地图上的热点可直接点击跳转，下面的列表则帮助你继续筛选适合自己的目的地。
        </p>
      </div>

      <div class="max-w-2xl rounded-3xl border border-white/15 bg-white/10 p-4 backdrop-blur">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索目的地，例如：北京、三亚、厦门"
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
    </section>

    <DestinationHeatmap
      :points="heatmapPoints"
      :loading="heatmapLoading"
      @select="handleHeatmapSelect"
    />

    <section id="destination-results" class="mb-12 scroll-mt-24">
      <div class="mb-6 flex flex-col gap-4 md:flex-row md:items-end md:justify-between">
        <div>
          <h2 class="text-2xl font-bold text-slate-900">热门目的地</h2>
          <p class="mt-2 text-sm text-slate-500">按站内真实热力聚合去重后排序，热力值越高越靠前。</p>
        </div>
        <button
          type="button"
          class="inline-flex items-center rounded-2xl border border-blue-200 bg-white px-4 py-2 text-sm font-semibold text-blue-600 transition hover:bg-blue-50"
          @click="scrollToHeatmap"
        >
          返回热力图
        </button>
      </div>

      <div v-if="popularDestinations.length" class="grid grid-cols-1 gap-6 md:grid-cols-2 xl:grid-cols-4">
        <div
          v-for="destination in popularDestinations"
          :key="destination.id"
          class="group relative h-80 cursor-pointer overflow-hidden rounded-3xl shadow-lg"
          @click="viewDetail(destination.id)"
        >
          <img
            :src="getDestinationImage(destination.name, destination.image)"
            :alt="destination.name"
            class="h-full w-full object-cover transition duration-700 group-hover:scale-110"
          />
          <div class="absolute inset-0 bg-gradient-to-t from-slate-950/85 via-slate-950/15 to-transparent"></div>
          <div class="absolute inset-x-0 bottom-0 p-6 text-white">
            <p class="text-xs uppercase tracking-[0.24em] text-white/70">{{ destination.country }}</p>
            <h3 class="mt-2 text-2xl font-bold">{{ stripSerialSuffix(destination.name) }}</h3>
            <p class="mt-2 text-sm font-medium text-cyan-200">
              真实热力 TOP {{ destination.heatRank || '-' }}
              <span class="ml-2 text-white/80">热力值 {{ formatHeatCount(destination.heatCount) }}</span>
            </p>
            <p class="mt-2 line-clamp-2 text-sm text-white/80">{{ getPopularSummary(destination) }}</p>
            <div class="mt-4 flex flex-wrap gap-2">
              <span
                v-for="tag in (destination.tags || []).slice(0, 3)"
                :key="tag"
                class="rounded-full bg-white/15 px-3 py-1 text-xs backdrop-blur"
              >
                {{ tag }}
              </span>
            </div>
          </div>
          <div class="absolute right-4 top-4 rounded-full bg-orange-500 px-3 py-1 text-sm font-bold text-white">
            {{ destination.heatRank ? `TOP ${destination.heatRank}` : '热门' }}
          </div>
        </div>
      </div>
    </section>

    <section class="mb-12">
      <div class="mb-6">
        <h2 class="text-2xl font-bold text-slate-900">按地区浏览</h2>
        <p class="mt-2 text-sm text-slate-500">切换地区后，会自动刷新下方目的地卡片。</p>
      </div>

      <el-tabs v-model="activeRegion" @tab-change="handleRegionChange">
        <el-tab-pane label="国内" name="domestic"></el-tab-pane>
        <el-tab-pane label="亚洲" name="asia"></el-tab-pane>
        <el-tab-pane label="欧洲" name="europe"></el-tab-pane>
        <el-tab-pane label="美洲" name="america"></el-tab-pane>
        <el-tab-pane label="其他" name="other"></el-tab-pane>
      </el-tabs>

      <div v-if="regionDestinations.length" class="mt-6 grid grid-cols-2 gap-4 md:grid-cols-4 xl:grid-cols-6">
        <button
          v-for="dest in regionDestinations"
          :key="dest.id"
          type="button"
          class="rounded-3xl border border-gray-200 bg-white p-5 text-center shadow-sm transition hover:-translate-y-1 hover:shadow-md"
          @click="viewDetail(dest.id)"
        >
          <div class="mb-3 text-4xl">{{ getDestinationEmoji(dest.name) }}</div>
          <div class="font-bold text-slate-900">{{ stripSerialSuffix(dest.name) }}</div>
          <div class="mt-1 text-xs text-slate-500">{{ dest.country }}</div>
        </button>
      </div>
    </section>

    <section>
      <div class="mb-6 flex flex-col gap-2 md:flex-row md:items-end md:justify-between">
        <div>
          <h2 class="text-2xl font-bold text-slate-900">当季推荐</h2>
          <p class="mt-2 text-sm text-slate-500">根据当前检索结果，优先展示更适合延展行程的目的地。</p>
        </div>
        <p class="text-sm text-slate-400">{{ seasonalDestinations.length }} 个候选目的地</p>
      </div>

      <div v-if="seasonalDestinations.length" class="grid grid-cols-1 gap-6 xl:grid-cols-2">
        <div
          v-for="destination in seasonalDestinations"
          :key="destination.id"
          class="flex cursor-pointer overflow-hidden rounded-3xl border border-gray-200 bg-white shadow-sm transition hover:-translate-y-1 hover:shadow-md"
          @click="viewDetail(destination.id)"
        >
          <div class="h-52 w-48 flex-shrink-0 overflow-hidden">
            <img
              :src="getDestinationImage(destination.name, destination.image)"
              :alt="destination.name"
              class="h-full w-full object-cover transition duration-500 hover:scale-110"
            />
          </div>
          <div class="flex-1 p-6">
            <h3 class="text-2xl font-bold text-slate-900">{{ stripSerialSuffix(destination.name) }}</h3>
            <p class="mt-3 text-sm leading-7 text-slate-600">{{ destination.description }}</p>
            <div class="mt-4 flex items-center text-sm text-slate-500">
              <span class="iconify mr-2 text-green-600" data-icon="material-symbols:wb-sunny-outline"></span>
              最佳季节：{{ getBestSeason(destination) }}
            </div>
            <div class="mt-4 flex flex-wrap gap-2">
              <span
                v-for="tag in destination.tags || []"
                :key="tag"
                class="rounded-full bg-blue-50 px-3 py-1 text-xs font-medium text-blue-600"
              >
                {{ tag }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-else description="没有找到匹配的目的地，请换个关键词试试" />
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import DestinationHeatmap from '@/components/destination/DestinationHeatmap.vue'
import { getDestinationHeatmap, getPopularDestinations, searchDestinations } from '@/api/modules/destination'
import type { Destination, DestinationHeatmapPoint } from '@/types'
import { getDestinationImage, getDestinationKey } from '@/utils/destinationImages'

const router = useRouter()

const searchKeyword = ref('')
const activeRegion = ref('domestic')

const popularDestinations = ref<Destination[]>([])
const regionDestinations = ref<Destination[]>([])
const seasonalDestinations = ref<Destination[]>([])
const heatmapPoints = ref<DestinationHeatmapPoint[]>([])
const heatmapLoading = ref(false)
const numberFormatter = new Intl.NumberFormat('zh-CN')

const destinationEmojiMap: Record<string, string> = {
  北京: '🏯',
  上海: '🌃',
  杭州: '🌿',
  成都: '🐼',
  西安: '🧱',
  大理: '🏞️',
  丽江: '🏔️',
  三亚: '🏝️',
  重庆: '🌉',
  厦门: '🌊',
  广州: '🛍️',
  深圳: '🚀'
}

const stripSerialSuffix = (name: string) => name.replace(/·\d+$/, '')
const getDestinationEmoji = (name: string) => destinationEmojiMap[getDestinationKey(name)] || '🧭'
const getBestSeason = (destination: Destination) => destination.bestSeason?.length ? destination.bestSeason.join('、') : '四季皆宜'
const formatHeatCount = (count?: number) => typeof count === 'number' ? numberFormatter.format(count) : '--'
const getPopularSummary = (destination: Destination) => {
  if (destination.heatRank && destination.heatCount) {
    return `当前榜单第 ${destination.heatRank} 名，基于站内真实热力聚合排序。`
  }
  return destination.description
}

const loadPopularDestinations = async () => {
  try {
    const res = await getPopularDestinations(4)
    popularDestinations.value = res.data
  } catch (error) {
    ElMessage.error('热门目的地加载失败')
  }
}

const handleSearch = async () => {
  try {
    const res = await searchDestinations({
      keyword: searchKeyword.value.trim(),
      region: activeRegion.value
    })

    regionDestinations.value = res.data.slice(0, 6)
    seasonalDestinations.value = res.data.slice(0, 4)
  } catch (error) {
    regionDestinations.value = []
    seasonalDestinations.value = []
    ElMessage.error('目的地搜索失败，请稍后重试')
  }
}

const loadHeatmap = async () => {
  heatmapLoading.value = true
  try {
    const res = await getDestinationHeatmap()
    heatmapPoints.value = res.data
  } catch (error) {
    heatmapPoints.value = []
    ElMessage.error('热力图加载失败，请稍后重试')
  } finally {
    heatmapLoading.value = false
  }
}

const handleRegionChange = () => {
  void handleSearch()
}

const viewDetail = (id: string | number) => {
  router.push({ name: 'DestinationDetail', params: { id } })
}

const handleHeatmapSelect = (point: DestinationHeatmapPoint) => {
  viewDetail(point.id)
}

const scrollToHeatmap = () => {
  document.getElementById('heatmap')?.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

onMounted(() => {
  void Promise.all([
    loadPopularDestinations(),
    handleSearch(),
    loadHeatmap()
  ])
})
</script>
