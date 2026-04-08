<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <div class="flex items-center justify-between mb-8">
      <h1 class="text-3xl font-bold">我的行程规划</h1>
      <el-button type="primary" @click="router.push('/planning')">
        <span class="iconify mr-1" data-icon="material-symbols:add"></span>
        新建规划
      </el-button>
    </div>

    <!-- 标签页 -->
    <el-tabs v-model="activeTab" @tab-change="fetchTrips">
      <el-tab-pane label="全部行程" name="all" />
      <el-tab-pane label="即将出发" name="upcoming" />
      <el-tab-pane label="已完成" name="completed" />
    </el-tabs>

    <!-- 加载中 -->
    <div v-if="loading" class="flex justify-center py-20">
      <el-icon class="animate-spin text-4xl text-blue-500"><Loading /></el-icon>
    </div>

    <!-- 行程卡片列表 -->
    <div v-else-if="filteredTrips.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-6">
      <div
        v-for="trip in filteredTrips"
        :key="trip.id"
        class="bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-lg transition-shadow"
      >
        <div class="p-6">
          <!-- 标题行 -->
          <div class="flex items-start justify-between mb-4">
            <div class="flex-1 min-w-0 pr-2">
              <h3 class="text-lg font-bold truncate mb-1">{{ trip.title }}</h3>
              <div class="flex items-center text-gray-500 text-sm mb-1">
                <span class="iconify mr-1 shrink-0" data-icon="material-symbols:location-on"></span>
                <span class="truncate">
                  {{ trip.origin ? `${trip.origin} → ` : '' }}{{ trip.destination }}
                </span>
              </div>
              <div v-if="trip.startDate" class="flex items-center text-gray-500 text-sm">
                <span class="iconify mr-1 shrink-0" data-icon="material-symbols:calendar-today"></span>
                {{ formatDate(trip.startDate) }}
                <span v-if="trip.days"> · {{ trip.days }} 天</span>
              </div>
            </div>
            <el-tag :type="statusOf(trip).type" size="small" class="shrink-0">
              {{ statusOf(trip).text }}
            </el-tag>
          </div>

          <!-- 预算 -->
          <div v-if="trip.budget" class="flex items-center justify-between text-sm mb-2">
            <span class="text-gray-500">预算</span>
            <span class="font-semibold text-orange-500">¥{{ trip.budget.toLocaleString() }}</span>
          </div>

          <!-- 兴趣偏好 -->
          <div v-if="trip.interests" class="flex flex-wrap gap-1 mb-3">
            <el-tag
              v-for="tag in trip.interests.split(',')"
              :key="tag"
              size="small"
              type="info"
              class="!text-xs"
            >{{ tag }}</el-tag>
          </div>

          <!-- 操作按钮 -->
          <div class="flex gap-2 mt-2">
            <el-button type="primary" size="small" class="flex-1" @click="openDetail(trip)">
              查看详情
            </el-button>
            <el-button size="small" @click="editTrip(trip)">编辑</el-button>
            <el-button size="small" type="danger" @click="confirmDelete(trip)">删除</el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else class="text-center py-24">
      <span class="iconify text-6xl text-gray-300 block mx-auto mb-4" data-icon="material-symbols:travel-explore"></span>
      <p class="text-gray-400 mb-6">还没有行程规划，快去创建一个吧</p>
      <el-button type="primary" @click="router.push('/planning')">立即规划</el-button>
    </div>

    <!-- 分页 -->
    <div v-if="total > pageSize" class="flex justify-center mt-10">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="fetchTrips"
      />
    </div>

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailVisible"
      :title="currentTrip?.title"
      width="780px"
      top="5vh"
      destroy-on-close
    >
      <div v-if="currentTrip" class="mb-2 flex flex-wrap gap-4 text-sm text-gray-500">
        <span v-if="currentTrip.origin || currentTrip.destination">
          <span class="iconify mr-1" data-icon="material-symbols:location-on"></span>
          {{ currentTrip.origin ? `${currentTrip.origin} → ` : '' }}{{ currentTrip.destination }}
        </span>
        <span v-if="currentTrip.startDate">
          <span class="iconify mr-1" data-icon="material-symbols:calendar-today"></span>
          {{ formatDate(currentTrip.startDate) }}
          <span v-if="currentTrip.days"> · {{ currentTrip.days }} 天</span>
        </span>
        <span v-if="currentTrip.budget">
          <span class="iconify mr-1" data-icon="material-symbols:payments"></span>
          预算 ¥{{ currentTrip.budget.toLocaleString() }}
        </span>
      </div>
      <div v-if="currentTrip.interests" class="flex flex-wrap gap-1 mb-1">
        <el-tag
          v-for="tag in currentTrip.interests.split(',')"
          :key="tag"
          size="small"
          type="info"
        >{{ tag }}</el-tag>
      </div>
      <el-divider class="my-3" />
      <!-- Markdown 渲染 -->
      <div
        class="markdown-body max-h-[60vh] overflow-y-auto pr-1"
        v-html="renderedDetail"
      />
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="primary" @click="editTrip(currentTrip!)">重新规划</el-button>
      </template>
    </el-dialog>
  </main>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Loading } from '@element-plus/icons-vue'
