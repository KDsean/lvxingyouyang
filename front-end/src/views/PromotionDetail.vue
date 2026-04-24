<template>
  <main class="bg-gray-50">
    <section v-if="promotion" class="relative min-h-[420px] overflow-hidden">
      <img
        :src="promotion.image"
        :alt="promotion.title"
        class="absolute inset-0 h-full w-full object-cover"
      />
      <div class="absolute inset-0 bg-gradient-to-r from-black/75 via-black/45 to-black/20"></div>
      <div class="relative mx-auto flex min-h-[420px] max-w-[1200px] flex-col justify-end px-4 py-12 text-white">
        <router-link to="/" class="mb-8 inline-flex w-fit items-center text-sm font-medium text-white/85 hover:text-white">
          ← 返回首页
        </router-link>
        <div class="max-w-3xl">
          <p class="mb-3 text-sm font-bold tracking-widest text-blue-200">{{ promotion.label }}</p>
          <h1 class="text-4xl font-black leading-tight md:text-6xl">{{ promotion.title }}</h1>
          <p class="mt-5 max-w-2xl text-lg leading-8 text-white/90">{{ promotion.summary }}</p>
        </div>
      </div>
    </section>

    <section v-if="promotion" class="mx-auto grid max-w-[1200px] gap-8 px-4 py-10 lg:grid-cols-[1fr_360px]">
      <div class="space-y-8">
        <div class="grid gap-4 md:grid-cols-3">
          <div v-for="item in promotion.facts" :key="item.label" class="rounded-lg border border-gray-200 bg-white p-5">
            <div class="text-sm text-gray-500">{{ item.label }}</div>
            <div class="mt-2 text-xl font-bold text-gray-900">{{ item.value }}</div>
          </div>
        </div>

        <section class="rounded-lg border border-gray-200 bg-white p-6">
          <h2 class="text-2xl font-bold">行程亮点</h2>
          <div class="mt-5 grid gap-4 md:grid-cols-2">
            <div v-for="highlight in promotion.highlights" :key="highlight" class="flex gap-3 rounded-lg bg-gray-50 p-4">
              <span class="mt-1 h-2 w-2 shrink-0 rounded-full bg-blue-600"></span>
              <p class="text-gray-700">{{ highlight }}</p>
            </div>
          </div>
        </section>

        <section class="rounded-lg border border-gray-200 bg-white p-6">
          <h2 class="text-2xl font-bold">参考安排</h2>
          <div class="mt-6 space-y-5">
            <div v-for="day in promotion.schedule" :key="day.day" class="border-l-2 border-blue-100 pl-5">
              <div class="text-sm font-bold text-blue-600">{{ day.day }}</div>
              <h3 class="mt-1 text-lg font-bold text-gray-900">{{ day.title }}</h3>
              <p class="mt-2 leading-7 text-gray-600">{{ day.description }}</p>
            </div>
          </div>
        </section>
      </div>

      <aside class="h-fit rounded-lg border border-gray-200 bg-white p-6 shadow-sm">
        <div class="text-sm text-gray-500">活动价</div>
        <div class="mt-2 text-3xl font-black text-orange-500">¥{{ promotion.price }}</div>
        <div class="mt-1 text-sm text-gray-500">/ 人起</div>
        <div class="mt-6 space-y-3 text-sm text-gray-600">
          <div v-for="service in promotion.services" :key="service" class="flex items-center gap-2">
            <span class="iconify text-blue-600" data-icon="material-symbols:check-circle"></span>
            <span>{{ service }}</span>
          </div>
        </div>
        <router-link
          :to="{ name: 'Planning', query: { q: promotion.title } }"
          class="mt-6 block rounded-lg bg-blue-600 px-5 py-3 text-center font-bold text-white transition hover:bg-blue-700"
        >
          定制此行程
        </router-link>
      </aside>
    </section>

    <section v-else class="mx-auto max-w-[900px] px-4 py-20 text-center">
      <h1 class="text-3xl font-bold">活动不存在</h1>
      <p class="mt-3 text-gray-500">请返回首页重新选择活动。</p>
      <router-link to="/" class="mt-8 inline-block rounded-lg bg-blue-600 px-6 py-3 font-bold text-white">
        返回首页
      </router-link>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

interface Promotion {
  label: string
  title: string
  summary: string
  image: string
  price: string
  facts: Array<{ label: string; value: string }>
  highlights: string[]
  schedule: Array<{ day: string; title: string; description: string }>
  services: string[]
}

const route = useRoute()

