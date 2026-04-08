<template>
  <aside class="w-full lg:w-72 shrink-0">
    <div class="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 sticky top-24">
      <h3 class="text-lg font-bold mb-6 flex items-center">
        <span class="iconify mr-2 text-blue-600" data-icon="material-symbols:explore"></span>
        出行服务
      </h3>
      <div class="grid grid-cols-2 gap-4">
        <router-link 
          to="/hotels" 
          class="nav-card flex flex-col items-center p-3 rounded-xl bg-blue-50 text-blue-700"
        >
          <span class="iconify text-2xl mb-1" data-icon="ic:baseline-hotel"></span>
          <span class="text-sm font-medium">酒店住宿</span>
        </router-link>
        
        <router-link 
          to="/flights" 
          class="nav-card flex flex-col items-center p-3 rounded-xl bg-indigo-50 text-indigo-700"
        >
          <span class="iconify text-2xl mb-1" data-icon="material-symbols:flight"></span>
          <span class="text-sm font-medium">机票预订</span>
        </router-link>
        
        <router-link 
          to="/trains" 
          class="nav-card flex flex-col items-center p-3 rounded-xl bg-purple-50 text-purple-700"
        >
          <span class="iconify text-2xl mb-1" data-icon="material-symbols:train"></span>
          <span class="text-sm font-medium">火车票</span>
        </router-link>
        
        <router-link 
          to="/cars" 
          class="nav-card flex flex-col items-center p-3 rounded-xl bg-green-50 text-green-700"
        >
          <span class="iconify text-2xl mb-1" data-icon="material-symbols:directions-car"></span>
          <span class="text-sm font-medium">租车自驾</span>
        </router-link>
        
        <router-link 
          to="/attractions" 
          class="nav-card flex flex-col items-center p-3 rounded-xl bg-orange-50 text-orange-700"
        >
          <span class="iconify text-2xl mb-1" data-icon="material-symbols:local-activity"></span>
          <span class="text-sm font-medium">景点门票</span>
        </router-link>
        
        <router-link 
          to="/guides" 
          class="nav-card flex flex-col items-center p-3 rounded-xl bg-red-50 text-red-700"
        >
          <span class="iconify text-2xl mb-1" data-icon="material-symbols:map"></span>
          <span class="text-sm font-medium">当地攻略</span>
        </router-link>
        
        <router-link 
          to="/restaurants" 
          class="nav-card flex flex-col items-center p-3 rounded-xl bg-teal-50 text-teal-700"
        >
          <span class="iconify text-2xl mb-1" data-icon="material-symbols:restaurant"></span>
          <span class="text-sm font-medium">美食林</span>
        </router-link>
        
        <router-link 
          to="/shopping" 
          class="nav-card flex flex-col items-center p-3 rounded-xl bg-rose-50 text-rose-700"
        >
          <span class="iconify text-2xl mb-1" data-icon="material-symbols:shopping-bag"></span>
          <span class="text-sm font-medium">全球购</span>
        </router-link>
      </div>

      <div class="mt-8 pt-8 border-t border-gray-100">
        <div class="bg-gray-100 rounded-lg p-4 relative overflow-hidden group">
          <div class="relative z-10">
            <p class="text-xs text-gray-500 uppercase font-bold tracking-wider">APP 专享特惠</p>
            <p class="text-sm mt-1">首单预订立减 ¥100</p>
            <img 
              alt="App Download QR" 
              class="w-20 h-20 mt-3 border-4 border-white rounded shadow-sm" 
              src="https://modao.cc/agent-py/media/generated_images/2026-03-01/ade8e5ffec7540e7a4f9b55aa4d346a2.jpg"
            />
          </div>
          <span class="iconify absolute -bottom-4 -right-4 text-7xl text-gray-200 opacity-50 group-hover:scale-110 transition" data-icon="ri:cellphone-fill"></span>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup lang="ts">
</script>