import { marked } from 'marked'
import type { TripPlan } from '@/types'
import { getMyTrips, deleteTrip as deleteTripApi } from '@/api/modules/user'

const router = useRouter()

// ── 状态 ──────────────────────────────────────────
const loading = ref(false)
const trips = ref<TripPlan[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = 12

const activeTab = ref<'all' | 'upcoming' | 'completed'>('all')

const detailVisible = ref(false)
const currentTrip = ref<TripPlan | null>(null)

// ── 计算 ──────────────────────────────────────────
const filteredTrips = computed(() => {
  const now = new Date()
  if (activeTab.value === 'upcoming') {
    return trips.value.filter(t => t.startDate && new Date(t.startDate) > now)
  }
  if (activeTab.value === 'completed') {
    return trips.value.filter(t => t.startDate && new Date(t.startDate) <= now)
  }
  return trips.value
})

const renderedDetail = computed(() => {
  if (!currentTrip.value?.planDetails) return '<p class="text-gray-400">暂无详情内容</p>'
  return marked.parse(currentTrip.value.planDetails) as string
})

// ── 数据加载 ──────────────────────────────────────
async function fetchTrips() {
  loading.value = true
  try {
    const res = await getMyTrips({ page: page.value, pageSize })
    const data = res.data?.data ?? res.data
    trips.value = data?.list ?? []
    total.value = data?.total ?? 0
  } catch (e: any) {
    ElMessage.error('加载历史规划失败：' + (e?.message ?? '未知错误'))
  } finally {
    loading.value = false
  }
}

onMounted(fetchTrips)

// ── 操作 ──────────────────────────────────────────
function openDetail(trip: TripPlan) {
  currentTrip.value = trip
  detailVisible.value = true
}

function editTrip(trip: TripPlan) {
  detailVisible.value = false
  const query: Record<string, string> = { edit: '1' }
  if (trip.origin) query.origin = trip.origin
  if (trip.destination) query.destination = trip.destination
  if (trip.days) query.days = String(trip.days)
  if (trip.budget) query.budget = String(trip.budget)
  if (trip.startDate) query.startDate = trip.startDate
  if (trip.interests) query.interests = trip.interests
  router.push({ path: '/planning', query })
}

async function confirmDelete(trip: TripPlan) {
  try {
    await ElMessageBox.confirm(`确定要删除「${trip.title}」吗？`, '删除确认', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning',
      confirmButtonClass: 'el-button--danger',
    })
    await deleteTripApi(trip.id)
    trips.value = trips.value.filter(t => t.id !== trip.id)
    total.value = Math.max(0, total.value - 1)
    ElMessage.success('已删除')
  } catch {
    // 用户取消
  }
}

// ── 工具函数 ──────────────────────────────────────
function formatDate(date: string) {
  return new Date(date).toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' })
}

function statusOf(trip: TripPlan): { text: string; type: '' | 'success' | 'info' | 'warning' | 'danger' } {
  if (!trip.startDate) return { text: '已保存', type: 'info' }
  const now = new Date()
  const start = new Date(trip.startDate)
  return start > now
    ? { text: '即将出发', type: 'success' }
    : { text: '已完成', type: '' }
}
</script>

<style scoped>
/* Markdown 渲染样式（与 Planning.vue 保持一致） */
.markdown-body :deep(h1),
.markdown-body :deep(h2),
.markdown-body :deep(h3) {
  font-weight: bold;
  margin: 0.8em 0 0.4em;
  line-height: 1.4;
}
.markdown-body :deep(h1) { font-size: 1.4em; }
.markdown-body :deep(h2) { font-size: 1.2em; }
.markdown-body :deep(h3) { font-size: 1.05em; }
.markdown-body :deep(p) {
  margin: 0.4em 0;
  line-height: 1.7;
}
.markdown-body :deep(ul),
.markdown-body :deep(ol) {
  padding-left: 1.4em;
  margin: 0.4em 0;
}
.markdown-body :deep(li) { margin: 0.2em 0; line-height: 1.6; }
.markdown-body :deep(strong) { font-weight: 700; }
.markdown-body :deep(blockquote) {
  border-left: 3px solid #e5e7eb;
  padding-left: 1em;
  color: #6b7280;
  margin: 0.5em 0;
}
.markdown-body :deep(code) {
  background: #f3f4f6;
  padding: 0.1em 0.4em;
  border-radius: 3px;
  font-size: 0.9em;
}
.markdown-body :deep(hr) {
  border: none;
  border-top: 1px solid #e5e7eb;
  margin: 1em 0;
}
</style>
