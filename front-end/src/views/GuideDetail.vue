<template>
  <main class="max-w-4xl mx-auto px-4 py-8">
    <el-button @click="goBack" class="mb-6">
      <span class="iconify mr-2" data-icon="material-symbols:arrow-back"></span>
      返回
    </el-button>

    <article class="bg-white rounded-2xl shadow-lg overflow-hidden">
      <div class="relative h-96">
        <img
          src="https://modao.cc/agent/placeholder.svg"
          alt="Guide"
          class="w-full h-full object-cover"
        />
      </div>

      <div class="p-8">
        <h1 class="text-3xl font-bold mb-4">大理洱海环游攻略：3天2夜深度游，带你玩转苍山洱海</h1>

        <div class="flex items-center gap-6 mb-6 pb-6 border-b">
          <div class="flex items-center gap-3">
            <img
              src="https://modao.cc/agent/placeholder.svg"
              alt="Author"
              class="w-12 h-12 rounded-full"
            />
            <div>
              <div class="font-bold">旅行达人小王</div>
              <div class="text-sm text-gray-500">2026-02-28</div>
            </div>
          </div>

          <div class="flex gap-4 text-gray-500">
            <span class="flex items-center">
              <span class="iconify mr-1" data-icon="material-symbols:visibility"></span>
              12,580
            </span>
            <span class="flex items-center">
              <span class="iconify mr-1" data-icon="material-symbols:thumb-up"></span>
              856
            </span>
          </div>
        </div>

        <div class="prose max-w-none">
          <p class="text-gray-600 leading-relaxed mb-6">
            大理，一个让人向往的地方。苍山洱海，风花雪月，这里有着独特的白族文化和美丽的自然风光。
            这次我们用3天2夜的时间，深度游览了大理的主要景点，现在把攻略分享给大家。
          </p>

          <h2 class="text-2xl font-bold mb-4">第一天：大理古城</h2>
          <p class="text-gray-600 leading-relaxed mb-6">
            上午抵达大理，入住古城内的客栈。下午游览大理古城，漫步在青石板路上，感受古城的韵味。
            晚上可以在人民路上品尝当地美食，体验大理的夜生活。
          </p>

          <h2 class="text-2xl font-bold mb-4">第二天：洱海环游</h2>
          <p class="text-gray-600 leading-relaxed mb-6">
            租一辆电动车或自行车，沿着洱海环游。推荐路线：古城 → 才村 → 喜洲 → 双廊。
            沿途可以欣赏洱海的美景，在喜洲品尝破酥粑粑，在双廊看日落。
          </p>

          <h2 class="text-2xl font-bold mb-4">第三天：苍山</h2>
          <p class="text-gray-600 leading-relaxed mb-6">
            乘坐索道上苍山，欣赏苍山洱海的壮丽景色。下午返回古城，准备返程。
          </p>
        </div>

        <div class="mt-8 pt-6 border-t flex gap-4">
          <el-button type="primary">
            <span class="iconify mr-2" data-icon="material-symbols:thumb-up"></span>
            点赞
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
  </main>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.back()
}
</script>
