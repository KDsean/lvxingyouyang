<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8 flex flex-col lg:flex-row gap-8">
    <Sidebar />
    
    <div class="flex-1 space-y-12">
      <!-- Hero Section -->
      <section class="relative h-[360px] rounded-3xl overflow-hidden shadow-2xl">
        <img 
          alt="Beautiful landscape" 
          class="w-full h-full object-cover" 
          src="https://modao.cc/agent-py/media/generated_images/2026-03-01/ff61418edb084fd496e2012d52236161.jpg"
        />
        <div class="absolute inset-0 bg-gradient-to-r from-black/60 to-transparent flex items-center px-12">
          <div class="text-white">
            <h1 class="text-5xl font-black mb-4 leading-tight">
              旅行有样，<br/>
              <span class="text-blue-400">定义你的生活方式</span>
            </h1>
            <p class="text-xl opacity-90 mb-8 max-w-lg">
              2026年首选旅游平台：用最智能的方式，探索最远方的诗意。全球150+国家行程覆盖。
            </p>
            <div class="flex space-x-4">
              <router-link to="/planning">
                <button class="bg-trip-gradient px-8 py-3 rounded-xl font-bold text-lg hover:shadow-xl transition transform hover:-translate-y-1">
                  立即规划行程
                </button>
              </router-link>
              <router-link to="/destinations">
                <button class="bg-white/20 backdrop-blur-md border border-white/30 px-8 py-3 rounded-xl font-bold text-lg hover:bg-white/30 transition">
                  探索目的地
                </button>
              </router-link>
            </div>
          </div>
        </div>
      </section>

      <!-- 精选酒店 -->
      <section>
        <div class="flex justify-between items-end mb-6">
          <div>
            <h2 class="text-3xl font-bold">精选酒店预订</h2>
            <p class="text-gray-500 mt-2">为您在全球范围内甄选高评分、极具设计感的住宿体验</p>
          </div>
          <router-link to="/hotels" class="text-blue-600 font-medium hover:underline">
            查看更多精选酒店 →
          </router-link>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
          <div class="group bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-md transition">
            <div class="relative h-64">
              <img 
                alt="Luxury Resort" 
                class="w-full h-full object-cover group-hover:scale-105 transition duration-500" 
                src="https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1000&q=80"
              />
              <span class="absolute top-4 left-4 bg-black/50 backdrop-blur text-white px-3 py-1 rounded-lg text-sm">
                三亚 · 海棠湾
              </span>
            </div>
            <div class="p-6">
              <h4 class="text-xl font-bold mb-2">三亚天际海景度假酒店</h4>
              <div class="flex items-center text-yellow-500 mb-4">
                <span class="iconify" data-icon="material-symbols:star"></span>
                <span class="iconify" data-icon="material-symbols:star"></span>
                <span class="iconify" data-icon="material-symbols:star"></span>
                <span class="iconify" data-icon="material-symbols:star"></span>
                <span class="iconify" data-icon="material-symbols:star"></span>
                <span class="ml-2 text-gray-500 text-sm">4.9/5 (2,400+ 条评价)</span>
              </div>
              <p class="text-gray-600 mb-6 text-sm leading-relaxed">
                独拥两公里私人沙滩，2026年全新装修，配备AI全屋智能控制系统，让您的海滨度假充满科技感与舒适度。
              </p>
              <div class="flex items-center justify-between">
                <div class="text-2xl font-bold text-orange-500">
                  ¥1,899<span class="text-sm font-normal text-gray-500"> /晚起</span>
                </div>
                <button class="bg-blue-600 text-white px-6 py-2 rounded-lg font-medium hover:bg-blue-700 transition">
                  立即预订
                </button>
              </div>
            </div>
          </div>

          <div class="group bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-md transition">
            <div class="relative h-64">
              <img 
                alt="Modern Hotel" 
                class="w-full h-full object-cover group-hover:scale-105 transition duration-500" 
                src="https://modao.cc/agent-py/media/generated_images/2026-03-01/5194616f91ac4327b106ec84dbe1e4a1.jpg"
              />
              <span class="absolute top-4 left-4 bg-black/50 backdrop-blur text-white px-3 py-1 rounded-lg text-sm">
                上海 · 外滩
              </span>
            </div>
            <div class="p-6">
              <h4 class="text-xl font-bold mb-2">外滩W·未来幻影酒店</h4>
              <div class="flex items-center text-yellow-500 mb-4">
                <span class="iconify" data-icon="material-symbols:star"></span>
                <span class="iconify" data-icon="material-symbols:star"></span>
                <span class="iconify" data-icon="material-symbols:star"></span>
                <span class="iconify" data-icon="material-symbols:star"></span>
                <span class="iconify" data-icon="material-symbols:star-half"></span>
                <span class="ml-2 text-gray-500 text-sm">4.8/5 (3,150+ 条评价)</span>
              </div>
              <p class="text-gray-600 mb-6 text-sm leading-relaxed">
                270度环幕江景房，直击外滩经典，配备米其林三星云端餐厅，是商务精英与潮流达人的不二之选。
              </p>
              <div class="flex items-center justify-between">
                <div class="text-2xl font-bold text-orange-500">
                  ¥2,450<span class="text-sm font-normal text-gray-500"> /晚起</span>
                </div>
                <button class="border border-blue-600 text-blue-600 px-6 py-2 rounded-lg font-medium hover:bg-blue-50 transition">
                  了解详情
                </button>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- AI 行程助手 -->
      <section class="bg-slate-900 rounded-3xl p-8 text-white relative overflow-hidden">
        <div class="relative z-10 flex flex-col md:flex-row items-center gap-8">
          <div class="w-full md:w-1/2">
            <div class="inline-block px-4 py-1 rounded-full bg-blue-500/20 border border-blue-500 text-blue-400 text-sm mb-4 tracking-widest uppercase">
              Smart AI 3.0
            </div>
            <h2 class="text-3xl font-bold mb-4">AI 行程管家，10秒懂你</h2>
            <p class="text-gray-400 mb-8 leading-relaxed">
              告诉我们你的预算、喜好和时间。基于 10 亿次旅行数据生成的实时行程，比你自己更懂你。
            </p>
            <div class="bg-white/10 p-4 rounded-2xl border border-white/10">
              <div class="flex items-center space-x-3 text-sm italic opacity-70 mb-3">
                <span class="iconify" data-icon="material-symbols:chat"></span>
                <span>您可以试着说: "我想在3月去大理住5天，喜欢古镇精品酒店，预算在5000以内..."</span>
              </div>
              <div class="flex gap-2">
                <input 
                  v-model="aiInput"
                  class="flex-1 bg-white/5 border border-white/20 rounded-xl px-4 py-3 focus:outline-none focus:border-blue-500 transition" 
                  placeholder="输入您的旅行意向..." 
                  type="text"
                  @keyup.enter="goToPlanning"
                />
                <button 
                  @click="goToPlanning"
                  class="bg-blue-600 p-3 rounded-xl hover:bg-blue-700"
                >
                  <span class="iconify text-xl" data-icon="material-symbols:send"></span>
                </button>
              </div>
            </div>
          </div>
          <div class="w-full md:w-1/2 flex justify-center">
            <div class="relative">
              <img 
                alt="AI Agent" 
                class="w-64 h-64 object-contain" 
                src="https://modao.cc/agent-py/media/generated_images/2026-03-01/7fa6a1cd4acd438aaecaef8efca88f6b.jpg"
              />
              <div class="absolute -top-4 -right-4 bg-white/10 backdrop-blur-xl p-4 rounded-2xl border border-white/20 animate-bounce">
                <div class="flex items-center space-x-2">
                  <div class="w-2 h-2 rounded-full bg-green-500 animate-pulse"></div>
                  <span class="text-xs font-mono">正在规划旅游路线</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="absolute top-0 right-0 w-96 h-96 bg-blue-600/10 blur-[100px] rounded-full"></div>
      </section>

      <!-- 旅游地图 -->
      <section>
        <div class="bg-white p-4 rounded-3xl shadow-sm border border-gray-100 h-[400px] relative">
          <div class="absolute inset-0 grayscale scale-110 opacity-20">
            <img 
              alt="Global Map" 
              class="w-full h-full object-cover" 
              src="https://modao.cc/agent-py/media/generated_images/2026-03-01/effa99d73c1349ffbff7c98949ee271b.jpg"
            />
          </div>
          <div class="relative z-10 flex flex-col items-center justify-center h-full text-center px-12">
            <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-6">
              <span class="iconify text-4xl text-blue-600" data-icon="material-symbols:location-on"></span>
            </div>
            <h3 class="text-2xl font-bold mb-4">实时热门目的地热力图</h3>
            <p class="text-gray-500 mb-8 max-w-md">
              热点正在北京、上海、三亚等热门目的地聚集。点击查看热度分布，并直接进入对应详情页。
            </p>
            <router-link
              :to="{ name: 'Destinations', hash: '#heatmap' }"
              class="inline-flex items-center bg-blue-600 text-white px-8 py-3 rounded-xl hover:bg-blue-700 transition font-bold"
            >
              查看互动地图
            </router-link>
          </div>
        </div>
      </section>

      <!-- 广告轮播 -->
      <section class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <router-link
          :to="{ name: 'PromotionDetail', params: { slug: 'swiss-ski-season' } }"
          class="group relative h-48 rounded-2xl overflow-hidden cursor-pointer shadow-sm"
        >
          <img 
            alt="Swiss Mountain" 
            class="w-full h-full object-cover group-hover:scale-110 transition duration-700" 
            src="https://modao.cc/agent-py/media/generated_images/2026-03-01/19d2da5cb5834e4f8ba9e05e015deb76.jpg"
          />
          <div class="absolute inset-0 bg-black/30 flex items-center justify-center flex-col text-white">
            <p class="text-xs tracking-widest mb-1 uppercase opacity-80 font-bold">早春特惠</p>
            <h4 class="text-xl font-bold">瑞士滑雪季</h4>
            <div class="mt-2 text-sm bg-white/20 px-3 py-1 rounded-full">了解更多</div>
          </div>
        </router-link>
        
        <router-link
          :to="{ name: 'PromotionDetail', params: { slug: 'kyoto-sakura-festival' } }"
          class="group relative h-48 rounded-2xl overflow-hidden cursor-pointer shadow-sm"
        >
          <img 
            alt="Kyoto Temple" 
            class="w-full h-full object-cover group-hover:scale-110 transition duration-700" 
            src="https://modao.cc/agent-py/media/generated_images/2026-03-01/468892ebe21341a592d6d1c75a0d720a.jpg"
          />
          <div class="absolute inset-0 bg-black/30 flex items-center justify-center flex-col text-white">
            <p class="text-xs tracking-widest mb-1 uppercase opacity-80 font-bold">文化之旅</p>
            <h4 class="text-xl font-bold">京都樱花祭</h4>
            <div class="mt-2 text-sm bg-white/20 px-3 py-1 rounded-full">了解更多</div>
          </div>
        </router-link>
        
        <router-link
          :to="{ name: 'PromotionDetail', params: { slug: 'phuket-diving' } }"
          class="group relative h-48 rounded-2xl overflow-hidden cursor-pointer shadow-sm"
        >
          <img 
            alt="Thai Beach" 
            class="w-full h-full object-cover group-hover:scale-110 transition duration-700" 
            src="https://modao.cc/agent-py/media/generated_images/2026-03-01/96ca2238e10c4cae9ffeaffefeb0392d.jpg"
          />
          <div class="absolute inset-0 bg-black/30 flex items-center justify-center flex-col text-white">
            <p class="text-xs tracking-widest mb-1 uppercase opacity-80 font-bold">限时秒杀</p>
            <h4 class="text-xl font-bold">普吉岛潜水</h4>
            <div class="mt-2 text-sm bg-white/20 px-3 py-1 rounded-full">了解更多</div>
          </div>
        </router-link>
      </section>

      <!-- 酒店对比表格 -->
      <section>
        <div class="mb-8">
          <h2 class="text-3xl font-bold">酒店等级对比</h2>
          <p class="text-gray-500 mt-2">清晰的标准对比，助您做出最合适的选择</p>
        </div>
        
        <div class="overflow-x-auto overflow-y-hidden rounded-2xl border border-gray-200 shadow-sm">
          <table class="w-max min-w-full text-left bg-white border-collapse">
            <thead>
              <tr class="bg-gray-50 text-gray-400 text-sm tracking-wider">
                <th class="px-6 py-4 font-bold border-b whitespace-nowrap">维度</th>
                <th class="px-6 py-4 font-bold border-b whitespace-nowrap">超级奢华 (7钻)</th>
                <th class="px-6 py-4 font-bold border-b whitespace-nowrap">五星尊享 (5钻)</th>
                <th class="px-6 py-4 font-bold border-b whitespace-nowrap">舒适优选 (3-4钻)</th>
                <th class="px-6 py-4 font-bold border-b whitespace-nowrap">经济便捷</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-100">
              <tr>
                <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">核心设施</td>
                <td class="px-6 py-4 whitespace-nowrap">直升机坪、私人管家、定制香氛</td>
                <td class="px-6 py-4 whitespace-nowrap">行政酒廊、大堂咖啡、健身中心</td>
                <td class="px-6 py-4 whitespace-nowrap">智能马桶、人体工学办公区</td>
                <td class="px-6 py-4 whitespace-nowrap">24H自助入住、共享洗衣</td>
              </tr>
              <tr>
                <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">增值服务</td>
                <td class="px-6 py-4 whitespace-nowrap">专人机场接送(劳斯莱斯)</td>
                <td class="px-6 py-4 whitespace-nowrap">下午茶权益、免费加床</td>
                <td class="px-6 py-4 whitespace-nowrap">免费瓶装水、行李寄存</td>
                <td class="px-6 py-4 whitespace-nowrap">周边导览手册</td>
              </tr>
              <tr>
                <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">平均均价</td>
                <td class="px-6 py-4 whitespace-nowrap"><span class="text-orange-500 font-bold">¥5,000+</span></td>
                <td class="px-6 py-4 whitespace-nowrap"><span class="text-gray-800">¥1,200 - 3,500</span></td>
                <td class="px-6 py-4 whitespace-nowrap"><span class="text-gray-800">¥400 - 800</span></td>
                <td class="px-6 py-4 whitespace-nowrap"><span class="text-gray-800">¥150 - 300</span></td>
              </tr>
              <tr>
                <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">退改政策</td>
                <td class="px-6 py-4 text-green-600 whitespace-nowrap">
                  <div class="flex items-center whitespace-nowrap">
                    <span class="iconify mr-1" data-icon="material-symbols:check-circle"></span> 入住前72小时免费退改
                  </div>
                </td>
                <td class="px-6 py-4 text-green-600 whitespace-nowrap">
                  <div class="flex items-center whitespace-nowrap">
                    <span class="iconify mr-1" data-icon="material-symbols:check-circle"></span> 入住前48小时免费退改
                  </div>
                </td>
                <td class="px-6 py-4 text-orange-500 whitespace-nowrap">
                  <div class="flex items-center whitespace-nowrap">
                    <span class="iconify mr-1" data-icon="material-symbols:info"></span> 标准价入住前24小时可退
                  </div>
                </td>
                <td class="px-6 py-4 text-orange-500 whitespace-nowrap">
                  <div class="flex items-center whitespace-nowrap">
                    <span class="iconify mr-1" data-icon="material-symbols:info"></span> 标准价入住前12小时可退
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Sidebar from '@/components/common/Sidebar.vue'

const router = useRouter()
const aiInput = ref('')

const goToPlanning = () => {
  router.push({ name: 'Planning', query: { q: aiInput.value } })
}
</script>
