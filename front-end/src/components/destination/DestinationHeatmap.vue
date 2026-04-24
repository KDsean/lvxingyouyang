<template>
  <section id="heatmap" class="mb-12 scroll-mt-24">
    <div class="overflow-hidden rounded-[32px] bg-white shadow-sm">
      <div class="grid lg:grid-cols-[minmax(0,1fr)_340px]">
        <div class="relative min-h-[460px] overflow-hidden bg-[radial-gradient(circle_at_top,_rgba(59,130,246,0.16),_transparent_42%),linear-gradient(180deg,_#f9fbff_0%,_#eff5ff_100%)] lg:min-h-[560px]">
          <div
            ref="viewportRef"
            class="absolute inset-0 z-10 overflow-hidden touch-none"
            @pointerdown="handlePointerDown"
            @pointermove="handlePointerMove"
            @pointerup="handlePointerUp"
            @pointercancel="handlePointerUp"
            @pointerleave="handlePointerUp"
            @wheel.prevent="handleWheel"
          >
            <div
              class="absolute inset-0 origin-top-left select-none will-change-transform"
              :class="isDragging ? 'cursor-grabbing' : (scale > minScale ? 'cursor-grab transition-transform duration-200 ease-out' : 'cursor-default transition-transform duration-200 ease-out')"
              :style="canvasStyle"
            >
              <div class="absolute opacity-25" :style="mapFrameStyle">
                <img
                  ref="mapImageRef"
                  :src="worldEquirectangularMap"
                  alt="World map"
                  class="h-full w-full object-contain"
                  draggable="false"
                  @load="handleImageLoad"
                />
              </div>
            </div>
          </div>

          <div v-if="sortedPoints.length" class="pointer-events-none absolute inset-0 z-20">
            <button
              v-for="point in sortedPoints"
              :key="point.id"
              type="button"
              class="pointer-events-auto group absolute -translate-x-1/2 -translate-y-1/2 focus:outline-none"
              :style="{ ...getMarkerPositionStyle(point), ...getBubbleStyle(point) }"
              @pointerdown.stop
              @click="emit('select', point)"
            >
              <span class="absolute inset-0 rounded-full bg-blue-500/20 blur-md"></span>
              <span class="absolute inset-0 rounded-full bg-blue-400/30 animate-ping"></span>
              <span class="relative flex h-full w-full items-center justify-center rounded-full border border-white/85 bg-gradient-to-br from-blue-500 to-blue-700 text-white shadow-xl shadow-blue-500/35">
                <span class="iconify" :style="getMarkerIconStyle(point)" data-icon="material-symbols:location-on-rounded"></span>
              </span>
              <span class="pointer-events-none absolute left-1/2 top-[calc(100%+12px)] hidden -translate-x-1/2 whitespace-nowrap rounded-2xl bg-slate-950/90 px-3 py-2 text-left text-white shadow-2xl group-hover:block group-focus:block">
                <span class="block text-sm font-semibold">{{ point.destination }}</span>
                <span class="block text-xs text-slate-300">实时热度 {{ formatCount(point.count) }}</span>
              </span>
            </button>
          </div>

          <div class="absolute inset-0 z-20 bg-gradient-to-b from-white/55 via-white/20 to-white/75 pointer-events-none"></div>

          <div class="relative z-30 p-6 md:p-8">
            <div class="max-w-md rounded-3xl bg-white/80 p-5 shadow-lg ring-1 ring-white/70 backdrop-blur">
              <p class="mb-2 text-xs font-semibold uppercase tracking-[0.28em] text-blue-600">Interactive Heatmap</p>
              <h2 class="mb-3 text-3xl font-bold text-slate-900">实时热门目的地热力图</h2>
              <p class="text-sm leading-7 text-slate-600">
                支持滚轮缩放和拖拽平移。热点越大表示当前关注度越高，点击任意热区可直接进入详情页。
              </p>
            </div>
          </div>

          <div v-if="loading" class="absolute inset-0 z-40 flex items-center justify-center">
            <div class="rounded-full bg-white/90 px-5 py-3 text-sm font-medium text-slate-600 shadow-lg">
              正在加载地图热点...
            </div>
          </div>

          <div class="absolute right-5 top-5 z-30 flex flex-col gap-3">
            <div class="rounded-2xl bg-white/90 p-2 shadow-lg ring-1 ring-white/80 backdrop-blur">
              <div class="flex items-center gap-2">
                <button
                  type="button"
                  class="flex h-10 w-10 items-center justify-center rounded-xl border border-gray-200 text-slate-700 transition hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-40"
                  :disabled="scale <= minScale"
                  @click="zoomOut"
                >
                  <span class="iconify text-lg" data-icon="material-symbols:remove"></span>
                </button>
                <div class="min-w-[70px] text-center text-sm font-semibold text-slate-700">{{ zoomLabel }}</div>
                <button
                  type="button"
                  class="flex h-10 w-10 items-center justify-center rounded-xl border border-gray-200 text-slate-700 transition hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-40"
                  :disabled="scale >= maxScale"
                  @click="zoomIn"
                >
                  <span class="iconify text-lg" data-icon="material-symbols:add"></span>
                </button>
              </div>
              <div class="mt-2 grid grid-cols-2 gap-2">
                <button
                  type="button"
                  class="rounded-xl bg-slate-900 px-3 py-2 text-xs font-semibold text-white transition hover:bg-slate-800"
                  @click="resetView"
                >
                  重置
                </button>
                <button
                  type="button"
                  class="rounded-xl bg-blue-600 px-3 py-2 text-xs font-semibold text-white transition hover:bg-blue-700"
                  @click="focusEastAsia"
                >
                  聚焦东亚
                </button>
              </div>
            </div>

            <div class="rounded-2xl bg-slate-950/80 px-4 py-3 text-xs leading-6 text-white shadow-lg backdrop-blur">
              <div>滚轮：缩放</div>
              <div>拖拽：平移地图</div>
            </div>
          </div>

          <div v-if="!loading && !sortedPoints.length" class="absolute inset-0 z-40 flex items-center justify-center">
            <div class="rounded-3xl bg-white/85 px-6 py-5 text-center shadow-lg backdrop-blur">
              <p class="text-lg font-semibold text-slate-800">暂未获取到热力图数据</p>
              <p class="mt-2 text-sm text-slate-500">稍后重试，或先查看下方目的地推荐。</p>
            </div>
          </div>
        </div>

        <aside class="border-t border-gray-200 bg-slate-950 p-6 text-white lg:border-l lg:border-t-0 lg:p-8">
          <div class="mb-6 grid grid-cols-2 gap-3">
            <div class="rounded-2xl bg-white/5 p-4 ring-1 ring-white/10">
              <p class="text-xs uppercase tracking-[0.24em] text-slate-400">热点数</p>
              <p class="mt-2 text-2xl font-bold">{{ sortedPoints.length }}</p>
            </div>
            <div class="rounded-2xl bg-white/5 p-4 ring-1 ring-white/10">
              <p class="text-xs uppercase tracking-[0.24em] text-slate-400">总热度</p>
              <p class="mt-2 text-2xl font-bold">{{ formatCount(totalHeat) }}</p>
            </div>
          </div>

          <div v-if="hottestPoint" class="mb-6 rounded-3xl bg-gradient-to-br from-cyan-400/25 to-blue-500/10 p-5 ring-1 ring-cyan-300/20">
            <p class="text-xs uppercase tracking-[0.24em] text-cyan-200">当前最热</p>
            <h3 class="mt-2 text-2xl font-bold">{{ hottestPoint.destination }}</h3>
            <p class="mt-2 text-sm text-slate-300">热度值 {{ formatCount(hottestPoint.count) }}，点击地图热点可直接查看详情。</p>
          </div>

          <div class="space-y-3">
            <div class="flex items-center justify-between">
              <h3 class="text-lg font-semibold">热门目的地榜</h3>
              <span class="text-xs text-slate-400">点击跳转</span>
            </div>
            <button
              v-for="(point, index) in topPoints"
              :key="point.id"
              type="button"
              class="w-full rounded-2xl border border-white/10 bg-white/5 p-4 text-left transition hover:bg-white/10"
              @click="emit('select', point)"
            >
              <div class="flex items-start justify-between gap-4">
                <div>
                  <p class="text-xs text-slate-400">NO.{{ index + 1 }}</p>
                  <h4 class="mt-1 text-base font-semibold">{{ point.destination }}</h4>
                  <p class="mt-1 text-xs text-slate-400">{{ point.country }}</p>
                </div>
                <div class="text-right">
                  <p class="text-lg font-bold text-cyan-300">{{ formatCount(point.count) }}</p>
                  <p class="text-xs text-slate-400">热度</p>
                </div>
              </div>
              <div class="mt-3 h-2 rounded-full bg-white/10">
                <div
                  class="h-2 rounded-full bg-gradient-to-r from-cyan-300 via-sky-400 to-blue-500"
                  :style="{ width: `${getProgress(point)}%` }"
                ></div>
              </div>
            </button>
          </div>
        </aside>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import type { DestinationHeatmapPoint } from '@/types'
