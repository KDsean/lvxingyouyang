<template>
  <main class="max-w-4xl mx-auto px-4 py-8">
    <el-button @click="goBack" class="mb-6">
      <span class="iconify mr-2" data-icon="material-symbols:arrow-back"></span>
      返回
    </el-button>

    <article v-if="guide" class="bg-white rounded-2xl shadow-lg overflow-hidden">
      <div class="relative h-80 md:h-96">
        <img
          :src="guideCover"
          :alt="guide.title"
          class="w-full h-full object-cover"
        />
        <div class="absolute left-6 bottom-6 bg-black/55 text-white px-4 py-2 rounded-full text-sm font-bold">
          {{ guide.destination }}
        </div>
      </div>

      <div class="p-6 md:p-8">
        <h1 class="text-3xl font-bold mb-4">{{ guide.title }}</h1>

        <div class="flex flex-wrap items-center gap-6 mb-6 pb-6 border-b">
          <div class="flex items-center gap-3">
            <img
              :src="guide.author.avatar"
              :alt="guide.author.name"
              class="w-12 h-12 rounded-full"
            />
            <div>
              <div class="font-bold">{{ guide.author.name }}</div>
              <div class="text-sm text-gray-500">{{ formatDate(guide.createdAt) }}</div>
            </div>
          </div>

          <div class="flex gap-4 text-gray-500">
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

        <div class="flex flex-wrap gap-2 mb-8">
          <span
            v-for="tag in guide.tags"
            :key="tag"
            class="px-3 py-1 bg-red-50 text-red-600 rounded text-sm"
          >
            {{ tag }}
          </span>
        </div>

        <div class="markdown-body max-w-none">
          <div v-html="renderedContent"></div>

          <section v-for="section in sections" :key="section.title" class="mb-8">
            <h2 class="text-2xl font-bold mb-4">{{ section.title }}</h2>
            <p class="text-gray-600 leading-relaxed">{{ section.body }}</p>
          </section>
        </div>

        <div class="mt-8 pt-6 border-t flex gap-4">
          <el-button type="primary" @click="liked = true">
            <span class="iconify mr-2" data-icon="material-symbols:thumb-up"></span>
            {{ liked ? '已点赞' : '点赞' }}
          </el-button>
          <el-button>
            <span class="iconify mr-2" data-icon="material-symbols:favorite-outline"></span>
            收藏
          </el-button>
          <el-button>
            <span class="iconify mr-2" data-icon="material-symbols:share"></span>
            分享
          </el-button>
        </div>
      </div>
    </article>

    <el-empty v-else description="攻略不存在" />
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MarkdownIt from 'markdown-it'
import { getGuideDetail } from '@/api/modules/guide'
import type { Guide } from '@/types'
import { getGuideAuthor, getGuideImage } from '@/utils/guideDisplay'

type GuideDetailData = Guide & {
  sections: {
    title: string
    body: string
  }[]
}

type FlatGuide = Partial<Guide> & {
  authorName?: string
  authorAvatar?: string
}

const route = useRoute()
const router = useRouter()
const liked = ref(false)
const guide = ref<GuideDetailData | null>(null)

const markdown = new MarkdownIt({
  breaks: true,
  linkify: true
})

