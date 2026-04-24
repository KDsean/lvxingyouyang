<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <el-button @click="goBack" class="mb-6">
      <span class="iconify mr-2" data-icon="material-symbols:arrow-back"></span>
      返回
    </el-button>

    <div v-if="loading" class="rounded-[32px] border border-gray-200 bg-white p-8 shadow-sm">
      <el-skeleton :rows="10" animated />
    </div>

    <div v-else-if="destination" class="overflow-hidden rounded-[32px] border border-gray-200 bg-white shadow-sm">
      <div class="relative h-[360px] md:h-[440px]">
        <img
          :src="destinationImage"
          :alt="displayName"
          class="h-full w-full object-cover"
        />
        <div class="absolute inset-0 bg-gradient-to-t from-slate-950/80 via-slate-950/20 to-transparent"></div>
        <div class="absolute inset-x-0 bottom-0 p-8 text-white md:p-10">
          <span class="inline-flex items-center rounded-full bg-white/15 px-3 py-1 text-sm backdrop-blur">
            {{ destination.country || '中国' }}
          </span>
          <h1 class="mt-4 text-4xl font-bold md:text-5xl">{{ displayName }}</h1>
          <p class="mt-4 max-w-3xl text-sm leading-7 text-white/85 md:text-base">
            {{ destination.description }}
          </p>
        </div>
      </div>

      <div class="grid gap-8 p-8 lg:grid-cols-[minmax(0,1fr)_320px]">
        <div>
          <section class="mb-8">
            <h2 class="mb-4 text-2xl font-bold text-slate-900">目的地速览</h2>
            <div class="grid gap-4 md:grid-cols-3">
              <div
                v-for="fact in quickFacts"
                :key="fact.label"
                class="rounded-3xl border border-gray-200 bg-slate-50 p-5"
              >
                <span class="iconify text-2xl text-blue-600" :data-icon="fact.icon"></span>
                <p class="mt-3 text-sm text-slate-500">{{ fact.label }}</p>
                <p class="mt-2 text-lg font-semibold text-slate-900">{{ fact.value }}</p>
              </div>
            </div>
          </section>

          <section class="mb-8">
            <h2 class="mb-4 text-2xl font-bold text-slate-900">推荐亮点</h2>
            <div class="grid gap-4 md:grid-cols-2">
              <div
                v-for="tip in travelTips"
                :key="tip.title"
                class="rounded-3xl border border-gray-200 bg-white p-5 shadow-sm"
              >
                <h3 class="text-lg font-semibold text-slate-900">{{ tip.title }}</h3>
                <p class="mt-2 text-sm leading-7 text-slate-600">{{ tip.content }}</p>
              </div>
            </div>
          </section>

          <section>
            <h2 class="mb-4 text-2xl font-bold text-slate-900">目的地标签</h2>
            <div class="flex flex-wrap gap-3">
              <span
                v-for="tag in destination.tags || []"
                :key="tag"
                class="rounded-full bg-blue-50 px-4 py-2 text-sm font-medium text-blue-600"
              >
                {{ tag }}
              </span>
              <span
                v-if="!destination.tags?.length"
                class="rounded-full bg-slate-100 px-4 py-2 text-sm text-slate-500"
              >
                等你来解锁更多玩法
              </span>
            </div>
          </section>
        </div>

        <aside class="space-y-4">
          <div class="rounded-[28px] bg-slate-950 p-6 text-white">
            <p class="text-xs uppercase tracking-[0.24em] text-slate-400">互动入口</p>
            <h3 class="mt-3 text-2xl font-bold">继续探索 {{ displayName }}</h3>
            <p class="mt-3 text-sm leading-7 text-slate-300">
              从热力图回看整体趋势，或直接把这个目的地带入 AI 行程规划。
            </p>
            <div class="mt-6 space-y-3">
              <button
                type="button"
                class="w-full rounded-2xl bg-blue-600 px-4 py-3 font-semibold text-white transition hover:bg-blue-700"
                @click="goToHeatmap"
              >
                返回热力图
              </button>
              <button
                type="button"
                class="w-full rounded-2xl border border-white/15 px-4 py-3 font-semibold text-white transition hover:bg-white/5"
                @click="goToPlanning"
              >
                用 AI 规划行程
              </button>
            </div>
          </div>

          <div class="rounded-[28px] border border-gray-200 bg-slate-50 p-6">
            <h3 class="text-lg font-semibold text-slate-900">最佳出行季节</h3>
            <div class="mt-4 flex flex-wrap gap-2">
              <span
                v-for="season in seasonTags"
                :key="season"
                class="rounded-full bg-white px-3 py-1 text-sm text-slate-700 ring-1 ring-gray-200"
              >
                {{ season }}
              </span>
            </div>
          </div>
        </aside>
      </div>
    </div>

    <el-empty v-else description="未找到对应目的地" />
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getDestinationDetail } from '@/api/modules/destination'
import type { Destination } from '@/types'
import { getDestinationImage } from '@/utils/destinationImages'

const route = useRoute()
const router = useRouter()

const destination = ref<Destination | null>(null)
const loading = ref(false)

const stripSerialSuffix = (name: string) => name.replace(/·\d+$/, '')

const displayName = computed(() => stripSerialSuffix(destination.value?.name ?? '目的地'))
const destinationImage = computed(() => getDestinationImage(destination.value?.name, destination.value?.image))
const seasonTags = computed(() => destination.value?.bestSeason?.length ? destination.value.bestSeason : ['四季皆宜'])

const quickFacts = computed(() => {
  if (!destination.value) {
    return []
  }

  return [
    {
      label: '国家/地区',
      value: destination.value.country || '中国',
      icon: 'material-symbols:travel'
    },
    {
      label: '热度评分',
      value: `${(destination.value.popularityScore ?? 6).toFixed(1)} / 10`,
      icon: 'material-symbols:trending-up'
    },
    {
      label: '最佳季节',
      value: seasonTags.value.join('、'),
      icon: 'material-symbols:wb-sunny-outline'
    }
  ]
})

const travelTips = computed(() => {
  if (!destination.value) {
    return []
  }

  return [
    {
      title: '适合这样玩',
      content: `${displayName.value}适合安排 2 到 4 天慢节奏探索，把白天留给地标打卡，晚上留给城市氛围与在地美食。`
    },
    {
      title: '行程建议',
      content: `如果你喜欢稳妥路线，可以围绕“核心景点 + 城市街区 + 在地体验”展开，把高热度景点放在第一天。`
    },
    {
      title: '出行提醒',
      content: `高热度目的地在旺季波动更明显，建议优先锁定机酒，再把想去的标签景点补进日程。`
    },
    {
      title: '推荐主题',
      content: destination.value.tags?.length
        ? `这座目的地尤其适合 ${destination.value.tags.join('、')} 等主题玩法。`
        : '这座目的地适合城市漫游、拍照打卡和轻松度假。'
    }
  ]
})

const loadDetail = async () => {
  const id = String(route.params.id ?? '')
  if (!id) {
    destination.value = null
    return
  }

  loading.value = true
  try {
    const res = await getDestinationDetail(id)
    destination.value = res.data
  } catch (error) {
    destination.value = null
    ElMessage.error('目的地详情加载失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

const goToHeatmap = () => {
  router.push({ name: 'Destinations', hash: '#heatmap' })
}

const goToPlanning = () => {
  router.push({ name: 'Planning', query: { q: displayName.value } })
}

watch(() => route.params.id, () => {
  void loadDetail()
})

onMounted(() => {
  void loadDetail()
})
</script>