import worldEquirectangularMap from '@/assets/maps/world-equirectangular.svg'

const props = withDefaults(defineProps<{
  points: DestinationHeatmapPoint[]
  loading?: boolean
}>(), {
  loading: false
})

const emit = defineEmits<{
  select: [point: DestinationHeatmapPoint]
}>()

const minScale = 1
const maxScale = 3.2
const zoomStep = 0.35
const mapProjectionInsets = {
  left: 0,
  right: 0,
  top: 0,
  bottom: 0
}
const numberFormatter = new Intl.NumberFormat('zh-CN')
const viewportRef = ref<HTMLElement | null>(null)
const mapImageRef = ref<HTMLImageElement | null>(null)
const scale = ref(1)
const offsetX = ref(0)
const offsetY = ref(0)
const isDragging = ref(false)
const activePointerId = ref<number | null>(null)
const dragStartX = ref(0)
const dragStartY = ref(0)
const dragOriginX = ref(0)
const dragOriginY = ref(0)
const viewportWidth = ref(0)
const viewportHeight = ref(0)
const mapAspectRatio = ref(2)
let resizeObserver: ResizeObserver | null = null

const sortedPoints = computed(() => [...props.points].sort((a, b) => b.count - a.count))
const topPoints = computed(() => sortedPoints.value.slice(0, 6))
const totalHeat = computed(() => sortedPoints.value.reduce((sum, point) => sum + point.count, 0))
const hottestPoint = computed(() => sortedPoints.value[0] ?? null)
const maxCount = computed(() => sortedPoints.value[0]?.count ?? 1)
const minCount = computed(() => sortedPoints.value[sortedPoints.value.length - 1]?.count ?? 1)
const zoomLabel = computed(() => `${Math.round(scale.value * 100)}%`)
const canvasStyle = computed(() => ({
  transform: `translate3d(${offsetX.value}px, ${offsetY.value}px, 0) scale(${scale.value})`
}))
const mapFrame = computed(() => {
  if (!viewportWidth.value || !viewportHeight.value) {
    return { left: 0, top: 0, width: 0, height: 0 }
  }

  const viewportRatio = viewportWidth.value / viewportHeight.value
  if (viewportRatio > mapAspectRatio.value) {
    const height = viewportHeight.value
    const width = height * mapAspectRatio.value
    return {
      left: (viewportWidth.value - width) / 2,
      top: 0,
      width,
      height
    }
  }

  const width = viewportWidth.value
  const height = width / mapAspectRatio.value
  return {
    left: 0,
    top: (viewportHeight.value - height) / 2,
    width,
    height
  }
})
const projectionFrame = computed(() => {
  const frame = mapFrame.value
  return {
    left: frame.left + frame.width * mapProjectionInsets.left,
    top: frame.top + frame.height * mapProjectionInsets.top,
    width: frame.width * (1 - mapProjectionInsets.left - mapProjectionInsets.right),
    height: frame.height * (1 - mapProjectionInsets.top - mapProjectionInsets.bottom)
  }
})
const mapFrameStyle = computed(() => ({
  left: `${mapFrame.value.left}px`,
  top: `${mapFrame.value.top}px`,
  width: `${mapFrame.value.width}px`,
  height: `${mapFrame.value.height}px`
}))