const sampleGuideDetails: Record<string, GuideDetailData> = {
  'sample-dali-3days': {
    id: 'sample-dali-3days',
    title: '大理洱海 3 天 2 夜慢旅行：骑行、古城和苍山日落',
    destination: '大理',
    author: { id: 'u101', name: '林小夏', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Lin' },
    content: '这条路线适合第一次去大理的人，节奏不赶，重点放在洱海、喜洲、双廊和苍山。',
    images: ['https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/4/44/Dali_Yunnan_China_Ornamented-Door-01.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['自驾游', '古镇', '摄影'],
    viewCount: 12840,
    likeCount: 936,
    createdAt: '2026-04-20T09:30:00',
    sections: [
      { title: '第一天：大理古城和才村码头', body: '上午抵达后住在古城附近，下午逛人民路、复兴路，傍晚去才村码头看洱海日落。' },
      { title: '第二天：洱海东线骑行', body: '从喜洲出发，经过海舌公园、双廊和挖色，沿途适合拍照，建议留足半天时间。' },
      { title: '第三天：苍山和返程', body: '上午坐索道上苍山，天气好时能俯瞰洱海，下午回古城吃一顿白族菜后返程。' }
    ]
  },
  'sample-sanya-family': {
    id: 'sample-sanya-family',
    title: '三亚亲子海岛攻略：亚龙湾、蜈支洲岛和免税店安排',
    destination: '三亚',
    author: { id: 'u102', name: '海风旅行记', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Sea' },
    content: '适合带孩子的 4 天海岛路线，住宿建议放在亚龙湾或海棠湾，减少换酒店的时间。',
    images: ['https://upload.wikimedia.org/wikipedia/commons/9/96/Beach_of_Sanya.jpg'],
    tags: ['亲子游', '海岛', '美食'],
    viewCount: 18320,
    likeCount: 1240,
    createdAt: '2026-04-18T14:15:00',
    sections: [
      { title: '海滩选择', body: '亚龙湾沙质细，适合亲子玩水；海棠湾酒店更新，但风浪更大，适合泡酒店。' },
      { title: '蜈支洲岛安排', body: '建议早上登岛，中午前完成主要项目，下午避开强日照后返程。' },
      { title: '吃饭和购物', body: '海鲜市场建议明码标价后再加工，免税店放在最后一天，避免提着行李跑动。' }
    ]
  },
  'sample-beijing-history': {
    id: 'sample-beijing-history',
    title: '北京历史文化两日线：故宫、景山、胡同和长城',
    destination: '北京',
    author: { id: 'u103', name: '阿舟', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Zhou' },
    content: '周末看北京中轴线和长城，适合第一次来北京、时间有限但想看重点景点的旅行者。',
    images: ['https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F0%2F00%2FSunset_of_the_Forbidden_City_2006.JPG&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['历史文化', '摄影', '徒步'],
    viewCount: 22190,
    likeCount: 1588,
    createdAt: '2026-04-15T10:00:00',
    sections: [
      { title: '第一天：故宫到景山', body: '上午进故宫，路线从午门到神武门，出门后上景山看紫禁城全景。' },
      { title: '傍晚：胡同和什刹海', body: '晚饭安排在鼓楼或什刹海附近，适合慢慢走，不建议再排太满。' },
      { title: '第二天：长城', body: '八达岭交通方便，慕田峪体验更轻松，旺季建议提前订票并早出发。' }
    ]
  },
  'sample-chengdu-food': {
    id: 'sample-chengdu-food',
    title: '成都美食 Citywalk：火锅、茶馆、熊猫基地一条线',
    destination: '成都',
    author: { id: 'u104', name: '椒盐地图', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Food' },
    content: '这条路线适合边吃边逛，从熊猫基地开始，下午回市区喝茶，晚上吃火锅。',
    images: ['https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/5/54/Chengdu-pandas-d10.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['美食', '摄影', '穷游'],
    viewCount: 17650,
    likeCount: 1166,
    createdAt: '2026-04-12T19:20:00',
    sections: [
      { title: '上午：熊猫基地', body: '早上入园更容易看到熊猫活动，建议预留 3 小时，不要把市区行程排得太紧。' },
      { title: '下午：人民公园喝茶', body: '回市区后去人民公园或望平街，安排盖碗茶和小吃。' },
      { title: '晚上：火锅和夜宵', body: '火锅建议提前排队，吃完可以去玉林或建设路继续夜宵。' }
    ]
  },
  'sample-hangzhou-weekend': {
    id: 'sample-hangzhou-weekend',
    title: '杭州周末不踩雷：西湖晨游、灵隐寺和龙井村',
    destination: '杭州',
    author: { id: 'u105', name: '南风', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Nan' },
    content: '周末短途杭州路线，重点避开西湖高峰人流，把寺庙、茶村和湖景串起来。',
    images: ['https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/0/07/20090524_Hangzhou_West_Lake_7531.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['徒步', '摄影', '古镇'],
    viewCount: 15430,
    likeCount: 988,
    createdAt: '2026-04-10T08:45:00',
    sections: [
      { title: '清晨：西湖', body: '早上从断桥或苏堤开始走，游客少，光线也更适合拍照。' },
      { title: '中午：灵隐寺', body: '灵隐寺和飞来峰建议一起安排，注意提前预约和错峰用餐。' },
      { title: '下午：龙井村', body: '去龙井村喝茶看山景，返程前留出打车或公交换乘时间。' }
    ]
  },
  'sample-lijiang-snow': {
    id: 'sample-lijiang-snow',
    title: '丽江古城到玉龙雪山：高反准备、索道和拍照点',
    destination: '丽江',
    author: { id: 'u106', name: '云上阿宁', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Yun' },
    content: '玉龙雪山一日游重点是提前预约索道、控制节奏，古城适合放在前后两晚慢逛。',
    images: ['https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2Ff%2Ffb%2FLijiang_Yunnan_Old-town-01.jpg%2F1280px-Lijiang_Yunnan_Old-town-01.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['蜜月', '摄影', '古镇'],
    viewCount: 14380,
    likeCount: 875,
    createdAt: '2026-04-08T16:10:00',
    sections: [
      { title: '出发前准备', body: '提前订大索道票，准备防晒、保暖外套和足够饮水。' },
      { title: '雪山路线', body: '上午上冰川公园，下午去蓝月谷，体力一般的人不要硬赶多个点。' },
      { title: '古城住宿', body: '建议住古城边缘，安静且行李进出方便，晚上再步行进古城。' }
    ]
  },
  'sample-xian-history': {
    id: 'sample-xian-history',
    title: '西安 48 小时历史线：兵马俑、城墙和回民街',
    destination: '西安',
    author: { id: 'u107', name: '长安客', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Changan' },
    content: '两天看西安重点历史景点，适合第一次来西安的人，交通以地铁和打车为主。',
    images: ['https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/3/38/Xian_China_Terracotta-Army-Museum-01.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['历史文化', '美食', '穷游'],
    viewCount: 16920,
    likeCount: 1042,
    createdAt: '2026-04-06T11:00:00',
    sections: [
      { title: '第一天：兵马俑', body: '兵马俑距离市区较远，建议单独安排半天以上，配合讲解体验更好。' },
      { title: '晚上：钟鼓楼和回民街', body: '晚饭去钟楼附近，回民街适合感受氛围，也可以去洒金桥找小吃。' },
      { title: '第二天：城墙和博物馆', body: '上午骑行古城墙，下午留给陕西历史博物馆，注意提前预约。' }
    ]
  },
  'sample-xiamen-island': {
    id: 'sample-xiamen-island',
    title: '厦门鼓浪屿一日游：船票、路线和沙坡尾夜景',
    destination: '厦门',
    author: { id: 'u108', name: '岛屿笔记', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Island' },
    content: '鼓浪屿适合安排一整天，傍晚回本岛去沙坡尾和中山路，路线轻松不绕路。',
    images: ['https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/b/b6/Gulangyu_Island_from_Zhongshan_Road%2C_Xiamen.jpg&w=1200&h=800&fit=cover&output=jpg'],
    tags: ['海岛', '摄影', '美食'],
    viewCount: 11960,
    likeCount: 722,
    createdAt: '2026-04-03T13:25:00',
    sections: [
      { title: '上午：上岛', body: '提前买船票，从邮轮中心码头出发，上岛后先走日光岩和菽庄花园。' },
      { title: '下午：老建筑散步', body: '下午放慢节奏看老别墅和街巷，避开正午暴晒。' },
      { title: '晚上：沙坡尾', body: '回本岛后去沙坡尾吃饭看夜景，再去中山路逛一圈。' }
    ]
  },
  'sample-harbin-ski': {
    id: 'sample-harbin-ski',
    title: '哈尔滨冬季滑雪攻略：冰雪大世界、亚布力和穿搭清单',
    destination: '哈尔滨',
    author: { id: 'u109', name: '北境旅行', avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Snow' },
    content: '冬季哈尔滨重点是保暖和路线节奏，冰雪大世界建议安排夜场，亚布力单独留一天。',
    images: ['https://upload.wikimedia.org/wikipedia/commons/5/51/Harbin_Ice_and_Snow_Festival_2013.jpg'],
    tags: ['滑雪', '摄影', '亲子游'],
    viewCount: 10180,
    likeCount: 690,
    createdAt: '2026-03-30T18:40:00',
    sections: [
      { title: '穿搭准备', body: '帽子、围巾、手套、雪地靴和暖宝宝都要准备，手机也要注意低温掉电。' },
      { title: '冰雪大世界', body: '建议下午进园，先拍白天冰雕，等亮灯后再拍夜景。' },
      { title: '亚布力滑雪', body: '初学者建议请教练，滑雪当天不要再安排太多市区景点。' }
    ]
  }
}

const sections = computed(() => guide.value?.sections || [])
const guideCover = computed(() => guide.value ? getGuideImage(guide.value) : '')
const renderedContent = computed(() => markdown.render(guide.value?.content || ''))

const normalizeGuide = (data: FlatGuide): GuideDetailData => ({
  id: String(data.id || route.params.id || ''),
  title: data.title || '未命名攻略',
  destination: data.destination || '目的地',
  author: getGuideAuthor(data),
  content: data.content || '这篇攻略正在整理中。',
  images: [getGuideImage(data)],
  tags: data.tags || [],
  viewCount: data.viewCount || 0,
  likeCount: data.likeCount || 0,
  createdAt: data.createdAt || new Date().toISOString(),
  sections: []
})

const loadGuide = async () => {
  const id = String(route.params.id || '')
  if (sampleGuideDetails[id]) {
    guide.value = sampleGuideDetails[id]
    return
  }

  try {
    const res = await getGuideDetail(id)
    guide.value = normalizeGuide(res.data as FlatGuide)
  } catch {
    guide.value = null
  }
}

const goBack = () => {
  router.back()
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('zh-CN')
}

onMounted(loadGuide)
</script>

<style scoped>
.markdown-body {
  color: #4b5563;
  line-height: 1.8;
}

.markdown-body :deep(h1) {
  margin: 1.5rem 0 1rem;
  color: #111827;
  font-size: 1.875rem;
  font-weight: 700;
}

.markdown-body :deep(h2) {
  margin: 1.5rem 0 0.75rem;
  color: #111827;
  font-size: 1.5rem;
  font-weight: 700;
}

.markdown-body :deep(h3) {
  margin: 1.25rem 0 0.5rem;
  color: #111827;
  font-size: 1.25rem;
  font-weight: 700;
}

.markdown-body :deep(p),
.markdown-body :deep(ul),
.markdown-body :deep(ol),
.markdown-body :deep(blockquote) {
  margin-bottom: 1rem;
}

.markdown-body :deep(ul) {
  list-style: disc;
  padding-left: 1.5rem;
}

.markdown-body :deep(ol) {
  list-style: decimal;
  padding-left: 1.5rem;
}

.markdown-body :deep(strong) {
  color: #111827;
  font-weight: 700;
}

.markdown-body :deep(a) {
  color: #2563eb;
  text-decoration: underline;
}

.markdown-body :deep(blockquote) {
  border-left: 4px solid #fecaca;
  padding-left: 1rem;
  color: #6b7280;
}
</style>
