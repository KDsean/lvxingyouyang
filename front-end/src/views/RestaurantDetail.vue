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
          alt="Restaurant"
          class="w-full h-full object-cover"
        />
      </div>

      <div class="p-8">
        <div class="flex items-start justify-between mb-6">
          <div class="flex-1">
            <h1 class="text-3xl font-bold mb-3">大董烤鸭店</h1>
            <div class="flex items-center text-yellow-500 mb-3">
              <span class="iconify text-xl mr-1" data-icon="material-symbols:star"></span>
              <span class="font-bold">4.8</span>
              <span class="text-gray-500 ml-2">/5</span>
              <span class="ml-4 bg-yellow-500 text-white px-2 py-1 rounded text-sm">米其林推荐</span>
            </div>
            <div class="flex items-center text-gray-600 mb-2">
              <span class="iconify mr-2" data-icon="material-symbols:location-on"></span>
              北京市朝阳区
            </div>
            <div class="flex items-center text-gray-600">
              <span class="iconify mr-2" data-icon="material-symbols:schedule"></span>
              营业时间：11:00-22:00
            </div>
          </div>
          <div class="text-right">
            <div class="text-2xl font-bold text-orange-500">¥200以上</div>
            <div class="text-gray-500">人均消费</div>
          </div>
        </div>

        <el-divider />

        <div class="mb-8">
          <h2 class="text-2xl font-bold mb-4">餐厅介绍</h2>
          <p class="text-gray-600 leading-relaxed">
            大董烤鸭店是北京著名的高端餐厅，以创新京菜和烤鸭闻名。
            餐厅将传统烤鸭技艺与现代烹饪理念相结合，创造出独特的"酥不腻"烤鸭。
          </p>
        </div>

        <div class="mb-8">
          <h2 class="text-2xl font-bold mb-4">招牌菜</h2>
          <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
            <div class="text-center p-4 border rounded-lg">
              <div class="font-bold">烤鸭</div>
            </div>
            <div class="text-center p-4 border rounded-lg">
              <div class="font-bold">酥不腻烤鸭</div>
            </div>
            <div class="text-center p-4 border rounded-lg">
              <div class="font-bold">意境菜</div>
            </div>
          </div>
        </div>

        <el-divider />

        <div class="flex gap-4">
          <el-button type="primary" size="large" class="flex-1">
            预订座位
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
