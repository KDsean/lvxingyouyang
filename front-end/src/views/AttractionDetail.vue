<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <el-button @click="goBack" class="mb-6">
      <span class="iconify mr-2" data-icon="material-symbols:arrow-back"></span>
      返回
    </el-button>

    <div class="bg-white rounded-2xl shadow-lg overflow-hidden">
      <div class="relative h-96">
        <img
          src="https://modao.cc/agent/placeholder.svg"
          alt="Attraction"
          class="w-full h-full object-cover"
        />
      </div>

      <div class="p-8">
        <div class="flex items-start justify-between mb-6">
          <div class="flex-1">
            <h1 class="text-3xl font-bold mb-3">故宫博物院</h1>
            <div class="flex items-center text-yellow-500 mb-3">
              <span class="iconify text-xl mr-1" data-icon="material-symbols:star"></span>
              <span class="font-bold">4.8</span>
              <span class="text-gray-500 ml-2">/5</span>
            </div>
            <div class="flex items-center text-gray-600 mb-2">
              <span class="iconify mr-2" data-icon="material-symbols:location-on"></span>
              北京市东城区景山前街4号
            </div>
            <div class="flex items-center text-gray-600">
              <span class="iconify mr-2" data-icon="material-symbols:schedule"></span>
              开放时间：08:30-17:00
            </div>
          </div>
          <div class="text-right">
            <div class="text-4xl font-bold text-orange-500">¥60</div>
            <div class="text-gray-500">门票</div>
          </div>
        </div>

        <el-divider />

        <div class="mb-8">
          <h2 class="text-2xl font-bold mb-4">景点介绍</h2>
          <p class="text-gray-600 leading-relaxed">
            中国明清两代的皇家宫殿，旧称紫禁城，是中国古代宫廷建筑之精华。
            故宫以三大殿为中心，占地面积约72万平方米，建筑面积约15万平方米，有大小宫殿七十多座，房屋九千余间。
          </p>
        </div>

        <el-divider />

        <div class="flex gap-4">
          <el-button type="primary" size="large" class="flex-1">
            购买门票
          </el-button>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.back()
}
</script>