const clamp = (value: number, min: number, max: number) => Math.min(max, Math.max(min, value))

const getViewportRect = () => viewportRef.value?.getBoundingClientRect() ?? null
const getViewportSize = () => ({
  width: viewportWidth.value || viewportRef.value?.clientWidth || 0,
  height: viewportHeight.value || viewportRef.value?.clientHeight || 0
})
const getPointRatio = (point: Pick<DestinationHeatmapPoint, 'lat' | 'lng'>) => ({
  x: (point.lng + 180) / 360,
  y: (90 - point.lat) / 180
})
const getProjectedCanvasPoint = (point: Pick<DestinationHeatmapPoint, 'lat' | 'lng'>) => {
  const ratio = getPointRatio(point)
  return {
    x: projectionFrame.value.left + projectionFrame.value.width * ratio.x,
    y: projectionFrame.value.top + projectionFrame.value.height * ratio.y
  }
}
const clampAxisOffset = (
  nextOffset: number,
  frameStart: number,
  frameSize: number,
  viewportSize: number,
  nextScale: number
) => {
  const scaledStart = frameStart * nextScale
  const scaledSize = frameSize * nextScale

  if (!viewportSize || !frameSize) {
    return nextOffset
  }

  if (scaledSize <= viewportSize) {
    return (viewportSize - scaledSize) / 2 - scaledStart
  }

  const minOffset = viewportSize - (scaledStart + scaledSize)
  const maxOffset = -scaledStart
  return clamp(nextOffset, minOffset, maxOffset)
}