const promotions: Record<string, Promotion> = {
  'swiss-ski-season': {
    label: '早春特惠',
    title: '瑞士滑雪季',
    summary: '覆盖少女峰、采尔马特与卢塞恩湖区，把雪场体验、观景列车和温泉酒店串成一条轻奢冬春线路。',
    image: 'https://modao.cc/agent-py/media/generated_images/2026-03-01/19d2da5cb5834e4f8ba9e05e015deb76.jpg',
    price: '18,800',
    facts: [
      { label: '推荐天数', value: '7天6晚' },
      { label: '最佳季节', value: '12月-4月' },
      { label: '适合人群', value: '情侣 / 家庭 / 滑雪爱好者' }
    ],
    highlights: [
      '少女峰区域一日滑雪体验，含基础装备与中文协助',
      '搭乘景观列车穿越阿尔卑斯山谷',
      '入住雪山景观酒店，安排温泉或SPA放松',
      '预留卢塞恩老城自由活动时间'
    ],
    schedule: [
      { day: 'Day 1-2', title: '抵达苏黎世，前往卢塞恩', description: '适应时差，漫步卡佩尔廊桥与湖畔街区，晚上入住湖景酒店。' },
      { day: 'Day 3-4', title: '少女峰滑雪与雪山观景', description: '根据水平安排滑雪教练或雪地徒步，非滑雪客人可前往观景台。' },
      { day: 'Day 5-7', title: '采尔马特与马特洪峰', description: '搭乘列车进入无车小镇，安排日出观景、精品餐厅和返程衔接。' }
    ],
    services: ['酒店含早', '当地交通建议', '滑雪装备预订协助', 'AI行程定制']
  },
  'kyoto-sakura-festival': {
    label: '文化之旅',
    title: '京都樱花祭',
    summary: '围绕清水寺、岚山、祇园和伏见稻荷设计赏樱路线，兼顾古都文化体验与轻松步行节奏。',
    image: 'https://modao.cc/agent-py/media/generated_images/2026-03-01/468892ebe21341a592d6d1c75a0d720a.jpg',
    price: '9,600',
    facts: [
      { label: '推荐天数', value: '5天4晚' },
      { label: '最佳季节', value: '3月下旬-4月上旬' },
      { label: '适合人群', value: '摄影 / 亲子 / 文化旅行' }
    ],
    highlights: [
      '清水寺、哲学之道与岚山错峰赏樱',
      '安排和服体验、茶道或怀石料理',
      '精选交通便利的四条河原町或京都站周边酒店',
      '保留奈良或宇治一日延展选项'
    ],
    schedule: [
      { day: 'Day 1', title: '抵达京都', description: '入住市中心酒店，晚上可前往鸭川与先斗町自由用餐。' },
      { day: 'Day 2-3', title: '东山与岚山赏樱', description: '串联清水寺、二年坂、哲学之道、渡月桥，减少重复换乘。' },
      { day: 'Day 4-5', title: '伏见稻荷与文化体验', description: '安排神社参访、茶道体验，并根据航班时间返回大阪或东京。' }
    ],
    services: ['赏樱路线规划', '酒店区域建议', '体验项目预约建议', '餐厅清单']
  },
  'phuket-diving': {
    label: '限时秒杀',
    title: '普吉岛潜水',
    summary: '以普吉岛为基地，覆盖皇帝岛、皮皮岛或斯米兰方向，适合初学体验潜和持证潜水员升级行程。',
    image: 'https://modao.cc/agent-py/media/generated_images/2026-03-01/96ca2238e10c4cae9ffeaffefeb0392d.jpg',
    price: '5,280',
    facts: [
      { label: '推荐天数', value: '4天3晚' },
      { label: '最佳季节', value: '11月-5月' },
      { label: '适合人群', value: '海岛度假 / 潜水入门' }
    ],
    highlights: [
      '中文潜店对接，支持体验潜水和持证深潜',
      '海岛快艇一日游，兼顾浮潜、沙滩和拍照',
      '酒店可选芭东、卡塔或卡伦区域',
      '预留泰式按摩和夜市自由活动'
    ],
    schedule: [
      { day: 'Day 1', title: '抵达普吉岛', description: '接机入住酒店，晚上前往海滩或夜市熟悉周边。' },
      { day: 'Day 2', title: '潜水体验日', description: '根据海况安排皇帝岛或皮皮岛方向，包含基础讲解与下水陪同。' },
      { day: 'Day 3-4', title: '海岛休闲与返程', description: '安排半日自由活动，可加订SPA、冲浪或日落餐厅。' }
    ],
    services: ['潜店对接', '酒店方案', '海岛活动建议', '出海注意事项']
  }
}

const promotion = computed(() => promotions[route.params.slug as string])
</script>
