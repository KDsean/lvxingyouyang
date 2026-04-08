<template>
  <footer class="bg-white border-t border-gray-100 mt-20 pt-16 pb-8">
    <div class="max-w-[1440px] mx-auto px-6">
      <div class="grid grid-cols-1 md:grid-cols-4 lg:grid-cols-6 gap-12 mb-12">
        <div class="col-span-1 md:col-span-2">
          <div class="flex items-center space-x-2 mb-6">
            <span class="iconify text-blue-600 text-3xl" data-icon="ri:pangea-fill"></span>
            <span class="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-indigo-600">
              旅行有样
            </span>
          </div>
          <p class="text-gray-500 max-w-sm mb-6 leading-relaxed">
            旅行有样是您 2026 年最可靠的旅行伙伴。我们致力于通过大数据与人工智能技术，为每一位旅行者推荐更具性价比、更有温度、更有"样"的旅行体验。
          </p>
          <div class="flex space-x-4">
            <el-tooltip content="微信: lvxingyouyang2026" placement="top">
              <a href="#" class="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center text-gray-400 hover:text-green-600 transition">
                <span class="iconify text-xl" data-icon="ri:wechat-fill"></span>
              </a>
            </el-tooltip>
            <el-tooltip content="微博: @旅行有样官方" placement="top">
              <a href="#" class="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center text-gray-400 hover:text-red-600 transition">
                <span class="iconify text-xl" data-icon="ri:weibo-fill"></span>
              </a>
            </el-tooltip>
            <el-tooltip content="豆瓣: 旅行有样" placement="top">
              <a href="#" class="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center text-gray-400 hover:text-green-700 transition">
                <span class="iconify text-xl" data-icon="ri:douban-fill"></span>
              </a>
            </el-tooltip>
            <el-tooltip content="小红书: 旅行有样" placement="top">
              <a href="#" class="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center text-gray-400 hover:text-red-500 transition">
                <span class="iconify text-xl" data-icon="mdi:book-open-page-variant"></span>
              </a>
            </el-tooltip>
          </div>
        </div>

        <div>
          <h5 class="font-bold mb-6">关于我们</h5>
          <ul class="space-y-4 text-gray-500 text-sm">
            <li><a href="#" class="hover:text-blue-600">品牌故事</a></li>
            <li><a href="#" class="hover:text-blue-600">媒体报道</a></li>
            <li><a href="#" class="hover:text-blue-600">加入我们</a></li>
            <li><a href="#" class="hover:text-blue-600">可持续发展</a></li>
          </ul>
        </div>

        <div>
          <h5 class="font-bold mb-6">客户中心</h5>
          <ul class="space-y-4 text-gray-500 text-sm">
            <li><a href="#" class="hover:text-blue-600">开发者 API</a></li>
            <li><a href="#" class="hover:text-blue-600">帮助中心</a></li>
            <li><a href="#" class="hover:text-blue-600">常见问题</a></li>
            <li><a href="#" class="hover:text-blue-600">意见反馈</a></li>
          </ul>
        </div>

        <div>
          <h5 class="font-bold mb-6">快捷链接</h5>
          <ul class="space-y-4 text-gray-500 text-sm">
            <li><router-link to="/destinations" class="hover:text-blue-600">全站地图</router-link></li>
            <li><router-link to="/hotels" class="hover:text-blue-600">特价日报</router-link></li>
            <li><a href="#" class="hover:text-blue-600">旅游摄影赛</a></li>
            <li><a href="#" class="hover:text-blue-600">签证代理</a></li>
          </ul>
        </div>

        <div>
          <h5 class="font-bold mb-6">联系方式</h5>
          <ul class="space-y-4 text-gray-500 text-sm">
            <li class="flex items-center">
              <span class="iconify mr-2 text-lg" data-icon="material-symbols:phone-in-talk-outline"></span>
              195-7485-905
            </li>
            <li class="flex items-center">
              <span class="iconify mr-2 text-lg" data-icon="material-symbols:mail-outline"></span>
              1971283224@qq.com
            </li>
            <li class="flex items-start">
              <span class="iconify mr-2 text-lg mt-1" data-icon="material-symbols:location-on-outline"></span>
              广东省湛江市麻章区湖光镇教育城新坡路1号致远楼众创空间
            </li>
          </ul>
        </div>
      </div>

      <div class="pt-8 border-t border-gray-100 flex flex-col md:flex-row justify-between items-center text-xs text-gray-400">
        <p>© 2026 旅行有样 Inc. 保留所有权利。粤ICP备20260301号</p>
        <div class="flex space-x-6 mt-4 md:mt-0">
          <a href="#" class="hover:text-gray-600">服务条款</a>
          <a href="#" class="hover:text-gray-600">隐私声明</a>
          <a href="#" class="hover:text-gray-600">Cookie 政策</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
</script>