const clampOffsets = (nextX: number, nextY: number, nextScale: number = scale.value) => {
  const { width, height } = getViewportSize()
  if (!width || !height) {
    return { x: nextX, y: nextY }
  }

  return {
    x: clampAxisOffset(nextX, mapFrame.value.left, mapFrame.value.width, width, nextScale),
    y: clampAxisOffset(nextY, mapFrame.value.top, mapFrame.value.height, height, nextScale)
  }
}

const setView = (nextScale: number, nextOffsetX: number, nextOffsetY: number) => {
  const clampedScale = clamp(nextScale, minScale, maxScale)
  const clampedOffsets = clampOffsets(nextOffsetX, nextOffsetY, clampedScale)
  scale.value = clampedScale
  offsetX.value = clampedOffsets.x
  offsetY.value = clampedOffsets.y
}

const zoomToClientPoint = (clientX: number, clientY: number, deltaScale: number) => {
  const rect = getViewportRect()
  if (!rect) {
    return
  }

  const targetScale = clamp(Number((scale.value + deltaScale).toFixed(2)), minScale, maxScale)
  if (targetScale === scale.value) {
    return
  }

  const pointX = clientX - rect.left
  const pointY = clientY - rect.top
  const worldX = (pointX - offsetX.value) / scale.value
  const worldY = (pointY - offsetY.value) / scale.value
  const nextOffsetX = pointX - worldX * targetScale
  const nextOffsetY = pointY - worldY * targetScale

  setView(targetScale, nextOffsetX, nextOffsetY)
}

const zoomFromCenter = (deltaScale: number) => {
  const rect = getViewportRect()
  if (!rect) {
    return
  }

  zoomToClientPoint(rect.left + rect.width / 2, rect.top + rect.height / 2, deltaScale)
}

const focusRegion = (lng: number, lat: number, targetScale: number) => {
  const { width, height } = getViewportSize()
  if (!width || !height) {
    return
  }

  const projectedPoint = getProjectedCanvasPoint({ lng, lat })
  const nextOffsetX = width / 2 - projectedPoint.x * targetScale
  const nextOffsetY = height / 2 - projectedPoint.y * targetScale
  setView(targetScale, nextOffsetX, nextOffsetY)
}

const getPointSize = (point: DestinationHeatmapPoint) => {
  const minPointSize = 16
  const maxPointSize = 34
  const zoomSizeFactor = Math.pow(scale.value, 0.82)

  if (maxCount.value === minCount.value) {
    return Math.round(clamp(24 / zoomSizeFactor, minPointSize, maxPointSize))
  }

  const ratio = (point.count - minCount.value) / (maxCount.value - minCount.value)
  const baseSize = 20 + ratio * 14
  return Math.round(clamp(baseSize / zoomSizeFactor, minPointSize, maxPointSize))
}

