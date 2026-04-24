<template>
  <main class="max-w-[1440px] mx-auto px-4 py-8">
    <!-- 头部 -->
    <div class="bg-gradient-to-r from-rose-600 to-pink-500 rounded-2xl shadow-lg p-8 mb-8 text-white">
      <h1 class="text-3xl font-bold mb-6 flex items-center">
        <span class="iconify mr-3" data-icon="material-symbols:shopping-bag"></span>
        全球购
      </h1>
      
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <el-input
          v-model="searchParams.keyword"
          placeholder="搜索商品"
          size="large"
          clearable
        >
          <template #prefix>
            <span class="iconify" data-icon="material-symbols:search"></span>
          </template>
        </el-input>
        
        <el-select v-model="searchParams.category" placeholder="商品分类" size="large" clearable>
          <el-option label="特产美食" value="food"></el-option>
          <el-option label="工艺品" value="craft"></el-option>
          <el-option label="服饰配饰" value="fashion"></el-option>
          <el-option label="护肤美妆" value="beauty"></el-option>
          <el-option label="电子产品" value="electronics"></el-option>
        </el-select>
        
        <el-button type="primary" size="large" @click="handleSearch" class="bg-white text-rose-600 hover:bg-gray-100">
          <span class="iconify mr-2" data-icon="material-symbols:search"></span>
          搜索
        </el-button>
      </div>
    </div>

    <!-- 热门推荐 -->
    <div class="mb-8">
      <h2 class="text-2xl font-bold mb-4">热门推荐</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4">
        <div
          v-for="product in hotProducts"
          :key="product.id"
          class="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-lg transition cursor-pointer"
          @click="viewDetail(product.id)"
        >
          <div class="relative aspect-square overflow-hidden">
            <img
              :src="getProductImage(product)"
              :alt="product.name"
              class="w-full h-full object-cover hover:scale-110 transition duration-500"
            />
            <div v-if="product.originalPrice" class="absolute top-2 right-2 bg-red-500 text-white px-2 py-1 rounded text-xs font-bold">
              特价
            </div>
          </div>
          
          <div class="p-4">
            <h3 class="font-bold mb-2 line-clamp-2 h-12">{{ product.name }}</h3>
            <div class="flex items-center justify-between">
              <div>
                <div class="text-rose-500 font-bold text-xl">¥{{ product.price }}</div>
                <div v-if="product.originalPrice" class="text-gray-400 text-sm line-through">¥{{ product.originalPrice }}</div>
              </div>
              <div class="text-xs text-gray-400">已售{{ product.sales }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 商品列表 -->
    <div>
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold">全部商品</h2>
        <div class="flex gap-4">
          <el-select v-model="sortBy" placeholder="排序" size="large" style="width: 150px">
            <el-option label="综合排序" value="default"></el-option>
            <el-option label="价格从低到高" value="price-asc"></el-option>
            <el-option label="价格从高到低" value="price-desc"></el-option>
            <el-option label="销量最高" value="sales"></el-option>
          </el-select>
        </div>
      </div>
      
      <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-6">
        <div
          v-for="product in products"
          :key="product.id"
          class="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100 hover:shadow-lg transition"
        >
          <div class="relative aspect-square overflow-hidden cursor-pointer" @click="viewDetail(product.id)">
            <img
              :src="getProductImage(product)"
              :alt="product.name"
              class="w-full h-full object-cover hover:scale-110 transition duration-500"
            />
          </div>
          
          <div class="p-4">
            <h3 class="font-bold mb-2 line-clamp-2 h-12 cursor-pointer hover:text-rose-600" @click="viewDetail(product.id)">
              {{ product.name }}
            </h3>
            
            <div class="flex flex-wrap gap-1 mb-3">
              <span
                v-for="tag in product.tags.slice(0, 2)"
                :key="tag"
                class="px-2 py-0.5 bg-rose-50 text-rose-600 rounded text-xs"
              >
                {{ tag }}
              </span>
            </div>
            
            <div class="flex items-end justify-between mb-3">
              <div>
                <div class="text-rose-500 font-bold text-xl">¥{{ product.price }}</div>
                <div v-if="product.originalPrice" class="text-gray-400 text-xs line-through">¥{{ product.originalPrice }}</div>
              </div>
              <div class="text-xs text-gray-400">库存{{ product.stock }}</div>
            </div>
            
            <el-button type="primary" size="small" class="w-full" @click="addToCart(product)">
              <span class="iconify mr-1" data-icon="material-symbols:shopping-cart"></span>
              加入购物车
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="flex justify-center mt-8">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProducts } from '@/api/modules/shopping'
import type { Product } from '@/types'
import { getProductImage } from '@/utils/businessImages'

const router = useRouter()

const searchParams = reactive({
  keyword: '',
  category: ''
})

const sortBy = ref('default')
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(100)

const products = ref<Product[]>([])
const hotProducts = ref<Product[]>([])

const handleSearch = async () => {
  try {
    console.log('开始搜索商品...')
    const params = {
      keyword: searchParams.keyword,
      category: searchParams.category,
      sortBy: sortBy.value,
      page: currentPage.value,
      pageSize: pageSize.value
    }
    
    console.log('搜索参数:', params)
    const res = await getProducts(params)
    console.log('搜索结果:', res)
    // getProducts 返回 PageResponse，数据在 res.data.list
    const list = res.data?.list ?? res.data ?? []
    products.value = list
    hotProducts.value = list.slice(0, 5)
    total.value = res.data?.total ?? list.length
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

const viewDetail = (id: string) => {
  router.push({ name: 'ProductDetail', params: { id } })
}

const addToCart = (product: Product) => {
  ElMessage.success(`已将 ${product.name} 加入购物车`)
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  handleSearch()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  console.log('购物页面已加载，准备获取数据...')
  handleSearch()
})
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