const getMarkerPositionStyle = (point: DestinationHeatmapPoint) => {
  const projectedPoint = getProjectedCanvasPoint(point)
  const left = offsetX.value + projectedPoint.x * scale.value
  const top = offsetY.value + projectedPoint.y * scale.value

  return {
    left: `${left}px`,
    top: `${top}px`
  }
}

const getBubbleStyle = (point: DestinationHeatmapPoint) => {
  const size = getPointSize(point)
  return {
    width: `${size}px`,
    height: `${size}px`
  }
}

const getMarkerIconStyle = (point: DestinationHeatmapPoint) => ({
  fontSize: `${Math.max(12, Math.round(getPointSize(point) * 0.48))}px`
})

const getProgress = (point: DestinationHeatmapPoint) => {
  if (!maxCount.value) {
    return 0
  }
  return Math.max(18, Math.round((point.count / maxCount.value) * 100))
}

const formatCount = (count: number) => {
  if (count >= 10000) {
    return `${(count / 10000).toFixed(1)}万`
  }
  return numberFormatter.format(count)
}

const zoomIn = () => {
  zoomFromCenter(zoomStep)
}

const zoomOut = () => {
  zoomFromCenter(-zoomStep)
}

const resetView = () => {
  setView(minScale, 0, 0)
}

const focusEastAsia = () => {
  focusRegion(108, 32, 2.15)
}

const handleWheel = (event: WheelEvent) => {
  if (!sortedPoints.value.length) {
    return
  }

  const deltaScale = event.deltaY < 0 ? zoomStep : -zoomStep
  zoomToClientPoint(event.clientX, event.clientY, deltaScale)
}

const handlePointerDown = (event: PointerEvent) => {
  if (!viewportRef.value || event.button !== 0 || scale.value <= minScale) {
    return
  }

  activePointerId.value = event.pointerId
  isDragging.value = true
  dragStartX.value = event.clientX
  dragStartY.value = event.clientY
  dragOriginX.value = offsetX.value
  dragOriginY.value = offsetY.value
  viewportRef.value.setPointerCapture(event.pointerId)
}

const handlePointerMove = (event: PointerEvent) => {
  if (!isDragging.value || activePointerId.value !== event.pointerId) {
    return
  }

  const deltaX = event.clientX - dragStartX.value
  const deltaY = event.clientY - dragStartY.value
  const clampedOffsets = clampOffsets(dragOriginX.value + deltaX, dragOriginY.value + deltaY)
  offsetX.value = clampedOffsets.x
  offsetY.value = clampedOffsets.y
}

const handlePointerUp = (event: PointerEvent) => {
  if (activePointerId.value !== event.pointerId) {
    return
  }

  if (viewportRef.value?.hasPointerCapture(event.pointerId)) {
    viewportRef.value.releasePointerCapture(event.pointerId)
  }

  isDragging.value = false
  activePointerId.value = null
}

const handleResize = () => {
  updateViewportMetrics()
  const clampedOffsets = clampOffsets(offsetX.value, offsetY.value)
  offsetX.value = clampedOffsets.x
  offsetY.value = clampedOffsets.y
}

const updateViewportMetrics = () => {
  if (!viewportRef.value) {
    return
  }

  viewportWidth.value = viewportRef.value.clientWidth
  viewportHeight.value = viewportRef.value.clientHeight
}

const handleImageLoad = () => {
  if (mapImageRef.value?.naturalWidth && mapImageRef.value?.naturalHeight) {
    mapAspectRatio.value = mapImageRef.value.naturalWidth / mapImageRef.value.naturalHeight
  }
  updateViewportMetrics()
}

onMounted(() => {
  updateViewportMetrics()
  if (typeof ResizeObserver !== 'undefined' && viewportRef.value) {
    resizeObserver = new ResizeObserver(() => {
      handleResize()
    })
    resizeObserver.observe(viewportRef.value)
  }
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  resizeObserver?.disconnect()
  window.removeEventListener('resize', handleResize)
})
</script>
